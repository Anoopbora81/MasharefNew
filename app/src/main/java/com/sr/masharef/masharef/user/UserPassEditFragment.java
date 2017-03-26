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
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.DialogToolbarInterface;
import com.sr.masharef.masharef.model.ABasicMessage;
import com.sr.masharef.masharef.model.AOwner;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.utility.Validation;
import com.sr.masharef.masharef.webservice.api.UserServices;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sr.masharef.masharef.R.id.cancel_image_view;

public abstract class UserPassEditFragment extends DialogFragment implements DialogToolbarInterface {

	Context context;


	ImageView backButton;
	TextView title;

	EditText oldPassword,new_password,confirmPassword;

	ProgressDialog progress;

	Button update;

	public UserPassEditFragment(Context context) {
		this.context 	= context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, R.style.AppTheme);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.user_password_edit, container, false);
		
		if (savedInstanceState ==  null) {

			backButton	= (ImageView)rootView.findViewById(cancel_image_view);
			title       = (TextView)rootView.findViewById(R.id.title);
			
			title.setVisibility(View.VISIBLE);
			title.setText(getString(R.string.change_password));

			backButton.setBackgroundResource(R.mipmap.ic_back);

			oldPassword 		= (EditText) rootView.findViewById(R.id.password);
			new_password 	= (EditText) rootView.findViewById(R.id.new_password);
			confirmPassword	= (EditText) rootView.findViewById(R.id.confirm_password);

			update			= (Button) rootView.findViewById(R.id.update);

			
			initializeListener();

		}
		
		return rootView;

	}

	private boolean validate() {

		Validation valid	= new Validation(context);
		valid.ShowAlerts(true);

		if (valid.checkIfEmpty(oldPassword, getString(R.string.old_password))){
			Log.d("return false;");
			return false;
		}



		if (valid.checkIfEmpty(new_password.getText().toString(), getString(R.string.new_password))){
			Log.d("return false;");
			return false;
		}

		if (valid.checkIfEmpty(confirmPassword.getText().toString(), getString(R.string.confirm_password)))
		{
			Log.d("return false;");
			return false;
		}else {
			if (!(valid.checkIfEqual(new_password.getText().toString(), getString(R.string.new_password), confirmPassword.getText().toString(), getString(R.string.confirm_password))))
			{
				Log.d("return false;");
				return false;
			}
		}

		if (oldPassword.getText().toString().equals(new_password.getText().toString())){
			valid.displayDialog("Old Password and New Password can't be same !");
			return false;
		}


		return true;
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


	private JSONObject getUpdatedUserDetail() {

		JSONObject userDetail = new JSONObject();

		try {
			userDetail.putOpt("old_password",oldPassword.getText().toString());
			userDetail.putOpt("new_password",new_password.getText().toString());
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
				userServ.updatePassword(context,userDetail,new UserServices.UserServiceListeners() {

					public void onSuccess(JsonObj model) {
						final ABasicMessage basicMessage = (ABasicMessage) model;

						Log.e("Updated At Date ===>>> " + basicMessage.getMessage());
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								progress.dismiss();
								Utils.alert(context, R.drawable.del_success_alert, R.string.success, basicMessage.getMessage());
								onBackPressed();
							}
						});

					}

					public void onForceLogout(final Exception error) {
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
	
	@Override
	public void onBackPressed() {
		Log.e("onBackPressed UserMailFragment");
	}
	
	@Override
	public void onDonePressed() {
		
	}

	public abstract void OnOwnerSelect(AOwner owner);


}
