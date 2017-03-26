
//APhone.java

//Created By Zuhair on Jan 9, 2017


package com.sr.masharef.masharef.model;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class APhone extends JsonObj {

	public String countryCode;
	public String national;
	public String international;

	
	public APhone() {
		super();
	}

	public APhone(JSONObject map) {
		super(map);
		if(!isEmpty){
			countryCode 	= AJSONObject.optString(map, "country_code", "");
			national 		= AJSONObject.optString(map, "national_number", "");
			international 	= AJSONObject.optString(map, "interNational_number", "");
		}
	}

	public String getInternationalNo() {
		return international;
	}
	
	@Override
	public void defaultInitialization() {
		super.defaultInitialization();
		countryCode 	= new String();
		national		= new String();
		international	= new String();
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject json = super.toJson();
		try{
			json.putOpt("countryCode", countryCode);
			json.putOpt("national", national);
			json.putOpt("international", international);
		}
		catch(JSONException e){
			Log.d(JSON_TAG,e.getMessage()+" at APhone toJson Module!!");
			e.printStackTrace();
		}
		
		return json;
	}

}
