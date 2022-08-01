package integration;

import java.net.URL;
import java.net.URLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class MailServerIntegration {
    // 1.) Convert JSON string to XML string
    public static String jsonToXml(String json) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            ObjectMapper objectMapper = new ObjectMapper();
            return xmlMapper.writeValueAsString(objectMapper.readValue(json, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 2.) Send XML string with HTTP POST
    public static String sendPost(String xml) {
        try {
            URLConnection connection = new URL("https://www.example.com/sendmail").openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setRequestProperty("Accept", "application/xml");
            connection.setRequestProperty("Content-Length", String.valueOf(xml.length()));
            connection.getOutputStream().write(xml.getBytes());
            return connection.getInputStream().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
