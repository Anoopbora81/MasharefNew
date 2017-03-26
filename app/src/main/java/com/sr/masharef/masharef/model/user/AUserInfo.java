
//AUserInfo.java
//Created By Zuhair on Feb 9, 2017


package com.sr.masharef.masharef.model.user;


import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AUserInfo extends JsonObj {

	public String userId;
	public String profileImage;
	public String profileThumbnail;
	
	public AUserInfo() {
		super();
	}

	
	public AUserInfo(JSONObject map) {
		super(map);
		if(!isEmpty){
			userId				= AJSONObject.optString(map, "userId", "");
			profileImage		= AJSONObject.optString(map, "profileImage", "");
			profileThumbnail	= AJSONObject.optString(map, "profileThumbnail", "");
		}
	}
	
	@Override
	public void defaultInitialization() {
		super.defaultInitialization();
		userId 			= new String();
		profileImage	= new String();
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject json = super.toJson();
		try{
			//json.putOpt("userId", userId);
			json.putOpt("profileImage", profileImage);
            json.putOpt("profileThumbnail", profileThumbnail);
		}
		catch(JSONException e){
			Log.d(JSON_TAG,e.getMessage()+" at AUserInfo toJson Module!!");
			e.printStackTrace();
		}
		
		return json;
	}


}
