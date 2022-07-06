# Amazon CodeWhisperer Example

This repository accompanies a hands-on workshop that demonstrates how to leverage Amazon CodeWhisperer for building a fully fledged serverless app on AWS.

## Amazon CodeWhisperer

Amazon CodeWhisperer is a machine learning (ML)–powered service that helps improve developer productivity by generating code recommendations based on their comments in natural language and code in the integrated development environment (IDE).

With Amazon CodeWhisperer, developers can simply write a comment that outlines a specific task in plain English, such as “upload a file to S3.” Based on this, CodeWhisperer automatically determines which cloud services and public libraries are best suited for the specified task, builds the specific code on the fly, and recommends the generated code snippets directly in the IDE. Moreover, CodeWhisperer seamlessly integrates with your Visual Studio Code and JetBrains IDEs, in such a way that you can stay focused and never leave the development environment. See the [Amazon CodeWhisperer](https://aws.amazon.com/codewhisperer/) page for details.

## Try out Amazon CodeWhisperer

You can use this code repository to try out Amazon CodeWhisperer by building a full-fledged, event-driven, serverless application. With the aid of Amazon CodeWhisperer, you'll write your own code that runs on top of AWS Lambda to interact with Amazon DynamoDB, Amazon SNS, Amazon SQS, Amazon S3, and third-party HTTP APIs to perform image recognition using Amazon Rekognition. The users can interact with the application by sending the URL of an image for processing, or by listing the images and the objects present on each image.

### Prerequisites

To use the CodeWhisperer with this repo, you will need an AWS account and an active Amazon CodeWhisperer activation.

### Setup

This code repository accompanies a workshop.  Use the workshop description and follow the steps for building and deploying the application.

## Getting Help

Use the community resources below for getting help with AWS CodeGuru Reviewer.

- Use GitHub issues to report bugs and request features.
- Open a support ticket with [AWS Support](https://docs.aws.amazon.com/awssupport/latest/user/getting-started.html).
- For contributing guidelines, refer to [CONTRIBUTING](CONTRIBUTING.md).

## Contributing

See [CONTRIBUTING](CONTRIBUTING.md#security-issue-notifications) for more information.

## License

This project is licensed under the Apache-2.0 License. See the [LICENSE](LICENSE) file.

## Working with this CDK Python project

The `cdk.json` file tells the CDK Toolkit how to execute your app.

This project is set up like a standard Python project.  The initialization
process also creates a virtualenv within this project, stored under the `.venv`
directory.  To create the virtualenv it assumes that there is a `python3`
(or `python` for Windows) executable in your path with access to the `venv`
package. If for any reason the automatic creation of the virtualenv fails,
you can create the virtualenv manually.

To manually create a virtualenv on MacOS and Linux:

```
$ python3 -m venv .venv
```

After the init process completes and the virtualenv is created, you can use the following
step to activate your virtualenv.

```
$ source .venv/bin/activate
```

If you are a Windows platform, you would activate the virtualenv like this:

```
% .venv\Scripts\activate.bat
```

Once the virtualenv is activated, you can install the required dependencies.

```
$ pip install -r requirements.txt
```

At this point you can now synthesize the CloudFormation template for this code.

```
$ cdk synth
```

To add additional dependencies, for example other CDK libraries, just add
them to your `setup.py` file and rerun the `pip install -r requirements.txt`
command.

## Useful commands

 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

Enjoy!
