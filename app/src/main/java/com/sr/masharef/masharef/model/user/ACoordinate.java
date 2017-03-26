
//ACoordinate.java
//Created By Zuhair on Jan 9, 2017


package com.sr.masharef.masharef.model.user;

import android.location.Location;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ACoordinate extends JsonObj {

	public double longitude;
	public double latitude;
	
	public ACoordinate(Location location) {
		super();
		
		if(location!=null){
			longitude = location.getLongitude();
			latitude  = location.getLatitude();
		}	
		
	}
	
	public ACoordinate(JSONObject map) {
		super(map);
		
		if(!isEmpty){
			longitude = AJSONObject.optDouble(map, "longitude");
			latitude  = AJSONObject.optDouble(map, "latitude");
		}
		
	}
	
	@Override
	public void defaultInitialization() {
		super.defaultInitialization();
		longitude = 0.0;
		latitude  = 0.0;	
	}
	
	@Override
	public JSONObject toJson() {
		
		JSONObject json = super.toJson();
		
		try{
			json.putOpt("longitude", longitude);
			json.putOpt("latitude", latitude);
		}	
		catch(JSONException e){
			Log.d(JSON_TAG,e.getMessage()+" at ACoordinate toJson Module!!");
			e.printStackTrace();
		}
		
		return json;
		
	}
	
	public boolean isEmpty(){
		return (longitude == 0.0 && latitude == 0.0);
	}

}
