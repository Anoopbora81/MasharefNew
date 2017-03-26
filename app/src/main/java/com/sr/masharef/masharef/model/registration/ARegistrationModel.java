
//ARegistrationModel.java

//Created By Zuhiar on May 20, 2014


package com.sr.masharef.masharef.model.registration;


import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ARegistrationModel extends JsonObj {

	public String villaNumber;
	public String phaseNumber;
	public String firstName;
	public String lastName;
	public String gender;
	public String email;
	public String password;
	public boolean isOwner;
	public String selectedlanguage;



	public AOwnerModel owner;


	public ARegistrationModel() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public ARegistrationModel(JSONObject map) {
		super(map);
		// TODO Auto-generated constructor stub
		if(!isEmpty){
			
			firstName 	= AJSONObject.optString(map, "first_name");
			lastName  	= AJSONObject.optString(map, "last_name");
			email 	  	= AJSONObject.optString(map, "email");
			password 	= AJSONObject.optString(map, "password");
			gender 		= AJSONObject.optString(map, "gender");
			villaNumber = AJSONObject.optString(map, "villa_number");
			phaseNumber	= AJSONObject.optString(map, "phase_number");

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
			json.putOpt("password", password);
			json.putOpt("gender", gender);
			json.putOpt("villa_number", villaNumber);
			json.putOpt("phase_number", phaseNumber);
			json.putOpt("is_owner", isOwner);
			if(owner !=null)
				json.putOpt("owner", owner.toJson());
			json.putOpt("language", selectedlanguage);
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(e.getMessage()+" At toJson() of ARegistrationModel class");
		}
		return json;
	}

}
