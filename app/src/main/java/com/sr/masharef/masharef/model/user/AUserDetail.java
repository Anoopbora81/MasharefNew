package com.sr.masharef.masharef.model.user;

import android.content.Context;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.common.ISO8601DateFormatter;
import com.sr.masharef.masharef.model.APhone;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.subaccount.ASubAccountModel;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class AUserDetail extends JsonObj {



	public String userId;
	public String profileImage;
	public String profileThumbnail;

	public Date updatedAt;

	public boolean checkedOut;
	public boolean pushRegistered;

	public String firstName;
	public String lastName;
	public String email;
	public APhone phone;

	public String ownerID;
	public String villaNumber;
	public String phaseNumber;

	public String old_password;
	public String new_password;

	public String code;
	public String message;

	public ASubAccountModel subAccount;

    public AUserLocale locale;

	public int user_gender = 0;

	
	private String modelData;

	public AUserActions userActions;
	
	public AUserDetail(Context context, JSONObject map) {
		super(map);
		
		if(!isEmpty){

			userId				= AJSONObject.optString(map, "userId", "");
			profileImage		= AJSONObject.optString(map, "profileImage", "");
			profileThumbnail	= AJSONObject.optString(map, "profileThumbnail", "");

			firstName 			= AJSONObject.optString(map, "firstname", "");
			lastName 			= AJSONObject.optString(map, "lastname", "");
			email				= AJSONObject.optString(map, "email", "");

			code				= AJSONObject.optString(map, "code", "");
			message				= AJSONObject.optString(map, "message", "");

			ownerID				= AJSONObject.optString(map, "ownerID");
			villaNumber			= AJSONObject.optString(map, "villa_number");
			phaseNumber			= AJSONObject.optString(map, "phase_number");

			phone				= new APhone(AJSONObject.optJSONObject(map, "phone"));

            modelData			= map.toString();




            checkedOut		 	= map.optBoolean("checkedOut");
			userActions 		= new AUserActions(AJSONObject.optJSONObject(map, "userActions"));

			subAccount			= new ASubAccountModel(AJSONObject.optJSONObject(map, "sub_account"));


            updatedAt 			= ISO8601DateFormatter.parseToDate(AJSONObject.optString(map, "updatedAt"));
	        pushRegistered 		= map.optBoolean("pushRegistered");
            locale              = new AUserLocale(AJSONObject.optJSONObject(map, "locale"));
           
            //fetchUserAccounts(map);
            
            //fetchAndSortUserTabs(map);
            
		}
	}

	@Override
	public void defaultInitialization() {
		super.defaultInitialization();
		userId 			= new String();
		profileImage	= new String();

		firstName 		= new String();
		lastName 		= new String();
		email 			= new String();
		ownerID		 	= new String();
		villaNumber 	= new String();
		phaseNumber 	= new String();
		phone 			= new APhone();
	}

	public void clearModelData(){
		modelData = null;
	}
	
	public boolean haveModelData(){
		return modelData!=null;
	}
	
	public String getModelData() {
		return modelData;
	}
	
	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		JSONObject json = super.toJson();
		try{
			//json.putOpt("updatedAt", ISO8601DateFormatter.fromDate(updatedAt));
			json.putOpt("firstName", firstName);
			json.putOpt("lastName", lastName);
			json.putOpt("email", email);
			json.putOpt("new_email", email);
			json.putOpt("del_phone", phone.toJson());

			if (old_password != null)
				json.putOpt("old_password", old_password);
			if (new_password != null)
				json.putOpt("new_password", email);

			/*json.putOpt("profileImage", profileImage);
            json.putOpt("profileThumbnail", profileThumbnail);*/

		}
		catch(JSONException e){
			Log.d(JSON_TAG,e.getMessage()+" at AUserDetail toJson Module!!");
			e.printStackTrace();
		}
		
		return json;
	}

}
