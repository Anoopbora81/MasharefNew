
//AMemberInfoModel.java

//Created By Zuhiar on Jan 20, 2017


package com.sr.masharef.masharef.model.user;


import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AOtherInfoModel extends JsonObj {

	public  String landlineNumber;
	public  String occupation;
	public  String workInstitute;
	public  String workAddress;
	public  String totalNoCars;


	public AOtherInfoModel() {
		// TODO Auto-generated constructor stub
		super();
	}

	public AOtherInfoModel(JSONObject map) {
		super(map);
		// TODO Auto-generated constructor stub
		if(!isEmpty){

			landlineNumber 	= AJSONObject.optString(map, "landline_number");
			occupation		= AJSONObject.optString(map, "occupation");
			workInstitute	= AJSONObject.optString(map, "work_institute");
			workAddress 	= AJSONObject.optString(map, "work_address");
			totalNoCars 	= AJSONObject.optString(map, "total_no_cars");



		}	
	}

	
	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		JSONObject json = super.toJson();
		try {
			json.putOpt("landline_number", landlineNumber);
			json.putOpt("occupation", occupation);
			json.putOpt("work_institute", workInstitute);
			json.putOpt("work_address", workAddress);
			json.putOpt("total_no_cars", totalNoCars);

		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(e.getMessage()+" At toJson() of AOtherInfoModel class");
		}
		return json;
	}

}
