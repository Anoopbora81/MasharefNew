package com.sr.masharef.masharef.login;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.keychain.KeychainManager;
import com.sr.masharef.masharef.constants.Constants;
import com.sr.masharef.masharef.constants.Constants.ResultKeys;
import com.sr.masharef.masharef.manager.FragmentActivityManager;
import com.sr.masharef.masharef.model.ABasicMessage;
import com.sr.masharef.masharef.model.AccessToken;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.user.AOwnerTemplate;
import com.sr.masharef.masharef.registration.RegistrationActivity;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.utility.Validation;
import com.sr.masharef.masharef.webservice.api.AccountServices;
import com.sr.masharef.masharef.webservice.api.AccountServices.AccountServiceListeners;
import com.sr.masharef.masharef.webservice.api.ForgetPassordServices;
import com.sr.masharef.masharef.webservice.api.UserServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends FragmentActivityManager {

    public static final String FORCE_LOGOUT_MSG = "force_msg";
    private static final int REGISTRATION_REQUEST = 1020;
    private static final int LOGIN_REQUEST			= 0000;

    Button login;
    Button register;
    RelativeLayout multi_lang;
    EditText villaNumber,user_name,password;
    TextView forgetPassword, forgetUsername;

    ProgressDialog dialog;
    Locale langLocale;

    ListView dialog_ListView;
    static final int CUSTOM_DIALOG_ID = 0;

    String[] listContent = {"English","عربى"};
    private Spinner villaSpinnerLogin;
    private String selectedOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login       = (Button) findViewById(R.id.login);
        register    = (Button) findViewById(R.id.register);
        villaNumber = (EditText) findViewById(R.id.villa_number);
        user_name   = (EditText) findViewById(R.id.user_name);
        password    = (EditText) findViewById(R.id.password);
        multi_lang  = (RelativeLayout) findViewById(R.id.multi_layout);
        forgetPassword    = (TextView) findViewById(R.id.forgetPassword);
        //forgetUsername    = (TextView) findViewById(R.id.forgetUsername);
        villaSpinnerLogin 	= (Spinner)findViewById(R.id.spinnerVillaNumLogin);

        setUpUI();
        initializeListener();
        multi_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(CUSTOM_DIALOG_ID);

            }
        });

    }

    private void setUpUI() {
        ArrayList<String> villaAdapterList= new ArrayList<String>();
        villaAdapterList.add(getString(R.string.select));
        villaAdapterList.add("A");
        villaAdapterList.add("B");
        ArrayAdapter<String> villaAdapter=new ArrayAdapter<String>(this, R.layout.spinner_text, villaAdapterList);
        villaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        villaSpinnerLogin.setAdapter(villaAdapter);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
            case CUSTOM_DIALOG_ID:
            dialog = new Dialog(LoginActivity.this);
            dialog.setContentView(R.layout.multi_lang_dialoga_layout);
            dialog.setTitle("Select Language");

            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
                dialog_ListView = (ListView)dialog.findViewById(R.id.lang_listview);

                dialog_ListView.setAdapter(new DialogListViewAdapter(this,listContent));
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        switch(i) {
                            case 0: //English

                                setSelectedLanguage(Constants.LAN_ENGLISH);
                                dismissDialog(CUSTOM_DIALOG_ID);
                                return;
                            case 1: //Arabic
                                setSelectedLanguage(Constants.LAN_ARABIC);
                                dismissDialog(CUSTOM_DIALOG_ID);
                                return;
                            default: //By default set to english

                                setSelectedLanguage(Constants.LAN_ENGLISH);
                                dismissDialog(CUSTOM_DIALOG_ID);

                        }
                    }
                });
        }
        return dialog;
    }


    class DialogListViewAdapter extends BaseAdapter{
        String[] rowList;
        Context context;
        TextView textView;

        DialogListViewAdapter(Context context,String[] rowList){
            this.rowList=rowList;
            this.context=context;
        }

        @Override
        public int getCount() {
            return rowList.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.custom_row_list, viewGroup, false);
             textView = (TextView) rowView.findViewById(R.id.custom_row_text);
             textView.setText(rowList[i]);
            return rowView;
        }
    }


    private void setSelectedLanguage(String langName) {

        String languageToLoad = Constants.LAN_ENGLISH;
        Locale locale = null;

        switch (langName){
            case Constants.LAN_ENGLISH:
                locale = new Locale(languageToLoad);
                break;
            case Constants.LAN_ARABIC:
                languageToLoad = Constants.LAN_ARABIC;
                locale = new Locale(languageToLoad);
                break;
            default:
                locale = new Locale(languageToLoad);
                break;

        }

        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        Intent intent = new Intent();
        intent.putExtra(ResultKeys.ACTIVITY_RESULT.toString(), Constants.LANGUAGE_CHANGED);
        setResult(RESULT_OK, intent);

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(Constants.APP_LANGUAGE, languageToLoad).commit();

        finish();
    }



    private void initializeListener() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    loginOperation(villaNumber.getText().toString()+selectedOption,user_name.getText().toString(),password.getText().toString());
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivityForResult(i, REGISTRATION_REQUEST);

            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popUpForgetDialog();
            }
        });

        villaSpinnerLogin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOption = adapterView.getItemAtPosition(i).toString();
                if (selectedOption.equalsIgnoreCase(getString(R.string.select))){
                    selectedOption = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    private void sendForgetpassword(String villNo, String userName) {

            JSONObject forgetJson = new JSONObject();
            try {
                forgetJson.put("villa_number", villNo);
                forgetJson.put("email", userName);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(e.getMessage() + " at verifyCodeForMobileNumber Module!!");
            }

            sendForgetpassword(forgetJson);

    }

    private void sendForgetpassword(final JSONObject forgetJson) {
        final ProgressDialog progress = Utils.getProgress(this, getString(R.string.forgetPassword));

        new Thread(new Runnable() {

            public void run() {

                ForgetPassordServices forgetPassordServices = new ForgetPassordServices(LoginActivity.this);

                boolean callStatus 	= forgetPassordServices.sendForgetPassword(forgetJson, new ForgetPassordServices.ForgetPassordListeners(){
                    @Override
                    public void onSuccess(JSONObject jsonJoiningEvents) {
                       // final float ratingfromServer = Float.parseFloat(AJSONObject.optString(jsonJoiningEvents, "current").toString());

                        runOnUiThread(new Runnable() {
                            public void run() {
                                // TODO Auto-generated method stub
                                //    rb.setRating(ratingfromServer);
//                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                Utils.alert(LoginActivity.this, getString(R.string.success));
                                progress.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onFailure(final Exception error) {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                // TODO Auto-generated method stub
                                //    rb.setRating(ratingfromServer);
//                                Toast.makeText(getApplicationContext(), "Sorry: "+((JResponseError) error).getMessage(), Toast.LENGTH_SHORT).show();
                                Utils.showAlertOnMainThread(LoginActivity.this, error.getMessage());
                                if(progress.isShowing()) {
                                    progress.dismiss();
                                }
                                progress.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onForceLogout(Exception error) {
                        progress.dismiss();
                    }

                });
                if(!callStatus)
                    progress.dismiss();

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

        switch (requestCode) {

            case REGISTRATION_REQUEST:

                switch (actResult) {

                    case Constants.RESULT_NONE:
                        finish();
                        break;

                    case Constants.RESULT_QUIT:
                        finish();
                        break;

                }

                break;

        }

    }

    boolean validate(){

        Validation valid	= new Validation(LoginActivity.this);
        valid.ShowAlerts(true);

        if (valid.checkIfEmpty(villaNumber, getString(R.string.villa_no))){
            return false;
        }

        if (valid.checkIfEmpty(user_name,getString(R.string.user_name))){
            return false;
        }

        if (valid.checkIfEmpty(password,getString(R.string.password))){
            return false;
        }

        return true;
    }

    private void loginOperation(String villaNumber,final String username, String password){

        final ProgressDialog progress = Utils.getProgress(this, getString(R.string.signing_in), getString(R.string.wait));

        AccountServices accountServices = new AccountServices(this);
        boolean callStatus = accountServices.doBasicLogin(villaNumber,username, password, new AccountServiceListeners() {

            @SuppressWarnings("static-access")
            @Override
            public void onSuccess(AccessToken token) {
                KeychainManager.storeAccessToken(LoginActivity.this, token);
                KeychainManager.storeLocalLoginUsername(LoginActivity.this, username);
                String deviceToken = null;
                try {
                    deviceToken =  KeychainManager.getDeviceToken(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HashMap<String, String> mHeaders = new HashMap<String, String>();
                mHeaders.put("Access-Token", token.getToken().toString());
                // We have to send header and accesstoken to server..
               // sendTokenToServer(mHeaders, deviceToken);
                sendTokenToServer(deviceToken);
                progress.dismiss();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onSuccess(AOwnerTemplate ownerTemplate) {

            }

            @Override
            public void onFailure(Exception e) {
                progress.dismiss();
                failureOperation(e);
            }

            @Override
            public void onForceLogout(Exception error) {

            }

        });

        if(!callStatus){
            progress.dismiss();
        }
        else{
            Utils.SoftKeyboard(this, user_name, false);
        }

    }

    private void failureOperation(final Exception e){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Utils.alert(LoginActivity.this, R.string.login_error,e.getMessage(), new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Utils.SoftKeyboard(LoginActivity.this, villaNumber, true);
                        villaNumber.setSelection(villaNumber.getText().length());
                    }
                });
            }
        });

    }

    public void callProgressDialog() {
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading..");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void popUpForgetDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.forget_dialog_layout);
        dialog.setCancelable(false);

        Button buttonDone = (Button) dialog.findViewById(R.id.buttonDone);
        Button buttonCancelF = (Button) dialog.findViewById(R.id.buttonCancelF);
        final EditText editTextVillForget =  (EditText) dialog.findViewById(R.id.editTextVillForget);
        final EditText editTextUserForget =  (EditText) dialog.findViewById(R.id.editTextUserForget);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String  editTextVillForget1 = editTextVillForget.getText().toString();
                String  editTextUserForget2 = editTextUserForget.getText().toString();
                boolean isValidVillaNo = false;
                boolean isValidUser = false;
                if(editTextVillForget1 != null && !editTextVillForget1.isEmpty())  {
                    isValidVillaNo = true;
                }
                if(editTextUserForget2 != null && !editTextUserForget2.isEmpty())
                {
                    isValidUser = true;
                }

                if(isValidUser && isValidVillaNo)
                {
                    sendForgetpassword(editTextVillForget1, editTextUserForget2);
                    dialog.dismiss();
                }
                else
                {
                    if(!isValidVillaNo){
//                        Toast.makeText(getApplicationContext(), "Please enter villa number", Toast.LENGTH_SHORT).show();
                        Utils.showAlertOnMainThread(LoginActivity.this, getString(R.string.villa_no_msg));
                    }
                    else if(!isValidUser)
                    {
//                        Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_SHORT).show();
                        Utils.showAlertOnMainThread(LoginActivity.this, getString(R.string.user_name_msg));
                    }
                }
            }
        });

        buttonCancelF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        dialog.show();
    }
   void sendTokenToServer(final String deviceToken){
       runOnUiThread(new Runnable() {
           @Override
           public void run() {


       final ProgressDialog progress = Utils.getProgress(LoginActivity.this, getString(R.string.signing_in), getString(R.string.wait));

       JSONObject jsonObject = new JSONObject();
       try {
           jsonObject.put("device_type","android");
           jsonObject.put("token",deviceToken);
       }catch (Exception e){

       }

       UserServices userServicesr = new UserServices(true);
       boolean callStatus = userServicesr.sendTokenToServer(LoginActivity.this, jsonObject, new UserServices.UserServiceListeners() {
           @Override
           public void onSuccess(JsonObj info) {
                ABasicMessage msg =  (ABasicMessage)info;
           }

           @Override
           public void onFailure(Exception error) {

           }

           @Override
           public void onForceLogout(Exception error) {

           }
       });

       if(!callStatus){
           progress.dismiss();
       }
       else{
           Utils.SoftKeyboard(LoginActivity.this, user_name, false);
       }
           }
       });
    }



/*
    private void popUpForgetUsernameDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.forget_username_dialog);
        dialog.setCancelable(false);

        Button buttonDone = (Button) dialog.findViewById(R.id.buttonDone);
        Button buttonCancelF = (Button) dialog.findViewById(R.id.buttonCancelF);
        final EditText editTextVillNoForgetUsername =  (EditText) dialog.findViewById(R.id.editTextVillNoForgetUsername );
        final EditText editTextMobileNoForgetUsername =  (EditText) dialog.findViewById(R.id.editTextMobileNoForgetUsername);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String  editTextVillNoForgetUsername1 = editTextVillNoForgetUsername.getText().toString();
                String  editTextMobileNoForgetUsername1 = editTextMobileNoForgetUsername.getText().toString();
                boolean isValidVillaNo = false;
                boolean isValidMob = false;
                if(editTextVillNoForgetUsername1 != null && !editTextVillNoForgetUsername1.isEmpty())  {
                    isValidVillaNo = true;
                }
                if(editTextMobileNoForgetUsername1 != null && !editTextMobileNoForgetUsername1.isEmpty())
                {
                    isValidMob = true;
                }

                if(isValidMob && isValidVillaNo)
                {
                    sendForgetUsername(editTextVillNoForgetUsername1, editTextMobileNoForgetUsername1);
                    dialog.dismiss();
                }
                else
                {
                    if(!isValidVillaNo){
                        Toast.makeText(getApplicationContext(), "Please enter villa number", Toast.LENGTH_SHORT).show();
                    }
                    else if(!isValidMob)
                    {
                        Toast.makeText(getApplicationContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonCancelF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        dialog.show();
    }*/

}
