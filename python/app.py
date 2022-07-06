#!/usr/bin/env python3
import os

import aws_cdk as cdk
from api.infrastructure import APIStack
from integration.infrastructure import IntegrationStack
from recognition.infrastructure import RekognitionStack

app = cdk.App()
apiStack = APIStack(app, "APIStack")
integrationStack = IntegrationStack(app, "IntegrationStack")
RekognitionStack(
    app,
    "RekognitionStack",
    sqs_url=apiStack.sqs_url,
    sqs_arn=apiStack.sqs_arn,
    sns_arn=integrationStack.sns_arn,
)

app.synth()
