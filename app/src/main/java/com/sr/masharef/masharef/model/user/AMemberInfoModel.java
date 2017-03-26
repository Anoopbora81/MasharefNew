
//AMemberInfoModel.java

//Created By Zuhiar on Jan 20, 2017


package com.sr.masharef.masharef.model.user;


import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AMemberInfoModel extends JsonObj {

	public int servent;
	public int drivers;
	public int adults;
	public int kids;


	public AMemberInfoModel() {
		// TODO Auto-generated constructor stub
		super();
	}

	public AMemberInfoModel(JSONObject map) {
		super(map);
		// TODO Auto-generated constructor stub
		if(!isEmpty){
			
			adults 	= AJSONObject.optInt(map, "adults");
			kids	= AJSONObject.optInt(map, "kids");
			servent	= AJSONObject.optInt(map, "servent");
			drivers = AJSONObject.optInt(map, "drivers");

		}	
	}

	
	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		JSONObject json = super.toJson();
		try {
			json.putOpt("adults", adults);
			json.putOpt("kids", kids);
			json.putOpt("servent", servent);
			json.putOpt("drivers", drivers);

		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(e.getMessage()+" At toJson() of AMemberInfoModel class");
		}
		return json;
	}

}
