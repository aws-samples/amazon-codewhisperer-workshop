const fs = require('fs');
const https = require('https');
const AWS = require('aws-sdk')
const s3 = new AWS.S3()

// 1.) Function to get a file from url

// 2.) Function to upload image to S3

// << Amazon CodeWhisperer generated code goes here >>


exports.handler = async function (event, context) {
    const S3_BUCKET = process.env.BUCKET_NAME

    const url = event.queryStringParameters.url
    const name = event.queryStringParameters.name

    // pass the output of method #1 as input to method #2

    return {
        statusCode: 200,
        body: "Successfully Uploaded Img!"
    }
}
