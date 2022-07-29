const AWS = require('aws-sdk')
const ddb = new AWS.DynamoDB()
const tableName = process.env.TABLE_NAME

// 1.) Function to list all items from a DynamoDB table
const getAllItemsFromTable = async (tableName) => {
    return ddb.scan({
        TableName: tableName
    }).promise()
}

exports.handler = async function (event, context) {
    // call method 1.) to scan items from DynamoDB
    const response = await getAllItemsFromTable(tableName)

    return {
        body: JSON.stringify(response.Items),
        statusCode: 200
    }
}
