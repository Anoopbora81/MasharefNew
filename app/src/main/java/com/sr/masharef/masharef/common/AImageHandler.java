
//AImageResponse.java

package com.sr.masharef.masharef.common;

import android.graphics.Bitmap;

public class AImageHandler implements Runnable {

	private ImageLoader loader;
	private Bitmap bitmap;
    private AImageRequestInfo requestInfo;
    
    public AImageHandler(ImageLoader loader, Bitmap bitmap, AImageRequestInfo requestInfo){
    	this.loader			= 	loader;
    	this.bitmap			=	bitmap;
    	this.requestInfo	=	requestInfo;
    }
    
    @Override
    public void run()
    {
        
    	if(loader.imageViewReused(requestInfo)){
            //TODO nothing here.
    	}
    	else{
	        if(bitmap!=null)
	            requestInfo.setImage(bitmap);
	        else
	            requestInfo.setDefaultImage();
    	}    
        
    }


}
