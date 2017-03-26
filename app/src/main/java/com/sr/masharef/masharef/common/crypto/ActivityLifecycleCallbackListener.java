
//ActivityLifecycleCallbackListener.java

//Created By Zuhair on Jun 9, 2017


package com.sr.masharef.masharef.common.crypto;

import android.app.Activity;
import android.os.Bundle;

public interface ActivityLifecycleCallbackListener {
	
	public void onActivityCreated(Activity activity, Bundle savedInstanceState);
	public void onActivityDestroyed(Activity activity);
	public void onActivityPaused(Activity activity);
	public void onActivityResumed(Activity activity);
	public void onActivitySaveInstanceState(Activity activity, Bundle outState);
	public void onActivityStarted(Activity activity);
	public void onActivityStopped(Activity activity);
	public void onUserLeaveHint(Activity activity);

}
