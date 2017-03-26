
//PhonebookServices.java
//Created By Zuhair on Feb 02, 2017

package com.sr.masharef.masharef.webservice.api;

import android.content.Context;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.JResponseError;
import com.sr.masharef.masharef.model.country.ACountry;
import com.sr.masharef.masharef.model.country.ACountryTemplate;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.ApiServiceUtils;
import com.sr.masharef.masharef.webservice.ApiServiceBase;

import org.json.JSONObject;

public class CountryServices {

	public interface CountryServiceListeners{

		void onSuccess(ACountryTemplate countryTemplate);
		void onSuccess(ACountry country);
		void onFailure(Exception error);
		void onForceLogout(Exception error);
	}

	private Context context;

	public CountryServices(Context context) {
		this.context = context;
	}

	public boolean getCountryList(final CountryServiceListeners listeners){

		MServiceBase service = new MServiceBase(context, R.string.get_country_list_api, null, null, MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.GET){
			@Override
			protected boolean onResponseReceived(String response, JSONObject JSON) throws JResponseError {
				boolean status = super.onResponseReceived(response, JSON);
				if(JSON.isNull("error")){
					if(listeners!=null){
						listeners.onSuccess(new ACountryTemplate(JSON));
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
