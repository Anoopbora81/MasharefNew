
package com.sr.masharef.masharef.model.registration;


import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AOwnerModel extends JsonObj {


	public String firstName;
	public String lastName;
	public String phoneNumber;
	public String email;


	public AOwnerModel() {
		// TODO Auto-generated constructor stub
		super();
	}

	public AOwnerModel(JSONObject map) {
		super(map);
		// TODO Auto-generated constructor stub
		if(!isEmpty){
			
			firstName 	= AJSONObject.optString(map, "first_name");
			lastName  	= AJSONObject.optString(map, "last_name");
			email 	  	= AJSONObject.optString(map, "email");
			phoneNumber = AJSONObject.optString(map, "phone_number");
		}	
	}

	
	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		JSONObject json = super.toJson();
		try {
			json.putOpt("first_name", firstName);
			json.putOpt("last_name", lastName);
			json.putOpt("phone_number", phoneNumber);
			json.putOpt("email", email);
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(e.getMessage()+" At toJson() of ARegistrationModel class");
		}
		return json;
	}

}
