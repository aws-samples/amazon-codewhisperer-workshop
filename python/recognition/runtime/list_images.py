import os
import boto3
import json

table_name = os.environ["TABLE_NAME"]
attribute_name = "image_name"

def list_ddb_items():
    client = boto3.client("dynamodb")
    return client.scan(
        TableName=table_name,
        Limit=100,
        Select="SPECIFIC_ATTRIBUTES",
        ProjectionExpression=attribute_name,
    )


def handler(event, context):
    # 1. Scan items from DynamoDB
    response = list_ddb_items()

    return {
        "body": json.dumps(response),
        "statusCode": 200
    }
