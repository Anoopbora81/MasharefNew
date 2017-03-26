package com.sr.masharef.masharef.verification.mobile;


import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.country.list.CountryListDialog.CountryListDialogListener;
import com.sr.masharef.masharef.common.country.model.ACountryDetail;
import com.sr.masharef.masharef.countrylist.CountryListFragment;
import com.sr.masharef.masharef.model.APhoneNumber;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.country.ACountry;
import com.sr.masharef.masharef.model.phonebook.AOTPObjects;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.utility.Validation;
import com.sr.masharef.masharef.verification.AVBaseFragment;
import com.sr.masharef.masharef.verification.AVBaseFragment.AVBaseFragmentListener;
import com.sr.masharef.masharef.webservice.api.UserServices;

import java.util.ArrayList;


public class MobileVerificationFragment extends AVBaseFragment implements CountryListDialogListener, AVBaseFragmentListener{

	private Button countryCodeSel;
	private Button sendNumberBtn;
	private Button logoutBtn;
	private EditText phoneTxt;
	private TextView title;
	private boolean verifyNumber;
	ArrayList<ACountry> countryListCache;
	ImageView back_image_view;

	public APhoneNumber phone;
	DialogFragment countryListDialog;
	public MobileVerificationFragment() {
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_mobile_verify, container, false);
		
		if(savedInstanceState == null){
			
			countryCodeSel = (Button)v.findViewById(R.id.country_sel);
			sendNumberBtn  = (Button)v.findViewById(R.id.send_number);
			logoutBtn	   = (Button)v.findViewById(R.id.cancel);
			phoneTxt	   = (EditText)v.findViewById(R.id.phone_number_text);
			title		   = (TextView)v.findViewById(R.id.title);
//			back_image_view 	=(ImageView)v.findViewById(R.id.cancel_image_view);
			
			
			phone = new APhoneNumber();
			verifyNumber = true;
			
			setUIComponents(v);
			addListeners();
			
			/*ACountryDetail detail = CountryListDialog.getDefaultCountry(getActivity());
			Log.e("detail is NULL "+(detail == null));
			if(detail!=null){
				setSelectedCountry(detail);
			}*/
			
		}
		
