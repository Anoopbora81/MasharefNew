package com.sr.masharef.masharef.manager.net;

import android.content.Context;

import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;

import java.util.ArrayList;

public class NetConnectivityManager {

	public interface NetConnectivityListener {
		public void onNetConnected(boolean isRegistrationCall);
		public void onNetDisconnected(boolean isRegistrationCall);
	}

	private static NetConnectivityManager instance;

	private Context context;
	private ArrayList<NetConnectivityListener> connectListener;
    
    /**
     * This method returns the existing instance of the class if exist otherwise return null.
     */
    
    public static NetConnectivityManager getInstance() {
    	return instance;
    }
    
    /**
     * This method returns the existing instance of the class if exist otherwise create new instance.
     */
    
    public static NetConnectivityManager getInstance(Context context) {
    	if(instance == null){
    		createInstance(context);
    	}
		return instance;
	}
    
    /**
     * This method create new instance of the class.
     */
    
    public static NetConnectivityManager createInstance(Context context) {
    	
    	if(instance == null){
    		//TODO nothing here
    	}
    	else{
    		instance.reset();
    	}
    	
    	instance = new NetConnectivityManager(context);
    	
    	return instance;
    	
    }
    
    /**
     * Notify the register listeners that whenever Internet is turned ON/OFF
     */
    
    public synchronized void informNetworkConnectivityChanged(boolean networkStatus){
    	informNetworkConnectivityChanged(networkStatus, false);
    }
    
    public void informNetworkConnectivityChanged(final boolean networkStatus, 
    		final boolean isRegistrationCall){
    	
    	new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (this) {
					if(networkStatus){
			    		informNetworkConnected(isRegistrationCall);
			    	}
			    	else{
			    		informNetworkDisconnected(isRegistrationCall);
			    	}
				}
			}
		}).start();
    	
    }
    
    /**
     * This method is used to notify the callaback listener once it gets registered in the Manager.
     */
    
    private synchronized void informRegistrationCallback(NetConnectivityListener listener){
    	
    	try{
	    	boolean networkStatus = Utils.isNetworkConnected(context, false);
	    	
	    	if(networkStatus){
	    		listener.onNetConnected(true);
	    	}
	    	else{
	    		listener.onNetDisconnected(true);
	    	}
    	}
    	catch(Throwable e){
    		e.printStackTrace();
    		Log.e(e.getMessage()+" At informNetworkConnectivityChanged(...) of NetConnectivityManager");
    	}
    	
    }
    
    private void reset(){
    	connectListener.clear();
    	instance = null;
    }
    
    public NetConnectivityManager(Context context) {
    	this.context 	= context;
    	connectListener = new ArrayList<NetConnectivityListener>();
    }
    
    /**
     * This api is used to register the listener which gets called 
     * whenever any change happened in internet connection.
     * </br>
     * </br>
     * <b>Note: </b> Returns true if registered successfully else returns false.
     */
    
    public void registerNetConnectionListener(NetConnectivityListener listener) {
    	if(listener == null || connectListener.contains(listener)){
        	//TODO nothing here
        }
        else{
        	connectListener.add(listener);
        	informRegistrationCallback(listener);
        }
    }
    
    /**
     * This api is used to remove the registered listener. 
     * </br>
     * </br>
     * <b>Note: </b> Returns true if listener removed successfully else returns false.
     */
    
    public boolean removeNetConnectionListener (NetConnectivityListener listener){
    	return connectListener.remove(listener);
    }
    
    private synchronized void informNetworkConnected(boolean isRegistrationCall){
    	for (int i=0; i< connectListener.size();i++) {
    		try{
    			NetConnectivityListener listener = connectListener.get(i);
    			listener.onNetConnected(isRegistrationCall);
    		}
    		catch(Throwable e){
        		e.printStackTrace();
        		Log.e(e.getMessage()+" At informNetworkConnected(...) of NetConnectivityManager");
        	}
		}
    }
    
    private synchronized void informNetworkDisconnected(boolean isRegistrationCall){
    	
    	for (int i=0; i<connectListener.size();i++) {
    		try{
    			NetConnectivityListener listener = connectListener.get(i); 
    			listener.onNetDisconnected(isRegistrationCall);
    		}
    		catch(Throwable e){
        		e.printStackTrace();
        		Log.e(e.getMessage()+" At informNetworkDisconnected(...) of NetConnectivityManager");
        	}
		}
    }

}
