import os
import boto3
import json

sqs = boto3.client("sqs")
rekognition = boto3.client("rekognition")
dynamodb = boto3.client("dynamodb")
sns = boto3.client("sns")

queue_url = os.environ["SQS_QUEUE_URL"]
table_name = os.environ["TABLE_NAME"]
topic_arn = os.environ["TOPIC_ARN"]

# Detect labels from image with Rekognition
# Save labels to DynamoDB
# Publish item to SNS
# Delete message from SQS

# <<Amazon CodeWhisperer generated code goes here>>



def handler(event, context):
    print(event)
    print(type(event))
    try:
        # 1. process message from SQS
        for Record in event.get("Records"):
            receipt_handle = Record.get("receiptHandle")
            for record in json.loads(Record.get("body")).get("Records"):
                bucket_name = record.get("s3").get("bucket").get("name")
                key = record.get("s3").get("object").get("key")

                # 2. Use Amazon Rekognition to recognize the image


                # 3. Persist result on DynamoDB


                # 4. Send message to SNS


                # 5. Delete img from SQS


    except Exception as e:
        print(e)
        print("Error processing object {} from bucket {}. ".format(key, bucket_name))
        raise e
