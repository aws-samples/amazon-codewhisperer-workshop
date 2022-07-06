import requests
from json2xml import json2xml


# Function to convert JSON to XML
def json_to_xml(json_data):
    return(json2xml.Json2xml(json_data).to_xml())

#Send message via HTTP post to the external e-mail server
def post(url, data):
    try:
        r = requests.post(url, data=data)
        return r.text
    except requests.exceptions.RequestException as e:
        print(e)

def handler(event, context):
    xml = json_to_xml(event['Records'])
    print(xml)
    #post("http://localhost:8080",payload)
    return {
        "message": "E-mail sent"
    }