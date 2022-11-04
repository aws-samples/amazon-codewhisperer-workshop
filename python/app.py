#!/usr/bin/env python3
import aws_cdk as cdk
from api.infrastructure import APIStack
from integration.infrastructure import IntegrationStack
from recognition.infrastructure import RekognitionStack

DEFAULT_REGION = 'us-west-2'

app = cdk.App()
apiStack = APIStack(app, "APIStack", env=cdk.Environment(region=DEFAULT_REGION))
integrationStack = IntegrationStack(app, "IntegrationStack", env=cdk.Environment(region=DEFAULT_REGION))
RekognitionStack(
    app,
    "RekognitionStack",
    sqs_url=apiStack.sqs_url,
    sqs_arn=apiStack.sqs_arn,
    sns_arn=integrationStack.sns_arn,
    env=cdk.Environment(region=DEFAULT_REGION)
)

app.synth()
