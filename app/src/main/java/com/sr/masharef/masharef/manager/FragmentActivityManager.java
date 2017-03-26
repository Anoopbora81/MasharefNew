
//FragmentActivityManager.java

//Created By Zuhair on Jan 10, 2017

package com.sr.masharef.masharef.manager;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.sr.masharef.masharef.common.crypto.ApplicationLifecycleCallbackListener;
import com.sr.masharef.masharef.constants.Constants;
import com.sr.masharef.masharef.manager.net.NetConnectivityManager;
import com.sr.masharef.masharef.utility.Log;


public class FragmentActivityManager extends FragmentActivity implements ApplicationLifecycleCallbackListener, NetConnectivityManager.NetConnectivityListener,
		OnRequestPermissionsResultCallback {

	public static final int REQUEST_TAKE_PHOTO = 150;
	
	private final String TAG = "zhr_fa_manager";
	private ApplicationManager appManager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		appManager = ApplicationManager.getInstacne();
		
		if(appManager!=null)
			appManager.onActivityCreated(this, savedInstanceState);
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(appManager!=null)
			appManager.onActivityStarted(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(appManager!=null)
			appManager.onActivityResumed(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(appManager!=null)
			appManager.onActivityPaused(this);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if(appManager!=null)
			appManager.onActivitySaveInstanceState(this, outState);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(appManager!=null)
			appManager.onActivityStopped(this);
	}
	
	@Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
		if(appManager!=null)
			appManager.onUserLeaveHint(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(appManager!=null)
			appManager.onActivityDestroyed(this);
	}
	
	@Override
	public void onBackPressed() {
		
		if(requestRemoveChildFragment()){
			//TODO Nothing to be here
		}
		else{
			defaultBackOperation();
		}
		
	}
	
	
	private boolean requestRemoveChildFragment(){
		
		boolean status = false;
		
		try{
			
			FragmentManager manager = getSupportFragmentManager();
			
			Log.i(TAG, "Request Remove Child Module Called with Backstack Count => "+manager.getBackStackEntryCount());
			
			if(manager.getBackStackEntryCount() > 0){
				manager.popBackStack();
				status = true;
			}
			
			/*for (int i = manager.getFragments().size() -1; i >=0; i--) {
				
				Fragment f = manager.getFragments().get(i);
				
				if(f!=null && !f.isHidden()){
					
					Log.i(TAG, "Visible fragment found with Class Name => "+f.getClass().getSimpleName());
					
					if(f instanceof FragmentX){
						
						Log.i(TAG, "Fragment X back Operation performed");
						((FragmentX) f).onBackButtonPressed();
					}
					else{
						
						Log.i(TAG, "Fragment remove transaction commited");
						manager.beginTransaction().remove(f).commit();
					}
					
					status = true;
					break;
					
				}
				
			}*/
			
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At requestRemoveChildFragment() of FragmentActivityManager");
		}
		
		return status;
		
	}
	
	private void defaultBackOperation(){
		Intent intent = new Intent();
		intent.putExtra(Constants.ResultKeys.ACTIVITY_RESULT.toString(), Constants.RESULT_QUIT);
		setResult(RESULT_OK, intent);
		finish();
	}

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
	public void onRequestPermissionsResult(int requestCode,
										   String[] permissions, int[] grantResults) {
		if(appManager!=null){
			appManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
	
	
}
