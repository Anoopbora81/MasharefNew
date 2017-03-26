package com.sr.masharef.masharef.model.country;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Zuhair on 02/02/17.
 */

public class ACountry extends JsonObj {



    private String country_id;
    private String country_name;
    private String country_code;
    private String countryISN;
    public String flag_icon;


    public ACountry(HashMap<String, String> info){
        super();

        if(info!=null){
            country_id      = info.get("id");
            country_name    = info.get("country_name");
            country_code    = info.get("country_code");
            countryISN      = info.get("country_isn");
            flag_icon       = info.get("flag_icon");
        }

    }

    public ACountry(JSONObject map) {
        super(map);

        if (!isEmpty) {
            this.country_id     = AJSONObject.optString(map, "id");
            this.country_name   = AJSONObject.optString(map, "country_name","");
            this.country_code   = AJSONObject.optString(map, "country_code","");
            this.countryISN     = AJSONObject.optString(map, "country_isn","");
            this.flag_icon      = AJSONObject.optString(map, "flag_icon","");

        }
    }

    public ACountry(String country_id, String country_name, String country_code, String countryISN){
        super();
        this.country_id     = country_id;
        this.country_name   = country_name;
        this.country_code   = country_code;
        this.countryISN     = countryISN;

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        try{
            json.putOpt("id", country_id);
            json.putOpt("country_name", country_name);
            json.putOpt("country_code", country_code);
            json.putOpt("country_isn", countryISN);
            json.putOpt("flag_icon", flag_icon);

        }
        catch(JSONException e){
            Log.d(JSON_TAG,e.getMessage()+" at ACountry toJson Module!!");
            e.printStackTrace();
        }
        return json;
    }


    public String getCountry_id() {
        return country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getCountryISN() {
        return countryISN;
    }

    public String getFlag_icon() {
        return flag_icon;
    }
}
