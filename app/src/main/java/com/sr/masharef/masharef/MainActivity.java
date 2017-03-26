package com.sr.masharef.masharef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.sr.masharef.masharef.common.keychain.KeychainManager;
import com.sr.masharef.masharef.constants.Constants;
import com.sr.masharef.masharef.constants.Constants.ResultKeys;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.login.LoginActivity;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.verification.AccountVerificationActivity;
import com.sr.masharef.masharef.webservice.api.UserServices;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private static final int LOGIN_REQUEST			= 0000;
    private static final int LOGOUT_REQUEST			= 9999;
    private static final int DASHBOARD_REQUEST		= 0003;
    private static final int ACCOUNT_VERIFY_REQUEST	= 0004;

    boolean isInitializing;
    private boolean isAlive;

    private ProgressBar progress;
    private RelativeLayout options;
    ImageButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);

        options		= (RelativeLayout) findViewById(R.id.components);
        progress	= (ProgressBar) findViewById(R.id.progress);
        logout      = (ImageButton) findViewById(R.id.logout);

        isAlive		= true;

        initializeOperation();
        initializeListener();
    }


    private void initializeListener() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutOperation("");
            }
        });
    }

    private void initializeOperation() {

        try {

            if(!isInitializing){
                isInitializing = true;
                StaticContext.getAccessToken(this);
                doPostLoginProcess();
            }

        }catch (Exception e) {
            e.printStackTrace();
            Log.d(e.getMessage()+" At initializeOperation() of MainActivity.");
            isInitializing = false;
            displayLoginActivity(null);

        }
    }

    private void doPostLoginProcess() {
        fetchUserProfile();
    }

    private void displayLoginActivity(String forceLogoutMsg) {

        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        if(forceLogoutMsg!=null)
            i.putExtra(LoginActivity.FORCE_LOGOUT_MSG, forceLogoutMsg);
        startActivityForResult(i, LOGIN_REQUEST);

    }


    private void fetchUserProfile() {

        // ===Check Network Connectivity and returns if internet not available
        if (!Utils.isNetworkConnected(this, true)) {
            isInitializing = false;
            return;
        }

        // ====Start the operation
        showProgress();

        new Thread(new Runnable() {
            @Override
            public void run() {

                UserServices userServ = new UserServices(false);
                userServ.getLoggedInUserDetails(MainActivity.this,new UserServices.UserServiceListeners() {

                    public void onSuccess(JsonObj model) {

                        isInitializing = false;

                        if (isAlive) {
                            AUserDetail user = (AUserDetail) model;
                            StaticContext.setLoggedInUserDetail(user,null);
                            Log.e("Updated At Date ===>>> "+ user.updatedAt);
                            doPostProfileValidation();
                        }

                    }

                    public void onForceLogout(final Exception error) {

                        isInitializing = false;
                        Log.e(error.getMessage()+ " At doPostLoginProcess() of MainActivity Class");

                        if (isAlive) {

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    logoutOperation(error.getMessage());
                                }
                            });
                        }

                    }

                    public void onFailure(Exception error) {
                        isInitializing = false;
                        Log.e(error.getMessage()+ " At doPostLoginProcess() of MainActivity Class");
                        Log.i("Alive Status ===>>> " + isAlive);
                        if (isAlive) {
                            Utils.showAlertOnMainThread(MainActivity.this,error.getMessage());
                            dismissProgress();
                        }
                    }
                });

            }
        }).start();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("onActivityResult on MainActivity class created");
        int actResult = Constants.RESULT_NONE;

        if (data != null) {
            actResult = data.getIntExtra(ResultKeys.ACTIVITY_RESULT.toString(),Constants.RESULT_NONE);
        }

        Log.e("requestCode "+requestCode);
        switch (requestCode) {

            case LOGIN_REQUEST:

                switch (actResult) {

                    case Constants.RESULT_NONE:
                        doPostLoginProcess();
                        break;

                    case Constants.RESULT_QUIT:
                        finish();
                        break;
                    case Constants.LANGUAGE_CHANGED:
                        displayLoginActivity(null);
                        break;
                }

                break;

            case DASHBOARD_REQUEST:
                switch (actResult) {

                    case LOGOUT_REQUEST:
                        logoutOperation("");
                        break;

                    case Constants.RESULT_LOGIN:

                        logoutOperation(data.getStringExtra(ResultKeys.LOGIN_ERROR_MSG.toString()));
                        break;

                    case Constants.RESULT_QUIT:
                        finish();
                        break;


                    case Constants.RESULT_NONE:
                        finish();
                        break;
                }

                break;

            case ACCOUNT_VERIFY_REQUEST:

                switch (actResult) {

                    case Constants.RESULT_OK:

                        requestDashboardView();
                        break;

                    case Constants.RESULT_LOGIN:
                        logoutOperation(data.getStringExtra(ResultKeys.LOGIN_ERROR_MSG.toString()));
                        break;

                    case Constants.RESULT_QUIT:
                        finish();
                        break;

                    case Constants.RESULT_RESTART:
                    case Constants.RESULT_NONE:
                        doPostLoginProcess();
                        break;

                }

                break;
        }
    }

    private void requestDashboardView() {

        Intent dash = new Intent(MainActivity.this, HomeActivity.class);
        startActivityForResult(dash,DASHBOARD_REQUEST);
    }


    private void doPostProfileValidation() {

        /*****Validation on Account*****/
        boolean status = doAccountVerification();

        if (status) {
            requestDashboardView();
        }else{
            dismissProgress();
        }

    }


    private void showProgress() {

        Log.i("Show Progress Called ===>>> ");

        try {

            boolean status = progress.post(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(View.VISIBLE);
                }
            });

            Log.i("Progress status = " + status);

            status = options.post(new Runnable() {
                @Override
                public void run() {
                    options.setVisibility(View.INVISIBLE);
                }
            });

            Log.i("Options status = " + status);

        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At showProgress() of MainActivity.class");
        }
    }

    private void dismissProgress() {

        Log.i("Dismiss Progress Called ===>>> ");

        try {

            boolean status = progress.post(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(View.INVISIBLE);
                }
            });

            Log.i("Progress status = " + status);

            status = options.post(new Runnable() {
                @Override
                public void run() {
                    options.setVisibility(View.VISIBLE);
                }
            });

            Log.i("Options status = " + status);

        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage()+ " At dismissProgress() of MainActivity.class");
        }
    }


    private void logoutOperation(final String errorMsg) {

        // remove this and uncomment below one
        localLogout(errorMsg);

        /*final ProgressDialog progress = Utils.getProgress(this,getString(R.string.logout));

        new Thread(new Runnable() {

            public void run() {

                UserServices userServ = new UserServices(false);

                boolean callStatus = userServ.requestCheckOut(MainActivity.this,new UserServices.UserServiceListeners() {

                    public void onSuccess(JsonObj model) {
                        localLogout(errorMsg);
                        progress.dismiss();
                    }

                    public void onForceLogout(Exception error) {
                        Log.e(error.getMessage()+ " At logoutOperation() of MainActivity Class");
                        localLogout(error.getMessage());
                        progress.dismiss();
                    }

                    public void onFailure(Exception error) {
                        Log.e(error.getMessage()+ " At logoutOperation() of MainActivity Class");
                        progress.dismiss();
                        localLogout(errorMsg);

                    }
                });

                if (!callStatus) {
                    progress.dismiss();
                    localLogout(errorMsg);
                }

            }

        }).start();*/

    }

    private boolean doAccountVerification(){

        if(AccountVerificationActivity.isAcccountVerified(null)){
            return true;
        }else{
            Intent i = new Intent(MainActivity.this, AccountVerificationActivity.class);
            startActivityForResult(i, ACCOUNT_VERIFY_REQUEST);
            return false;
        }
    }

    private void localLogout(String errorMsg) {
        KeychainManager.resetAccessToken(MainActivity.this);
        StaticContext.reset();

        displayLoginActivity(errorMsg);
    }

    public static boolean sendLogoutRequest(Activity activity, Exception error){

        boolean status = true;//(error==null)?Utils.isNetworkConnected(activity, true):true;

        if(status){
            Intent data = new Intent();
            data.putExtra(ResultKeys.ACTIVITY_RESULT.toString(), Constants.RESULT_LOGIN);

            if(error!=null)
                data.putExtra(ResultKeys.LOGIN_ERROR_MSG.toString(), error.getMessage());

            activity.setResult(Activity.RESULT_OK, data);
            activity.finish();
        }




        return status;

    }
}
