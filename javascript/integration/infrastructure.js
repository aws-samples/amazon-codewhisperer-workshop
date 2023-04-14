const { Stack, Duration } = require('aws-cdk-lib');
const lambda = require('aws-cdk-lib/aws-lambda');
const lambda_events = require('aws-cdk-lib/aws-lambda-event-sources');
const sqs = require('aws-cdk-lib/aws-sqs');
const sns = require('aws-cdk-lib/aws-sns');
const sns_subs = require('aws-cdk-lib/aws-sns-subscriptions');

class IntegrationStack extends Stack {

    get snsArn() { return this.rekognizedEventTopicArn }

    constructor(scope, id, props) {
        super(scope, id, props);

        const rekognizedQueue = new sqs.Queue(
            this,
            "rekognized_image_queue", {
            visibilityTimeout: Duration.seconds(30)
        }
        )

        const sqsSubscription = new sns_subs.SqsSubscription(
            rekognizedQueue, {
            rawMessageDelivery: true
        }
        )

        const rekognizedEventTopic = new sns.Topic(
            this,
            "rekognized_image_topic"
        )

        this.rekognizedEventTopicArn = rekognizedEventTopic.topicArn
        rekognizedEventTopic.addSubscription(sqsSubscription)

        const integrationLambda = new lambda.Function(
            this,
            "IntegrationLambda", {
            runtime: lambda.Runtime.NODEJS_16_X,
            handler: "send-email.handler",
            code: lambda.Code.fromAsset("integration/runtime")
        }
        )

        const invokeEventSource = new lambda_events.SqsEventSource(rekognizedQueue)
        integrationLambda.addEventSource(invokeEventSource)
    }   
}

module.exports = { IntegrationStack }