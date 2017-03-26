/*
 * AUserLocale.java

 *
 * Created By Zuhair on Jan, 9, 2017
 */

package com.sr.masharef.masharef.model.user;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class AUserLocale extends JsonObj {

    private String displayName;
    private Locale locale;

    public AUserLocale(JSONObject map){
        super(map);
        if (!isEmpty()){
            displayName = AJSONObject.optString(map, "displayName", "");
            locale      = new Locale(AJSONObject.optString(map, "language"), AJSONObject.optString(map, "country"));
        }
    }

    public Locale getLocale(){
        return locale;
    }

    public String getDisplayName(){
        return displayName;
    }

    @Override
    public void defaultInitialization() {
        super.defaultInitialization();
        /*displayName = "Chinese";
        locale      = new Locale("vi","VN");*/
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        try {
            json.putOpt("displayName", displayName);
            json.putOpt("language", locale.getLanguage());
            json.putOpt("country", locale.getCountry());
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At toJson() of AUserLocale");
        }
        return json;
    }

    @Override
    public boolean equals(Object o) {
        boolean status = false;

        if (o instanceof AUserLocale){
            AUserLocale locale = (AUserLocale)o;

            Locale l1 = locale.getLocale();
            Locale l2 = this.locale;
            status = (l1 == l2) || (locale.getLocale().getCountry() == this.locale.getCountry() &&
                    locale.getLocale().getLanguage() == this.locale.getLanguage());
        }

        return status;
    }
}
