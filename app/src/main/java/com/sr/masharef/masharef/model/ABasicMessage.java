package com.sr.masharef.masharef.model;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Zuhair on 15/02/17.
 */

public class ABasicMessage extends JsonObj{



    private String code;
    private String message;

    public ABasicMessage(HashMap<String, String> info){
        super();

        if(info!=null){
            code    = info.get("code");
            message = info.get("message");
        }

    }

    public ABasicMessage(JSONObject map) {
        super(map);

        if (!isEmpty) {
            this.code       = AJSONObject.optString(map, "code");
            this.message    = AJSONObject.optString(map, "message","");
        }
    }

    public ABasicMessage(String code, String message){
        super();
        this.code       = code;
        this.message    = message;
    }

    public String getOwnerId() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        try{
            json.putOpt("code", code);
            json.putOpt("message", message);
        }
        catch(JSONException e){
            Log.d(JSON_TAG,e.getMessage()+" at ABasicMessage toJson Module!!");
            e.printStackTrace();
        }
        return json;
    }
}
