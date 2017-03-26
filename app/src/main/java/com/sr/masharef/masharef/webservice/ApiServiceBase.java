
package com.sr.masharef.masharef.webservice;

import android.content.Context;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.constants.Constants;
import com.sr.masharef.masharef.manager.ApplicationManager;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.JResponseError;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.preferences.ApiServicePreferences;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author Zuhair, Eeshan,
 */

public abstract class ApiServiceBase {
    
  //======================================================================
	protected static final int APP_FORCE_UPDATE_ERROR_CODE 	= 330345;
	protected static final int AuthorizationErrorCode 		= 401;
	protected static final int TIMEOUT 						= 1000*30;
	protected static final int MAX_PROGRESS					= 100;
	
	public static final String TAG 						= "zhr_api";
	
	protected ServerReuqestMethod method;
	protected String requestUrl;
	protected JSONObject requestParams;
	protected HashMap<String, String> headers;
	protected boolean validateVersion;
	
    protected boolean interrupted = false;
	protected Context ctx;
	
	 public static int getTimeout() {
		return TIMEOUT;
	 }
    
  //======================================================================
    
    public enum ServerUrlAction{
    	None, Local, Facebook 
    }

 //======================================================================
    
    public enum ServerReuqestMethod{
    	GET, POST, DELETE, MULTIPART
    }
    
  //======================================================================
    
    public ApiServiceBase(Context ctx){
    	this.ctx = ctx;
    }
    
  //======================================================================
    
    public void interrupt(){ interrupted = true; }
    
    
  //======================================================================
    public void requestApiOperation(String apiUrl, ServerReuqestMethod method, HashMap<String, String> headers){
    	requestApiOperation(apiUrl, method, headers, null);
    }
    
  //======================================================================
    
