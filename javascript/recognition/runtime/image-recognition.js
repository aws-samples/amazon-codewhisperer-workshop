const AWS = require('aws-sdk')
const sqs = new AWS.SQS()
const rekognition = new AWS.Rekognition()
const dynamodb = new AWS.DynamoDB()
const sns = new AWS.SNS()

const queueUrl = process.env.SQS_QUEUE_URL
const tableName = process.env.TABLE_NAME
const topicArn = process.env.TOPIC_ARN

// 1.) Detect labels from image with Rekognition as "labels"
const detectImgLabels = async (bucketName, key, maxLabels = 10, minConfidence = 70) => {
    const params = {
        Image: {
            S3Object: {
                Bucket: bucketName,
                Name: key
            }
        },
        MaxLabels: maxLabels,
        MinConfidence: minConfidence
    }

    const response = await rekognition.detectLabels(params).promise()
    return response
}

// 2.) Save labels to DynamoDB
const writeToDynamoDb = async (tableName, item) => {
    const params = {
        TableName: tableName,
        Item: item
    }

    await dynamodb.putItem(params).promise()
}

// 3.) Publish item to SNS
const triggerSNS = async (message) => {
    const params = {
        Message: message,
        Subject: "CodeWhisperer Workshop Success!",
        TopicArn: topicArn
    }

    const response = await sns.publish(params).promise()
    console.log(response)
}

// 4.) Delete message from SQS
const deleteFromSqs = async (receiptHandle) => {
    const params = {
        QueueUrl: queueUrl,
        ReceiptHandle: receiptHandle
    }

    await sqs.deleteMessage(params).promise()
}

exports.handler = async function (event, context) {
    try {
        var key, bucketName;

        // process message from SQS
        for (var eventRecord of event.Records) {
            const receiptHandle = eventRecord.receiptHandle
            for (var record of JSON.parse(eventRecord.body).Records) {
                bucketName = record.s3.bucket.name
                key = record.s3.object.key

                // call method 1.) to generate image label and store as var "labels"
                const labels = await detectImgLabels(bucketName, key)
                console.log(key, labels.Labels)

                // code snippet to create dynamodb item from labels
                const dbResult = []
                for (var label of labels.Labels) {
                    dbResult.push(label.Name)
                }

                const dbItem = {
                    "image": { "S": key },
                    "labels": { "S": JSON.stringify(dbResult) }
                }

                // call method 2.) to store "dbItem" result on DynamoDB
                await writeToDynamoDb(tableName, dbItem)
            
                // call method 3.) to send message to SNS
                await triggerSNS(JSON.stringify(dbResult))

                // call method 4.) to delete img from SQS
                await deleteFromSqs(receiptHandle)
            }
        }
    } catch (error) {
        console.log(error)
        console.log(`Error processing object ${key} from bucket ${bucketName}.`)
        throw error
    }
}
