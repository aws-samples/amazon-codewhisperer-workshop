#!/usr/bin/env node

const cdk = require('aws-cdk-lib');
const { APIStack } = require('./api/infrastructure');
const { IntegrationStack } = require('./integration/infrastructure');
const { RekognitionStack } = require('./recognition/infrastructure');

const DEFAULT_REGION = 'us-east-2'

const defaultEnvironment = {
  region: DEFAULT_REGION
}

const app = new cdk.App();

const apiStack = new APIStack(app, 'APIStack', { env: defaultEnvironment });
const integrationStack = new IntegrationStack(app, "IntegrationStack", { env: defaultEnvironment })
new RekognitionStack(
  app,
  "RekognitionStack", {
  sqsUrl: apiStack.sqsUrl,
  sqsArn: apiStack.sqsArn,
  snsArn: integrationStack.snsArn,
  env: defaultEnvironment
}
)

app.synth()
