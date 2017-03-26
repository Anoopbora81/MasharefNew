package com.sr.masharef.masharef.context;

import android.content.Context;

import com.sr.masharef.masharef.common.keychain.KeychainManager;
import com.sr.masharef.masharef.manager.ApplicationManager;
import com.sr.masharef.masharef.model.AccessToken;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.model.user.AUserLocale;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;

import java.util.Date;

public class StaticContext {

    public interface AUserDetailChangeListener{
        boolean onTimestampUpdated(Date updatedTime);
        boolean onLocaleUpdated(AUserLocale locale);
        void onNothingChanged();
    }

	private static AccessToken accessToken;
	private static String appVersion;
	private static AUserDetail loggedInUserDetail;


	public static AUserDetail getLoggedInUserDetail() {
		if(loggedInUserDetail == null){
			loggedInUserDetail = KeychainManager.storedUserDetails(ApplicationManager.getInstacne().getApplicationContext());
		}
		return loggedInUserDetail;
	}
	
	public static String getUserId() {
		String userId = null;
		if(StaticContext.hasUserLoggedIn(ApplicationManager.getInstacne().getApplicationContext())){
			userId = StaticContext.getLoggedInUserDetail().userId;
		}
		return userId;
	}

    /**
     * This method is used to set logged in user detail. It will also notify about the changes through listener
     * if you provide it. Each change notification will require you to return boolean value. if return value will
     * be false then it will investigate further changes otherwise it will stop del_right there and
     * sets the new user detail.
     *
     * @param loggedInUserDetail The detail of the user.
     * @param listener The listener which notify the changes of the profile.
     */

	public static void setLoggedInUserDetail(AUserDetail loggedInUserDetail, AUserDetailChangeListener listener) {

        /*Retrieve old user details*/
        AUserDetail oldUserDetail = StaticContext.loggedInUserDetail;

        /*Set new user details*/
		KeychainManager.storeUserDetails(ApplicationManager.getInstacne().getApplicationContext(), loggedInUserDetail);
		StaticContext.loggedInUserDetail = loggedInUserDetail;

        /*Validate new user details*/
        validateUserDetailChanges(oldUserDetail, loggedInUserDetail, listener);

	}

    private static void validateUserDetailChanges(AUserDetail oldUserDetail, AUserDetail newUserDetail, AUserDetailChangeListener listener){

        if (listener == null){
            return;
        }

        if (oldUserDetail == null || newUserDetail == null){
            listener.onNothingChanged();
        }
        else{

            //Validate Locale
            if (!oldUserDetail.locale.equals(newUserDetail.locale)
                    && (listener.onLocaleUpdated(newUserDetail.locale))) return;
            Log.i("Locale validated!!!");

            //Validate Timestamp
            if (!oldUserDetail.updatedAt.equals(newUserDetail.updatedAt)
                    && (listener.onTimestampUpdated(newUserDetail.updatedAt))) return;
            Log.i("Timestamp validated!!!");

            listener.onNothingChanged();

        }

    }
	
	public static boolean hasUserLoggedIn(Context context){
		
		AccessToken token = null;
		try {
			token = getAccessToken(context);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("**Error** hasUserLoggedIn(...) of StaticContext class Msg:: "+e.getMessage());
		}
		
		return (token!=null && !token.getToken().isEmpty());		
	}

	public static AccessToken getAccessToken(Context context) throws Exception {
		
		if(accessToken == null){
			accessToken = KeychainManager.storedAccessToken(context);
		}
		return accessToken;
		
	}
	
	public static String getCleintAppVersion(Context context){
	
		if(appVersion == null){
			appVersion = ApplicationManager.getInstacne().getStaticAppName()+"-Android,"+ Utils.getAppVersionName(context);
		}	
    	return appVersion;

	}
	

	
	public static void reset(){
		accessToken = null;
		appVersion = null;
		loggedInUserDetail = null;

	}

}
