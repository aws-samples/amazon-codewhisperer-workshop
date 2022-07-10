import aws_cdk as cdk
from aws_cdk import Stack
from constructs import Construct
from aws_cdk import Duration
from aws_cdk import aws_sqs as sqs
from aws_cdk import aws_sns_subscriptions as sns_subs
from aws_cdk import aws_sns as sns
from aws_cdk import aws_lambda as lambda_
from aws_cdk import aws_lambda_event_sources as lambda_events

class IntegrationStack(Stack):
    def __init__(self, scope: Construct, construct_id: str, **kwargs) -> None:
        super().__init__(scope, construct_id, **kwargs)

        rekognized_queue = sqs.Queue(
            self,
            id="rekognized_image_queue",
            visibility_timeout=Duration.seconds(30)
        )

        sqs_subscription = sns_subs.SqsSubscription(
            rekognized_queue,
            raw_message_delivery=True
        )

        rekognized_event_topic = sns.Topic(
            self,
            id="rekognized_image_topic"
        )

        self.rekognized_event_topic_arn = rekognized_event_topic.topic_arn
        rekognized_event_topic.add_subscription(sqs_subscription)

        integration_lambda = lambda_.Function(
            self,
            "IntegrationLambda",
            runtime=lambda_.Runtime.PYTHON_3_7,
            handler="send_email.handler",
            code=lambda_.Code.from_asset("integration/runtime"),
        )

        invoke_event_source = lambda_events.SqsEventSource(rekognized_queue)
        integration_lambda.add_event_source(invoke_event_source)


    @property
    def sns_arn(self) -> str:
        return self.rekognized_event_topic_arn