import os
import boto3
import json

table_name = os.environ["TABLE_NAME"]
attribute_name = "image_name"

# Function to list all items from a DynamoDB table

# <<Amazon CodeWhisperer generated code goes here>>

def handler(event, context):
    # 1. Scan items from DynamoDB

    return {
        #"body": json.dumps(response),
        "statusCode": 200
    }
