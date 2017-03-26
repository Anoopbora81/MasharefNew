package com.sr.masharef.masharef.common;

import android.graphics.Bitmap;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemoryCache {

    private static final String TAG = "MemoryCache";
    private Map<String, SoftReference<Bitmap>> cache;

    public MemoryCache(){
    	cache = Collections.synchronizedMap(new LinkedHashMap<String, SoftReference<Bitmap>>(10,1.5f,true));
    }

    public Bitmap get(String id){
    	
    	Bitmap b = null;
    	
        try{
            b = cache.get(id).get();
        }
        catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage()+" at get(...) of MemoryCache");
        }
        
        return b;
    }

    public void put(String id, Bitmap bitmap){
        try{
            cache.put(id, new SoftReference<Bitmap>(bitmap));
        }
        catch(Throwable th){
            th.printStackTrace();
            Log.e(TAG, th.getMessage()+" at put(...) of MemoryCache");
        }
    }
    
    public void clear(String id) {
        try{
            cache.remove(id);
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage()+" at Clear(...) of MemoryCache");
        }
    }
    
    public void clear() {
        try{
            cache.clear();
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage()+" at clear() of MemoryCache");
        }
    }
    
}