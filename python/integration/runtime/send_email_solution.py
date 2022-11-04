from xml.etree.ElementTree import Element, tostring
import requests

# 1.) Convert JSON string to XML string
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

# 2.) Send XML string with HTTP POST
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
    # post('https://www.example.com/sendmail', xml)
    return {
        "status": 200,
        "message": "Success!"
    }