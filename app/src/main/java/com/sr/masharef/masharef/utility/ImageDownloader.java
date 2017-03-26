
package com.sr.masharef.masharef.utility;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.constants.Constants;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Zuhair
 */
//======================================================================
public class ImageDownloader {
    
	public interface ImageDownloaderListener{
		
		public void onProccessCompleted(Bitmap bitmap);
		
	}
	
	private static final int MAX_WIDTH = 2048;
    private static final int MAX_HEIGHT = 2048;
    
    private final String TAG	=	"zhr_img";
	
    ConcurrentHashMap<String, ImageDownloaderTask> urlTask = new ConcurrentHashMap<String, ImageDownloaderTask>();
    ConcurrentHashMap<String, ArrayList<ImageView>> urlViews = new ConcurrentHashMap<String, ArrayList<ImageView>>();
    /** Contains urls that return null bitmap */
    private ArrayList<String> nullUrls = new ArrayList<String>();
    
    private File pDiskCache;
    private LruCache<String, Bitmap> cache;
    private int width, height;
    private int defaultImg;//R.drawable.default_img;
    /* Variable determining if caching in memory should be done */
    private boolean memCacheAllowed = true;
    /* Variable determining if image from persistent storage should be loaded asynchronously */
    private boolean asyncFromPStorage = true;
    
   
    //=======================================================
    
    public ImageDownloader(Context c){
    	initiailize(c, Constants.kNone, Constants.kNone, Constants.kNone);
    }
    
    //=======================================================
    
    public ImageDownloader(Context c, int width, int height){
    	initiailize(c, width, height, Constants.kNone);
    }
    
    //=======================================================
    /**@param c Context
     * @param width Desired image width. If you want original image provide -1
     * @param height Desired image height. If you want original image provide -1
     */
    public ImageDownloader(Context c, int width, int height, int defaultImg){
        initiailize(c, width, height, defaultImg);
    }
    
    private void initiailize(Context c, int width, int height, int defaultImg){
    	
    	int memClass = ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        
        int memSize = 1024 * 1024 * memClass / 8;
        this.width = width;
        this.height = height;
        this.defaultImg = defaultImg;
        cache = new LruCache<String, Bitmap>(memSize);
    	
    }
    
    //=======================================================
    /**@param c Context
     * @param cache The LruCache< String, Bitmap> object that you want to use
     * @param width Desired image width. If you want original image provide -1
     * @param height Desired image height. If you want original image provide -1
     */
    public ImageDownloader(Context c, LruCache<String, Bitmap> cache, int width, int height){
        
        this.cache = cache;
        this.width = width;
        this.height = height;
    }
    
    //=======================================================
    public void clear(){
        
        urlTask = null;
        urlViews = null;
        nullUrls = null;
        pDiskCache = null;
    }
    
    //=======================================================
    public LruCache<String,Bitmap> cache(){
        return cache;
    }
    
    //=======================================================
    /** If set set false, image will not be cache in memory (LruCache) */
    public void allowMemoryCache(boolean allowed){
        memCacheAllowed = allowed;
    }
    
    //=======================================================
    private void addViewForScheduledDownload(String url, ImageView view){
        
        //=== add imageView to be scheduled, when downloaded
    	if(view!=null){
	        ArrayList<ImageView> views = urlViews.get(url);
	        if(views==null) 
	        	views = new ArrayList<ImageView>();
	        views.add(view);
	        urlViews.put(url, views);
    	}    
    }
    
    //=======================================================
    
    public static File getCacheDir(Context c, boolean createIt, boolean hideFiles) throws IOException {
    	return getCacheDir(null, c, createIt, hideFiles);
    }
    
    //=======================================================
    
    public static File getCacheDir(String identifier, Context c, boolean createIt, boolean hideFiles)
            throws IOException {
    	
    	File f = null;
        String state = Environment.getExternalStorageState();
        
        
        if(state.equals(Environment.MEDIA_MOUNTED)){
            //==== return external cache
            f = new File(Environment.getExternalStorageDirectory(), c.getString(R.string.app_name));
            if(identifier!=null)
                f = new File(f, identifier);
            
        }/*else{
            //==== return del_phone cache
            f = c.getCacheDir();
        }*/
        
        Log.d("File ===>> "+f);
        
        if(f == null){
        	throw new IOException("No Media Found!!!");
        }
        
        Log.d("File Path ==>> "+f.getAbsolutePath());
        Log.d("Exist Status ==>> "+f.exists()+" Create Status ==>> "+createIt);
        
        if(createIt && !f.exists()){
        	
        	Log.d("Directory Created ==>> "+f.mkdirs());
        	
            if(hideFiles){
                File hide = new File(f,".nomedia");
                if(!hide.exists())
                    hide.createNewFile();
            }                               
        }
        
        return f;
    }
    
