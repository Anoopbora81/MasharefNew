package com.sr.masharef.masharef.common;

//FileCache.java

import android.content.Context;
import android.os.Environment;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.utility.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileCache {
    
	private static final String TAG	= "zhr_file";
			//ApplicationManager.getInstacne().getString(R.string.FileCache);
	
    private File cacheDir;
    
    /**
     * This constructor create the cache dir if not exist for the app. Make sure external storage 
     * is available to use otherwise no directory will be created.
     * 
     * @param identifier Name of sub dir inside app dir.
     * @param context Context of the app which want to use this class.
     * @param hideFiles Boolean set to true if you want to hide the files.
     */ 
    
    public FileCache(String identifier, Context context, boolean hideFiles){

        String state = Environment.getExternalStorageState();
        
        
        if(state.equals(Environment.MEDIA_MOUNTED)){
            cacheDir = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name));
            if(identifier!=null)
                cacheDir = new File(cacheDir, identifier);
        }
        
        if(cacheDir == null){
        	Log.e(TAG, "Oops! External Disk is not available for caching images");
        }
        else{
        
	        Log.d(TAG, "File Path ==>> "+cacheDir.getAbsolutePath());
	        Log.d(TAG, "Exist Status ==>> "+cacheDir.exists());
	        
	        if(!cacheDir.exists()){
	        	
	        	Log.d(TAG, "Directory Created ==>> "+cacheDir.mkdirs());
	        	
	            if(hideFiles){
	                File hide = new File(cacheDir,".nomedia");
	                if(!hide.exists()){
						try {
							hide.createNewFile();
						} catch (IOException e) {e.printStackTrace();}
	                }	
	            }
	            
	        }
	      
        }    
        
    }
    
    /**
     * This api is use to get the cache dir.
     * </br>
     * @return Returns Cache File if available else returns null.
     */
    
    public File getCacheDir(){
    	return cacheDir;
    }
    
    /**
     * This api is use to check whether cache dir is available for use or not.
     * </br>
     * @return Returns true if available else returns false.
     */
    
    public boolean isCacheAvailable(){
    	return cacheDir!=null;
    }
    
    /**
     * This api is used to create a File object using hashcode of string inside the cache dir.
     * @param url Name of file.
     * @return Returns File instance if created successfully.
     * @throws Exception if name or cache is null.
     */
    
    public File getFile(String url) throws Exception {
		return new File(cacheDir, String.valueOf(url.hashCode()));
    }
    
    /**
     * This api is used to create a File object using default image naming convention.
     * @param context The Context of the app.
     * @return Returns File instance if created successfully.
     * @throws Exception if cache is null.
     */
    public File getImageFile(Context context) throws Exception {
    	return getImageFile(context, null);
    }
    
    public File getImageFile(Context context, String imageFileName) throws Exception {
    	
    	if(imageFileName == null){
    		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    		imageFileName = "IMG_" + timeStamp +".jpg";
    	}
    	
        return new File(cacheDir, imageFileName);
	        
	}
    
    /**
     * This api is use to save the file into the cache dir.
     * @param is InputStream of the file to cache.
     * @param url Url of the file whose stream you want to cache.
     * @return Returns true if cached successfully else returns false.
     */
    
    public boolean cacheStreamToDisk(InputStream is, String url){
    	
    	isCacheAvailable();
    	OutputStream os;
		boolean cached = false;
    	try {
    		if(isCacheAvailable()){
				os = new FileOutputStream(getFile(url));
				copyStream(is, os);
		        os.close();
		        cached = true;
    		}
    		else{
    			//TODO nothing here
    		}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At cacheStreamToDisk(...) of cacheStreamToDisk");
		} 
		catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At cacheStreamToDisk(...) of cacheStreamToDisk");
		}
    	catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At cacheStreamToDisk(...) of cacheStreamToDisk");
		}
    	
    	return cached;
        
    }
    
    /**
     * This api is use to get the InputStream from the file saved into cache dir.
     * @param url Url of the file whose stream you want to get.
     * @return Returns InputStream if found else returns null.
     */
    
    public FileInputStream getStreamFromCache(String url){
        
        try{
            
            File f = new File(cacheDir, String.valueOf(url.hashCode()));
            if(f.exists())
                return new FileInputStream(f);
            
        }
        catch(Exception e){
        	e.printStackTrace(); 
        	Log.e(TAG, e.getMessage()+" At getStreamFromCache(...) of FileCache");
        }
        
        return null;
    }
    
    /**
     * This api is use for copying the input stream into the output stream.
     * @param is InputStream from which you want to copy.
     * @param os OutputStream into which you want to copy.
     * @return Returns true if done successfully else returns false.
     */
    
    public boolean copyStream(InputStream is, OutputStream os)
    {
    	boolean status = false;
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
            status = true;
        }
        catch(Exception e){
        	e.printStackTrace();
    		Log.e(TAG, e.getMessage()+" At copyStream(...) of ImageLoder");
        }
        
        return status;
        
    }
    
    /**
     * This api clears the files & dir of the cache dir.
     * @return Returns true if done successfully else returns false.
     */
    
    public boolean clear(){
    	
    	boolean status = false;
    	try{
	        FileUtils.cleanDirectory(cacheDir);
	        status = true;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		Log.e(TAG, e.getMessage()+" At clear() of ImageLoder");
    	}
    	
    	return status;
    	
    }
    
    /**
     * This api clear the having the same file id.
     * @return Returns true if done successfully else returns false.
     */
    
    public boolean clear(String id){
    	
    	boolean status = false;
    	try{
    		File f = new File(cacheDir, String.valueOf(id.hashCode()));
	        if(f.exists()){
	        	status = f.delete();
	        }	
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		Log.e(TAG, e.getMessage()+" At clear() of ImageLoder");
    	}
    	
    	return status;
    	
    }

}