const AWS = require('aws-sdk')
const ddb = new AWS.DynamoDB()
const tableName = process.env.TABLE_NAME

// 1.) Function to list all items from a DynamoDB table

// <<Amazon CodeWhisperer generated code goes here>>

exports.handler = async function (event, context) {
    // call method 1.) to scan items from DynamoDB

    return {
        // body: JSON.stringify(response.Items),
        statusCode: 200
    }
}
