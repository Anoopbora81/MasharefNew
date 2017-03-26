package com.sr.masharef.masharef.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.widget.ImageView;

import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    
	
	public enum TransformationType{
		None, RoundedCorner, Circle
	}
	
	private static final String TAG	= "zhr_image";
    private MemoryCache memoryCache;
    private FileCache fileCache;
    private Map<ImageView, String> imageViews;
    private ExecutorService executorService;
    private TransformationType transType;
    private Handler handler;
    private int width, height;
    
    
    /**
     * @param context The Context of the current app.
     * @param width The maximum width of image you want to display.
     * @param height The maximum height of image you want to display.
     * @return The newly created instance of an ImageLoader class. 
     */
    
    public ImageLoader(Context context, int width, int height){
    	initalize(null, context, width, height, TransformationType.None);
    }
    
    /**
     * @param context The Context of the current app.
     * @param width The maximum width of image you want to display.
     * @param height The maximum height of image you want to display.
     * @param transType The transformation you want to set into the bitmap.
     * @return The newly created instance of an ImageLoader class. 
     */
    
    public ImageLoader(Context context, int width, int height, TransformationType transType){
    	initalize(null, context, width, height, transType);
    }
    
    /**
     * @param identifier The name of dir inside which you want to cache the images.
     * @param context The Context of the current app.
     * @param width The maximum width of image you want to display.
     * @param height The maximum height of image you want to display.
     * @return The newly created instance of an ImageLoader class. 
     */
    
    public ImageLoader(String identifier, Context context, int width, int height){
        initalize(identifier, context, width, height, TransformationType.None);
    }
    
    /**
     * @param identifier The name of dir inside which you want to cache the images.
     * @param context The Context of the current app.
     * @param width The maximum width of image you want to display.
     * @param height The maximum height of image you want to display.
     * @param transType The transformation you want to set into the bitmap.
     * @return The newly created instance of an ImageLoader class. 
     */
    
    public ImageLoader(String identifier, Context context, int width, int height, TransformationType transType){
        initalize(identifier, context, width, height, transType);
    }
    
    private void initalize(String identifier, Context context, int width, int height, TransformationType transType){
    	
    	this.width 		= width;
		this.height 	= height;
		this.transType	= transType;
		
		handler			= new Handler();
    	memoryCache 	= new MemoryCache();
    	fileCache		= new FileCache(identifier, context, true);
    	
    	imageViews		= Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
        executorService	= Executors.newFixedThreadPool(5);
        
    }
    
    /**
     * This api is use to load the image. Firstly it check the image inside the memory cache. If not available,
     * Then it checked it inside File Cache Dir. If not available there also then download it from server and 
     * set it to the referenced ImageView.
     * 
     * @param url The url of the image you want to load.
     * @param defaultImgRes The default image resource reference.
     * @param imageView The view in which you want to display the image. 
     */
    
    public void loadImage(String url, int defaultImgRes, ImageView imageView)
    {
    	
    	try{
	        tryQueuingPhoto(url, defaultImgRes, imageView);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		Log.e(e.getMessage()+" At loadImage(...) of ImageLoader");
    	}
    }
      
    /**
     * This api is use to load the image. Firstly it check inside File Cache Dir. If not available then download it from server and 
     * set it to the referenced ImageView.
     * 
     * @param url The url of the image you want to load.
     * @param defaultImgRes The default image resource reference.
     * @param imageView The view in which you want to display the image. 
     */
    
    private void tryQueuingPhoto(String url, int defaultImgRes, ImageView imageView)
    {
        AImageRequestInfo reqInfo=new AImageRequestInfo(url, defaultImgRes, imageView);        
        
        if(reqInfo.isValid()){
        	
        	/*
             * Try to find the file from Memory Cache
             */
            Bitmap b = memoryCache.get(url);
            
            if(b!=null){
            	reqInfo.setImage(b);
            }    
            else{
            	reqInfo.setDefaultImage();
            	imageViews.put(imageView, url);
            	executorService.submit(new AImageRequest(this, reqInfo));
            }	
        }
        else{
        	reqInfo.setDefaultImage();
        }
    }
    
    /**
     * This api is use to get the bitmap instance of the provided url.
     * 
     * @param url The url of the image. 
     * @return The Bitmap of the provided url if found else returns null.
     */
    
    public Bitmap getBitmap(String url)
    {
    	
        /*
         * Try to find and decode the file from fileCache Directory
         */
    	
        Bitmap b = decodeFile(url);
        	
        if(b!=null)
            return b;
        
        /*
         * try to download it from server fisrt and then decode it
         */ 
        
        try {
        	
            URL imageUrl = new URL(url+"?id=0");
            
            Log.i(TAG, "Request Image URL => "+url);
            
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            
            InputStream is=conn.getInputStream();
            
            if(fileCache.cacheStreamToDisk(is, url)){
            	b = decodeFile(url);
            }
            else{
            	b = BitmapFactory.decodeStream(is);
            }
            
            conn.disconnect();
            
            return b;
        } 
        catch (Throwable ex){
           ex.printStackTrace();
           
           if(ex instanceof OutOfMemoryError){
               memoryCache.clear();
           }
           
           return null;
           
        }
    }
    
    private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
    	
    	Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);
    	Canvas canvas = new Canvas(output);

    	final int color = 0xff424242;
    	final Paint paint = new Paint();
    	final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    	final RectF rectF = new RectF(rect);
    	final float roundPx = bitmap.getWidth()/2;

    	paint.setAntiAlias(true);
    	paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    	canvas.drawARGB(0, 0, 0, 0);
    	paint.setColor(color);
    	canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, roundPx, paint);
    	
    	//For Circle Configuration
    	//canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);

    	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    	canvas.drawBitmap(bitmap, rect, rect, paint);

    	if(bitmap!=output){
    		bitmap.recycle();
    	}
    	
    	return output;
     
           
    }

    /**
     * This api is use to get the bitmap after reducing it to the smaller size in case 
     * it is bigger than the provide size. 
     * 
     * @param url The url of the image. 
     * @return The Bitmap of the provided url if operation done successfully else returns null.
     */
    
    private Bitmap decodeFile(String url){
        
        Bitmap b = null;
        try {
        	
	        InputStream is = fileCache.getStreamFromCache(url);
	        
	        if(is == null){
	        	//TODO nothing here.
	        }
	        else{
		        
	        	if(width == -1 || height == -1){
		            b = BitmapFactory.decodeStream(is);
		        }
		        else{
		            
		            BitmapFactory.Options opt = new BitmapFactory.Options();
		            opt.inJustDecodeBounds = true;
		            
		            BitmapFactory.decodeStream(is, null, opt);
		            
		            is = fileCache.getStreamFromCache(url);
		            //Log.d(TAG, "Before InSampleSize => "+opt.inSampleSize);
		            opt.inSampleSize = Utils.calculateInSampleSize(opt, width, height);
		            opt.inJustDecodeBounds = false;
		            //Log.d(TAG, "After Calculation InSampleSize => "+opt.inSampleSize);
		            b = BitmapFactory.decodeStream(is, null, opt);
		            
		        }
		        
				is.close();
				
				b = tryTransformation(b);
				
	        }

		} 
        
        catch (IOException e) {e.printStackTrace(); Log.e(e.getMessage()+" At decodeFile(...) of ImageLoader");}
        catch (NullPointerException e) {e.printStackTrace(); Log.e(e.getMessage()+" At decodeFile(...) of ImageLoader");}
        catch (IllegalArgumentException e) {e.printStackTrace(); Log.e(e.getMessage()+" At decodeFile(...) of ImageLoader");}
        
        return b;
    }
    
    private Bitmap tryTransformation(Bitmap bitmap) {

    	if(transType == TransformationType.None){
    		return bitmap;
    	}
    	else{
    	 
    		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);
        	Canvas canvas = new Canvas(output);

        	final int color = 0xff424242;
        	final Paint paint = new Paint();
        	final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        	paint.setAntiAlias(true);
        	paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        	canvas.drawARGB(0, 0, 0, 0);
        	paint.setColor(color);
        	
        	switch (transType) {
        	
				case RoundedCorner:
					
		        	RectF rectF = new RectF(rect);
		        	float corner = 12;
					canvas.drawRoundRect(rectF, corner, corner, paint);
					
					break;
					
				case Circle:
					
					int x = bitmap.getWidth()/2;
					int y = bitmap.getHeight()/2;
					canvas.drawCircle(x, x, x>y?y:x, paint);
					
					break;

			}

        	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        	canvas.drawBitmap(bitmap, rect, rect, paint);

        	if(bitmap!=output){
        		bitmap.recycle();
        	}
        	
        	return output;
    		
    	}   
    }
    
    
    /**
     * This api is use to check whether the requested image view is changed or it is still same.  
     * 
     * @param requestInfo The info of the image request. 
     * @return Returns true if it is same else false.
     */
    
    public boolean imageViewReused(AImageRequestInfo requestInfo){
        String tag=imageViews.get(requestInfo.getImageView());
        if(tag==null || !tag.equals(requestInfo.getUrl()))
            return true;
        return false;
    }
    
    /**
     * This api is use to send a request to the Handler to update the ImageView with provided Bitmap.
     * 
     * @param bitmap The Bitmap which you want to set on the ImageView.
     * @param requestInfo The information of the Request. 
     */
    
    public void handleImage(Bitmap bitmap, AImageRequestInfo requestInfo){
    	AImageHandler handle=new AImageHandler(this, bitmap, requestInfo);
        handler.post(handle);
    }
    
    /**
     * This api is use to put the bitmap into memory cache.
     * 
     * @param url The Url of the Image against which you want to store this bitmap.
     * @param bitmap The Bitmap you want to cache. 
     */
    
    public void memCache(String url, Bitmap bitmap){
    	memoryCache.put(url, bitmap);
    }
    
    /**
     * This api is use to Clear Memory & File Cache
     * 
     */
    
    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
    
    /** 
     * Clears from memory cache and disk cache 
     */
    public void clearFromCache(String url){
        memoryCache.clear(url);
        fileCache.clear(url);
    }

}
