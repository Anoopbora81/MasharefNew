
//ApplicationManager.java
//Created By Zuhair on Jan 9, 2017


package com.sr.masharef.masharef.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;

import com.sr.masharef.masharef.common.crypto.ActivityLifecycleCallbackListener;
import com.sr.masharef.masharef.common.crypto.ApplicationLifecycleCallbackListener;
import com.sr.masharef.masharef.common.permission.PermissionRequestor;
import com.sr.masharef.masharef.manager.locale.LocaleManager;
import com.sr.masharef.masharef.manager.net.NetConnectivityManager;
import com.sr.masharef.masharef.utility.CurrentLocation;
import com.sr.masharef.masharef.utility.ImageDownloader;
import com.sr.masharef.masharef.utility.Log;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public abstract class ApplicationManager extends Application implements ActivityLifecycleCallbackListener,
		OnRequestPermissionsResultCallback {

	protected static final String TAG = "zhr_app";
	private static final int AUTO_RESET_TIME = 10 * 60; //===In Seconds
	
	private static Activity currActivity;
	private static ApplicationManager instance;
	private Date sleepTime;
	private boolean isAppInBackground;
	private boolean isExternalAppReq;
	private File logFile;
	private PermissionRequestor requestor;
	public static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		context = getApplicationContext();
        /*This Fabric initialization must be the first statement always just to prevent log
        * feature getting cyclic crashes on each call of Log.e(...).
        */

        //Fabric.with(this, new Crashlytics());

        /*ZendeskConfig.INSTANCE.init(getApplicationContext(), ZendeskClientConfig.getZendeskUrl(this), ZendeskClientConfig.getZendeskAppID(this),
                ZendeskClientConfig.getOAuthID(this));*/
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        tryForceUpdateLocale(newConfig);
        Log.i(TAG, "Config Change!! Locale Name: " + newConfig.locale.getDisplayName());
    }

    private void createLogFile(){
		try {
			logFile = new File(ImageDownloader.getCacheDir(this, true, true), "Logs.txt");
			
			if(!logFile.exists()){
				logFile.createNewFile();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getLogFile() {
		return logFile;
	}
	
	public static ApplicationManager getInstacne(){
		return instance;
	}
	
	public static Activity getCurrentActivity(){
		return currActivity;
	}
	
	public boolean isExternalAppReq() {
		return isExternalAppReq;
	}

	public void setExternalAppReq(boolean isExternalAppReq) {
		Log.i(TAG, "Set External App Request ===>> "+isExternalAppReq);
		this.isExternalAppReq = isExternalAppReq;
	}

	
	/*
	 * Activity Life-cycle Callback Listener Delegates implementation
	 */

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "On Activity Created >>>> "+activity.getLocalClassName());
		createLogFile();
		
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		// TODO Auto-generated method stub
		Log.i(TAG, "On Activity Destroyed >>>> "+activity.getLocalClassName());
		if(isAppInBackground){
			sleepTime = null;
		}
		/*stopTrackingRestrat();*/
	}

	@Override
	public void onActivityPaused(Activity activity) {
		
		Log.i(TAG, "On Activity Paused >>>> "+activity.getLocalClassName());
		
		if(activity instanceof NetConnectivityManager.NetConnectivityListener){
			NetConnectivityManager.getInstance(this).
			removeNetConnectionListener((NetConnectivityManager.NetConnectivityListener) activity);
		}	
		
		/*startTrackingRestart();*/
		
		
	}

	@Override
	public void onActivityResumed(Activity activity) {
		// TODO Auto-generated method stub
		Log.i(TAG, "On Activity Resumed >>>> "+activity.getLocalClassName());
		Log.e(TAG,"Current Activity on Resume==>> "+currActivity);
		
		//===Check for app enter in foreground 
		if(currActivity == null && !isExternalAppReq){
			onForeground(activity);
		}
		
		currActivity = activity;
		isExternalAppReq = false;
		
		if(activity instanceof NetConnectivityManager.NetConnectivityListener){
			NetConnectivityManager.getInstance(this).
			registerNetConnectionListener((NetConnectivityManager.NetConnectivityListener) activity);
		}	
		
		/*stopTrackingRestrat();*/
		
		/*if(RestartBroadcastReceiver.isRestart(activity)){
			RestartBroadcastReceiver.restrart(activity);
		}*/
		
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "On Activity SaveInstacneState >>>> "+activity.getLocalClassName());
	}

	@Override
	public void onActivityStarted(Activity activity) {
		// TODO Auto-generated method stub
		Log.i(TAG, "On Activity Started >>>> "+activity.getLocalClassName());
	}

	@Override
	public void onActivityStopped(Activity activity) {
		// TODO Auto-generated method stub
		Log.i(TAG, "On Activity Stopped >>>> "+activity.getLocalClassName());
		
		//===Check for app going to background
		if(currActivity == activity && !isExternalAppReq){
			onBackground(activity);
			currActivity = null;
		}
	}
	
	@Override
	public void onUserLeaveHint(Activity activity) {
		// TODO Auto-generated method stub
		Log.i(TAG, "On Activity Leaved >>>> "+activity.getLocalClassName());
	}
	
	/*
	 * Application Life-cycle modules for background and foreground calls
	 */

	public void onBackground(Activity activity) {
		
		Log.i(TAG, "On Application Going to background >>>> "+activity.getLocalClassName());
		
		isAppInBackground = true;
		sleepTime = Calendar.getInstance().getTime();
		
		//=====Callbacks the app lifecycle listener if exist
		
		if(activity instanceof ApplicationLifecycleCallbackListener){
			((ApplicationLifecycleCallbackListener)activity).onBackground(this);
		}
		
	}

	public void onForeground(Activity activity) {
		
		Log.i(TAG, "On Application Coming to foreground >>>> "+activity.getLocalClassName());
		
		isAppInBackground = false;
		
		//=====Update The Users current Location
		CurrentLocation.quickLocationUpdate(getApplicationContext());
		
		//=====Callbacks the app lifecycle listener if exist
		
		if(activity instanceof ApplicationLifecycleCallbackListener){
			((ApplicationLifecycleCallbackListener)activity).onForeground(this);
			checkAndSendRestartRequest(activity);
		}

		sleepTime = null;
		
	}
	
	private void checkAndSendRestartRequest(Activity activity){
		// TODO Auto-generated method stub
		
		Log.d(TAG, "Sleep Time ===>>>" + sleepTime);
		
		/*if(sleepTime != null){
			long secondDiff = Utils.getDateDiff(sleepTime, new Date(), TimeUnit.SECONDS);
			if(secondDiff>=AUTO_RESET_TIME){*/
				onRestartRequest(activity);
		/*	}
		}*/
	}

	public void onRestartRequest(Activity activity) {
		// TODO Auto-generated method stub
		Log.i(TAG, "On Application Restart Request >>>> " + activity.getLocalClassName());
		if(activity instanceof ApplicationLifecycleCallbackListener){
			Log.i(TAG, "On Application Restart Requested >>>> "+activity.getLocalClassName());
			((ApplicationLifecycleCallbackListener)activity).onRestartRequest(this);
		}	
	}
	
	/*public void onNetConnectivityChanged(boolean status){
		
		if(currActivity!=null && !isExternalAppReq){	
			try{
				Log.i(TAG, "On Net Connectivity Changed >>>> "+currActivity.getLocalClassName()+" Status "+status);
				if(currActivity instanceof ApplicationLifecycleCallbackListener){
					((ApplicationLifecycleCallbackListener)currActivity).onNetConnectivityChanged(status);
				}
			}
			catch(NullPointerException e){
				e.printStackTrace();
				Log.e(e.getMessage()+" At onNetConnectivityChanged() of ApplicationManager");
			}
		}	
		
	}*/
	
	public static void releaseMemory(){
		Runtime.getRuntime().gc();
	}
	
	public void requestAppUpdate(){
		if(currActivity!=null){
			currActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateAppRequest(currActivity);
				}
			});
		}
	}
	
	/*
	 * ActivityCompat Permission Request & Callback Listeners.
	 */
	
	public void requestPermissions(PermissionRequestor requestor, String[] permissions, int requestCode) throws NullPointerException {
		if(currActivity == null){
			throw new NullPointerException("No Activity Found.");
		}
		else{
			this.requestor = requestor;
			ActivityCompat.requestPermissions(currActivity, permissions, requestCode);
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String[] permissions, int[] grantResults) {
		if(requestor!=null && requestor.getRequestCode() == requestCode){
			requestor.onRequestPermissionsResult(requestCode, permissions, grantResults);
			requestor = null;
		}
	}

    public Configuration requestNewLocale(Locale newLocale){
        LocaleManager.setLocale(this, newLocale);
        return configureLocale(newLocale);
    }

    protected Configuration configureLocale(){
        return configureLocale(LocaleManager.getLocale(this));
    }

    protected Configuration configureLocale(Locale newLocale){

        Configuration config = getBaseContext().getResources().getConfiguration();
        if (newLocale == null){
            //Nothing need to be done here.
        }
        else{
            config.locale = newLocale;
            getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            Log.i(TAG, "Configured User Locale: " + newLocale.getLanguage()+"-"+newLocale.getCountry());
        }
        return config;

    }

    private void tryForceUpdateLocale(Configuration newConfig){
        Locale customLocale = LocaleManager.getLocale(this);
        if (customLocale == null){
            //TODO nothing here.
        }
        else if(newConfig.locale.getCountry()!=customLocale.getCountry()||
                newConfig.locale.getLanguage()!=customLocale.getLanguage()){
            configureLocale(customLocale);
        }
    }

    protected abstract void updateAppRequest(Activity activity);
    public abstract String getStaticAppName();

	/*private void startTrackingRestart(){
		AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
	    Intent intent = new Intent(this, RestartBroadcastReceiver.class);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AUTO_RESET_TIME, pendingIntent);
	}
	
	private void stopTrackingRestrat(){
		AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
	    Intent intent = new Intent(this, RestartBroadcastReceiver.class);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	    am.cancel(pendingIntent);
	}*/
	
}
