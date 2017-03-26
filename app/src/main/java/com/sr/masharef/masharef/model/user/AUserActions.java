package com.sr.masharef.masharef.model.user;


import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AUserActions extends JsonObj {

	public boolean assignMobile;
	public boolean openTermsAndConditions;
	public boolean openAccountReview;
	public boolean otherInformation;
	public boolean memberInformation;
	public boolean subAccount;


	public AUserActions(JSONObject map) {
		super(map);
		// TODO Auto-generated constructor stub
		if(!isEmpty){
			assignMobile					= map.optBoolean("assignMobile");
			openTermsAndConditions			= map.optBoolean("termsAndCondition");
			openAccountReview				= map.optBoolean("accountReview");
			otherInformation				= map.optBoolean("otherInformation");
			memberInformation				= map.optBoolean("memberInformation");
			subAccount						= map.optBoolean("subAccount");

		}
	}
	
	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		JSONObject json = super.toJson();
		try{
			json.putOpt("assignMobile", String.valueOf(this.assignMobile));
		}	
		catch(JSONException e){
			Log.d(JSON_TAG,e.getMessage()+" at AUserActions toJson Module!!");
			e.printStackTrace();
		}
		return json;
	}

}
