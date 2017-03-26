package com.sr.masharef.masharef.model.phonebook;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.model.JsonObj;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AContactTemplate extends JsonObj {

	public static ArrayList<AContact> contactList;

	public AContactTemplate(JSONObject map, String categoryID) {
		super(map);

		if(!isEmpty){
			JSONArray contactArr = AJSONObject.optJSONArray(map, "contact_list");
			if(contactArr.length() >0) {
				int catID =  Integer.parseInt(categoryID);
				MDatabaseManager.getInstance().deleteContactList(catID);
			}
			contactList = new ArrayList<AContact>();
	        for (int i = 0; i < contactArr.length(); i++) {
				contactList.add(new AContact((JSONObject)contactArr.optJSONObject(i)));
			}
		}
	}

}
