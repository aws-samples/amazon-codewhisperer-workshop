package com.cwworkshop.recognition;

import software.constructs.Construct;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import com.cwworkshop.LambdaBundling;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.iam.Group;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.User;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.EventSourceMappingOptions;

public class RecognitionStack extends Stack {
    public RecognitionStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public RecognitionStack(final Construct scope, final String id, final RecognitionStackProps props) {
        super(scope, id, props);

        // create new IAM group and user
        final Group group = Group.Builder.create(this, "RekGroup").build();
        final User user = User.Builder.create(this, "RekUser").build();

        // add IAM user to the new group
        user.addToGroup(group);

        // create DynamoDB table to hold Rekognition results
        final Table table = Table.Builder.create(this, "Classifications")
                .partitionKey(Attribute.builder().name("image").type(AttributeType.STRING).build())
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        AssetOptions packageAssetOptions = AssetOptions.builder().bundling(LambdaBundling.get("recognition")).build();

        // create Lambda function
        final Function lambda_function = Function.Builder.create(this, "image_recognition")
                .runtime(Runtime.JAVA_11)
                .timeout(Duration.seconds(60))
                .memorySize(1024)
                .handler("recognition.RecognitionHandler")
                .code(Code.fromAsset("../software/recognition", packageAssetOptions))
                .environment(Map.of(
                        "TABLE_NAME", table.getTableName(),
                        "SQS_QUEUE_URL", props.getSqsUrl(),
                        "TOPIC_ARN", props.getSnsArn()))
                .build();

        lambda_function.addEventSourceMapping("ImgRekognitionLambda",
                EventSourceMappingOptions.builder().eventSourceArn(props.getSqsArn()).build());

        // add Rekognition permissions for Lambda function
        final PolicyStatement rekognition_statement = PolicyStatement.Builder.create()
                .actions(Collections.singletonList("rekognition:DetectLabels"))
                .resources(Collections.singletonList("*"))
                .build();
        lambda_function.addToRolePolicy(rekognition_statement);

        // add SNS permissions for Lambda function
        final PolicyStatement sns_permission = PolicyStatement.Builder.create()
                .actions(Collections.singletonList("sns:publish"))
                .resources(Collections.singletonList("*"))
                .build();
        lambda_function.addToRolePolicy(sns_permission);

        // grant permission for lambda to receive/delete message from SQS
        final PolicyStatement sqs_permission = PolicyStatement.Builder.create()
                .actions(Arrays.asList(
                        "sqs:ChangeMessageVisibility",
                        "sqs:DeleteMessage",
                        "sqs:GetQueueAttributes",
                        "sqs:GetQueueUrl",
                        "sqs:ReceiveMessage"))
                .resources(Collections.singletonList("*"))
                .build();

        lambda_function.addToRolePolicy(sqs_permission);

        // grant permissions for lambda to read/write to DynamoDB table
        table.grantReadWriteData(lambda_function);

        // grant permissions for lambda to read from bucket
        final PolicyStatement s3_permission = PolicyStatement.Builder.create()
                .actions(Collections.singletonList("s3:get*"))
                .resources(Collections.singletonList("*"))
                .build();
        lambda_function.addToRolePolicy(s3_permission);

        // add additional API Gateway and lambda to list ddb
        final Function list_img_lambda = Function.Builder.create(this, "ListImagesLambda")
                .functionName("ListImagesLambda")
                .runtime(Runtime.JAVA_11)
                .timeout(Duration.seconds(60))
                .memorySize(1024)
                .code(Code.fromAsset("../software/recognition", packageAssetOptions))
                .handler("recognition.ListItemsHandler")
                .environment(Map.of("TABLE_NAME", table.getTableName()))
                .build();

        final RestApi api = RestApi.Builder.create(this, "REST_API")
                .restApiName("List Images Service")
                .description("CW workshop - list images recognized from workshop.")
                .build();

        final LambdaIntegration list_images = LambdaIntegration.Builder.create(list_img_lambda)
                .requestTemplates(Map.of("application/json", "{ \"statusCode\": \"200\" }"))
                .build();

        api.getRoot().addMethod("GET", list_images);

        table.grantReadData(list_img_lambda);
    }
}
