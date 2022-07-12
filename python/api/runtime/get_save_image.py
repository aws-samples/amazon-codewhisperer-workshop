import os
import json
import boto3
import requests
import botocore.exceptions

s3_client = boto3.client("s3")
S3_BUCKET = os.getenv('BUCKET_NAME')

# 1.) Function to get a file from url

# 2.) Function to upload image to S3

# <<Amazon CodeWhisperer generated code goes here>>


def handler(event, context):
    url = event["queryStringParameters"]["url"]
    name = event["queryStringParameters"]["name"]

    # pass the output of method #1 as input to method #2

    return {
        'statusCode': 200,
        'body': json.dumps('Successfully Uploaded Img!')
    }
