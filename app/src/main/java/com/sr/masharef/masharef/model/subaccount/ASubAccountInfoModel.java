
//ASubAccountInfoModel.java
//Created By Zuhair on Jan 24, 2017


package com.sr.masharef.masharef.model.subaccount;


import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ASubAccountInfoModel extends JsonObj {

	public String firstName;
	public String lastName;
	public String gender;
	public String email;
	public String relation;

	public ASubAccountInfoModel() {
		// TODO Auto-generated constructor stub
		super();
	}

	public ASubAccountInfoModel(JSONObject map) {
		super(map);
		// TODO Auto-generated constructor stub
		if(!isEmpty){
			
			firstName 	= AJSONObject.optString(map, "first_name");
			lastName  	= AJSONObject.optString(map, "last_name");
			email 	  	= AJSONObject.optString(map, "email");
			gender 		= AJSONObject.optString(map, "gender");
			relation	= AJSONObject.optString(map, "relation");


		}	
	}

	
	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		JSONObject json = super.toJson();
		try {
			json.putOpt("first_name", firstName);
			json.putOpt("last_name", lastName);
			json.putOpt("email", email);
			json.putOpt("gender", gender);
			json.putOpt("relation", relation);

		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(e.getMessage()+" At toJson() of ASubAccountInfoModel class");
		}
		return json;
	}

}
