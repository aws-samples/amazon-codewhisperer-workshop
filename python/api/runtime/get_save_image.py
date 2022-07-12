import os
import json
import boto3
import requests
import botocore.exceptions

s3_client = boto3.client("s3")
S3_BUCKET = os.getenv('BUCKET_NAME')

# Function to get a file from url
def get_file_from_url(url):
    try:
        response = requests.get(url)
        return response
    except requests.exceptions.RequestException as e:
        print(e)

# Function to upload image to S3
def upload_image_to_s3(bucket, key, data):
    """
    Uploads an image to S3
    """
    try:
        print("Uploading image to S3")
        s3_client.put_object(Body=data, Bucket=bucket, Key=key)
        return True
    except botocore.exceptions.ClientError as e:
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

    
