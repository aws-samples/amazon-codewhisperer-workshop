var convert = require('xml-js')

// 1.) Convert JSON string to XML string
const jsonToXml = function (json) {
    return convert.json2xml(json, { compact: true, spaces: 4 })
}

// 2.) Send string with HTTP POST
const post = async (url, data) => {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest()
        xhr.open('POST', url)
        xhr.setRequestHeader('Content-Type', 'application/xml')
        xhr.onload = () => {
            if (xhr.status >= 200 && xhr.status < 300) {
                resolve(xhr.response)
            } else {
                reject(xhr.statusText)
            }
        }
        xhr.onerror = () => reject(xhr.statusText)
        xhr.send(data)
    })
}

exports.handler = async function (event, context) {

    // call method 1.) with var "event" to convert json to xml
    const data = jsonToXml(event)
    console.log(data)

    // call method 2.) to post xml
    // await post('https://www.example.com/sendmail', xml)

    return {
        statusCode: 200,
        message: "Success!"
    }
}
