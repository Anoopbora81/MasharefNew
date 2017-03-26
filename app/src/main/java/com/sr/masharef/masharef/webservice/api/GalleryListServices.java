
//Created By Windows on Jan 27, 2017

package com.sr.masharef.masharef.webservice.api;

import android.content.Context;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.JResponseError;
import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.model.event.EventObjectsTemplate;
import com.sr.masharef.masharef.model.gallery.AGalleryTemplate;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.ApiServiceBase.ServerReuqestMethod;
import com.sr.masharef.masharef.webservice.ApiServiceUtils;

import org.json.JSONObject;

public class GalleryListServices {

	public interface GalleryListServiceListeners{
		void onSuccess(AGalleryTemplate galleryTemplate);
		void onFailure(Exception error);
		void onForceLogout(Exception error);
	}

	private Context context;

	public GalleryListServices(Context context) {
		this.context = context;
	}

	public boolean getGalleryList(JSONObject urlParams,final GalleryListServiceListeners listeners){

		MServiceBase service = new MServiceBase(context, R.string.get_gallery_api, null, urlParams,MServiceBase.getAccessTokenHeader(context), ServerReuqestMethod.POST){

			@Override
			protected boolean onResponseReceived(String response, JSONObject JSON) throws JResponseError {
				boolean status = super.onResponseReceived(response, JSON);
				if(JSON.isNull("error")){
					if(listeners!=null){
//						listeners.onSuccess(JSON);
						listeners.onSuccess(new AGalleryTemplate(JSON));
					}
				}else{
					throw new JResponseError(Utils.getMapFromJsonObject(AJSONObject.optJSONObject(JSON, "error")));
				}

				return status;

			}

			@Override
			protected void onError(Exception e) {
				super.onError(e);
				if(listeners!=null){
					listeners.onFailure(e);
				}
			}

		};

		return ApiServiceUtils.callService(context, service);

	}


}

