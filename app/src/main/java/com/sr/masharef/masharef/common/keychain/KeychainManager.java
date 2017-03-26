package com.sr.masharef.masharef.common.keychain;

import android.content.Context;
import com.sr.masharef.masharef.common.JResponseError;
import com.sr.masharef.masharef.model.AccessToken;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.preferences.Preferences;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class KeychainManager {

	private static final String TAG = "zhr_key";
	private enum keychainKeys{
		usrDetails, usrName, passwd, accId, authTkn, regId, versionCode, btScannerName, btScannerAddress, deviceTknForFcm
	}
	
	//===================================
	
	public static void storeLocalLoginUsername(Context context, String username)
	{
	    Preferences pref = new Preferences(context);
	    pref.set(keychainKeys.usrName.toString(), username);
	    pref.commit();
	}

	public static String storedLocalLoginUsername(Context context)
	{
	    Preferences pref = new Preferences(context);
	    return pref.get(keychainKeys.usrName.toString());
	}
	
	//===================================
	public static void storeUserDetails(Context context, AUserDetail userDetail){
		
		if(userDetail.haveModelData()){
			Preferences pref = new Preferences(context);
			pref.set(keychainKeys.usrDetails.toString(), userDetail.getModelData());
			pref.commit();
			
			userDetail.clearModelData();
		}
		
	}
	
	public static AUserDetail storedUserDetails(Context context){
		
		Preferences pref = new Preferences(context);
		
		AUserDetail userDetail = null ;
		JSONObject map;
		
		try{
			map = new JSONObject(pref.get(keychainKeys.usrDetails.toString()));
			if(map!=null){
				userDetail = new AUserDetail(context, map);
			}	
		} 
		catch (JSONException e) {
			e.printStackTrace();
			Log.e(e.getMessage()+" At storedUserDetails(...) of KeyChainManager");
		}
		
		
		
		return userDetail;
	}
	

	

	
	//===================================
	
	public static void storeAccessToken(Context context, AccessToken token){
		Preferences pref = new Preferences(context);
		
		String accIDStr;
		String tokenStr;
		
		if(token == null){
			accIDStr = "token is null";
			tokenStr = "token is null";
		}
		else{
			accIDStr = (token.getAccountId() == null)?"token.getAccountId() is null ":token.getAccountId();
			tokenStr = (token.getToken() == null)?"token.getToken() is null ":token.getToken();	
		}
		
	    pref.set(keychainKeys.accId.toString(), accIDStr);
	    pref.set(keychainKeys.authTkn.toString(), tokenStr);
	    pref.commit();
	    
	}
	
	//===================================
	
	public static AccessToken storedAccessToken(Context context) throws Exception {
		
		Preferences pref = new Preferences(context);
				
		String accountID = pref.get(keychainKeys.accId.toString());
		String authToken = pref.get(keychainKeys.authTkn.toString());
		
		if(accountID.isEmpty()){
			throw new JResponseError("Account ID is empty. Auth Token is "+authToken);
		}

		if(authToken.isEmpty()){
			throw new JResponseError("Auth Token is empty. Account ID is "+accountID);
		}
		
		return new AccessToken(accountID, authToken);
		
	}
	
	//===================================
	
	public static void resetLocalLoginUsername(Context context){
		Preferences pref = new Preferences(context);
		pref.removeKey(keychainKeys.usrName.toString());
		pref.commit();
	}
	
	//===================================
	
	public static void resetBtScannerAddress(Context context){
		Preferences pref = new Preferences(context);
		pref.removeKey(keychainKeys.btScannerAddress.toString());
		pref.commit();
	}
	
	//===================================
	
	public static void resetAccessToken(Context context){
		Preferences pref = new Preferences(context);
		pref.removeKey(keychainKeys.accId.toString());
		pref.removeKey(keychainKeys.authTkn.toString());
		pref.commit();
	}
	
	//===================================
	
	public static void resetUserDetails(Context context){
		Preferences pref = new Preferences(context);
		pref.removeKey(keychainKeys.usrDetails.toString());
		pref.commit();
	}

	public static void saveDeviceToken(Context context, String token){
		Preferences pref = new Preferences(context);
		String tokenStr;

		if(token == null){
			tokenStr = "FCM token is null";
		}
		else{
			tokenStr = token;
		}

		pref.set(keychainKeys.deviceTknForFcm.toString(), tokenStr);
		pref.commit();
	}
	public static String getDeviceToken(Context context) throws Exception {

		Preferences pref = new Preferences(context);
		String accTokenFcm = pref.get(keychainKeys.deviceTknForFcm.toString());

		if(accTokenFcm.isEmpty()){
			throw new JResponseError("Auth Token is empty. ");
		}
		return accTokenFcm;
	}
}
