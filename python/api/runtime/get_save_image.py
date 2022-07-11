import os
import logging
import json
from urllib.request import urlopen
from botocore.exceptions import ClientError
import boto3 

S3_BUCKET=os.getenv('BUCKET_NAME')
s3_client = boto3.client('s3')

# Function to get a file from url
def get_file_from_url(url):
    """
    Downloads a file from a url
    """
    try:
        print("Downloading file from url: " + url)
        response = urlopen(url)
        return response.read()
    except Exception as e:
        print("Error downloading file from url: " + url)
        print(e)
        return None

# Function to upload image to S3
def upload_image_to_s3(bucket, key, data):
    """
    Uploads an image to S3
    """
    try:
        print("Uploading image to S3")
        s3_client.put_object(Body=data, Bucket=bucket, Key=key)
        return True
    except ClientError as e:
        print("Error uploading image to S3")
        print(e)
        return False

# <<Amazon CodeWhisperer generated code goes here>>

def handler(event, context):
    url = event["queryStringParameters"]["url"]
    name = event["queryStringParameters"]["name"]

    upload_image_to_s3(S3_BUCKET, name, get_file_from_url(url))
    return {
        'statusCode': 200,
        'body': json.dumps('Successfully Uploaded Img!')
    }
