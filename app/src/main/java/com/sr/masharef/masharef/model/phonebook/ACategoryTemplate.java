package com.sr.masharef.masharef.model.phonebook;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.model.JsonObj;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ACategoryTemplate extends JsonObj {

	public ArrayList<ACategory> categoryList;

	public ACategoryTemplate(JSONObject map) {
		super(map);

		if(!isEmpty){
			
			JSONArray categoryArr = AJSONObject.optJSONArray(map, "phone_category");
			if(categoryArr.length() >0)
				MDatabaseManager.getInstance().deleteCategeory();
			categoryList = new ArrayList<ACategory>();
	        for (int i = 0; i < categoryArr.length(); i++) {
				categoryList.add(new ACategory((JSONObject)categoryArr.optJSONObject(i)));
			}
		}
	}

}
