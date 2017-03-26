
//AccountServices.java
//Created By Zuhair on Jan 9, 2017

package com.sr.masharef.masharef.webservice.api;

import android.content.Context;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.JResponseError;
import com.sr.masharef.masharef.model.AccessToken;
import com.sr.masharef.masharef.model.registration.ARegistrationModel;
import com.sr.masharef.masharef.model.user.AOwnerTemplate;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.ApiServiceUtils;
import com.sr.masharef.masharef.webservice.ApiServiceBase;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountServices {

	public interface AccountServiceListeners{
		void onSuccess(AccessToken token);
		void onSuccess(AOwnerTemplate ownerTemplate);
		void onFailure(Exception error);
		void onForceLogout(Exception error);
	}
	
	private Context context;
	
	public AccountServices(Context context) {
		this.context = context;
	}
	
	public boolean doBasicLogin(String villaNumber,String userName, String password, final AccountServiceListeners listeners){
		
		JSONObject params = new JSONObject();
		try {
			params.put("villa_number", villaNumber);
			params.put("username", userName);
			params.put("password", password);

		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(e.getMessage()+" at doBasicLoginWithEmail Module!!");
		}
		
		MServiceBase service = new MServiceBase(context, R.string.login_api, null, params,  null, ApiServiceBase.ServerReuqestMethod.POST){
			
			@Override
			protected boolean onResponseReceived(String response,
					JSONObject JSON) throws JResponseError {
				boolean status = super.onResponseReceived(response, JSON);
				
				if(JSON.isNull("error")){
					if(listeners!=null){
						listeners.onSuccess(new AccessToken(Utils.getMapFromJsonObject(JSON)));
					}
				}
				else{
					throw new JResponseError(Utils.getMapFromJsonObject(AJSONObject.optJSONObject(JSON, "error")));
				}
				
				return status;
				
			}
			
			@Override
			protected void onError(Exception e) {
				super.onError(e);
				if(listeners!=null){
					listeners.onFailure(e);
				}
			}

		};
		
		return ApiServiceUtils.callService(context, service);
		
	}
	

	
	public boolean doRegistration(ARegistrationModel regModel, final AccountServiceListeners listeners){

		MServiceBase service = new MServiceBase(context, R.string.registration_api, null, regModel.toJson(), null, ApiServiceBase.ServerReuqestMethod.POST){
			
			@Override
			protected boolean onResponseReceived(String response, JSONObject JSON) throws JResponseError {
				boolean status = super.onResponseReceived(response, JSON);
				if(JSON.isNull("error")){
					if(listeners!=null){
						listeners.onSuccess(new AccessToken(Utils.getMapFromJsonObject(JSON)));
					}
				}else{
					throw new JResponseError(Utils.getMapFromJsonObject(AJSONObject.optJSONObject(JSON, "error")));
				}
				
				return status;
				
			}
			
			@Override
			protected void onError(Exception e) {
				super.onError(e);
				if(listeners!=null){
					listeners.onFailure(e);
				}
			}

		};
		
		return ApiServiceUtils.callService(context, service);
		
	}


	public boolean getOwnerList(final AccountServiceListeners listeners){

		MServiceBase service = new MServiceBase(context, R.string.get_owner_list_api, null, null, null, ApiServiceBase.ServerReuqestMethod.GET){

			@Override
			protected boolean onResponseReceived(String response, JSONObject JSON) throws JResponseError {
				boolean status = super.onResponseReceived(response, JSON);
				if(JSON.isNull("error")){
					if(listeners!=null){
						listeners.onSuccess(new AOwnerTemplate(JSON));
					}
				}else{
					throw new JResponseError(Utils.getMapFromJsonObject(AJSONObject.optJSONObject(JSON, "error")));
				}

				return status;

			}

			@Override
			protected void onError(Exception e) {
				super.onError(e);
				if(listeners!=null){
					listeners.onFailure(e);
				}
			}

		};

		return ApiServiceUtils.callService(context, service);

	}

}
