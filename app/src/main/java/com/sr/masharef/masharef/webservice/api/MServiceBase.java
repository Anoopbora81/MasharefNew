package com.sr.masharef.masharef.webservice.api;

import android.content.Context;
import android.location.Location;

import com.sr.masharef.masharef.common.JResponseError;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.manager.locale.LocaleManager;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.model.AccessToken;
import com.sr.masharef.masharef.model.user.ACoordinate;
import com.sr.masharef.masharef.utility.CurrentLocation;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.ApiServiceBase;
import com.sr.masharef.masharef.webservice.ApiServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MServiceBase extends ApiServiceBase implements Runnable {

	private static final int MAX_LOC_VALID_TIME = 5 * 60 * 1000;

	public MServiceBase(Context ctx, int api, JSONObject urlParams, JSONObject requestParams,
						boolean addLocation, HashMap<String, String> headers, boolean validateVersion, ServerReuqestMethod method) {
		super(ctx);
		initialize(ctx, ApiServiceUtils.getUrl(ctx, api, urlParams),
				requestParams, addLocation, headers, validateVersion, method);
	}

	public MServiceBase(Context ctx, int api, JSONObject urlParams, JSONObject requestParams,
						boolean addLocation, HashMap<String, String> headers, ServerReuqestMethod method) {
		super(ctx);
		initialize(ctx, ApiServiceUtils.getUrl(ctx, api, urlParams),
				requestParams, addLocation, headers, true, method);
	}

	public MServiceBase(Context ctx, int api, JSONObject urlParams, JSONObject requestParams,
						HashMap<String, String> headers, boolean validateVersion, ServerReuqestMethod method) {
		super(ctx);
		initialize(ctx, ApiServiceUtils.getUrl(ctx, api, urlParams),
				requestParams, false, headers, validateVersion, method);
	}

	public MServiceBase(Context ctx, int api, JSONObject urlParams, JSONObject requestParams,
						HashMap<String, String> headers, ServerReuqestMethod method) {
		super(ctx);
		initialize(ctx, ApiServiceUtils.getUrl(ctx, api, urlParams), 
				requestParams, false, headers, true, method);
	}
	
//	public RUServiceBase(Context ctx, String requestUrl, JSONObject requestParam, 
//			HashMap<String, String> headers, ServerReuqestMethod method){
//		super(ctx);
//		initialize(ctx, requestUrl, requestParam, false, headers, method);
//	}
	
	private void initialize(Context ctx, String requestUrl, JSONObject requestParams,
							boolean addLocation, HashMap<String, String> headers, boolean validateVersion, ServerReuqestMethod method){
		
		this.validateVersion 	= validateVersion;
		this.method 			= method;
		this.requestUrl 		= requestUrl; 
		this.headers 			= bindDefaultHeaders(ctx, headers);
		
		if(requestParams!=null){
			this.requestParams = (addLocation)?addLocationToRequest(requestParams):requestParams;
		}
		else{
			this.requestParams = null;
		}
		
	}
	
	public void run() {
		if(requestUrl == null){
			onError(new JResponseError(ctx.getResources().getString(R.string.null_request_url_msg)));
		}
		else{
			requestApiOperation(requestUrl, method, headers, requestParams);
		}
	}
	
	private HashMap<String, String> bindDefaultHeaders(Context context, HashMap<String, String> headers){
		
		if(headers == null)
			headers = new HashMap<String, String>();
		
		/*
		 * Add Default Headers
		 */
		
		headers.putAll(getDefaultHeaders(context));
		
		return headers; 
	}
	
	public static HashMap<String, String> getDefaultHeaders(Context context){
		HashMap<String, String> defaultHeaders = new HashMap<String, String>();
		defaultHeaders.put("APPNAME-VERSION", StaticContext.getCleintAppVersion(context));
        //defaultHeaders.put("PreferredLanguage", getPreferredLanguage(context));
        defaultHeaders.put("USER-DEVICE", Utils.getDeviceName());
        defaultHeaders.put("USER-AGENT", Utils.getDeviceVersion());
		return defaultHeaders;
	}

    private static String getPreferredLanguage(Context context){

        Locale locale = LocaleManager.getLocale(context);
        if (locale == null){
            locale = context.getApplicationContext().getResources().getConfiguration().locale;
        }
        return locale.getLanguage()+"-"+locale.getCountry();

    }
	
	public static HashMap<String, String> getAccessTokenHeader(Context context){
		
		HashMap<String, String> headers = new HashMap<String, String>();
		
		try {
			AccessToken token = StaticContext.getAccessToken(context);
			Log.i("", "token == "+token);
			if(token.getToken()!=null){
				//headers.put("ACCESS-TOKEN", token.getToken());
				headers.put("Access-Token", token.getToken());
			}
			else{
				headers.put("ACCESS-TOKEN-ERROR", "token.getToken() IS NULL");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			headers.put("ACCESS-TOKEN-ERROR", "StaticContext.getAccessToken(context) Sends the exception: "+e.getMessage());
		}		
		
		return headers;
		
	}
	
	public static JSONObject addLocationToRequest(JSONObject requestParam){
		
		Location location = CurrentLocation.getLocation();
		
		if(isValidLocation(location)){
			ACoordinate loc = new ACoordinate(location);
			try{
				requestParam.putOpt("userLocation", loc.toJson());
			}
			catch(JSONException e){
				e.printStackTrace();
				Log.e("Error At: addLocationToRequest(...) In: ServiceBase With Msg: "+e.getMessage());
			}
		}
		
		return requestParam;
		
	}
	
	private static boolean isValidLocation(Location location){
		
		boolean status = false;
		
		Log.d(TAG, "Validating Location ===>>> "+location);
		
		if(location!=null){
			
			long diff = Math.abs(Calendar.getInstance().getTimeInMillis() - location.getTime());
			
			Log.d(TAG,"Diff ==>>> "+diff);
			Log.d(TAG,"onLocationChanged("+location.getLongitude()+", "+location.getLatitude()+", "+new Date(location.getTime()).toString()+", "+location.getProvider()+")");
			status = (diff<MAX_LOC_VALID_TIME);
			
		}	
		
		return status;
	}
	

	@Override
	protected boolean onResponseReceived(String response, JSONObject JSON)
			throws JResponseError {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onError(Exception e) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDone(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onStreamReceived(InputStream is) throws JResponseError{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onForceLogout(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onProgressUpdate(int progress, int max, String message) {
		// TODO Auto-generated method stub
		
	}

	
}
