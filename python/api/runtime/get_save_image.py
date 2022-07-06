import os
import logging
from urllib.request import urlopen
import boto3
from botocore.exceptions import ClientError

s3_client = boto3.client("s3")

# Function to get a file from url
# Function to upload image to S3

# <<Amazon CodeWhisperer generated code goes here>>

def handler(event, context):
    url = event["queryStringParameters"]["url"]
    name = event["queryStringParameters"]["name"]