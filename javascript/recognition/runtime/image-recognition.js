const AWS = require('aws-sdk')
const sqs = new AWS.SQS()
const rekognition = new AWS.Rekognition()
const dynamodb = new AWS.DynamoDB()
const sns = new AWS.SNS()

const queueUrl = process.env.SQS_QUEUE_URL
const tableName = process.env.TABLE_NAME
const topicArn = process.env.TOPIC_ARN

// 1.) Detect labels from image with Rekognition

// 2.) Save labels to DynamoDB

// 3.) Publish item to SNS

// 4.) Delete message from SQS

// <<Amazon CodeWhisperer generated code goes here>>

exports.handler = async function (event, context) {
    try {
        var key, bucketName;

        // process message from SQS
        for (var eventRecord of event.Records) {
            const receiptHandle = eventRecord.receiptHandle
            for (var record of JSON.parse(eventRecord.body).Records) {
                const bucketName = record.s3.bucket.name
                const key = record.s3.object.key

                // call method 1.) to generate image label and store as var "labels"

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
            
                // call method 3.) to send message to SNS

                // call method 4.) to delete img from SQS
            }
        }
    } catch (error) {
        console.log(error)
        console.log(`Error processing object ${key} from bucket ${bucketName}.`)
        throw error
    }
}

