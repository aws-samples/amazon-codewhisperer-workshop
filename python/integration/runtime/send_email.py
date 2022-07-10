from xml.etree.ElementTree import Element, tostring
import requests

# Transform json to xml
def json_to_xml(json_data):
    """
    :param json_data: json data
    :return: xml data
    """
    xml_data = Element('xml')
    for key, value in json_data.items():
        if isinstance(value, dict):
            xml_data.append(json_to_xml(value))
        else:
            xml_data.append(Element(key))
            xml_data[-1].text = str(value)
    return xml_data

#Send message via HTTP post to the external e-mail server
def post(url, data):
    try:
        r = requests.post(url, data=data)
        return r.text
    except requests.exceptions.RequestException as e:
        print(e)

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
    xml = json_to_xml(event)
    xmlstr = tostring(xml, encoding='utf8', method='xml')
    print(xmlstr)
    return {
        "message": "E-mail sent"
    }