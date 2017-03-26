package com.sr.masharef.masharef.model.event;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.model.JsonObj;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventObjectsTemplate extends JsonObj {

	public ArrayList<AEventObjects> eventList;

	public EventObjectsTemplate(JSONObject map) {
		super(map);

		if (!isEmpty) {

			if (!isEmpty) {

				JSONArray eventsObjectArray = AJSONObject.optJSONArray(map, "event_list");
				if(eventsObjectArray.length() >0)
					MDatabaseManager.getInstance().deleteEvent();
				eventList = new ArrayList<AEventObjects>();
				for (int i = 0; i < eventsObjectArray.length(); i++) {
					eventList.add(new AEventObjects((JSONObject) eventsObjectArray.optJSONObject(i)));
				}
			}
		}
	}
}