    public void requestApiOperation(String apiUrl, ServerReuqestMethod method, HashMap<String, String> headers, JSONObject requestParams/*, HashMap<String, File> data*/){
    	
    	HttpURLConnection conn = null;
    	boolean doneStatus = false;
    	boolean isErrorStream = false;
    	interrupted = false;
    	onProgressUpdate(10, MAX_PROGRESS, ctx.getString(R.string.preparing_req));
    	
        try{
            
            URL url = new URL(apiUrl);
            
            if(Utils.isDebug(ApplicationManager.getInstacne())){//BuildConfig.DEBUG
	            Log.d(TAG, "Request url is  ====>>> "+url);
	            Log.d(TAG, "Request Json is ====>>> "+requestParams);
	            Log.d(TAG, "Request Headers ====>>> "+((headers!=null)?headers.toString():"NULL"));
	            if(!validateVersion)
	            	Log.e(TAG, "Version validation not requested");
            }
            
            conn = getConnection(url, method, headers);
            onProgressUpdate(20, MAX_PROGRESS, ctx.getString(R.string.sending_req));
            conn.connect();
            
            if(onConnection(conn)){
                
            	if(method == ServerReuqestMethod.POST && requestParams!=null ){
            		
            		OutputStreamWriter writer= new OutputStreamWriter(conn.getOutputStream());
                    //writer.write(requestParams.toString(3)); // don't know why its making a problem
					writer.write(requestParams.toString());
                    writer.close();
                    
            	}
            	else if(method == ServerReuqestMethod.MULTIPART){
            		
            		MultipartUtility multipart = new MultipartUtility(conn, "UTF-8");
    				
            		HashMap<String, File> imagesData = new HashMap<String, File>();
            		
    				//======Send Form Fields
    				
    				if(requestParams!=null){
    					for(Iterator<String> iter = requestParams.keys(); iter.hasNext();) {
    					    
    						String key = iter.next();
    					    
    					    if(requestParams.get(key) instanceof JSONObject){
    					    	multipart.addFormField(key, requestParams.getJSONObject(key));
    					    }
    					    else if(requestParams.get(key) instanceof File){
    					    	imagesData.put(key, (File) requestParams.get(key));
    					    }
    					    else{
        					    multipart.addFormField(key, AJSONObject.optString(requestParams, key,""));
    					    }
    					    
    					}
    				}
    				
    				//====Send Form Images
    				
    				multipart.addFormField("action","saveObject");
    				
    				for(Iterator<String> iter = imagesData.keySet().iterator(); iter.hasNext();) {
						
        				String key = iter.next();
        				multipart.addFilePart(key, imagesData.get(key));
        				
        				Log.d("Image Field Added =====> "+imagesData.get(key));
					}
    				
    				multipart.completeInput();
            		
            	}
            	
            	/*
            	 *  I put the try catch block here just because it's not necessary that
            	 *  error stream is not null. It gets all the data after an attempt 
            	 *  to get data from server.   
            	 */
            	
            	InputStream stream = null;
            	
            	try{
            		onProgressUpdate(30, MAX_PROGRESS, ctx.getString(R.string.receiving_data));
					stream = conn.getInputStream();

					int status = conn.getResponseCode();

					if (status == HttpsURLConnection.HTTP_OK){


						stream = conn.getInputStream();
						//BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));

						//StringBuffer sb = new StringBuffer("");
						//String line="";

						//while((line = in.readLine()) != null) {

						//	sb.append(line);
						//	break;
						//}

						//byte[] bytes = sb.toString().getBytes();

						/*
						 * Get ByteArrayInputStream from byte array.
						 */

						//InputStream inputStream = new ByteArrayInputStream(bytes);
						//stream = inputStream;//conn.getInputStream();

						//in.close();
						//return sb.toString();

					}else {
						onError(new JResponseError(ctx.getResources().getString(R.string.null_response_msg)));
					}

            	}
            	catch(IOException e){
            		e.printStackTrace();
            		Log.e(TAG,e.getMessage()+" At requestApiOperation(...) of ApiServiceBase");
            		
            		stream = conn.getErrorStream();
            		isErrorStream = true;
            	}
            	
            	if(stream==null){
            		Log.i(TAG,"Null Stream!!!");
            		onError(new JResponseError(ctx.getResources().getString(R.string.null_response_msg)));
            	}
            	else if(isErrorStream){
            		Log.i(TAG,"Error Stream!!!");
    				onError(getStreamJResponseError(stream));
    			}
            	else if(validateVersion && !isValidAppVersion(ctx, conn.getHeaderField("RocketUncle-App-Min-Ver"))){
            		ApplicationManager.getInstacne().requestAppUpdate();
            		onError(new JResponseError(Utils.getAppName(ctx)+" not updated."));
            	}
            	else if(!interrupted){
                
            		if(onStreamReceived(stream)){
                		/*Nothing need to be done here as the response is already processed by sub-class.*/
                	}
            		else{
            			
            			onProgressUpdate(60, MAX_PROGRESS, ctx.getString(R.string.processing_data));
	                	
                    	String responseStr = getString(stream);
                    	onProgressUpdate(70, MAX_PROGRESS, ctx.getString(R.string.processing_data));
                    	Log.d(TAG,"Response String ===>>> "+responseStr);
                    	
                    	if(responseStr==null){
                    		onError(new JResponseError(ctx.getResources().getString(R.string.null_response_msg)));
                    	}
                    	else{
                    		
                    		JSONObject jsonObj = (responseStr.length()>0)?new JSONObject(responseStr):new JSONObject();
                			
                    		onProgressUpdate(80, MAX_PROGRESS, ctx.getString(R.string.processing_data));
                			//String code =  AJSONObject.optString(jsonObj, "code");
                			if(isValidResponse(getJResponseError(jsonObj, "", "", ""))){
                				Log.d(TAG,"On Response Recieved");
                				onProgressUpdate(90, MAX_PROGRESS, ctx.getString(R.string.processing_data));
    							doneStatus = onResponseReceived(responseStr, jsonObj);
    						}else{
								onError(getJResponseError(jsonObj, "", "", ""));
//								doneStatus = onResponseReceived(responseStr, jsonObj);
							}
                			
                    	}
            		}
	            	
            	}     
                
            }
            else{
            	Log.d(TAG, "Error In Connection!!!");
            	onError(new JResponseError("Error in Connection!!!"));
            }    
            
            conn.disconnect();
            
            if(!interrupted && doneStatus){
            	onDone(this);
            }
            
        }
        catch(MalformedURLException e){
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage()+" Occurs At Request Api Operation MalformedURLException Catched");
        	Log.e(TAG, "Connection Error Stream ==== >>> "+getErrorStream(conn));
            onError(e);
        }
        catch(IOException e){
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage()+" Occurs At Request Api Operation IOException Catched");
        	Log.e(TAG, "Connection Error Stream ==== >>> "+getErrorStream(conn));
            onError(e);
        }
        catch(OutOfMemoryError e){
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage()+" Occurs At Request Api Operation OutOfMemoryError Catched");
        	Log.e(TAG, "Connection Error Stream ==== >>> "+getErrorStream(conn));
        	ApplicationManager.releaseMemory();
            onError(new JResponseError(e.getMessage()));
        }
        catch(NullPointerException e){
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage()+" Occurs At Request Api Operation NullPointerException Catched");
        	Log.e(TAG, "Connection Error Stream ==== >>> "+getErrorStream(conn));
            onError(e);
        }
        catch(JSONException e){
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage()+" Occurs At Request Api Operation JSONException Catched");
        	Log.e(TAG, "Connection Error Stream ==== >>> "+getErrorStream(conn));
            onError(e);
		}
        catch (JResponseError e) {	
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage()+" Occurs At Request Api Operation JSONException Catched");
        	Log.e(TAG, "Connection Error Stream ==== >>> "+getErrorStream(conn));
        	
        	if(isValidResponse(e)){
        		onError(e);
        	}
		}
        catch(Exception e){
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage()+" Occurs At Request Api Operation Exception Catched");
        	Log.e(TAG, "Connection Error Stream ==== >>> "+getErrorStream(conn));
            onError(e);
        }
    	
    }
    
    //======================================================================
    
    private boolean isValidResponse(JResponseError error){
    	
    	boolean status = true;
    	
    	if(error.getCode() == AuthorizationErrorCode){
			onForceLogout(error);
			status = false;
		}
		else if(error.getCode() != Constants.kNone){
			onError(error);
			status = false;
		}
    	
    	return status;
    	
    }
    
    //======================================================================
    
    private JResponseError getStreamJResponseError(InputStream stream){

    	String msg 			= ctx.getResources().getString(R.string.default_response_error_msg);
    	String code			= String.valueOf(Constants.kNone);
    	String type			= String.valueOf(Constants.kNoneTypeError);
    	JSONObject jsonObj	= null;
    	
		try {
			String responseStr = getString(stream);
			jsonObj = (responseStr.length()>0)?new JSONObject(responseStr):new JSONObject();
		} 
		catch (OutOfMemoryError e) {
			e.printStackTrace();
			Log.e(e.getMessage()+" At getStreamJResponseError(...) of ApiServiceBase");
		} 
		catch (IOException e) {
			e.printStackTrace();
			Log.e(e.getMessage()+" At getStreamJResponseError(...) of ApiServiceBase");
		}
		catch (JSONException e) {
			e.printStackTrace();
			Log.e(e.getMessage()+" At getStreamJResponseError(...) of ApiServiceBase");
		}

		if(jsonObj == null){
			return new JResponseError(msg, code, type);
		}
		else{
			return getJResponseError(jsonObj, msg, code, type);
		}
		
    }
    
    private JResponseError getJResponseError(JSONObject jsonObj, String msg, String code, String type){
		
		msg		= AJSONObject.optString(jsonObj, JResponseError.MSG, AJSONObject.optString(jsonObj,
					"Message",AJSONObject.optString(jsonObj, "error", msg)));
		code	= AJSONObject.optString(jsonObj, JResponseError.CODE, code);
		type	= AJSONObject.optString(jsonObj, JResponseError.TYPE, type);
		
		return new JResponseError(msg, code, type);
    }
    
    //======================================================================
    
    private static boolean isValidAppVersion(Context context, String minAppVersion){
    	
    	boolean status = true;
    	
    	if(minAppVersion!=null && minAppVersion.length()>0){
	    	
    		String appVerCodes[] 	= Utils.getAppVersionName(context).split("\\.");
	    	String minAppVerCodes[] = minAppVersion.split("\\.");
	    	
	    	try{
	    		
	    		for (int i = 0; i < minAppVerCodes.length && i < appVerCodes.length; i++){
	    			
	    			int appVerCode = Integer.parseInt(appVerCodes[i]);
	    			int minAppVerCode = Integer.parseInt(minAppVerCodes[i]);
	    			
	    			if(appVerCode == minAppVerCode){
	    				//TODO nothing here
	    			}
	    			else{
	    				status = appVerCode>minAppVerCode;
	    				break;
	    			}
	    			
	    		}
			}
	    	catch(NumberFormatException e){
    			e.printStackTrace();
    			Log.e(e.getMessage()+" At validateAppVersion(...) of ApiServiceBase");
    		}
	    	
    	}	
    	
    	return status;
    	
    }
    
    //======================================================================
    
    private InputStream getErrorStream(HttpURLConnection connection){
    	
    	InputStream stream = null;
    	
    	try{
    		stream = connection.getErrorStream();
    	}
    	catch(Exception e){
    		e.getStackTrace();
    		Log.e(e.getMessage()+" At getErrorStream(...) of ApiServiceBase");
    	}
    	
    	return stream;
    	
    }
    
  //======================================================================
 
    private HttpURLConnection getConnection(URL url, ServerReuqestMethod method, HashMap<String, String> headers) throws IOException {
    	
    	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    	
    	connection.setConnectTimeout(TIMEOUT);
    	addHeaders(connection, headers);
    	
    	/*
    	 * Code below check the preferences and bypass SSL certificate authorization if configuration matched.
    	 */
    	
    	if(ApiServicePreferences.getServerProtocol(ctx) == ApiServicePreferences.ServerUrlProtocolType.https
    			&& ApiServicePreferences.getCertificateAuthority(ctx) == ApiServicePreferences.ServerUrlCertificateAuthority.SelfSigned){
    		Log.e(TAG, "Establishing unsecure connection on HTTPS url.");
    		HttpsURLUnsafeConnection.setup((HttpsURLConnection) connection);
    	}
    	
        switch (method) {
        
			case GET:
				connection.setRequestMethod("GET");
				//connection.setRequestProperty("Connection", "Keep-Alive");
				break;

			case POST:
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-type", "application/json; charset=" + "UTF-8");
		    	//connection.setDoOutput(true);
		    	connection.setDoInput(true);
				break;
				
			case DELETE:
				connection.setRequestMethod("DELETE");
				break;
				
			case MULTIPART:

				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setDoOutput(true); // indicates POST method
				connection.setDoInput(true);
				connection.setRequestProperty("Content-Type","multipart/form-data; boundary=" + MultipartUtility.boundary);
				break;

		
		}
        
    	return connection;

    }
    
 //======================================================================
 
    private void addHeaders(HttpURLConnection connection, Map<String, String> headers){
    	if(headers!=null){
    		String keys[] = headers.keySet().toArray(new String[]{});
    		for (int i = 0; i < keys.length; i++) {
				connection.setRequestProperty(keys[i], headers.get(keys[i]));
			}
    	}
    }
    
 //======================================================================
    
    @Deprecated
    public void callUrl(String rurl, ServerReuqestMethod method){
        
    	boolean status = false;
        try{
            
            interrupted = false;
            HttpURLConnection conn = (HttpURLConnection)new URL(rurl).openConnection();
            conn.setConnectTimeout(TIMEOUT);
            
            if(onConnection(conn)){
                InputStream stream= new BufferedInputStream(conn.getInputStream());
                if(!interrupted)
                	status = onStreamReceived(stream);
            }
            else{
            	
            }
            conn.disconnect();
                        
            if(!interrupted && status){
            	onDone(this);
            }
            
        }catch(Exception e){
            
            Log.d(TAG, e.getMessage()+" At CallUrl in ApiServiceBase Class"); 
            onError(e);
           
        }
    }
    
  //======================================================================
    
    /** Invoked when connection is established */
    protected boolean onConnection(HttpURLConnection conn){
        return true;
    }
    
  //======================================================================
    @Deprecated
    public void postData(String purl, String data){
        
    	HttpURLConnection conn = null;
    	boolean status = false;
        try{
            
            interrupted = false;
            String postData = data;
            
            Log.d(TAG, "request url is "+purl);
            Log.d(TAG, "request xml is "+postData);
            
            URL url = new URL(purl);
            
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestProperty("Content-type", "text/xml; charset=" + "UTF-8");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            
            if(onConnection(conn)){
                
            	Log.d(TAG, "Connection established ====>>> ");
                OutputStreamWriter writer= new OutputStreamWriter(conn.getOutputStream());
                writer.write(postData);
                writer.close();
                
                Log.d(TAG, "Stream Writed =====>>> ");
                
                Log.d(TAG, "Connection Status === "+conn);
                
                InputStream stream=new BufferedInputStream(conn.getInputStream());
                Log.d(TAG, "Stream taken as an input =====>>> ");
                if(!interrupted){
                	status = onStreamReceived(stream);
                }	
                
            }
            else{
            	Log.d(TAG,"Error In Connection =====>>> ");
            }    
            
            conn.disconnect();
            
            if(!interrupted && status){
            	onDone(this);
            }
            
        }catch(Exception e){
        	Log.d(TAG, e.getMessage()+" Occurs At Post Data Module");
        	Log.d(TAG, "Connection Error Stream ==== >>> "+conn.getErrorStream());
            onError(e);
        }
    }
    
  //======================================================================
    
    protected String getString(InputStream is) throws OutOfMemoryError, IOException {

        String s = "";
       /* int l = -1;
        byte[] b = new byte[1024];
        
        while((l=is.read(b)) != -1 && !interrupted){
            s += new String(b, 0 ,l);
        }    
        is.close();*/
        
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, "UTF-8");
        s = writer.toString();
        return (interrupted)?null:s/*Html.fromHtml(s).toString()*/;
        
    }
    
  //======================================================================
    
    protected String convertStreamToString(InputStream is) throws IOException {
    	
        ByteArrayOutputStream oas = new ByteArrayOutputStream(10 * 1024 * 1024);
        
        copyStream(is, oas);
        
        String t = oas.toString();
        oas.close();
        oas = null;

        return t;
    }
    
  //======================================================================
    
    private void copyStream(InputStream is, OutputStream os)
    {
        final int buffer_size = 1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
 
    
 
	
  //======================================================================
	
	public static String getRequestJSON(HashMap<String, String> params){
		
		String requestStr = null;
		try {
			
			JSONObject obj = new JSONObject(params);
			requestStr = obj.toString(3);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(JsonObj.JSON_TAG,e.getMessage()+" at getRequestJSON Module!!");
			e.printStackTrace();
		}
		
		return requestStr;

	}
	
  //======================================================================
   
    /** 
     * This method is invoked when stream is received from server.
     * @param is The stream which contains the data.
     * 
     * @return True if stream is processed else return false.
     */
    
    protected abstract boolean onStreamReceived(final InputStream is) throws JResponseError;
    
    /** 
     * This method is invoked when response processed from input stream.
     * @param response The string formatted response.
     * @param JSON The JSONObjct formatted response.
     * 
     * @throws JSONException In case the response is not in JSON format.
     * 
     *  @return True if you want to invoke onDone(Object obj) api call else return false.
     */
    protected abstract boolean onResponseReceived(final String response, JSONObject JSON) throws JResponseError;
    
    /** 
     * This method is invoked when error occurred during processing.
     */
    protected abstract void onError(final Exception e);
    
    /** 
     * This method is invoked when all things are done and nothing is left now.
     */
    protected abstract void onDone(Object obj);
    
    /** 
     * This method is invoked when all things are done and nothing is left now.
     */
    protected abstract void onForceLogout(final Exception e);
    
    /**
     * This method is invoked when progress gets updated with progress information message.
     */
    protected abstract void onProgressUpdate(int progress, int max, String message);
   
}