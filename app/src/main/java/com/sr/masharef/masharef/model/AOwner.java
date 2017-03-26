package com.sr.masharef.masharef.model;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Zuhair on 19/01/17.
 */

public class AOwner extends JsonObj{



    private String owner_id;
    private String owner_name;
    private String villa_number;
    private String phase_number;

    public AOwner(HashMap<String, String> info){
        super();

        if(info!=null){
            owner_id        = info.get("id");
            owner_name      = info.get("owner_name");
            villa_number    = info.get("villa_number");
            phase_number    = info.get("phase_number");
        }

    }

    public AOwner(JSONObject map) {
        super(map);

        if (!isEmpty) {
            this.owner_id       = AJSONObject.optString(map, "id");
            this.owner_name     = AJSONObject.optString(map, "owner_name","");
            this.villa_number   = AJSONObject.optString(map, "villa_number","");
            this.phase_number   = AJSONObject.optString(map, "phase_number","");
        }
    }

    public AOwner(String owner_id, String owner_name,String villa_number,String phase_number){
        super();
        this.owner_id       = owner_id;
        this.owner_name     = owner_name;
        this.villa_number   = villa_number;
        this.phase_number   = phase_number;
    }

    public String getOwnerId() {
        return owner_id;
    }

    public String getOwnerName() {
        return owner_name;
    }

    public String getVillaNumber() {
        return villa_number;
    }

    public String getPhaseNumber() {
        return phase_number;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        try{
            json.putOpt("owner_id", owner_id);
            json.putOpt("owner_name", owner_name);
            json.putOpt("villa_number", villa_number);
            json.putOpt("phase_number", phase_number);
        }
        catch(JSONException e){
            Log.d(JSON_TAG,e.getMessage()+" at AOwner toJson Module!!");
            e.printStackTrace();
        }
        return json;
    }
}
