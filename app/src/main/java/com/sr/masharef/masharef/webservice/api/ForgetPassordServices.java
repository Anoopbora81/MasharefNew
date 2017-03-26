
//Created By Windows on Jan 27, 2017

package com.sr.masharef.masharef.webservice.api;

import android.content.Context;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.JResponseError;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.ApiServiceBase.ServerReuqestMethod;
import com.sr.masharef.masharef.webservice.ApiServiceUtils;

import org.json.JSONObject;

public class ForgetPassordServices {

	public interface ForgetPassordListeners{
	//	void onSuccess(EventObjectsTemplate jsonArrayOfEvents);
		void onSuccess(JSONObject jsonJoiningEvents);
		void onFailure(Exception error);
		void onForceLogout(Exception error);
	}

	private Context context;

	public ForgetPassordServices(Context context) {
		this.context = context;
	}

	public boolean sendForgetPassword(JSONObject requestJson, final ForgetPassordListeners listeners){

		MServiceBase service = new MServiceBase(context, R.string.forgetPasswordapi, null,  requestJson, MServiceBase.getAccessTokenHeader(context), ServerReuqestMethod.POST){
			@Override
			protected boolean onResponseReceived(String response, JSONObject JSON) throws JResponseError {
				boolean status = super.onResponseReceived(response, JSON);
				if(JSON.isNull("error")){
					if(listeners!=null){
						listeners.onSuccess(JSON);
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

