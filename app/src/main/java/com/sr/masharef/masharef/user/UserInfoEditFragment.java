package com.sr.masharef.masharef.user;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.DialogToolbarInterface;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.utility.Validation;
import com.sr.masharef.masharef.webservice.api.UserServices;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sr.masharef.masharef.R.id.cancel_image_view;

public abstract class UserInfoEditFragment extends DialogFragment implements DialogToolbarInterface {

	Context context;

	ImageView backButton;
	TextView title;

	EditText villa_no,phase_no,first_name,last_name;
	RadioButton maleRadio, femaleRadio;

	ProgressDialog progress;

	Button update;

	public UserInfoEditFragment(Context context) {
		this.context	= context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, R.style.AppTheme);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.user_info_edit, container, false);
		
		if (savedInstanceState ==  null) {

			backButton	= (ImageView)rootView.findViewById(cancel_image_view);
			title       = (TextView)rootView.findViewById(R.id.title);
			
			title.setVisibility(View.VISIBLE);
			title.setText(getString(R.string.user_detail));

			backButton.setBackgroundResource(R.mipmap.ic_back);

			villa_no 			= (EditText) rootView.findViewById(R.id.villa_no);
			phase_no 			= (EditText) rootView.findViewById(R.id.phase_no);
			first_name			= (EditText) rootView.findViewById(R.id.first_name);
			last_name			= (EditText) rootView.findViewById(R.id.last_name);

			maleRadio 			= (RadioButton) rootView.findViewById(R.id.male_rdo);
			femaleRadio			= (RadioButton) rootView.findViewById(R.id.female_rdo);

			update				= (Button) rootView.findViewById(R.id.update);

			initializeListener();
			setUserDetail();
		}
		
		return rootView;

	}

	void setUserDetail(){

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {

				AUserDetail userDetail = StaticContext.getLoggedInUserDetail();

				if (userDetail != null){

					if (userDetail.firstName != null  && !(userDetail.firstName.equalsIgnoreCase("")))
						first_name.setText(userDetail.firstName);
					if (userDetail.lastName != null  && !(userDetail.lastName.equalsIgnoreCase("")))
						last_name.setText(userDetail.lastName);

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

		update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (validate()){
					updateUser(getUpdatedUserDetail());
				}
			}
		});
		
	}

	private JSONObject  getUpdatedUserDetail() {

		JSONObject userDetail = new JSONObject();

		try {
			userDetail.putOpt("firstname",first_name.getText().toString());
			userDetail.putOpt("lastname",last_name.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}


		return userDetail;
	}

	private void updateUser(final JSONObject userDetail) {


		// ===Check Network Connectivity and returns if internet not available
		if (!Utils.isNetworkConnected(context, true)) {
			return;
		}

		// ====Start the operation
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progress = Utils.getProgress(context, context.getString(R.string.updating_user));
			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {

				UserServices userServ = new UserServices(false);
				userServ.updateUser(context,userDetail,new UserServices.UserServiceListeners() {

					public void onSuccess(JsonObj model) {
							final AUserDetail user = (AUserDetail) model;
							StaticContext.setLoggedInUserDetail(user,null);
							Log.e("Updated At Date ===>>> "+ user.updatedAt);

							getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								setUserDetail();
								Utils.alert(context,R.drawable.del_success_alert,R.string.success,user.message);
								}
							});

							onDonePressed();
							progress.dismiss();
					}

					public void onForceLogout(final Exception error)
					{
						Utils.showAlertOnMainThread(context, error.getMessage());
						progress.dismiss();
					}

					public void onFailure(Exception error) {

						Utils.showAlertOnMainThread(context, error.getMessage());
						progress.dismiss();
					}
				});

			}
		}).start();

	}

	private boolean validate() {

		Validation valid	= new Validation(context);
		valid.ShowAlerts(true);

		if (valid.checkIfEmpty(villa_no, getString(R.string.villa_no))){
			return false;
		}

		if (valid.checkIfEmpty(phase_no, getString(R.string.phase_no))){
			return false;
		}

		if (valid.checkIfEmpty(first_name, getString(R.string.first_name))){
			return false;
		}

		if (valid.checkIfEmpty(last_name, getString(R.string.last_name))){
			return false;
		}

		/*if (!maleRadio.isChecked() && !femaleRadio.isChecked()){
			valid.displayDialog("Select Gender");
			return false;
		}*/

		return true;
	}

	@Override
	public void onBackPressed() {
		Log.e("onBackPressed UserInfoFragment");
	}
	
	@Override
	public void onDonePressed() {
	}


}