    //=======================================================
    /** If true : images will be loaded asynchronously from persistent storage, else otherwise. 
     * Default true. */
    public void loadImageFromPStorageAs(boolean asynchronously){
        asyncFromPStorage = asynchronously;
    }
    
    //=======================================================
    public void clearFromMemoryCache(String url){
        cache.remove(url);
    }
    
  //=======================================================
    public static boolean clearCompleteCache(Context context){
    	
    	boolean status = true;
    	try{
	    	File cacheDir = getCacheDir(context, true, true);
	        FileUtils.cleanDirectory(cacheDir);
    	}
    	catch(IOException e){
    		e.printStackTrace();
    		Log.e(e.getMessage()+" At clearCompleteCache of ImageDownloader class");
    		status = false;
    	}
    	return status;
    }
    
  //=======================================================
    
    public static File getImageFile(Context context) throws IOException {
    	return getImageFile(context, null);
    }
    
    public static File getImageFile(Context context, String imageFileName) throws IOException {
    	
    	if(imageFileName == null){
    		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    		imageFileName = "JPEG_" + timeStamp +".jpg";
    	}
    	
    	
    	Log.d("Image File Name ===>>> "+imageFileName);
        File storageDir = ImageDownloader.getCacheDir("Temp", context, true, true);
        /*File image = File.createTempFile(
            imageFileName,   prefix 
            ".jpg",          suffix 
            storageDir       directory 
        );*/
        return new File(storageDir, imageFileName);
	        
	}
    
    //=======================================================
    public static boolean clearFromCache(Context context, String url){
    	
    	boolean status = true;
    	try{
	    	ImageDownloader downloader = new ImageDownloader(context);
	    	File cacheDir = getCacheDir(context, true, true);
	    	if(cacheDir!=null)
	    		downloader.allowPersistentDiskCaching(cacheDir);
	    	downloader.clearFromCache(url);
    	}
    	catch(IOException e){
    		e.printStackTrace();
    		Log.e(e.getMessage()+" At clearCompleteCache of ImageDownloader class");
    		status = false;
    	}
    	return status;
    	
    }
    
    //=======================================================
    /** Clears from memory cache and disk cache */
    public void clearFromCache(String url){
        cache.remove(url);
        File f = new File(pDiskCache, url.hashCode()+"");
        if(f.exists())f.delete();
    }
 /*   
    //=======================================================
    public void download(String url, ImageDownloaderListener listener){
    	download(url, null, defaultImg, listener);
    }
    
    //=======================================================
    public void download(String url, ImageView view){
    	download(url, view, defaultImg, null);
    }
    
    public void download(String url, int defaultImg, ImageView view){
    	download(url, view, defaultImg, null);
    }*/
    
    //=======================================================
    private void download(String url, ImageView view, int defaultImg, ImageDownloaderListener listener){
        
    	Log.i(TAG, "Download File =>> "+url);
    	
        Bitmap b = memCacheAllowed==true?cache.get(url):null;
        if(b!=null){
            
            Log.i(TAG, "is already in cache");
            if(listener!=null){ 
            	listener.onProccessCompleted(b);
            }	
            else if(view!=null){
        		view.setImageBitmap(b);
            }	
            return;
            
        }else if(asyncFromPStorage && existsInPCache(url)){
            
        	Log.i(TAG, "Exist in PCache Url");
            try{
            	
            	b = getBitmap(getStreamFromPCache(url), url);
            	tryMemCacheBitmap(url, b);
            	
            	if(listener!=null){ 
                	listener.onProccessCompleted(b);
                }	
                else if(view!=null){
            		view.setImageBitmap(b);
                }
            	
                return;
                
            }catch(Exception e){
            	
            	e.printStackTrace();
            	Log.e(TAG, e.getMessage()+" At download(...) of ImageDownloader");
            	
            	if(listener!=null){ 
            		listener.onProccessCompleted(null);
            	}	
            	else if(defaultImg!=Constants.kNone && view!=null){ 
            		view.setImageResource(defaultImg);
            	}	
            	
            }
                
        }else if(nullUrls.contains(url)){
        	
        	Log.i(TAG, "Null Url contains the Download Url");
        	
        	if(listener!=null){ 
        		listener.onProccessCompleted(null);
        	}	
        	else if(defaultImg!=Constants.kNone && view!=null){ 
        		view.setImageResource(defaultImg);
        	}	
            return;
            
        }
        
        if(listener==null && view!=null){
        	addViewForScheduledDownload(url, view);
        }
        
        if(urlTask.containsKey(url)){
        	//TODO nothing here because the task is already in downloading process
        	Log.i(TAG, "Task Already Exist in Queue==>> ");
        	
        }else{
            Log.i(TAG, "Executing the Task for URL after adding in queue==>> ");
            ImageDownloaderTask task = new ImageDownloaderTask(listener);
            urlTask.put(url, task);
            task.execute(url);
        }
        
    }
    
