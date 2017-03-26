
//AUserTC.java
//RocketUncleCommon

//Created By Eeshan Jamal on 24 Mar, 2015
//Copyright (c) 2015 Smart Communities. All rights reserved.

package com.sr.masharef.masharef.model.user;


import com.sr.masharef.masharef.common.ISO8601DateFormatter;
import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;

import org.json.JSONObject;

import java.util.Calendar;

public class AUserTC extends JsonObj {

	public String title;
	public String content;
	public String Terms_and_conditions;
	public Calendar updatedAt;
	
	public AUserTC(JSONObject map) {
		super(map);
		
		if(!isEmpty){
			title 		= AJSONObject.optString(map, "title", "");
			content 	= AJSONObject.optString(map, "content","");
			Terms_and_conditions 	= AJSONObject.optString(map, "Terms_and_conditions","");

			updatedAt 	= ISO8601DateFormatter.parseToCalendar(AJSONObject.optString(map, "updatedAt"));
		}
		
	}

}
