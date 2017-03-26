package com.sr.masharef.masharef.verification.accountreview;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.verification.AVBaseFragment;
import com.sr.masharef.masharef.webservice.api.UserServices;


public class AccountReviewFragment extends AVBaseFragment {

	Button logoutBtn,refreshBtn;

	public AccountReviewFragment() {
		super();
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.account_review, container, false);
		
		if(savedInstanceState == null){

			logoutBtn	= (Button)v.findViewById(R.id.logout);
			refreshBtn	= (Button)v.findViewById(R.id.refresh);

			addListeners();
		}	
		
		return v;
		
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	private void addListeners(){

		logoutBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				doLogout();
			}
		});

		refreshBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getUserDetail();
			}
		});

	}

	void getUserDetail(){

		final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.checking_profile));

		new Thread(new Runnable() {
			@Override
			public void run() {

				UserServices jobService = new UserServices();
				boolean callStatus = jobService.getLoggedInUserDetails(getActivity(), new UserServices.UserServiceListeners() {
					@Override
					public void onSuccess(JsonObj model) {
						AUserDetail user = (AUserDetail) model;
						StaticContext.setLoggedInUserDetail(user,null);

						if(listener!=null){
							listener.onSuccessEvent();
						}

						progress.dismiss();
					}

					@Override
					public void onFailure(Exception error) {

						Log.e(error.getMessage()+" At getUserDetail() of AccountReviewFragment Class");
						Utils.showAlertOnMainThread(getActivity(), error.getMessage());
						progress.dismiss();


					}

					@Override
					public void onForceLogout(Exception error) {
						Log.e(error.getMessage()+" At getUserDetail() of AccountReviewFragment Class");
						progress.dismiss();
						if(listener!=null){
							listener.onLogoutEvent(error);
						}
					}
				});

				if(!callStatus)
					progress.dismiss();

			}
		}).start();

	}

}
