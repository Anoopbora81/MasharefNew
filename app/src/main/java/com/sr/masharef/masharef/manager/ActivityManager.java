
//ActivityManager.java


package com.sr.masharef.masharef.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;

import com.sr.masharef.masharef.common.crypto.ApplicationLifecycleCallbackListener;
import com.sr.masharef.masharef.constants.Constants;
import com.sr.masharef.masharef.constants.Constants.ResultKeys;
import com.sr.masharef.masharef.manager.net.NetConnectivityManager;

public class ActivityManager extends Activity implements ApplicationLifecycleCallbackListener, NetConnectivityManager.NetConnectivityListener,
		OnRequestPermissionsResultCallback {
	
	private ApplicationManager appManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appManager = ApplicationManager.getInstacne();
		
		if(appManager!=null)
			appManager.onActivityCreated(this, savedInstanceState);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(appManager!=null)
			appManager.onActivityStarted(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(appManager!=null)
			appManager.onActivityResumed(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(appManager!=null)
			appManager.onActivityPaused(this);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(appManager!=null)
			appManager.onActivitySaveInstanceState(this, outState);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(appManager!=null)
			appManager.onActivityStopped(this);
	}
	
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		if(appManager!=null)
			appManager.onUserLeaveHint(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(appManager!=null)
			appManager.onActivityDestroyed(this);
	}
	
	protected void defaultBackOperation(){
		Intent intent = new Intent();
		intent.putExtra(ResultKeys.ACTIVITY_RESULT.toString(), Constants.RESULT_QUIT);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	/*
	 * Application Life-cycle Callback Listeners.
	 */

	@Override
	public void onBackground(Application app) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onForeground(Application app) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRestartRequest(Application app) {
		// TODO Auto-generated method stub
	}
	
	/*
	 * Internet Connectivity Callback Listeners.
	 */

	@Override
	public void onNetConnected(boolean isRegistrationCall) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetDisconnected(boolean isRegistrationCall) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * ActivityCompat Callback Listeners.
	 */
	
	@Override
	public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
		if(appManager!=null){
			appManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
	
}
