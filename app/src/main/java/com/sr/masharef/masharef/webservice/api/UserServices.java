package com.sr.masharef.masharef.webservice.api;

import android.content.Context;

import com.sr.masharef.masharef.common.JResponseError;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.model.ABasicMessage;
import com.sr.masharef.masharef.model.APhoneNumber;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.phonebook.AOTPObjects;
import com.sr.masharef.masharef.model.subaccount.ASubAccountModel;
import com.sr.masharef.masharef.model.user.AMemberInfoModel;
import com.sr.masharef.masharef.model.user.AOtherInfoModel;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.model.user.AUserTC;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.webservice.ApiServiceUtils;
import com.sr.masharef.masharef.webservice.ApiServiceBase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zuhair on 10/01/17.
 */

public class UserServices {

    private boolean showNetworkAlert;

    public interface UserServiceListeners{
        void onSuccess(JsonObj info);
        void onFailure(Exception error);
        void onForceLogout(Exception error);
    }

    public UserServices(){
        showNetworkAlert = true;
    }

    public UserServices(boolean showNetworkAlert){
        this.showNetworkAlert = showNetworkAlert;
    }

    public boolean getLoggedInUserDetails(final Context context, final UserServiceListeners listeners){

        MServiceBase service = new MServiceBase(context, R.string.get_user_detail_api, null, null, MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.POST){

            @Override
            protected boolean onResponseReceived(String response, JSONObject JSON) throws JResponseError {
                boolean status = super.onResponseReceived(response, JSON);
                AUserDetail user = new AUserDetail(context, JSON);
                if(listeners!=null){
                    listeners.onSuccess(user);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }

    public boolean requestCheckOut(final Context context, final UserServiceListeners listeners){

        MServiceBase service = new MServiceBase(context, R.string.checkout_api, null, null, MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.POST){

            @Override
            protected boolean onResponseReceived(String response, JSONObject JSON) throws JResponseError {
                boolean status = super.onResponseReceived(response, JSON);
                AUserDetail user = new AUserDetail(context, JSON);
                if(listeners!=null){
                    listeners.onSuccess(user);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }


    public boolean requestMobileVerificationCode(Context context, APhoneNumber phone, final UserServiceListeners listeners){

        MServiceBase service = new MServiceBase(context, R.string.mobile_register_api, null, phone.toJson(), MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.POST){

            @Override
            protected boolean onResponseReceived(String response,JSONObject JSON) throws JResponseError {
                boolean status = super.onResponseReceived(response, JSON);
                if(listeners!=null){

                    AOTPObjects otpModel = new AOTPObjects(JSON);

                    listeners.onSuccess(otpModel);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }

    public boolean verifyCodeForMobileNumber(final Context context, APhoneNumber phone, final UserServiceListeners listeners){


        JSONObject params = new JSONObject();
        try {
            params.put("verificationCode", phone.verificationCode);
            params.put("interNationalNumber", phone.interNationalNumber);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(e.getMessage()+" at verifyCodeForMobileNumber Module!!");
        }

        MServiceBase service = new MServiceBase(context, R.string.mobile_code_verify_api, null, params, MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.POST){

            @Override
            protected boolean onResponseReceived(String response, JSONObject JSON) throws JResponseError {
                boolean status = super.onResponseReceived(response, JSON);
                AUserDetail user = new AUserDetail(context, JSON);
                if(listeners!=null){
                    listeners.onSuccess(user);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }

    public boolean getTermsAndConditionsCustomer(final Context context,JSONObject param, final UserServiceListeners listeners){


        MServiceBase service = new MServiceBase(context, R.string.terms_conditions_get_cust_api, param, null, MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.GET){

            @Override
            protected boolean onResponseReceived(String response,
                                                 JSONObject JSON) throws JResponseError {
                boolean status = super.onResponseReceived(response, JSON);
                AUserTC userTC = new AUserTC(JSON);
                if(listeners!=null){
                    listeners.onSuccess(userTC);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                // TODO Auto-generated method stub
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                // TODO Auto-generated method stub
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }

    public boolean acceptTermsAndConditions(final Context context, final UserServiceListeners listeners){

        MServiceBase service = new MServiceBase(context, R.string.terms_conditions_accept_api, null, null, MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.GET){

            @Override
            protected boolean onResponseReceived(String response,JSONObject JSON) throws JResponseError {
                // TODO Auto-generated method stub
                boolean status = super.onResponseReceived(response, JSON);
                AUserDetail detail = new AUserDetail(context, JSON);
                if(listeners!=null){

                    listeners.onSuccess(detail);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                // TODO Auto-generated method stub
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                // TODO Auto-generated method stub
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }


    // ----


    public boolean addMemberInformation(final Context context, AMemberInfoModel memberInfo, final UserServiceListeners listeners){

        MServiceBase service = new MServiceBase(context, R.string.postmemberinfo_api, null, memberInfo.toJson(), MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.POST){

            @Override
            protected boolean onResponseReceived(String response,JSONObject JSON) throws JResponseError {
                boolean status = super.onResponseReceived(response, JSON);
                AUserDetail user = new AUserDetail(context, JSON);
                if(listeners!=null){
                    listeners.onSuccess(user);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }

    public boolean addOtherInformation(final Context context, AOtherInfoModel otherInfo, final UserServiceListeners listeners){

        MServiceBase service = new MServiceBase(context, R.string.postotherinfo_api, null, otherInfo.toJson(), MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.POST){

            @Override
            protected boolean onResponseReceived(String response,JSONObject JSON) throws JResponseError {
                boolean status = super.onResponseReceived(response, JSON);
                AUserDetail user = new AUserDetail(context, JSON);
                if(listeners!=null){
                    listeners.onSuccess(user);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }

    public boolean addSubAccount(final Context context, ASubAccountModel model, final UserServiceListeners listeners){

        MServiceBase service = new MServiceBase(context, R.string.addsubaccount_api, null, model.toJson(), MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.POST){

            @Override
            protected boolean onResponseReceived(String response,JSONObject JSON) throws JResponseError {
                // TODO Auto-generated method stub
                boolean status = super.onResponseReceived(response, JSON);
                AUserDetail detail = new AUserDetail(context, JSON);
                if(listeners!=null){

                    listeners.onSuccess(detail);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                // TODO Auto-generated method stub
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                // TODO Auto-generated method stub
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }




    public boolean updateUser(final Context context, JSONObject userDetail, final UserServiceListeners listeners){

        MServiceBase service = new MServiceBase(context, R.string.update_user_detail_api, null, userDetail, MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.POST){

            @Override
            protected boolean onResponseReceived(String response,JSONObject JSON) throws JResponseError {
                boolean status = super.onResponseReceived(response, JSON);
                AUserDetail user = new AUserDetail(context, JSON);
                if(listeners!=null){
                    listeners.onSuccess(user);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }



    public boolean updatePassword(final Context context, JSONObject userDetail, final UserServiceListeners listeners){

        MServiceBase service = new MServiceBase(context, R.string.update_password_detail_api, null, userDetail, MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.POST){

            @Override
            protected boolean onResponseReceived(String response,JSONObject JSON) throws JResponseError {
                boolean status = super.onResponseReceived(response, JSON);
                ABasicMessage user = new ABasicMessage(JSON);
                if(listeners!=null){
                    listeners.onSuccess(user);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }

    public boolean uploadMedia(final Context context,  JSONObject urlParams, final UserServiceListeners listeners){

        MServiceBase service = new MServiceBase(context, R.string.uploadmedia, null, urlParams, MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.MULTIPART){

            @Override
            protected boolean onResponseReceived(String response,JSONObject JSON) throws JResponseError {
                boolean status = super.onResponseReceived(response, JSON);
                //AUserDetail user = new AUserDetail(context, JSON);
                ABasicMessage msg = new ABasicMessage(JSON);
                if(listeners!=null){
                    listeners.onSuccess(msg);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }


    public boolean sendTokenToServer(final Context context, JSONObject tokenDetail, final UserServiceListeners listeners){

        MServiceBase service = new MServiceBase(context, R.string.registerDeviceTokenNotification, null, tokenDetail, MServiceBase.getAccessTokenHeader(context), ApiServiceBase.ServerReuqestMethod.POST){

            @Override
            protected boolean onResponseReceived(String response,JSONObject JSON) throws JResponseError {
                boolean status = super.onResponseReceived(response, JSON);
                ABasicMessage user = new ABasicMessage(JSON);
                if(listeners!=null){
                    listeners.onSuccess(user);
                }
                return status;
            }

            @Override
            protected void onError(Exception e) {
                super.onError(e);
                if(listeners!=null){
                    listeners.onFailure(e);
                }
            }

            @Override
            protected void onForceLogout(Exception e) {
                super.onForceLogout(e);
                if(listeners!=null){
                    listeners.onForceLogout(e);
                }
            }

        };

        return ApiServiceUtils.callService(context, service, showNetworkAlert);

    }


}
