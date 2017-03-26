package com.sr.masharef.masharef.model.phonebook;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Zuhair on 25/01/17.
 */

public class ACategory extends JsonObj {



    private String category_id;
    private String category_name;


    public ACategory(HashMap<String, String> info){
        super();

        if(info!=null){
            category_id        = info.get("ID");
            category_name      = info.get("name");

        }

    }

    public ACategory(JSONObject map) {
        super(map);

        if (!isEmpty) {
            this.category_id       = AJSONObject.optString(map, "ID");
            this.category_name     = AJSONObject.optString(map, "name","");
            if(category_id != null && category_name != null) {
                int catId = Integer.parseInt(category_id);
           /*   ArrayList<ACategory> categoryList = MDatabaseManager.getInstance().getCategeoryList();
                if(categoryList.size() >0)
                MDatabaseManager.getInstance().deleteCategeory();*/
                MDatabaseManager.getInstance().insertCategeory(catId, category_name);
            }
        }
    }

    public ACategory(String category_id, String category_name){
        super();
        this.category_id       = category_id;
        this.category_name     = category_name;
    }

    public String getCategoryId() {
        return category_id;
    }

    public String getCategoryName() {
        return category_name;
    }



    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        try{
            json.putOpt("id", category_id);
            json.putOpt("name", category_name);

        }
        catch(JSONException e){
            Log.d(JSON_TAG,e.getMessage()+" at ACategory toJson Module!!");
            e.printStackTrace();
        }
        return json;
    }
}
