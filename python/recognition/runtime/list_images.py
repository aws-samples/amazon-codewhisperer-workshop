import os
import boto3
import json

table_name = os.environ["TABLE_NAME"]

# 1.) Function to list all items from a DynamoDB table

# <<Amazon CodeWhisperer generated code goes here>>

def handler(event, context):
    # call method 1.) to scan items from DynamoDB

    return {
        #"body": json.dumps(response),
        "statusCode": 200
    }
