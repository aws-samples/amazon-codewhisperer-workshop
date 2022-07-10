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

# Calls Amazon Rekognition DetectLabels API to classify images in S3
def detectImgLabels(bucket_name, key, maxLabels=10, minConfidence=70):
    image = {
        "S3Object": {
            "Bucket": bucket_name,
            "Name": key
        }
    }
    response = rekognition.detect_labels(Image=image, MaxLabels=10, MinConfidence=70)
    return response


# Write item to DynamoDB
def writeToDynamoDb(tableName, item):
    dynamodb.put_item(
        TableName=tableName,
        Item=item
    )

# Send message to SNS
def triggerSNS(message):
    response = sns.publish(
        TopicArn=topic_arn,
        Message=message,
        Subject="CodeWhisperer Workshop Success!",

    )
    print(response)

# Delete message from SQS
def deleteFromSqs(receipt_handle):
    sqs.delete_message(
        QueueUrl=queue_url,
        ReceiptHandle=receipt_handle
    )


def handler(event, context):
    print(event)
    try:
        # 1. Read message from SQS
        for Record in event.get("Records"):
            receipt_handle = Record.get("receiptHandle")
            for record in json.loads(Record.get("body")).get("Records"):
                bucket_name = record.get("s3").get("bucket").get("name")
                key = record.get("s3").get("object").get("key")

                # 2. Use Amazon Rekognition to recognize the image
                labels = detectImgLabels(bucket_name=bucket_name, key=key)
                print(key, labels["Labels"])

                # 3. Persist result on DynamoDB
                db_result = []
                json_labels = json.dumps(labels["Labels"])
                db_labels = json.loads(json_labels)
                for label in db_labels:
                    db_result.append(label["Name"])
                db_item = {
                    "image": {"S": key},
                    "labels": {"S": str(db_result)}
                }

                writeToDynamoDb(tableName=table_name, item=db_item)

                # 4. Send message to SNS
                triggerSNS(str(db_result))

                # 5. Delete img from SQS
                deleteFromSqs(receipt_handle=receipt_handle)

    except Exception as e:
        print(e)
        print("Error processing object {} from bucket {}. ".format(key, bucket_name))
        raise e