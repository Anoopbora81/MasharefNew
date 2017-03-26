package com.sr.masharef.masharef.model.phonebook;

/**
 * Created by welcome on 1/12/2017.
 */

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.JsonObj;

import org.json.JSONObject;

public class AOTPObjects extends JsonObj {

    public String code;
    public String message;
    public String otp;

    public AOTPObjects(String code, String  message, String otp) {

        this.code       = code;
        this.message    = message;
        this.otp        = otp;
    }

    public AOTPObjects(JSONObject map) {
        super(map);
        if (!isEmpty) {

            this.code       = AJSONObject.optString(map, "code","");
            this.message    = AJSONObject.optString(map, "message","");
            this.otp        = AJSONObject.optString(map, "otp","");

        }
    }
}