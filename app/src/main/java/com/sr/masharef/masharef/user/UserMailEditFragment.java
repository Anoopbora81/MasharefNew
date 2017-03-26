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
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.model.AOwner;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.utility.Validation;
import com.sr.masharef.masharef.webservice.api.UserServices;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sr.masharef.masharef.R.id.cancel_image_view;

public abstract class UserMailEditFragment extends DialogFragment implements DialogToolbarInterface {

	Context context;


	ImageView backButton;
	TextView title;

	EditText oldEmail,email,confirmEmail;

	ProgressDialog progress;

	Button update;

	public UserMailEditFragment(Context context) {
		this.context 	= context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, R.style.AppTheme);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.user_mail_edit, container, false);
		
		if (savedInstanceState ==  null) {

			backButton	= (ImageView)rootView.findViewById(cancel_image_view);
			title       = (TextView)rootView.findViewById(R.id.title);
			
			title.setVisibility(View.VISIBLE);
			title.setText(getString(R.string.change_email));

			backButton.setBackgroundResource(R.mipmap.ic_back);

			oldEmail 		= (EditText) rootView.findViewById(R.id.ole_email);
			email 			= (EditText) rootView.findViewById(R.id.email);
			confirmEmail	= (EditText) rootView.findViewById(R.id.confirm_email);

			update				= (Button) rootView.findViewById(R.id.update);


			initializeListener();

		}
		
		return rootView;

	}

	private boolean validate() {

		Validation valid	= new Validation(context);
		valid.ShowAlerts(true);

		if (valid.validEmailSimple(oldEmail, getString(R.string.old_email))){
			return false;
		}



		if (!(valid.validEmailSimple(email.getText().toString(), getString(R.string.email)))){
			return false;
		}

		if (!(valid.validEmailSimple(confirmEmail.getText().toString(), getString(R.string.confirm_email))))
		{
			return false;
		}else {
			if (!(valid.checkIfEqual(email.getText().toString(), getString(R.string.email), confirmEmail.getText().toString(), getString(R.string.confirm_email))))
			{
				return false;
			}
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
			userDetail.putOpt("email",oldEmail.getText().toString());
			userDetail.putOpt("new_email",email.getText().toString());
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
						AUserDetail user = (AUserDetail) model;
						StaticContext.setLoggedInUserDetail(user,null);
						Log.e("Updated At Date ===>>> "+ user.updatedAt);
						progress.dismiss();
					}

					public void onForceLogout(final Exception error) {
						Utils.showAlertOnMainThread(context, error.getMessage());
						progress.dismiss();
					}

					public void onFailure(Exception error) {
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
