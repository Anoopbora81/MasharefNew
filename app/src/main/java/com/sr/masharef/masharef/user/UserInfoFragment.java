package com.sr.masharef.masharef.user;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.DialogToolbarInterface;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.model.AOwner;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.utility.Log;

import static com.sr.masharef.masharef.R.id.cancel_image_view;

public abstract class UserInfoFragment extends DialogFragment implements DialogToolbarInterface {

	Context context;

	ImageView backButton;
	TextView title;

	TextView villa_no,phase_no,email,first_name,last_name;
	RadioButton maleRadio, femaleRadio;

	ProgressDialog progress;

	DialogFragment userInfoEditDialog,userMailEditDialog,userPassEditDialog;

	ImageView edit_info,edit_email,edit_password;


	public UserInfoFragment(Context context) {
		this.context 	= context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, R.style.AppTheme);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.user_info, container, false);
		
		if (savedInstanceState ==  null) {

			backButton	= (ImageView)rootView.findViewById(cancel_image_view);
			title       = (TextView)rootView.findViewById(R.id.title);

			title.setVisibility(View.VISIBLE);
			title.setText(getString(R.string.user_detail));

			backButton.setBackgroundResource(R.mipmap.ic_back);

			villa_no 			= (TextView) rootView.findViewById(R.id.villa_no);
			phase_no 			= (TextView) rootView.findViewById(R.id.phase_no);
			first_name			= (TextView) rootView.findViewById(R.id.first_name);
			last_name			= (TextView) rootView.findViewById(R.id.last_name);
			email				= (TextView) rootView.findViewById(R.id.email);

			maleRadio 			= (RadioButton) rootView.findViewById(R.id.male_rdo);
			femaleRadio			= (RadioButton) rootView.findViewById(R.id.female_rdo);

			edit_info			= (ImageView) rootView.findViewById(R.id.edit_info);
			edit_email			= (ImageView) rootView.findViewById(R.id.edit_email);
			edit_password		= (ImageView) rootView.findViewById(R.id.edit_password);

			initializeListener();
			setUserInfoDetail();
		}
		
		return rootView;

	}

	 void setUserInfoDetail(){

		 getActivity().runOnUiThread(new Runnable() {
			 @Override
			 public void run() {

				 AUserDetail userDetail = StaticContext.getLoggedInUserDetail();

				 if (userDetail != null){
					 if (userDetail.firstName != null  && !(userDetail.firstName.equalsIgnoreCase("")))
						 first_name.setText(userDetail.firstName);
					 if (userDetail.lastName != null  && !(userDetail.lastName.equalsIgnoreCase("")))
						 last_name.setText(userDetail.lastName);

					 String mail = userDetail.email;

					 if (mail != null  && !(mail.equalsIgnoreCase("")))
						 email.setText(mail);

					 if (userDetail.villaNumber != null && !(userDetail.villaNumber.equalsIgnoreCase(""))){
						 villa_no.setText(userDetail.villaNumber);
					 }

					 if (userDetail.phaseNumber != null && !(userDetail.phaseNumber.equalsIgnoreCase(""))){
						 phase_no.setText(userDetail.phaseNumber);
					 }
				 }
			 }
		 });



	}
	
	@Override
	public void onStart() {
		super.onStart();

	}

	void initializeListener(){
		
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});


		edit_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String fragmentTag = "userInfoEdit";

				userInfoEditDialog = new  UserInfoEditFragment(getActivity()){
					@Override
					public void onBackPressed() {
						userInfoEditDialog.dismiss();
					}

					@Override
					public void onDonePressed() {
						setUserInfoDetail();
						userInfoEditDialog.dismiss();
					}

				};

				userInfoEditDialog.show(getFragmentManager(),fragmentTag);

			}
		});

		edit_email.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String fragmentTag = "userMailEdit";

				userMailEditDialog= new  UserMailEditFragment(getActivity()){
					@Override
					public void onBackPressed() {
						userMailEditDialog.dismiss();
					}

					@Override
					public void onDonePressed() {
						userMailEditDialog.dismiss();
					}

					@Override
					public void OnOwnerSelect(AOwner owner) {
						userMailEditDialog.dismiss();
					}
				};

				userMailEditDialog.show(getFragmentManager(),fragmentTag);

			}
		});


		edit_password.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String fragmentTag = "userPassEdit";

				userPassEditDialog= new  UserPassEditFragment(getActivity()){
					@Override
					public void onBackPressed() {
						userPassEditDialog.dismiss();
					}

					@Override
					public void onDonePressed() {
						userPassEditDialog.dismiss();
					}

					@Override
					public void OnOwnerSelect(AOwner owner) {
						userPassEditDialog.dismiss();
					}
				};

				userPassEditDialog.show(getFragmentManager(),fragmentTag);

			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		Log.e("onBackPressed UserInfoFragment");
	}
	
	@Override
	public void onDonePressed() {
		
	}
}
