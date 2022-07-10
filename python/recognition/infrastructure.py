# declare SQS that reacts to image upload SNS
# declare SNS to where it sends the items

from aws_cdk import (
    aws_iam as iam,
    aws_lambda as _lambda,
    aws_dynamodb as ddb,
    aws_apigateway as apigateway,
    Stack
)
from constructs import Construct


class RekognitionStack(Stack):
    def __init__(self, scope: Construct, id: str, sqs_url: str, sqs_arn: str, sns_arn: str, **kwargs) -> None:
        super().__init__(scope, id, **kwargs)

        # create new IAM group and user
        group = iam.Group(self, "RekGroup")
        user = iam.User(self, "RekUser")

        # add IAM user to the new group
        user.add_to_group(group)

        # create DynamoDB table to hold Rekognition results
        table = ddb.Table(
            self,
            "Classifications",
            partition_key=ddb.Attribute(name="image", type=ddb.AttributeType.STRING)
        )

        # create Lambda function
        lambda_function = _lambda.Function(
            self,
            "image_recognition",
            runtime=_lambda.Runtime.PYTHON_3_7,
            handler="image_recognition.handler",
            code=_lambda.Code.from_asset("recognition/runtime"),
            environment={
                "TABLE_NAME": table.table_name,
                "SQS_QUEUE_URL": sqs_url,
                "TOPIC_ARN": sns_arn,
            },
        )

        lambda_function.add_event_source_mapping("ImgRekognitionLambda", event_source_arn=sqs_arn)

        # add Rekognition permissions for Lambda function
        rekognition_statement = iam.PolicyStatement()
        rekognition_statement.add_actions("rekognition:DetectLabels")
        rekognition_statement.add_resources("*")
        lambda_function.add_to_role_policy(rekognition_statement)

        # add SNS permissions for Lambda function
        sns_permission = iam.PolicyStatement()
        sns_permission.add_actions("sns:publish")
        sns_permission.add_resources("*")
        lambda_function.add_to_role_policy(sns_permission)

        # grant permission for lambda to receive/delete message from SQS
        sqs_permission = iam.PolicyStatement()
        sqs_permission.add_actions("sqs:ChangeMessageVisibility")
        sqs_permission.add_actions("sqs:DeleteMessage")
        sqs_permission.add_actions("sqs:GetQueueAttributes")
        sqs_permission.add_actions("sqs:GetQueueUrl")
        sqs_permission.add_actions("sqs:ReceiveMessage")
        sqs_permission.add_resources("*")
        lambda_function.add_to_role_policy(sqs_permission)

        # grant permissions for lambda to read/write to DynamoDB table
        table.grant_read_write_data(lambda_function)

        # grant permissions for lambda to read from bucket
        s3_permission = iam.PolicyStatement()
        s3_permission.add_actions("s3:get*")
        s3_permission.add_resources("*")
        lambda_function.add_to_role_policy(s3_permission)

        # add additional API Gateway and lambda to list ddb
        list_img_lambda = _lambda.Function(
            self,
            "ListImagesLambda",
            function_name="ListImagesLambda",
            runtime=_lambda.Runtime.PYTHON_3_7,
            code=_lambda.Code.from_asset("recognition/runtime"),
            handler="list_images.handler",
            environment={"TABLE_NAME": table.table_name}
        )

        api = apigateway.RestApi(
            self,
            "REST_API",
            rest_api_name="List Images Service",
            description="CW workshop - list images recognized from workshop."
        )

        list_images = apigateway.LambdaIntegration(
            list_img_lambda,
            request_templates={"application/json": '{ "statusCode": "200" }'}
        )

        api.root.add_method("GET", list_images)

        table.grant_read_data(list_img_lambda)
