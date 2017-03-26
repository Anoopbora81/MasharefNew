package com.sr.masharef.masharef.model.gallery;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.phonebook.ACategory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AGalleryTemplate extends JsonObj {

	public ArrayList<AGallery> galleryArrayList;

	public AGalleryTemplate(JSONObject map) {
		super(map);

		if(!isEmpty){
			if (!isEmpty) {

				JSONArray galleryObjectArray = AJSONObject.optJSONArray(map, "gallery_category");
				if(galleryObjectArray.length() >0)
					MDatabaseManager.getInstance().deleteGallery();
				galleryArrayList = new ArrayList<AGallery>();
				for (int i = 0; i < galleryObjectArray.length(); i++) {
					galleryArrayList.add(new AGallery((JSONObject) galleryObjectArray.optJSONObject(i)));
				}
			}
		}
	}

}
