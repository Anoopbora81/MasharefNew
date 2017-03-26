
//ApplicationLifecycleCallbackListener.java

//Created By Zuhair on Aug 9, 2017

package com.sr.masharef.masharef.common.crypto;

import android.app.Application;

public interface ApplicationLifecycleCallbackListener {

	public void onForeground(Application application);
	public void onBackground(Application application);
	public void onRestartRequest(Application application);
	
}
