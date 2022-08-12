const fs = require('fs');
const https = require('https');
const AWS = require('aws-sdk')
const s3 = new AWS.S3()

// 1.) Function to download a file from URL
const downloadFileFromUrl = async (url, fileName) => {
    return new Promise((resolve, reject) => {
        fileName = `/tmp/${fileName}`;
        const file = fs.createWriteStream(fileName);
        https.get(url, response => {
            response.pipe(file);
            file.on('finish', () => {
                file.close(() => resolve());
            });
        }).on('error', error => {
            fs.unlink(fileName);
            reject(error.message);
        });
    });
}

// 2.) Function to upload image to S3
const uploadImage = async (bucket, fileName) => {
    const inputStream = fs.createReadStream(`/tmp/${fileName}`);

    const params = {
        Bucket: bucket,
        Key: fileName,
        Body: inputStream
    }

    const s3Response = await s3.upload(params).promise()
    return s3Response
}

exports.handler = async function (event, context) {
    const S3_BUCKET = process.env.BUCKET_NAME

    const url = event.queryStringParameters.url
    const name = event.queryStringParameters.name

    // pass the output of method #1 as input to method #2
    await downloadFileFromUrl(url, name)
    await uploadImage(S3_BUCKET, name)

    return {
        statusCode: 200,
        body: "Successfully Uploaded Img!"
    }
}
