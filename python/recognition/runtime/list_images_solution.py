import os
import boto3
import json

table_name = os.environ["TABLE_NAME"]

# 1.) Function to list all items from a DynamoDB table
def list_items(table_name):
    dynamodb = boto3.resource("dynamodb")
    table = dynamodb.Table(table_name)
    response = table.scan()
    items = response['Items']
    return items

# <<Amazon CodeWhisperer generated code goes here>>

def handler(event, context):
    # call method 1.) to scan items from DynamoDB
    response = list_items(table_name)
    return {
        "body": json.dumps(response),
        "statusCode": 200
    }