package com.sr.masharef.masharef.model;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class JsonObj {

	public static final String JSON_TAG = "zhr_json";
	protected boolean isEmpty = false; 
	
	public JsonObj(){
		super();
		defaultInitialization();
	}
	
	public JsonObj(JSONObject map){
		super();
		if(map == null) defaultInitialization();
	}
	
	public JsonObj(Cursor cursor){
		super();
		if(cursor == null) defaultInitialization();
	}
	
	public JSONObject toJson(){
		JSONObject jsonObj = new JSONObject();
		return jsonObj;
	}
	
	public void defaultInitialization(){
		//Log.d("defaultInitialization At JsonObj");
		isEmpty = true;
	}
	
	public JSONArray toJsonArray(ArrayList<JsonObj> jsonArray){
		
		JSONArray returnArray = new JSONArray();
		for (JsonObj jsonObj : jsonArray) {
			returnArray.put(jsonObj.toJson());
		}
		return returnArray;
	}

	public boolean isEmpty(){
		return isEmpty;
	}

}
