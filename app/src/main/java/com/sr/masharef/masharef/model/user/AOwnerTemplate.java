package com.sr.masharef.masharef.model.user;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.AOwner;
import com.sr.masharef.masharef.model.JsonObj;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AOwnerTemplate extends JsonObj {

	public ArrayList<AOwner> ownerList;

	public AOwnerTemplate(JSONObject map) {
		super(map);

		if(!isEmpty){
			
			JSONArray ownerArr = AJSONObject.optJSONArray(map, "owners");
			ownerList = new ArrayList<AOwner>();
	        for (int i = 0; i < ownerArr.length(); i++) {
				ownerList.add(new AOwner((JSONObject)ownerArr.optJSONObject(i)));
			}
		}
	}

}
