package com.sr.masharef.masharef.verification.memberinformation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.user.AMemberInfoModel;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.verification.AVBaseFragment;
import com.sr.masharef.masharef.webservice.api.UserServices;

import java.util.ArrayList;
import java.util.List;


public class MemberInformationFragment extends AVBaseFragment {

	private TextView title;

	//private ProgressBar progress;

	Button logoutBtn,save;

	Spinner driver_spinner,servent_spinner,kids_spinner,adult_spinner;

	List<String> spinnerData;

//	ImageView back_member_image;

	public MemberInformationFragment() {
		super();
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.member_information, container, false);
		
		if(savedInstanceState == null){
		
			//progress	= (ProgressBar)v.findViewById(R.id.progress);
			title 		= (TextView)v.findViewById(R.id.title);
			logoutBtn	= (Button)v.findViewById(R.id.cancel);
			save		= (Button)v.findViewById(R.id.save);

			adult_spinner 	= (Spinner)v.findViewById(R.id.adult_spinner);
			kids_spinner 	= (Spinner)v.findViewById(R.id.kids_spinner);
			servent_spinner	= (Spinner)v.findViewById(R.id.servent_spinner);
			driver_spinner 	= (Spinner)v.findViewById(R.id.driver_spinner);
//			back_member_image = (ImageView)v.findViewById(R.id.cancel_image_view) ;


			setUIComponents();
			addListeners();
			setAdapter();
		}	
		
		return v;
		
	}

	void setAdapter(){
		prepareSpinner();

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, spinnerData);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		driver_spinner.setAdapter(dataAdapter);
		servent_spinner.setAdapter(dataAdapter);
		kids_spinner.setAdapter(dataAdapter);
		adult_spinner.setAdapter(dataAdapter);

	}

	@Override
	public void onStart() {
		super.onStart();


	}

	private void setUIComponents(){
		title.setText(getString(R.string.member_information));
		logoutBtn.setVisibility(View.VISIBLE);
//		back_member_image.setBackgroundResource(R.mipmap.ic_back);


	}
	
	private void addListeners(){

		logoutBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				doLogout();
			}
		});

		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addMemberInformation(getActivity(),getSelection());
			}
		});

	}

	void prepareSpinner(){

		spinnerData = new ArrayList<String>();


		for (int i=0 ; i<11;i++)
		{
			spinnerData.add(""+i);
		}

	}

	AMemberInfoModel getSelection(){
		AMemberInfoModel memberInfo = new AMemberInfoModel();

		memberInfo.adults 	= adult_spinner.getSelectedItemPosition();
		memberInfo.kids 	= kids_spinner.getSelectedItemPosition();
		memberInfo.servent 	= servent_spinner.getSelectedItemPosition();
		memberInfo.drivers 	= driver_spinner.getSelectedItemPosition();

		return memberInfo;
	}

	public void addMemberInformation(final Context context, final AMemberInfoModel memberInfo){

		final ProgressDialog progress = Utils.getProgress(context, context.getString(R.string.processing_number));

		new Thread(new Runnable() {

			public void run() {

				UserServices userServ = new UserServices();
				boolean callStatus 	= userServ.addMemberInformation(context, memberInfo, new UserServices.UserServiceListeners() {

					public void onSuccess(JsonObj model) {

						AUserDetail user = (AUserDetail)model;

						if(user == null || user.userActions.memberInformation){
							displayErrorOnMainThread();
						}
						else{
							StaticContext.setLoggedInUserDetail(user,null);
							if(listener!=null){
								listener.onSuccessEvent();
							}
						}

						/*if(listener!=null){
							listener.onSuccessEvent();
						}*/


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

	private void displayErrorOnMainThread() {

		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getActivity(),"displayErrorOnMainThread in member info \n user == null || user.userActions.memberInformation",Toast.LENGTH_LONG).show();
			}
		});
	}

}