		return v;
		
	}
	
	private void setUIComponents(View rootView){
		
		title.setText(getString(R.string.confirm_your_number));
		logoutBtn.setVisibility(View.VISIBLE);
//		back_image_view.setBackgroundResource(R.mipmap.ic_back);

	}
	
	private void addListeners(){
		
		countryCodeSel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//new CountryListDialog(getActivity(), MobileVerificationFragment.this).show();

				String fragmentTag = "countryList";

				countryListDialog = new CountryListFragment(getActivity(),countryListCache) {
					@Override
					public void OnCountrySelect(ACountry country) {
						setSelectedCountry(country);
						//verifyMobileNumber();
						countryListDialog.dismiss();
					}

					@Override
					public void CacheCountryList(ArrayList<ACountry> countryList) {
						if (countryList != null)
							countryListCache = countryList;
					}

					@Override
					public void onBackPressed() {
						countryListDialog.dismiss();
					}
				};

				countryListDialog.show(getActivity().getFragmentManager(),"");


			}
		});
		
		logoutBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doLogout();
			}
		});
		
		sendNumberBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestCodeOperation();
			}
		});
		
		/*phoneTxt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
		
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(verifyNumber)
					verifyMobileNumber();
			}
		});*/
		
	}




	/*private void verifyMobileNumber(){
		
		String currentNumber = phoneTxt.getText().toString();
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
			PhoneNumber myNumber = phoneUtil.parse(currentNumber, phone.countryCode);
			if(myNumber!=null && phoneUtil.isValidNumber(myNumber)){
				verifyNumber = false;
				phoneTxt.setText(phoneUtil.format(myNumber, PhoneNumberFormat.NATIONAL));
				verifyNumber = true;
				
				sendButtonEnable(true);
				
			}else{
				sendButtonEnable(false);
			}
		}
		catch (NumberParseException e) {
			e.printStackTrace();
			Log.e(e.getMessage()+" At verifyMobileNumber() module of MobileVerificationFragment.");
			sendButtonEnable(false);
			
		}
		
	}*/
	
	/*void sendButtonEnable(boolean enable){
		if (enable) {
			//sendNumberBtn.setBackgroundResource(R.color.blue_but_enable);
			sendNumberBtn.setEnabled(true);
		}else {
			//sendNumberBtn.setBackgroundResource(R.color.blue_but_disable);
			sendNumberBtn.setEnabled(false);
		}
	}*/
	
	private boolean verifyMobileNumberOnTap(){

		Validation valid	= new Validation(getActivity());
		valid.ShowAlerts(true);

		if (phone == null || phone.countryId == null || phone.countryId.equalsIgnoreCase("")) {
		//	Toast.makeText(getActivity(),"Select Country Code", Toast.LENGTH_LONG).show();
			Utils.alert(getActivity(), getString(R.string.select_country_code));
			return false;
		}

		else if (valid.checkIfEmpty(phoneTxt, getString(R.string.phone_number))){
			return false;
		}
		
		String currentNumber = phoneTxt.getText().toString();

		//PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		boolean status = false;
		
		/*try {
			PhoneNumber myNumber = phoneUtil.parse(currentNumber, phone.countryCode);
			if(myNumber!=null && phoneUtil.isValidNumber(myNumber)){
				String internationalNumber	= phoneUtil.format(myNumber, PhoneNumberFormat.E164);
		        String nationalNumber		= phoneUtil.format(myNumber, PhoneNumberFormat.NATIONAL);
		        phone.nationalNumber		= nationalNumber;
		        phone.interNationalNumber	= internationalNumber;
				status = true;
			}
		} 
		catch (NumberParseException e) {
			e.printStackTrace();
			Log.e(e.getMessage()+" At verifyMobileNumberOnTap() module of MobileVerificationFragment.");
		}*/

		String internationalNumber	= "+"+phone.countryISN+currentNumber;
		String nationalNumber		= currentNumber;
		phone.nationalNumber		= nationalNumber;
		phone.interNationalNumber	= internationalNumber;
		status = true;
		
		return status;
		
	}
	
	
	private void requestCodeOperation(){
		
		//===Hide Keyboard if opened
		Utils.SoftKeyboard(getActivity(), phoneTxt, false);
		
		//===Verify Number 
		boolean isValidNum = verifyMobileNumberOnTap();
		//boolean isValidNum = validate();
		
		if(isValidNum){
			doRequestMobileVerifyCodeProcess(getActivity(), false, phone);
		}/*else{
			Utils.alert(getActivity(), getString(R.string.number_not_valid));
		}*/
		
	}

	boolean validate(){

		Validation valid	= new Validation(getActivity());
		valid.ShowAlerts(true);

		if (phone == null || phone.countryId == null || phone.countryId.equalsIgnoreCase("")) {
			Utils.alert(getContext(), getString(R.string.select_country_code));
			//Toast.makeText(getActivity(),"Select Country Code", Toast.LENGTH_LONG).show();
			return false;
		}

		else if (valid.checkIfEmpty(phoneTxt, getString(R.string.phone_number))){
			return false;
		}

		verifyMobileNumberOnTap();

		return true;
	}
	
	public void doRequestMobileVerifyCodeProcess(final Context context, final boolean isResend, final APhoneNumber phone){
		
		final ProgressDialog progress = Utils.getProgress(context, context.getString(R.string.processing_number));
		
		new Thread(new Runnable() {
			
			public void run() {
				
				UserServices userServ = new UserServices();
				boolean callStatus 	= userServ.requestMobileVerificationCode(context, phone, new UserServices.UserServiceListeners() {
					
					public void onSuccess(JsonObj model) {
						
						//===Show Verification Activity

						String receivedOTP = null;
						AOTPObjects otpModel = (AOTPObjects)model;

						if (model != null){
							try {
								receivedOTP = otpModel.otp;
								Log.d("OTP OTP OTP OTP OTP OTP OTP OTP OTP XXXXX "+receivedOTP);
							}catch (Exception e){
								Log.e("ERROR while getting OTP from JsonObj");
								e.printStackTrace();
							}
						}
						
						if(!isResend){
							addVerifyCodeFragment(receivedOTP);
						}	
						
						progress.dismiss();
						
					}
					
					public void onForceLogout(Exception error) {
						Log.e(error.getMessage()+" At doRequestMobileVerifyCodeProcess() of MobileVerificationFragment Class");
						progress.dismiss();
						if(listener!=null){
							listener.onLogoutEvent(error);
						}
					}
					
					public void onFailure(Exception error) {
						Log.e(error.getMessage()+" At doRequestMobileVerifyCodeProcess() of MobileVerificationFragment Class");
						Utils.showAlertOnMainThread(context, error.getMessage());
						progress.dismiss();
					}
					
				});
				
				if(!callStatus)
					progress.dismiss();

			}
			
		}).start();
		
	}

	private void setSelectedCountry(ACountryDetail country){
		Log.e("name: "+country.countryName+" code : "+country.countryCode+" ISN : "+country.countryISN);
	    phone.countryName = country.countryName;
	    phone.countryCode = country.countryCode;
	    phone.countryISN  = country.countryISN;
	    countryCodeSel.setText(phone.countryCode+" "+phone.countryCodeWithLeadingPlus());
	}

	private void setSelectedCountry(ACountry country){
		Log.e("name: "+country.getCountry_name()+" code : "+country.getCountry_code()+" ISN : "+country.getCountryISN());
		phone.countryId = country.getCountry_id();
		phone.countryName = country.getCountry_name();
		phone.countryCode = country.getCountry_code();
		phone.countryISN  = country.getCountryISN();
		countryCodeSel.setText(phone.countryCode+" "+phone.countryCodeWithLeadingPlus());

	}
	
	/*
	 * Country Code Selection Delegate
	 */

	@Override
	public void selectedCountryWithDetail(ACountryDetail countryDetail) {
		setSelectedCountry(countryDetail);
		//verifyMobileNumber();
		
	}
	
	private void addVerifyCodeFragment(String receivedOTP){
		
		MobileCodeVerificationFragment fragment = new MobileCodeVerificationFragment(receivedOTP);
		fragment.phone = this.phone;
		fragment.listener = this;
		fragment.parent = this;
		getActivity().getSupportFragmentManager().beginTransaction()
		.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
		.add(R.id.contents, fragment)
		.addToBackStack(null)
		.commit();
		
	}
	

	@Override
	public void onLogoutEvent(Exception error) {
		if(listener!=null){
			listener.onLogoutEvent(error);
		}
	}

	@Override
	public void onCloseEvent() {
		if(listener!=null){
			listener.onCloseEvent();
		}
	}

	@Override
	public void onSuccessEvent() {
		if(listener!=null){
			listener.onSuccessEvent();
		}
	}

}