    //=======================================================
    public synchronized Bitmap getBitmap(InputStream is, String url) throws MalformedURLException, IOException {
        
        Bitmap b;
        
        if(width == -1 || height == -1){
            b = BitmapFactory.decodeStream(is);
        }else{
            
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            
            BitmapFactory.decodeStream(is, null, opt);
            
            is = null;
            opt.inSampleSize = Utils.calculateInSampleSize(opt, width, height);
            
            opt.inJustDecodeBounds = false;
            
            is = getAndSaveStream(url);
            b = BitmapFactory.decodeStream(is, null, opt);
            
        }
        
        is.close();
        return b;
    }
    
    
    //====Module for Image Re-Sampling Operation
    
    public static int calculateInSampleSize(int width, int height){
    	return calculateInSampleSize(width, height, MAX_WIDTH, MAX_HEIGHT);
    }
    
    public static int calculateInSampleSize(int width, int height, int maxWidth, int maxHeight){
    	
    	int inSampleSize = 1;
    	
    	int diffWidth;
    	int diffHeight;
    	
    	if(width>maxWidth && height>maxHeight){
    		
    		diffWidth = width - maxWidth;
    		diffHeight = height - maxHeight;
    		
    		if(diffWidth>diffHeight){
    			inSampleSize = applyCalculationInSampleSize(width, maxWidth);
    		}
    		else{
    			inSampleSize = applyCalculationInSampleSize(height, maxHeight);
    		}
    		
    	}
    	else if(width>maxWidth){
    		inSampleSize = applyCalculationInSampleSize(width, maxWidth);
    	}
    	else{
    		inSampleSize = applyCalculationInSampleSize(height, maxHeight);
    	}
    	
    	Log.d("Calculated InSample Size ===>> "+inSampleSize);
    	
    	return inSampleSize;
    	
    }
    
    private static int applyCalculationInSampleSize(int value, int max){
    	
    	int defaultInSampleSize = 1;
    	
    	defaultInSampleSize += value/max;
    	
    	return defaultInSampleSize;
    	
    }
    
    

    /*
     * This module is just used to measure the dimensions and calculate the inSampleSize
     * then pass it to the main reSampleImageOperation module.
     */
    
