// declare SQS that reacts to image upload SNS
// declare SNS to where it sends the items

const { Stack } = require('aws-cdk-lib');
const iam = require('aws-cdk-lib/aws-iam');
const ddb = require('aws-cdk-lib/aws-dynamodb');
const lambda = require('aws-cdk-lib/aws-lambda');
const apigateway = require('aws-cdk-lib/aws-apigateway');

class RekognitionStack extends Stack {

    constructor(scope, id, props) {
        super(scope, id, props);

        // create new IAM group and user
        const group = new iam.Group(this, "RekGroup")
        const user = new iam.User(this, "RekUser")

        // add IAM user to the new group
        user.addToGroup(group)

        // create DynamoDB table to hold Rekognition results
        const table = new ddb.Table(
            this,
            "Classifications", {
            partitionKey: { name: "image", type: ddb.AttributeType.STRING }
        }
        )

        // create Lambda function
        const lambdaFunction = new lambda.Function(
            this,
            "image_recognition", {
            runtime: lambda.Runtime.NODEJS_16_X,
            handler: "image-recognition.handler",
            code: lambda.Code.fromAsset("recognition/runtime"),
            environment: {
                "TABLE_NAME": table.tableName,
                "SQS_QUEUE_URL": props.sqsUrl,
                "TOPIC_ARN": props.snsArn,
            }
        }
        )

        lambdaFunction.addEventSourceMapping("ImgRekognitionLambda", { eventSourceArn: props.sqsArn })

        // add Rekognition permissions for Lambda function
        const rekognitionStatement = new iam.PolicyStatement()
        rekognitionStatement.addActions("rekognition:DetectLabels")
        rekognitionStatement.addResources("*")
        lambdaFunction.addToRolePolicy(rekognitionStatement)

        // add SNS permissions for Lambda function
        const snsPermission = new iam.PolicyStatement()
        snsPermission.addActions("sns:publish")
        snsPermission.addResources("*")
        lambdaFunction.addToRolePolicy(snsPermission)

        // grant permission for lambda to receive/delete message from SQS
        const sqsPermission = new iam.PolicyStatement()
        sqsPermission.addActions("sqs:ChangeMessageVisibility")
        sqsPermission.addActions("sqs:DeleteMessage")
        sqsPermission.addActions("sqs:GetQueueAttributes")
        sqsPermission.addActions("sqs:GetQueueUrl")
        sqsPermission.addActions("sqs:ReceiveMessage")
        sqsPermission.addResources("*")
        lambdaFunction.addToRolePolicy(sqsPermission)

        // grant permissions for lambda to read/write to DynamoDB table
        table.grantReadWriteData(lambdaFunction)

        // # grant permissions for lambda to read from bucket
        const s3Permission = new iam.PolicyStatement()
        s3Permission.addActions("s3:get*")
        s3Permission.addResources("*")
        lambdaFunction.addToRolePolicy(s3Permission)

        // add additional API Gateway and lambda to list ddb
        const listImgLambda = new lambda.Function(
            this,
            "ListImagesLambda", {
            functionName: "ListImagesLambda",
            runtime: lambda.Runtime.NODEJS_16_X,
            code: lambda.Code.fromAsset("recognition/runtime"),
            handler: "list-images.handler",
            environment: { "TABLE_NAME": table.tableName }
        }
        )

        const api = new apigateway.RestApi(
            this,
            "REST_API", {
            restApiName: "List Images Service",
            description: "CW workshop - list images recognized from workshop."
        }
        )

        const listImages = new apigateway.LambdaIntegration(
            listImgLambda, {
            requestTemplates: { "application/json": '{ "statusCode": "200" }' }
        }
        )

        api.root.addMethod("GET", listImages)

        table.grantReadData(listImgLambda)
    }
}

module.exports = { RekognitionStack }
