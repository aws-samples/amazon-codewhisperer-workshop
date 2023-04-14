const { Stack, Duration } = require('aws-cdk-lib');
const s3 = require('aws-cdk-lib/aws-s3');
const lambda = require('aws-cdk-lib/aws-lambda');
const apigateway = require('aws-cdk-lib/aws-apigateway');
const sqs = require('aws-cdk-lib/aws-sqs');
const sns = require('aws-cdk-lib/aws-sns');
const sns_subs = require('aws-cdk-lib/aws-sns-subscriptions');
const s3n = require('aws-cdk-lib/aws-s3-notifications');

class APIStack extends Stack {

    get sqsUrl() { return this.uploadQueueUrl }

    get sqsArn() { return this.uploadQueueArn }

    constructor(scope, id, props) {
        super(scope, id, props);

        const bucket = new s3.Bucket(this, "CW-Workshop-Images")

        const imageGetAndSaveLambda = new lambda.Function(
            this,
            "ImageGetAndSaveLambda", {
            functionName: "ImageGetAndSaveLambda",
            runtime: lambda.Runtime.NODEJS_16_X,
            code: lambda.Code.fromAsset("api/runtime"),
            handler: "get-save-image.handler",
            environment: { "BUCKET_NAME": bucket.bucketName }
        }
        )

        bucket.grantReadWrite(imageGetAndSaveLambda)

        const api = new apigateway.RestApi(
            this,
            "REST_API", {
            restApiName: "Image Upload Service",
            description: "CW workshop - upload image for workshop."
        }
        )

        const getImageIntegration = new apigateway.LambdaIntegration(
            imageGetAndSaveLambda, {
            requestTemplates: { "application/json": '{ "statusCode": "200" }' }
        }
        )

        api.root.addMethod("GET", getImageIntegration)

        const uploadQueue = new sqs.Queue(
            this,
            "uploaded_image_queue", {
            visibilityTimeout: Duration.seconds(30)
        }
        )

        this.uploadQueueUrl = uploadQueue.queueUrl
        this.uploadQueueArn = uploadQueue.queueArn

        const sqsSubscription = new sns_subs.SqsSubscription(
            uploadQueue, {
            rawMessageDelivery: true
        }
        )

        const uploadEventTopic = new sns.Topic(
            this,
            "uploaded_image_topic"
        )

        uploadEventTopic.addSubscription(sqsSubscription)

        bucket.addEventNotification(
            s3.EventType.OBJECT_CREATED_PUT,
            new s3n.SnsDestination(uploadEventTopic)
        )

    }
}

module.exports = { APIStack }