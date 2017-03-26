package com.sr.masharef.masharef.verification.mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.model.APhoneNumber;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.verification.AVBaseFragment;
import com.sr.masharef.masharef.webservice.api.UserServices;

import static com.sr.masharef.masharef.R.id.cancel_image_view;


public class MobileCodeVerificationFragment extends AVBaseFragment {


	private static final int VERIFICATION_CODE_LENGTH  = 6;
	private Button backBtn;
	private Button verifyBtn;
	private Button resendCodeBtn;
	private EditText codeTxt;
	private TextView title;
	private TextView verificationMsg;
	private TextView errorMsg;
	ImageView otpIcon;
	ImageView backButton;

	String receivedOTP;

	public  MobileVerificationFragment parent; 
	public APhoneNumber phone;
	
	public MobileCodeVerificationFragment() {
		super();
	}

	public MobileCodeVerificationFragment(String otp) {
		receivedOTP = otp;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_mobile_code_verify, container, false);
		
		if(savedInstanceState == null){
			
			verifyBtn 		= (Button)v.findViewById(R.id.verify_btn);
			resendCodeBtn 	= (Button)v.findViewById(R.id.resend_code_btn);
			backBtn			= (Button)v.findViewById(R.id.cancel);
			codeTxt			= (EditText)v.findViewById(R.id.verify_code_txt);
			verificationMsg = (TextView)v.findViewById(R.id.label2);
			errorMsg		= (TextView)v.findViewById(R.id.error_label);
			title 			= (TextView)v.findViewById(R.id.title);
			otpIcon			= (ImageView)v.findViewById(R.id.otp_icon);
			backButton		= (ImageView)v.findViewById(cancel_image_view);


			
			setUIComponents(v);
			addListeners();
			
		}
		
		return v;
		
	}
	
	private void setUIComponents(View rootView){
		
		Button sync = (Button)rootView.findViewById(R.id.done);
		sync.setVisibility(View.INVISIBLE);
		
		title.setText(getString(R.string.confirm_otp));

		backBtn.setVisibility(View.VISIBLE);
		errorMsg.setVisibility(View.INVISIBLE);
		
		String msg = getString(R.string.sent_msg_title, phone.interNationalNumber);
		int startIndex = msg.indexOf("+");
		int endIndex = msg.indexOf(this.getString(R.string.with), startIndex);
		
		Spannable WordToSpan = new SpannableString(msg);
		WordToSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.colorAccent)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		verificationMsg.setText(WordToSpan);
		
		resendCodeBtn.setText(Utils.getUnderLineString(resendCodeBtn.getText().toString()));

		backButton.setBackgroundResource(R.mipmap.ic_back);

		
	}
	
	private void addListeners(){
		
		verifyBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				phone.verificationCode = codeTxt.getText().toString();
				doRequestVerifyCodeProcess();
			}

		});
		
		resendCodeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				parent.doRequestMobileVerifyCodeProcess(getActivity(), true, phone);
			}
		});
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishFragment();
			}
		});
		
		codeTxt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				verifyBtn.setEnabled(s.toString().length() == VERIFICATION_CODE_LENGTH);
				if (s.toString().length() == VERIFICATION_CODE_LENGTH) {
					//verifyBtn.setBackgroundResource(R.color.blue_but_enable);
				}else {
					//verifyBtn.setBackgroundResource(R.color.blue_but_disable);
				}

				if (codeTxt.getText().toString().equals(receivedOTP)){
					otpIcon.setBackgroundResource(R.mipmap.right_otp);
				}else
				{
					otpIcon.setBackgroundResource(R.mipmap.wrong_otp);
				}
			}
		});
		
	}
	
	private void displayErrorOnMainThread(){
		
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				errorMsg.setVisibility(View.VISIBLE);
			}
		});
		
	}
	
	public void doRequestVerifyCodeProcess(){
		
		final ProgressDialog progress = Utils.getProgress(getActivity(), getString(R.string.processing_code));
		
		new Thread(new Runnable() {
			
			public void run() {
				
				UserServices userServ = new UserServices();
				boolean callStatus 	= userServ.verifyCodeForMobileNumber(getActivity(), phone, new UserServices.UserServiceListeners() {
					
					public void onSuccess(JsonObj model) {
						
						AUserDetail user = (AUserDetail)model;
						
						if(user == null || user.userActions.assignMobile){
							displayErrorOnMainThread();
						}
						else{
							StaticContext.setLoggedInUserDetail(user,null);
							if(listener!=null){
								listener.onSuccessEvent();
							}
						}
						
						Utils.SoftKeyboard(getActivity(), codeTxt, false);
						progress.dismiss();
						
					}
					
					public void onForceLogout(Exception error) {
						
						Log.e(error.getMessage()+" At doRequestVerifyCodeProcess() of MobileCodeVerificationFragment Class");
						progress.dismiss();
						if(listener!=null){
							listener.onLogoutEvent(error);
						}
					}
					
					public void onFailure(Exception error) {
						
						Log.e(error.getMessage()+" At doRequestVerifyCodeProcess() of MobileCodeVerificationFragment Class");
						Utils.showAlertOnMainThread(getActivity(), error.getMessage());
						progress.dismiss();
					}
					
				});
				
				if(!callStatus)
					progress.dismiss();
				
			}
			
		}).start();
		
	}
}
