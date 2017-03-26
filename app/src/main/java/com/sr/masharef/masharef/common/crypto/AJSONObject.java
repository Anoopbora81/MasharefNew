
//AJSONObject.java

//Created By Zuhair on Jan 9, 2017


package com.sr.masharef.masharef.common.crypto;

import org.json.JSONArray;
import org.json.JSONObject;

public class AJSONObject {
	
	//======String Methods
	
	public static String optString(JSONObject object, String name) {
		// TODO Auto-generated method stub
		String value;
		if(object.isNull(name)){
			value = null;
		}
		else{
			value = object.optString(name);
		}
		return value;
	}
	
	public static String optString(JSONObject object, String name, String fallback) {
		// TODO Auto-generated method stub
		String value;
		if(object.isNull(name)){
			value = fallback;
		}
		else{
			value = object.optString(name, fallback);
		}
		return value;
	}
	
	//======Double Methods
	
	public static double optDouble(JSONObject object, String name) {
		// TODO Auto-generated method stub
		double value;
		if(object.isNull(name)){
			value = Double.NaN;
		}
		else{
			value = object.optDouble(name);
		}
		return value;
	}
	
	public static double optDouble(JSONObject object, String name, double fallback) {
		// TODO Auto-generated method stub
		double value;
		if(object.isNull(name)){
			value = fallback;
		}
		else{
			value = object.optDouble(name, fallback);
		}
		return value;
	}
	
	//======Long Methods
	
		public static Long optLong(JSONObject object, String name) {
			// TODO Auto-generated method stub
			Long value;
			if(object.isNull(name)){
				value = Long.MIN_VALUE;
			}
			else{
				value = object.optLong(name);
			}
			return value;
		}
		
		public static Long optLong(JSONObject object, String name, Long fallback) {
			// TODO Auto-generated method stub
			Long value;
			if(object.isNull(name)){
				value = fallback;
			}
			else{
				value = object.optLong(name, fallback);
			}
			return value;
		}
	
	//======Int Methods
	
	public static int optInt(JSONObject object, String name) {
		// TODO Auto-generated method stub
		int value;
		if(object.isNull(name)){
			value = 0;
		}
		else{
			value = object.optInt(name);
		}
		return value;
	}
	
	public static int optInt(JSONObject object, String name, int fallback) {
		// TODO Auto-generated method stub
		int value;
		if(object.isNull(name)){
			value = fallback;
		}
		else{
			value = object.optInt(name, fallback);
		}
		return value;
	}
	
	//======JSONObject Method
	
		public static JSONObject optJSONObject(JSONObject object, String name) {
			// TODO Auto-generated method stub
			JSONObject value;
			if(object.isNull(name)){
				value = null;
			}
			else{
				value = object.optJSONObject(name);
			}
			return value;
		}
		
	//======JSONArray Method
	
		public static JSONArray optJSONArray(JSONObject object, String name) {
			// TODO Auto-generated method stub
			JSONArray value;
			if(object.isNull(name)){
				value = null;
			}
			else{
				value = object.optJSONArray(name);
			}
			return value;
		}
	
		
	
	
	
}
