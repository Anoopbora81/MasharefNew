package com.sr.masharef.masharef.model.gallery;

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

public class AGallery extends JsonObj {



    public String gallery_id;
    public String gallery_url;
    public String gallery_index;
    public String gallery_user_type;
    public String gallery_media_type;
    public String gallery_image_icon;
    public String media_url;
    public String uploaded_date;

    public AGallery(HashMap<String, String> info){
        super();
        if(info!=null){
            gallery_id        = info.get("ID");
            gallery_url      = info.get("name");
        }

    }

    public AGallery(JSONObject map) {
        super(map);
        if (!isEmpty) {
                this.gallery_index = AJSONObject.optString(map, "index","");
                this.gallery_user_type = AJSONObject.optString(map, "is_private","");
                this.gallery_media_type = AJSONObject.optString(map, "media_type","");
                this.gallery_image_icon =(AJSONObject.optString((AJSONObject.optJSONObject(map, "media_list")).optJSONObject("icon"),"image")).toString();
                int private_id = Integer.parseInt(gallery_user_type);
//                int media_image = Integer.parseInt(gallery_user_type);
                this.media_url = AJSONObject.optString(map, "media_url","");
                MDatabaseManager.getInstance().insertGalleryCategeory(private_id,gallery_media_type,gallery_image_icon,media_url);
                System.out.println("gallery index "+gallery_index+" gallery_user_type "+gallery_user_type+" gallery_media_type " +
                        gallery_media_type+" gallery_image_icon"+gallery_image_icon);
        }
    }

    public AGallery(String private_id, String gallery_media_type,String media_image,String media_url){
        super();
        this.gallery_user_type       = private_id;
        this.gallery_media_type     = gallery_media_type;
        this.gallery_image_icon     = media_image;
        this.media_url     = media_url;
    }

    public String getCategoryId() {
        return gallery_id;
    }

    public String getCategoryName() {
        return gallery_url;
    }



    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        try{
            json.putOpt("id", gallery_id);
            json.putOpt("name", gallery_url);

        }
        catch(JSONException e){
            Log.d(JSON_TAG,e.getMessage()+" at ACategory toJson Module!!");
            e.printStackTrace();
        }
        return json;
    }
}
