
//ASubAccountModel.java
//Created By Zuhair on Jan 24, 2017

package com.sr.masharef.masharef.model.subaccount;

import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ASubAccountModel extends JsonObj {

	private ArrayList<ASubAccountInfoModel> subAccountList;

	private boolean skipped;

	public ASubAccountModel(boolean skipped) {
		this.skipped = skipped;
	}

	public ASubAccountModel(boolean skipped,ArrayList<ASubAccountInfoModel> accountList) {
		this.skipped 		= skipped;
		this.subAccountList = accountList;
	}

	public ASubAccountModel(JSONObject map) {
		super(map);
		// TODO Auto-generated constructor stub
		if(!isEmpty){
			skipped = map.optBoolean("skipped");



		}	
	}

	
	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		JSONObject json = super.toJson();

		try {

			json.putOpt("skipped", skipped);
			if (subAccountList != null){
				
				JSONArray returnArray = new JSONArray();
				for (JsonObj jsonObj : subAccountList) {
					returnArray.put(jsonObj.toJson());
				}
				json.putOpt("sub_account_list", returnArray);
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(e.getMessage()+" At toJson() of ASubAccountModel class");
		}
		return json;
	}

}
