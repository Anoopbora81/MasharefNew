package com.sr.masharef.masharef.verification.termsncondition;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.model.user.AUserTC;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.verification.AVBaseFragment;
import com.sr.masharef.masharef.webservice.api.UserServices;
import com.sr.masharef.masharef.webservice.api.UserServices.UserServiceListeners;

import org.json.JSONException;
import org.json.JSONObject;


public class TermsConditionsFragment extends AVBaseFragment {
	
	private TextView title;
	private TextView tcTxt;
	private Button decline;
	private Button agree;
	private ProgressBar progress;
	AUserTC userTC;
	ImageView back_terms_image;
	//ImageButton logoutBtn;
	//ImageButton doneButton;
	
	public TermsConditionsFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_terms_conditions, container, false);
		
		if(savedInstanceState == null){
		
			progress	= (ProgressBar)v.findViewById(R.id.progress);
			title 		= (TextView)v.findViewById(R.id.title);
			tcTxt 		= (TextView)v.findViewById(R.id.terms_conditions_text);
			agree		= (Button)v.findViewById(R.id.agree);
			decline		= (Button)v.findViewById(R.id.decline);
			back_terms_image = (ImageView)v.findViewById(R.id.cancel_image_view);
			//logoutBtn	= (ImageButton)v.findViewById(R.id.left_back_button);
			//doneButton	= (ImageButton)v.findViewById(R.id.right_button);

			setUIComponents();
			addListeners();
		}	
		
		return v;
		
	}

	@Override
	public void onStart() {
		super.onStart();
		if (userTC == null)
			fetchTermsAndConditions();
	}

	private void setUIComponents(){
		title.setText(getString(R.string.terms_and_conditions));
		//doneButton.setVisibility(View.INVISIBLE);
		enableDisableAcceptTerms(false);
		back_terms_image.setVisibility(View.GONE);
//		back_terms_image.setBackgroundResource(R.mipmap.ic_back);
	}
	
	private void addListeners(){

		/*logoutBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doLogout();
			}
		});*/
		
		agree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				termConditionAgreedAction();
			}
		});
		
		decline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doLogout();
			}
		});
		
	}
	
	private void enableDisableProgress(final boolean enable){
		
		progress.post(new Runnable() {
			
			@Override
			public void run() {
				
				if(enable){
					progress.setVisibility(View.VISIBLE);
					tcTxt.setVisibility(View.GONE);
					decline.setEnabled(false);
				}
				else{
					progress.setVisibility(View.GONE);
					tcTxt.setVisibility(View.VISIBLE);
					decline.setEnabled(true);
				}
				
			}
		});
		
	}
	
	private void enableDisableAcceptTerms(final boolean enable){
		agree.post(new Runnable() {
			
			@Override
			public void run() {
				agree.setEnabled(enable);
			}
		});
	}
	
	private void fetchTermsAndConditions(){
		
		enableDisableProgress(true);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				UserServices jobService = new UserServices();

				JSONObject param = new JSONObject();
				String selectedLanguage = Utils.getSelectedLanguage(getActivity());

				try {
					param.putOpt("lang",selectedLanguage);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				boolean callStatus = jobService.getTermsAndConditionsCustomer(getActivity(),param, new UserServiceListeners() {
					
					@Override
					public void onSuccess(JsonObj model) {
						
						userTC = (AUserTC)model;
						
						enableDisableProgress(false);
						enableDisableAcceptTerms(true);
						
						tcTxt.post(new Runnable() {
							@Override
							public void run() {
								//title.setText(userTC.title);
								//tcTxt.setText(userTC.content);
								tcTxt.setText(userTC.Terms_and_conditions);

							}
						});
						
					}
					
					@Override
					public void onForceLogout(Exception error) {
						
						Log.e(error.getMessage()+" At fetchTermsAndConditions() of TermsConditionsFragment Class");
						enableDisableProgress(false);
						
						if(listener!=null){
							listener.onLogoutEvent(error);
						}
						
					}
					
					@Override
					public void onFailure(Exception error) {
						Log.e(error.getMessage()+" At fetchTermsAndConditions() of TermsConditionsFragment Class");
						Utils.showAlertOnMainThread(getActivity(), error.getMessage());
						enableDisableProgress(false);
					}
					
				});
				
				if(!callStatus)
					enableDisableProgress(false);
				
			}
			
		}).start();
		
	}
	
	private void termConditionAgreedAction(){
		
		final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.updating_profile));
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				UserServices jobService = new UserServices();
				boolean callStatus = jobService.acceptTermsAndConditions(getActivity(), new UserServiceListeners() {
					
					@Override
					public void onSuccess(JsonObj model) {
						
						AUserDetail userDetail = (AUserDetail)model;

						StaticContext.setLoggedInUserDetail(userDetail,null);
						
						if(listener!=null){
							listener.onSuccessEvent();
						}
						
						progress.dismiss();
						
					}
					
					@Override
					public void onForceLogout(Exception error) {
						Log.e(error.getMessage()+" At termConditionAgreedAction() of TermsConditionsFragment Class");
						progress.dismiss();
						if(listener!=null){
							listener.onLogoutEvent(error);
						}

					}
					
					@Override
					public void onFailure(Exception error) {
						Log.e(error.getMessage()+" At termConditionAgreedAction() of TermsConditionsFragment Class");
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
