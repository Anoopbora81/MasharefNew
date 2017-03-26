
//AImageRequestInfo.java

package com.sr.masharef.masharef.common;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class AImageRequestInfo {

	private String url;
	private int defaultImgRes;
    private ImageView imageView;
    
    public AImageRequestInfo(String u, int defaultRes, ImageView i){
        url=u; 
        defaultImgRes = defaultRes;
        imageView=i;
    }
    
    public String getUrl() {
		return url;
	}

	public int getDefaultImgRes() {
		return defaultImgRes;
	}

	public ImageView getImageView() {
		return imageView;
	}

    public void setDefaultImage(){
    	imageView.setScaleType(ScaleType.CENTER_INSIDE);
    	imageView.setImageResource(defaultImgRes);
    }
    
    public void setImage(Bitmap bitmap){
    	imageView.setScaleType(ScaleType.FIT_XY);
    	imageView.setImageBitmap(bitmap);
    	
    }
    
    public boolean isValid(){
    	return !(url == null || url.isEmpty());
    }
	    

}
