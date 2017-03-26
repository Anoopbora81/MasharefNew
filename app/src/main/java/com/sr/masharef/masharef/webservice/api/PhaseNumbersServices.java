
//PhonebookServices.java
//Created By Zuhair on Jan 25, 2017

package com.sr.masharef.masharef.webservice.api;

import android.content.Context;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.JResponseError;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.ApiServiceBase.ServerReuqestMethod;
import com.sr.masharef.masharef.webservice.ApiServiceUtils;

import org.json.JSONObject;

public class PhaseNumbersServices {

	public interface PhaseNumbersServiceListeners{

	/*	//void onSuccess(AContactTemplate contactTemplate);
		void onSuccess();
	//	void onSuccess(ACategoryTemplate categoryTemplate);
		void onFailure(Exception error);
		void onForceLogout(Exception error);*/
		void onSuccess(JSONObject jsonJoiningEvents);
		void onFailure(Exception error);
		void onForceLogout(Exception error);
	}

	private Context context;

	public PhaseNumbersServices(Context context) {
		this.context = context;
	}

	public boolean getPhaseList(final PhaseNumbersServiceListeners listeners){

		MServiceBase service = new MServiceBase(context, R.string.getPhaseListapi, null, null, null, ServerReuqestMethod.GET){

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
