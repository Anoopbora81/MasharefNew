package com.sr.masharef.masharef.model.country;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ACountryTemplate extends JsonObj {

	public ArrayList<ACountry> countryList;

	public ACountryTemplate(JSONObject map) {
		super(map);

		if(!isEmpty){
			
			JSONArray countryArr = AJSONObject.optJSONArray(map, "country_list");
			countryList = new ArrayList<ACountry>();
	        for (int i = 0; i < countryArr.length(); i++) {
				countryList.add(new ACountry((JSONObject)countryArr.optJSONObject(i)));
			}
		}
	}

}
