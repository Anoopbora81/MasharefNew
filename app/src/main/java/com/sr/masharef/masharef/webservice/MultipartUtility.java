
//MultipartUtility.java

//Created By Zuhair on Jan 9, 2017

package com.sr.masharef.masharef.webservice;


import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class MultipartUtility {

    /*public static String boundary = "Boundary+0xAbCdEfGbOuNdArY";*/
    public static String boundary = "Boundary+*****";

    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset)
            throws IOException {
        initialize(requestURL, null, charset);
    }

    public MultipartUtility(HttpURLConnection httpCon, String charset) throws IOException {
    	initialize(null, httpCon, charset);
    }

    private void initialize(String requestURL, HttpURLConnection httpCon, String charset) throws IOException {

    	this.charset = charset;

        // creates a unique boundary based on time stamp
        //boundary ;
        if(httpCon == null){
        	URL url = new URL(requestURL);
        	httpConn = (HttpURLConnection) url.openConnection();
        	httpConn.setRequestMethod("POST");
        	httpConn.setUseCaches(false);
        	httpConn.setDoOutput(true); // indicates POST method
        	httpConn.setDoInput(true);
        	httpConn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + MultipartUtility.boundary);
        }
        else{
        	this.httpConn = httpCon;
        }

//        httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
//        httpConn.setRequestProperty("Test", "Bonjour");
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    public HttpURLConnection getConnection(){
    	return httpConn;
    }

    /**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    public void addFormField(String name, String value) {

        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();

        Log.i("Form Field key = "+name+" Value = "+value+" Added=====>>>>");

    }

    /**
     * Adds all json keys as a form field to the request
     * @param name field name
     * @param object JSONObject
     */
    public void addFormField(String name, JSONObject object){

    	Iterator<String> keys = object.keys();
    	while(keys.hasNext()){
    		String key = keys.next();
    		if(object.opt(key) instanceof JSONObject){
    			addFormField(name+"["+key+"]", object.optJSONObject(key));
    		}
    		else{
    			addFormField(name+"["+key+"]", AJSONObject.optString(object, key, ""));
    		}
    	}

    }

    /**
     * Adds a upload file section to the request
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();



        FileInputStream inputStream = new FileInputStream(uploadFile);


        int bytesAvailable = inputStream.available();
        int maxBufferSize = 2*1024;

        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = inputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            outputStream.write(buffer, 0, bufferSize);
            bytesAvailable = inputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = inputStream.read(buffer, 0, bufferSize);
        }

     /*   byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }*/
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    
    public void addStreamPart(String fieldName, InputStream inputStream)
            throws IOException {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromStream(inputStream))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();
 
        //FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();
         
        writer.append(LINE_FEED);
        writer.flush();    
    }
 
    /**
     * Adds a header field to the request.
     * @param name - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }
    
    public void completeInput(){
    	writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();
        //Log.d("Writer Length === "+writer.toString());
    }
     
    /**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public List<String> finish() throws IOException {
        List<String> response = new ArrayList<String>();
 
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();
 
        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
 
        return response;
    }
    
    public static void createImagePostWithToken(URL imageUrl, HashMap<String, String> headers, HashMap<String, String> params, File file) {

        String lineEnd = "\r\n";
        String twoHyphens = "--";

        // generating byte[] boundary here

        HttpURLConnection conn = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;

        try
        {
            long contentLength;
            int serverResponseCode;
            String serverResponseMessage;
            //File file = new File(imagePath);            
            FileInputStream fileInputStream = new FileInputStream(file);
            conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);         
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            String stringForLength = new String();

            //stringForLength += "Content-Type: multipart/form-data;boundary=" + boundary;

            //=====Set Headers
            String headerKeys[] = headers.keySet().toArray(new String[]{});
            for (String headerKey : headerKeys) {
            	conn.setRequestProperty(headerKey, headers.get(headerKey));
            	//stringForLength += (headerKey+":"+headers.get(headerKey));
			}

            
            String keys[] = params.keySet().toArray(new String[]{});
            
            for (String key : keys) {
            	stringForLength += twoHyphens + boundary + lineEnd + "Content-Disposition: form-data; name=\""+key+"\"" + lineEnd;
                stringForLength += "Content-Type: text/plain;charset=UTF-8" + lineEnd + "Content-Length: " + params.get(key).length() + lineEnd + lineEnd;
                stringForLength += params.get(key) + lineEnd + twoHyphens + boundary + lineEnd;
			}
            
            

           /* stringForLength += "Content-Disposition: form-data; name=\"text\"" + lineEnd;
            stringForLength += "Content-Type: text/plain;charset=UTF-8" + lineEnd + "Content-Length: " + text.length() + lineEnd + lineEnd;
            stringForLength += text + lineEnd + twoHyphens + boundary + lineEnd;

            stringForLength += "Content-Disposition: form-data; name=\"type\"" + lineEnd;
            stringForLength += "Content-Type: text/plain;charset=UTF-8" + lineEnd + "Content-Length: " + type.length() + lineEnd + lineEnd;
            stringForLength += type + lineEnd + twoHyphens + boundary + lineEnd;*/

            stringForLength += twoHyphens + boundary + lineEnd + "Content-Disposition: form-data; name=\"objectData\"" + lineEnd;
            stringForLength += "Content-Type: application/octet-stream" + lineEnd + "Content-Length: " + file.length() + lineEnd + lineEnd;
            stringForLength += lineEnd + twoHyphens + boundary + twoHyphens + lineEnd;

            int totalLength = stringForLength.getBytes().length + (int)file.length();           
            conn.setFixedLengthStreamingMode(totalLength); 

            Log.d("Total Length ===>>> "+totalLength);
            
            outputStream = new DataOutputStream( conn.getOutputStream() );
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            // access token 

            for (String key : keys) {
            	outputStream.writeBytes("Content-Disposition: form-data; name=\""+key+"\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
                outputStream.writeBytes("Content-Length: " + params.get(key).length() + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(params.get(key) + lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			}
            
/*
            // text 

            outputStream.writeBytes("Content-Disposition: form-data; name=\"text\"" + lineEnd);
            outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            outputStream.writeBytes("Content-Length: " + text.length() + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(text + lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            // type 

            outputStream.writeBytes("Content-Disposition: form-data; name=\"type\"" + lineEnd);
            outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            outputStream.writeBytes("Content-Length: " + type.length() + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(type + lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
*/
            // image

            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"objectData\"" + lineEnd);
            //outputStream.writeBytes(lineEnd);
            outputStream.writeBytes("Content-Type: application/octet-stream" + lineEnd);
            outputStream.writeBytes("Content-Length: " + file.length() + lineEnd);
            outputStream.writeBytes(lineEnd);           

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
            outputStream.write(buffer, 0, bufferSize);          
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            Log.d("connection outputstream size is " + outputStream.size());

            fileInputStream.close();
            
            Log.d("11111");
            
            outputStream.flush();
            
            Log.d("222222");
            
            
            
            // finished with POST request body

            Log.d("Stream Closed Successfully");
         // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            
            Log.d("server response code ===>>> "+ serverResponseCode);
            
            serverResponseMessage = conn.getResponseMessage();

            
            Log.d("server response message ===>>> "+ serverResponseMessage);

            outputStream.close();
            
            Log.d("33333");
            
            conn.disconnect();


        } catch (MalformedURLException e)
        {
            Log.d("posttemplate", "malformed url"+ e);
            //TODO: catch exception;
        } catch (IOException e)
        {
            Log.d("posttemplate", "ioexception"+ e);
            //TODO: catch exception
        }        

    }
    
}