package api;

import java.net.URL;
import java.net.URLConnection;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class FileDownloader {
    
    // 1.) Function to download a file from URL
    public static void downloadFile(String fileURL, String fileName) {
        try {
            // 2.) Open a URL connection
            URLConnection urlConnection = new URL(fileURL).openConnection();

            // 3.) Specify a file name and directory to save the file
            urlConnection.setRequestProperty("Content-Disposition", "attachment; filename=" + fileName);

            // 4.) Get the input stream of the connection
            java.io.InputStream inputStream = urlConnection.getInputStream();

            // 5.) Create a new file and write the contents
            java.io.File file = new java.io.File(fileName);
            java.io.OutputStream outputStream = new java.io.FileOutputStream(file);

            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // 6.) Close the output stream
            outputStream.close();

            // 7.) Close the input stream
            inputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2.) Function to upload image to S3 bucket
    public static void uploadFile(String fileName, String bucketName) {
        // 1.) Build a client
        AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

        // 2.) Upload a file to S3
        s3.putObject(bucketName, fileName, new java.io.File(fileName));
    }
}
