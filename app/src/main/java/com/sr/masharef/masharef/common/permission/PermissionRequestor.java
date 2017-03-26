
//PermissionRequestor.java

//Created By Zuhair on 11 Jan, 2017

package com.sr.masharef.masharef.common.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.text.Html;

import com.sr.masharef.masharef.manager.ApplicationManager;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;

import java.util.ArrayList;

public class PermissionRequestor implements OnRequestPermissionsResultCallback {

	public interface PermissionRequestorListener{
		public void onPermissionGranted();
		public void onPermissionDenied();
	}
	
	private PermissionRequestorListener listener;
	private int requestCode;
	private String permissions[];
	private String rationalMessage;
	
	public static boolean isPermissionAccessible(Context context, String permission){
    	return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }
	
	public static boolean isPermissionAlreadyDenied(Activity activity, String permission){
    	return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
	
	public PermissionRequestor(PermissionRequestorListener listener, String rationalMessage, int requestCode, String... permissions) {
		this.listener 			= listener;
		this.requestCode		= requestCode;
		this.permissions		= permissions;
		this.rationalMessage	= rationalMessage;
		
	}
	
	public void request(Activity activity){
		
		ArrayList<String> requestedPermissions 	= new ArrayList<String>();
		
		/*
		 * Check and add those permissions in list which need confirmation from user.
		 */
		for (String permission : permissions) {
			if(!isPermissionAccessible(activity, permission)){
				requestedPermissions.add(permission);
			}
		}
		
		permissions = requestedPermissions.toArray(new String[]{});
		
		if(permissions.length>0){
			
			/*if(shouldShowRequestPermissionRationale(activity, permissions)){*/
				showRequestPermissionRationaleDialog(activity);
			/*}
			else{
				requestPermissions(activity, permissions);
			}*/
		
		}
		else if(listener!=null){
			listener.onPermissionGranted();
		}
		
	}
	
	/**
	 * Check to see if any one of the requesting permission already denied.
	 */
	
	private boolean shouldShowRequestPermissionRationale(Activity activity, String[] reqPermissions){
		
		boolean status = false;
		
		for (String reqPermission : reqPermissions) {
			if(isPermissionAlreadyDenied(activity, reqPermission)){
				status = true;
				break;
			}
		}
		
		return status;
	}
	
	private void showRequestPermissionRationaleDialog(Activity activity){
		
		Utils.decisionAlertOnMainThread(activity, android.R.drawable.ic_dialog_info, R.string.permission_info, appendNote(activity, rationalMessage),
				activity.getString(R.string.proceed), new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						requestPermissions(permissions);
					}
				}, activity.getString(R.string.cancel), new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(listener!=null){
							listener.onPermissionDenied();
						}
					}
				});
		
	}
	
	private CharSequence appendNote(Context context, String message){
		return Html.fromHtml(message+"\n\n"+context.getString(R.string.permission_info_note, Utils.getAppName(context)));
	}
	
	private boolean requestPermissions(String[] permissions){
		boolean status = false; 
		try{
			ApplicationManager.getInstacne().requestPermissions(this, permissions, requestCode);
			status = true;
		}
		catch(NullPointerException e){
			e.printStackTrace();
			Log.e(e.getMessage()+" At requestPermissions(...) of PermissionRequestor");
		}
		return status;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		
		if(listener!=null) {
			
			boolean allGranted = grantResults.length>0;
			
			for (int result : grantResults) {
				if(result == PackageManager.PERMISSION_DENIED){
					allGranted = false;
					break;
				}
			}
			
            if (allGranted) {
            	listener.onPermissionGranted();
            } else {
            	listener.onPermissionDenied();
            }
            
	    }
		
	}
	
	public int getRequestCode() {
		return requestCode;
	}

}