    public static void reSampleImageOperation(Context context, Uri srcUri, Uri destUri){
    	
    	try{
    		
    	    BitmapFactory.Options options = new BitmapFactory.Options();
    	    options.inJustDecodeBounds = true;
	  		BitmapFactory.decodeFile(srcUri.getPath(), options);
	  			
  			int inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight);
  			
  			reSampleImageOperation(context, srcUri, destUri, inSampleSize);
	  			
    	}
    	catch(NullPointerException e){
    		e.printStackTrace();
    		Log.e(e.getMessage()+" At reSampleImageOperation(...) AProfileFieldMap");
    	}
    	
    }
    
    /*
     * Added the Rotation operation with ReSampling operation just because after
     * Re-sampling, The image file saved on the destUri and image orientation identification 
     * gone from it. So we can't re-correct the orientation after image saved. That's why 
     * rotation operation also performed before saving the image.  
     */
    
    public static void reSampleImageOperation(Context context, Uri srcUri, Uri destUri, int sampleSize){
  		
  		try {
  			
  			System.gc();
  			
  			if(sampleSize > 1){
  			
	  			BitmapFactory.Options options	=	new BitmapFactory.Options();
	  			options.inSampleSize 			= 	sampleSize;
	  			InputStream is 					= 	context.getContentResolver().openInputStream(srcUri);
	  			Bitmap mainBitmap				=	BitmapFactory.decodeStream(is,null,options);
				
	  			if(mainBitmap!=null){
	  				
	  				int rotationAngle = Utils.getImageRotationAngle(srcUri.getPath());
	  				Bitmap rotatedBitmap = Utils.getPortraitViewBitmap(rotationAngle, mainBitmap);
	  				
	  				if(rotatedBitmap == null){
	  					rotatedBitmap = mainBitmap;
	  				}
	  				
	  				FileOutputStream outStream 	= 	new FileOutputStream(destUri.getPath());
	  				rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
	  				outStream.flush();
	  				outStream.close();
				
	  				rotatedBitmap = null;
					mainBitmap.recycle();
					System.gc();
					
	  			}
	  				
				Log.d("Image Resampled & File Saved ===>> "+destUri.getPath());
				
  			}
  			else{
  				Log.d("No need of ReSampling the image");
  			}
			
  		}
  		catch (NullPointerException e) {
  			e.printStackTrace();
  			Log.e(e.getMessage()+" At reSampleImageOperation(...) AProfileFieldMap");
  		}
  		catch (FileNotFoundException e) {
  			e.printStackTrace();
  			Log.e(e.getMessage()+" At reSampleImageOperation(...) AProfileFieldMap");
  		}
  		catch (IOException e) {
  			e.printStackTrace();
  			Log.e(e.getMessage()+" At reSampleImageOperation(...) AProfileFieldMap");
  		}
  		
  		System.gc();
  		
  	}
    
    
    //====Module for Image Resize Operation

    public static void aspectResizeImageOperation(Context context, Uri uri){
    	aspectResizeRotateImageOperation(context, uri, uri, 413, 472);	
    }
    
    /*
     * Added the Rotation operation with Resize operation just because after
     * Re-sizing, The image file saved on the destUri and image orientation identification 
     * gone from it. So we can't re-correct the orientation after image saved. That's why 
     * rotation operation also performed before saving the image.  
     */
    
  	public static void aspectResizeRotateImageOperation(Context context, Uri srcUri, Uri destUri, int MAX_WIDTH, int MAX_HEIGHT){

		System.gc();
		Bitmap mainBitmap = getBitmapFromUri(context, srcUri);
		
		try{
		
  			if(mainBitmap!=null){
	  			
	  			int width  = mainBitmap.getWidth();
	  			int height = mainBitmap.getHeight();
	  			
	  			mainBitmap.recycle();
	  			System.gc();
	  			
	  			int sampleSize  = calculateInSampleSize(width, height, MAX_WIDTH, MAX_HEIGHT);
	  			
	  			BitmapFactory.Options options	=	new BitmapFactory.Options();
	  			options.inSampleSize 			= 	sampleSize;
	  			InputStream is 					= 	context.getContentResolver().openInputStream(srcUri);
	  			mainBitmap						=	BitmapFactory.decodeStream(is,null,options);
	  			
	  			
	  			Bitmap rotatedBitmap = rotationOperation(context, mainBitmap, srcUri, destUri);
	  			
	  			if(rotatedBitmap == null){
	  				
	  				saveImageOnFile(mainBitmap, context, destUri);
	  				
	  				mainBitmap.recycle();
	  				System.gc();
	  				
	  			}
	  			else{
	  				
	  				mainBitmap.recycle();
	  				System.gc();
	  				
	  				saveImageOnFile(rotatedBitmap, context, destUri);
	  				
	  				rotatedBitmap.recycle();
	  				System.gc();
	  				
	  			}
	  			
  			}
		}
		catch(IOException e){
			e.printStackTrace();
			Log.e(e.getMessage()+" At aspectResizeRotateImageOperation(...) of ImageDownloader");
		}

  	}
  	
  	private static Bitmap scaleImageOperation(Context context, Uri uri, int FINAL_WIDTH, int FINAL_HEIGHT){
  		
  		Bitmap mainBitmap = null;
  		Bitmap tempBitmap = getBitmapFromUri(context, uri);
  		
		if(tempBitmap!=null){
  			mainBitmap = scaleImageOperation(tempBitmap, FINAL_WIDTH, FINAL_HEIGHT);
  			tempBitmap.recycle();
  			System.gc();
  		}

  		return mainBitmap;
  		
  	}
  	
  	private static Bitmap getBitmapFromUri(Context context, Uri uri){
  		
  		Bitmap tempBitmap = null;
  		try{
  			tempBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
  		}
  		catch(FileNotFoundException e){
  			e.printStackTrace();
			Log.e(e.getMessage()+" At getBitmapFromUri(...) AProfileFieldMap");
  		}
  		
  		return tempBitmap;
  		
  	}
  	
  	public static boolean rotationOperation(Context context, Uri srcUri, Uri destUri){
  		
  		boolean status = false;
  		
  		Bitmap mainBitmap = getBitmapFromUri(context, srcUri);
  		Bitmap rotatedBitmap = rotationOperation(context, mainBitmap, srcUri, destUri);
  		
  		if(rotatedBitmap!=null){
  			
  			mainBitmap.recycle();
  			System.gc();
  			
  			saveImageOnFile(rotatedBitmap, context, destUri);
  			
  			rotatedBitmap.recycle();
  			System.gc();
  			
  			status = true;
  		}
  		else{
  			mainBitmap.recycle();
  			System.gc();
  		}
  		
  		return status;
  		
  	}
  	
  	private static Bitmap rotationOperation(Context context, Bitmap mainBitmap, Uri srcUri, Uri destUri){
  		
  		Bitmap rotatedBitmap = null;
  		
  		if(mainBitmap!=null){
			int rotationAngle = Utils.getImageRotationAngle(srcUri.getPath());
			if(rotationAngle>0){
				rotatedBitmap = Utils.getPortraitViewBitmap(rotationAngle, mainBitmap);
			}	
		}
  		
  		return rotatedBitmap;
  		
  	}
  	
  	private static void saveImageOnFile(Bitmap mainBitmap, Context context, Uri destUri){
  		
  		try{
			FileOutputStream outStream = new FileOutputStream(destUri.getPath());
			mainBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
			
			outStream.flush();
			outStream.close();
			
			Log.d("Image File Saved ===>> "+destUri.getPath());
		}
		catch (FileNotFoundException e) {
  			e.printStackTrace();
  			Log.e(e.getMessage()+" At rotationOperation(...) of AProfileFieldMap");
  		}
  		catch (IOException e) {
  			e.printStackTrace();
  			Log.e(e.getMessage()+" At rotationOperation(...) of AProfileFieldMap");
  		}
		
		mainBitmap = null;
		
  	}
  	
  	private static Bitmap scaleImageOperation(Bitmap mainBitmap, int FINAL_WIDTH, int FINAL_HEIGHT){
  		
  		int width = mainBitmap.getWidth();
		int height = mainBitmap.getHeight();
		 
		float scaleWidth = ((float) FINAL_WIDTH) / width;
		float scaleHeight = ((float) FINAL_HEIGHT) / height;
		 
		//===Create a matrix for manipulation
		Matrix matrix = new Matrix();
		
		//====Resize the bitmap
		matrix.postScale(scaleWidth, scaleHeight);  				
		
		//====Recreate the bitmap
		return Bitmap.createBitmap(mainBitmap, 0, 0, width, height, matrix, false);
  		
  	}
    
    //=======================================================
    /** Returns bitmap from file cache, else downloads and save at cache */
  	private synchronized InputStream getAndSaveStream(String url) throws MalformedURLException, IOException {
        
        if(existsInPCache(url)){
            
            //Log.i("from disk => "+url);
            return getStreamFromPCache(url);
        }
        else{
            
            //Log.i("downloaded======="+url);
            InputStream is = new URL(url).openStream();
            if(writeToFile(is,url))
                is = getStreamFromPCache(url);
            return is;
        }
    }
    
    //=======================================================
    private boolean writeToFile(InputStream is, String url){
        
        if(pDiskCache == null)
            return false; 
        
        try{
            
            File f = new File(pDiskCache, url.hashCode()+"");
            //Log.i("path =====", f.getAbsolutePath()+".");
            if(!f.exists())
                f.createNewFile();
            
            FileOutputStream fos = new FileOutputStream(f);
            byte[] b = new byte[1024];
            int read = -1;
            while((read = is.read(b)) != -1)
                fos.write(b,0,read);
            
            fos.close();
            is.close();
            
            is = null;
            fos = null;
            
            return true;
            
        }catch(Exception e){Log.ex(e);}
        
        return false;
    }
    
    //=======================================================
    private boolean existsInPCache(String url){
        
        if(pDiskCache == null)
            return false;
        else{
            
            File f = new File(pDiskCache, url.hashCode()+"");
            return f.exists();
        }
    }
    
    //=======================================================
    public File getPCachePath(String url){
        
        if(pDiskCache == null)
            return null;
        else{
            
            File f = new File(pDiskCache, url.hashCode()+"");
            if(f.exists()) return f; else return null;
        }
    }
    
    //=======================================================
    private InputStream getStreamFromPCache(String url){
        
        try{
            
            File f = new File(pDiskCache, url.hashCode()+"");
            if(f.exists())
                return new FileInputStream(f);
            
        }catch(Exception e){Log.ex(e);}
        
        return null;
    }
    
    
    //=======================================================
    /** Removes ongoing downloads of images */
    public void cancelDownloads(){
        
        Set<String> keys = urlTask.keySet();
        for(String key:keys){
            
            urlTask.get(key).cancel(true);
            urlTask.remove(key);
            //Log.i("Task removed for url", key);
        }
    }
    
    //=======================================================
    /** Allows images to be saved on directory, then later retrieved from it. */
    public void allowPersistentDiskCaching(File f){
        
        pDiskCache = f;
    }
    
    //=======================================================================
    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        
        String url;
        ImageDownloaderListener listener;
        public ImageDownloaderTask(ImageDownloaderListener listener) {
			// TODO Auto-generated constructor stub
        	this.listener = listener;
		}
        
        //=======================================================
        @Override
        protected Bitmap doInBackground(String... params) {
            
            try{
                
                url = params[0];
                InputStream is = null;
                
                is = getAndSaveStream(url);                
                
                Bitmap b = getBitmap(is, url);
                
                System.gc();
                
                return b;
                
            }catch(Exception e){
            	e.printStackTrace();
            	Log.d(e.getMessage()+ " At ImageDownloaderTask class ");
            }
            
            return null;
        }
        
        //=======================================================
        @Override
        protected void onPostExecute(Bitmap b){
            
    		if(b == null){
    			nullCacheUrl(url);
            }
    		else{
    			tryMemCacheBitmap(url, b);
    		}
            
        		
        	if(listener!=null){
        		listener.onProccessCompleted(b);
        	}
        	else{
    
    			try{
    				ArrayList<ImageView> views = urlViews.get(url);
    				
	                for(ImageView v : views){
	                	if(b!=null){
	                        v.setImageBitmap(b);
	                    }    
	                    else if(defaultImg!=Constants.kNone) {
	                        	v.setImageResource(defaultImg);
	                    }
	                }
    			}
    			catch(ArrayIndexOutOfBoundsException e){
    				e.printStackTrace();
    				Log.e(e.getMessage()+ "At OnPostExecute(...) of ImageDownloader Async Task");
    			}
    			catch(NullPointerException e){
    				e.printStackTrace();
    				Log.e(e.getMessage()+ "At OnPostExecute(...) of ImageDownloader Async Task");
    			}
    			catch (Exception e) {
    				e.printStackTrace();
    				Log.e(e.getMessage()+ "At OnPostExecute(...) of ImageDownloader Async Task");
				}
	            
        	}
        	
        	Log.d("Removing Task = == ");
            urlTask.remove(url);
            
        }
        
    }
    
    private synchronized void nullCacheUrl(String url){
    	
    	try{
	    	if(!nullUrls.contains(url))
	               nullUrls.add(url);
    	}
    	catch(NullPointerException e){
    		e.printStackTrace();
    		Log.e(e.getMessage()+" At nullCacheUrl(...) of ImageDownloader");
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		Log.e(e.getMessage()+" At nullCacheUrl(...) of ImageDownloader");
		}
    	
    }
    
    private synchronized void tryMemCacheBitmap(String url, Bitmap b){
    	
    	if(memCacheAllowed){
    		try{
    			cache.put(url, b);
    		}
    		catch(NullPointerException e){
    			e.printStackTrace();
    			Log.e(e.getMessage()+" At memCacheBitmap(...) of ImageDownloader");
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    			Log.e(e.getMessage()+" At memCacheBitmap(...) of ImageDownloader");
			}
        }
    	
    }
    
    
    //=======================================================================
    /*
     * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
     */
    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }
        
        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
