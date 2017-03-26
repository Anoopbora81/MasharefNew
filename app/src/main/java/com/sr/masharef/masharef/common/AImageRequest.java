
//AImageRequest.java

package com.sr.masharef.masharef.common;

import android.graphics.Bitmap;

import com.sr.masharef.masharef.utility.Log;


public class AImageRequest implements Runnable {

	AImageRequestInfo requestInfo;
	ImageLoader loader;
    
	AImageRequest(ImageLoader  loader, AImageRequestInfo requestInfo){
        this.loader			=	loader;
        this.requestInfo	=	requestInfo;
    }
    
    @Override
    public void run() {
        
    	try{
            
    		if(loader.imageViewReused(requestInfo)){
    			//TODO nothing here.
    		}
    		else{
    			
    			Bitmap bmp=loader.getBitmap(requestInfo.getUrl());
                loader.memCache(requestInfo.getUrl(), bmp);
                
                if(loader.imageViewReused(requestInfo)){
                    //TODO nothing here.
                }
                else{
                	 loader.handleImage(bmp, requestInfo);
                }

    		}    
            
        }
        catch(Throwable th){
            th.printStackTrace();
            Log.e(th.getMessage()+" at run() of ImageRequest");
        }
    	
    }
    
    

}
