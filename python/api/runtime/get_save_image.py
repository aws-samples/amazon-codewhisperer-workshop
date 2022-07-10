import os
import json

S3_BUCKET = os.getenv('BUCKET_NAME')

# Function to get a file from url
# Function to upload image to S3

# <<Amazon CodeWhisperer generated code goes here>>

def handler(event, context):
    url = event["queryStringParameters"]["url"]
    name = event["queryStringParameters"]["name"]

    # Get the file from the url

    return {
        'statusCode': 200,
        'body': json.dumps('Successfully Uploaded Img!')
    }