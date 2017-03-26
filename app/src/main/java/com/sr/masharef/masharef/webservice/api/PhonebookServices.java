
//PhonebookServices.java
//Created By Zuhair on Jan 25, 2017

package com.sr.masharef.masharef.webservice.api;

import android.content.Context;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.JResponseError;
import com.sr.masharef.masharef.model.phonebook.ACategoryTemplate;
import com.sr.masharef.masharef.model.phonebook.AContact;
import com.sr.masharef.masharef.model.phonebook.AContactTemplate;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.ApiServiceBase.ServerReuqestMethod;
import com.sr.masharef.masharef.webservice.ApiServiceUtils;

import org.json.JSONObject;

public class PhonebookServices {

	public interface PhonebookServiceListeners{

		void onSuccess(AContactTemplate contactTemplate);
		void onSuccess();
		void onSuccess(ACategoryTemplate categoryTemplate);
		void onFailure(Exception error);
		void onForceLogout(Exception error);
	}

	private Context context;

	public PhonebookServices(Context context) {
		this.context = context;
	}

	public boolean getPhoneCategory(final PhonebookServiceListeners listeners){

		MServiceBase service = new MServiceBase(context, R.string.get_phonebook_category_api, null, null, null, ServerReuqestMethod.GET){

			@Override
			protected boolean onResponseReceived(String response, JSONObject JSON) throws JResponseError {
				boolean status = super.onResponseReceived(response, JSON);
				if(JSON.isNull("error")){
					if(listeners!=null){

						listeners.onSuccess(new ACategoryTemplate(JSON));
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

	public boolean getPhoneContactList(final String categoryID, final PhonebookServiceListeners listeners){

		JSONObject param = new JSONObject();

		try {
			param.putOpt("category_id",categoryID);
		}catch (Exception e){
			e.printStackTrace();
		}

		MServiceBase service = new MServiceBase(context, R.string.get_pb_getcontactlist_api, null, param, null, ServerReuqestMethod.POST){
			@Override
			protected boolean onResponseReceived(String response, JSONObject JSON) throws JResponseError {
				boolean status = super.onResponseReceived(response, JSON);
				if(JSON.isNull("error")){
					if(listeners!=null){
						listeners.onSuccess(new AContactTemplate(JSON, categoryID));
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

	public boolean addContact(AContact contact, final PhonebookServiceListeners listeners){

		MServiceBase service = new MServiceBase(context, R.string.add_contact_api, null, contact.toJson(), MServiceBase.getAccessTokenHeader(context), ServerReuqestMethod.POST){
			@Override
			protected boolean onResponseReceived(String response, JSONObject JSON) throws JResponseError {
				boolean status = super.onResponseReceived(response, JSON);
				if(JSON.isNull("error")){
					if(listeners!=null){

						listeners.onSuccess();
					}
				}else{
					throw new JResponseError(Utils.getMapFromJsonObject(AJSONObject.optJSONObject(JSON, "error")));
				}

				return status;
			}
		};


		return ApiServiceUtils.callService(context, service);
	}

}
