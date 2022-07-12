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

# 1.) Detect labels from image with Rekognition as "labels"
def detectImgLabels(bucket_name, key, maxLabels=10, minConfidence=70):
    image = {
        "S3Object": {
            "Bucket": bucket_name,
            "Name": key
        }
    }
    response = rekognition.detect_labels(Image=image, MaxLabels=10, MinConfidence=70)
    return response


# 2.) Save labels to DynamoDB
def writeToDynamoDb(tableName, item):
    dynamodb.put_item(
        TableName=tableName,
        Item=item
    )

# 3.) Publish item to SNS
def triggerSNS(message):
    response = sns.publish(
        TopicArn=topic_arn,
        Message=message,
        Subject="CodeWhisperer Workshop Success!",

    )
    print(response)

# 4.) Delete message from SQS
def deleteFromSqs(receipt_handle):
    sqs.delete_message(
        QueueUrl=queue_url,
        ReceiptHandle=receipt_handle
    )


def handler(event, context):
    print(event)
    try:
        # Read message from SQS
        for Record in event.get("Records"):
            receipt_handle = Record.get("receiptHandle")
            for record in json.loads(Record.get("body")).get("Records"):
                bucket_name = record.get("s3").get("bucket").get("name")
                key = record.get("s3").get("object").get("key")

                # call method 1.) to generate image label and store as var "labels"
                labels = detectImgLabels(bucket_name=bucket_name, key=key)
                print(key, labels["Labels"])

                # code snippet to create dynamodb item from labels
                db_result = []
                json_labels = json.dumps(labels["Labels"])
                db_labels = json.loads(json_labels)
                for label in db_labels:
                    db_result.append(label["Name"])
                db_item = {
                    "image": {"S": key},
                    "labels": {"S": str(db_result)}
                }

                # call method 2.) to store "db_item" result on DynamoDB
                writeToDynamoDb(tableName=table_name, item=db_item)

                # call method 3.) to send message to SNS
                triggerSNS(str(db_result))

                # call method 4.) to delete img from SQS
                deleteFromSqs(receipt_handle=receipt_handle)

    except Exception as e:
        print(e)
        print("Error processing object {} from bucket {}. ".format(key, bucket_name))
        raise e