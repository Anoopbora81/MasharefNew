package com.sr.masharef.masharef.webservice;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.JsonReader;
import android.util.JsonToken;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.manager.ApplicationManager;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.preferences.ApiServicePreferences;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Iterator;

public class ApiServiceUtils {


	public static String getUrl(Context ctx){
        return getBaseUrl(ctx);
    }
	
    public static String getUrl(Context ctx, JSONObject params){
        return addQueryToUrl(getBaseUrl(ctx), params);
    }
    
    public static String getUrl(Context ctx, int api){
        return getCompleteApiUrl(ctx, api, ApiServiceBase.ServerUrlAction.None, null);
    }
    
    public static String getUrl(Context ctx, int api, JSONObject params){
        return getCompleteApiUrl(ctx, api, ApiServiceBase.ServerUrlAction.None, params);
    }
   
    //======================================================================
    
    private static String getCompleteApiUrl(Context ctx, int api, ApiServiceBase.ServerUrlAction action, JSONObject params){
        
    	String url = getBaseUrl(ctx) + getApiUrl(ctx, api);
    	
    	/*
    	 * Validate the server url action and add it to the url if there is any specific action.
    	 */
    	
    	if(action== ApiServiceBase.ServerUrlAction.None){
    		//TODO nothing here as no specific action found.
    	}
    	else{
    		url += "/"+action.toString();
    	}
    	
    	/*
    	 * Validate the url params and add if any param exist then add it to the url.
    	 */
    	
    	url = addQueryToUrl(url, params);
    	
    	return url;
    	
    }
    
    private static String getBaseUrl(Context ctx){
		if(ctx == null)
		{
			ctx = ApplicationManager.context;
		}
    	return ApiServicePreferences.getServerProtocol(ctx)+"://"+ApiServicePreferences.getServerUrl(ctx);
    }
    
    
    private static String getApiUrl(Context ctx, int api){
    	
    	String apiStr = "";
    	
    	try{
    		apiStr = ctx.getResources().getString(R.string.api_version);
    	}
    	catch(NotFoundException e){
    		e.printStackTrace();
    		Log.e(ApiServiceBase.TAG, e.getMessage()+" At getApiUrl(...) of ApiServiceUtils");
    	}
    	
    	/*return (apiStr.isEmpty())?apiStr:"/zuhair/"+apiStr+"/"+ctx.getString(api);*/
		return (apiStr.isEmpty())?apiStr:"/"+apiStr+"/"+ctx.getString(api);
    }
    

    //======================================================================
    
    public static String addQueryToUrl(String url, JSONObject params){
    	
    	if(params!=null && params.length()>0){
    		for(Iterator<String> iter = params.keys(); iter.hasNext();) {
			    String key = iter.next();
			    try{
				    String encodedQuery = key+"="+ URLEncoder.encode(AJSONObject.optString(params, key,""),"UTF-8");
	    			url += ((url.contains("?")?"&":"?")+encodedQuery);
			    }
			    catch (UnsupportedEncodingException e) {
		    		Log.e(ApiServiceBase.TAG, e.getMessage()+" At addQueryToUrl(...) of ApiServiceUtils");
					e.printStackTrace();
				}

			}
    	}
    	
    	return url;
    	
    }
    
    //======================================================================
    
    public static void streamPrinter(JsonReader reader){
    	
    	try{
    		
	    	while (true) {
	             JsonToken token = reader.peek();
	             switch (token) {
	             case BEGIN_ARRAY:
	            	 Log.d(ApplicationManager.getInstacne().getLogFile(), "zhr", "Begin Array");
	                 reader.beginArray();
	                 break;
	             case END_ARRAY:    
	                 reader.endArray();
	                 Log.d(ApplicationManager.getInstacne().getLogFile(), "zhr", "End Array");
	                 break;
	             case BEGIN_OBJECT:
	            	 Log.d(ApplicationManager.getInstacne().getLogFile(), "zhr", "Begin Object");
	            	 reader.beginObject();
	                 break;
	             case END_OBJECT:
	            	 Log.d(ApplicationManager.getInstacne().getLogFile(), "zhr", "End Object");
	            	 reader.endObject();
	                 break;
	             case NAME:
	            	 Log.d(ApplicationManager.getInstacne().getLogFile(), "zhr", "Name: "+reader.nextName());
	                 break;
	             case STRING:
	            	 Log.d(ApplicationManager.getInstacne().getLogFile(), "zhr", "String: "+reader.nextString());
	                 break;
	             case NUMBER:
	                 String n = reader.nextString();
	                 Log.d(ApplicationManager.getInstacne().getLogFile(), "zhr", "Number: "+new BigDecimal(n));
	                 break;
	             case BOOLEAN:
	            	 Log.d(ApplicationManager.getInstacne().getLogFile(), "zhr", "Boolean: "+reader.nextBoolean());
	                 break;
	             case NULL:
	            	 Log.d(ApplicationManager.getInstacne().getLogFile(), "zhr", "Null Value");
	                 break;
	             case END_DOCUMENT:
	            	 Log.d(ApplicationManager.getInstacne().getLogFile(), "zhr", "End Document");
	                 return;
	             }
	    	}
	    	
    	}
    	catch(IOException e){
    		e.printStackTrace();
    	}
    	
    }
    
    
    //======================================================================
    
    public static boolean callService(Context ctx, Runnable r){
    	return callService(ctx, r, true);
    }
    
	public static boolean callService(Context ctx, Runnable r, boolean showAlert){
        
		boolean status = true;
		
		if(Utils.isNetworkConnected(ctx, showAlert)){
			Thread t = new Thread(r);
	        t.setName(r.getClass().getName());
	        t.start();
        }
        else{
        	status = false;
        }    
		
		return status;
		
	}

}
