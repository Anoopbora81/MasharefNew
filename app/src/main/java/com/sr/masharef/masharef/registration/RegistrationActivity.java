package com.sr.masharef.masharef.registration;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.common.keychain.KeychainManager;
import com.sr.masharef.masharef.constants.Constants.ResultKeys;
import com.sr.masharef.masharef.login.LoginActivity;
import com.sr.masharef.masharef.manager.ActivityManager;
import com.sr.masharef.masharef.manager.ApplicationManager;
import com.sr.masharef.masharef.model.ABasicMessage;
import com.sr.masharef.masharef.model.AOwner;
import com.sr.masharef.masharef.model.AccessToken;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.registration.AOwnerModel;
import com.sr.masharef.masharef.model.registration.ARegistrationModel;
import com.sr.masharef.masharef.model.user.AOwnerTemplate;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.utility.Validation;
import com.sr.masharef.masharef.webservice.api.AccountServices;
import com.sr.masharef.masharef.webservice.api.AccountServices.AccountServiceListeners;
import com.sr.masharef.masharef.webservice.api.PhaseNumbersServices;
import com.sr.masharef.masharef.webservice.api.UserServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.sr.masharef.masharef.R.id.select_owner_layout;

public class RegistrationActivity extends ActivityManager {

	private Button cancel;
	EditText villa_no,phase_no,email,confirm_email,password,confirm_password,first_name,last_name, first_name_owner,last_name_owner,phone_number_owner,email_owner;

	TextView select_owner;
	RelativeLayout selectOwnerLayout;

	Button register;

	RadioGroup genderRadioGroup, ownerRenterRdoGroup;

	RadioButton ownerRadio, renterRadio, maleRadio, femaleRadio;

	AOwnerTemplate ownerTemplate;
	AOwner selectedOwner;

	DialogFragment ownerListDialog;
	ImageView cancel_image_view;

	Context c;
	private Spinner villaSpinner, spinnerPhaseNum;
	private String selectedPhaseNum ="",selectedOption = "";
	ArrayList<String> phaselist ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		cancel  			= (Button)findViewById(R.id.cancel);
		register  			= (Button)findViewById(R.id.register);

		villa_no 			= (EditText) findViewById(R.id.villa_no);
		phase_no 			= (EditText) findViewById(R.id.phase_no);
		first_name			= (EditText) findViewById(R.id.first_name);
		last_name			= (EditText) findViewById(R.id.last_name);
		email				= (EditText) findViewById(R.id.email);
		confirm_email		= (EditText) findViewById(R.id.confirm_email);
		password			= (EditText) findViewById(R.id.password);
		confirm_password	= (EditText) findViewById(R.id.confirm_password);

		selectOwnerLayout	= (RelativeLayout)findViewById(select_owner_layout);

		genderRadioGroup 	= (RadioGroup) findViewById(R.id.gender_rdo_group);
		ownerRenterRdoGroup = (RadioGroup) findViewById(R.id.owner_renter_rdo_group);

		ownerRadio 			= (RadioButton) findViewById(R.id.owner_rdo);
		renterRadio 		= (RadioButton) findViewById(R.id.renter_rdo);
		maleRadio 			= (RadioButton) findViewById(R.id.male_rdo);
		femaleRadio			= (RadioButton) findViewById(R.id.female_rdo);
		cancel_image_view 	= (ImageView)findViewById(R.id.cancel_image_view);


		villaSpinner 		= (Spinner)findViewById(R.id.spinnerVillaNum);
		spinnerPhaseNum 	= (Spinner)findViewById(R.id.spinnerPhaseNum);

		first_name_owner 	= (EditText) findViewById(R.id.first_name_owner);
		last_name_owner 	= (EditText) findViewById(R.id.last_name_owner);
		phone_number_owner 	= (EditText) findViewById(R.id.phone_number_owner);
		email_owner 		= (EditText) findViewById(R.id.email_owner);

		setupUIComponents();

		getPhaseNumber();
		initializeListener();

		//getOwnerList();

		getFragmentManager();

	}

	private void getPhaseNumber() {
		final ProgressDialog progress = Utils.getProgress(this,getString(R.string.getting_phase_number));

		new Thread(new Runnable() {

			public void run() {


				PhaseNumbersServices userServ = new PhaseNumbersServices(ApplicationManager.context);

				boolean callStatus 	= userServ.getPhaseList(new PhaseNumbersServices.PhaseNumbersServiceListeners(){
					@Override
					public void onSuccess(JSONObject jsonJoiningEvents) {
						JSONArray phaseListArray = AJSONObject.optJSONArray(jsonJoiningEvents, "phaseList");

						phaselist = new ArrayList<String>();
						phaselist.add(getString(R.string.select_phase));
						for (int i = 0; i < phaseListArray.length(); i++) {
							String phaseId =(AJSONObject.optString(phaseListArray.optJSONObject(i), "phase")).toString();
							phaselist.add(phaseId);
						}
						initializeAdapterOnMainThread();
						progress.dismiss();
					}

					@Override
					public void onFailure(Exception error) {

					}

					@Override
					public void onForceLogout(Exception error) {

					}

				});


				if(!callStatus)
					progress.dismiss();

			}

		}).start();

	}

	private void initializeAdapterOnMainThread() {

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					initializeAdapter();
				}
			});
	}
	private void initializeAdapter() {
		ArrayAdapter<String> villaAdapter=new ArrayAdapter<String>(this, R.layout.spinner_item, phaselist){

			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);

				((TextView) v).setTextSize(16);

				return v;

			}
		};

		villaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPhaseNum.setAdapter(villaAdapter);

	}

	@Override
	protected void onStart() {
		super.onStart();

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
	
	
	@Override
	public void onBackPressed() {
		
		Intent data = new Intent();
		data.putExtra(ResultKeys.ACTIVITY_RESULT.toString(), 9089);
		setResult(RESULT_OK,data);
		finish();
		
	}
	
	private void setupUIComponents(){
		
		TextView title	= (TextView)findViewById(R.id.title);
		Button done		= (Button)findViewById(R.id.done);

		title.setText(getString(R.string.registration));

		cancel.setVisibility(View.VISIBLE);
		done.setVisibility(View.INVISIBLE);
		cancel_image_view.setBackgroundResource(R.mipmap.ic_back);

		ArrayList<String> villaAdapterList= new ArrayList<String>();
		villaAdapterList.add(getString(R.string.select));
		villaAdapterList.add("A");
		villaAdapterList.add("B");
		ArrayAdapter<String> villaAdapter=new ArrayAdapter<String>(this, R.layout.spinner_text, villaAdapterList);
		villaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		villaSpinner.setAdapter(villaAdapter);


	}
	
	private void initializeListener(){

		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra(ResultKeys.ACTIVITY_RESULT.toString(), 9089);
				setResult(RESULT_OK,data);
				finish();
			}
		});

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				registerOperation();
			}
		});


		genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				if (checkedId == R.id.male_rdo){

				}else if (checkedId == R.id.female_rdo){

				}
			}
		});


		ownerRenterRdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				if (checkedId == R.id.owner_rdo){
					//select_owner.setVisibility(View.INVISIBLE);
					selectOwnerLayout.setVisibility(View.GONE);
				}else if (checkedId == R.id.renter_rdo){
					//select_owner.setVisibility(View.VISIBLE);
					selectOwnerLayout.setVisibility(View.VISIBLE);
				}
			}
		});

		villaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				selectedOption = adapterView.getItemAtPosition(i).toString();
				if (selectedOption.equalsIgnoreCase(getString(R.string.select))){
					selectedOption = "";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}

		});

		spinnerPhaseNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				selectedPhaseNum = adapterView.getItemAtPosition(i).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});


	}

	
	private void registerOperation(){

		if (validate()){

			final ProgressDialog progress = Utils.getProgress(this, getString(R.string.registering), getString(R.string.wait));
			Utils.SoftKeyBoard(this, false);

			final ARegistrationModel model = new ARegistrationModel();

			model.villaNumber	= villa_no.getText().toString()+selectedOption;
			model.phaseNumber 	= selectedPhaseNum;
			model.email 		= email.getText().toString();
			model.firstName 	= first_name.getText().toString();
			model.lastName 		= last_name.getText().toString();
			model.password 		= password.getText().toString();

			model.selectedlanguage = Utils.getSelectedLanguage(this);

			if (maleRadio.isChecked()){
				model.gender = "1";
			}else if (femaleRadio.isChecked()){
				model.gender = "0";
			}

			if (ownerRadio.isChecked()){
				model.isOwner = true;
			}else if (renterRadio.isChecked()){

				AOwnerModel aOwnerModel =  new AOwnerModel();

				aOwnerModel.firstName 		= first_name_owner.getText().toString();
				aOwnerModel.lastName 		= last_name_owner.getText().toString();
				aOwnerModel.phoneNumber  	= phone_number_owner.getText().toString();
				aOwnerModel.email 			= email_owner.getText().toString();

				model.owner 	= aOwnerModel;
				model.isOwner 	= false;
			}


			AccountServices accountServices = new AccountServices(this);

			boolean callStatus = accountServices.doRegistration(model, new AccountServiceListeners() {

				@Override
				public void onSuccess(AccessToken token) {
					KeychainManager.storeAccessToken(RegistrationActivity.this, token);
					KeychainManager.storeLocalLoginUsername(RegistrationActivity.this, model.email);

					String deviceToken = null;
					try {
						deviceToken =  KeychainManager.getDeviceToken(getApplicationContext());
					} catch (Exception e) {
						e.printStackTrace();
					}
					sendTokenToServer(deviceToken);
					progress.dismiss();
					setResult(RESULT_OK);
					finish();
				}

				@Override
				public void onSuccess(AOwnerTemplate ownerTemplate) {

				}

				@Override
				public void onFailure(Exception e) {
					Utils.showAlertOnMainThread(RegistrationActivity.this, R.string.registration_error,e.getMessage());
					progress.dismiss();
				}

				@Override
				public void onForceLogout(Exception error) {

				}
			});

			if(!callStatus){
				progress.dismiss();
			}

		}

	}


	/*private void getOwnerList(){

		final ProgressDialog progress = Utils.getProgress(this, getString(R.string.loading), getString(R.string.wait));
		Utils.SoftKeyBoard(this, false);

		AccountServices accountServices = new AccountServices(this);

		boolean callStatus = accountServices.getOwnerList(new AccountServiceListeners() {

			@Override
			public void onSuccess(AccessToken token) {

			}

			@Override
			public void onSuccess(AOwnerTemplate ownerTemplate) {

				if (ownerTemplate != null)
					RegistrationActivity.this.ownerTemplate = ownerTemplate;
				else
					renterRadio.setEnabled(false);

				progress.dismiss();
			}

			@Override
			public void onFailure(Exception e) {
				Utils.showAlertOnMainThread(RegistrationActivity.this, R.string.registration_error,e.getMessage());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						renterRadio.setEnabled(false);
					}
				});

				progress.dismiss();
			}

			@Override
			public void onForceLogout(Exception error) {
				progress.dismiss();
			}
		});

		if(!callStatus){
			progress.dismiss();
		}

	}*/

	void sendTokenToServer(final String deviceToken){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


		final ProgressDialog progress = Utils.getProgress(RegistrationActivity.this, getString(R.string.signing_in), getString(R.string.wait));

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("device_type","android");
			jsonObject.put("token",deviceToken);
		}catch (Exception e){

		}

		UserServices userServicesr = new UserServices(true);
		boolean callStatus = userServicesr.sendTokenToServer(RegistrationActivity.this, jsonObject, new UserServices.UserServiceListeners() {
			@Override
			public void onSuccess(JsonObj info) {
				ABasicMessage msg =  (ABasicMessage)info;
			}

			@Override
			public void onFailure(Exception error) {

			}

			@Override
			public void onForceLogout(Exception error) {

			}
		});

		if(!callStatus){
			progress.dismiss();
		}
		/*else{
			Utils.SoftKeyboard(this, user_name, false);
		}*/
            }
        });
	}

	boolean validate(){

		Validation valid	= new Validation(RegistrationActivity.this);
		valid.ShowAlerts(true);

		if (valid.checkIfEmpty(villa_no, getString(R.string.villa_no))){
			return false;
		}

		/*if (valid.checkIfEmpty(phase_no, getString(R.string.phase_no))){
			return false;
		}*/

		if (spinnerPhaseNum.getSelectedItemPosition() ==0){
			valid.displayDialog(getString(R.string.select_phase));
			return false;
		}




		if (valid.checkIfEmpty(first_name, getString(R.string.first_name))){
			return false;
		}

		if (valid.checkIfEmpty(last_name, getString(R.string.last_name))){
			return false;
		}

		if (!maleRadio.isChecked() && !femaleRadio.isChecked()){
			Utils.alert(RegistrationActivity.this,getString(R.string.regsister_gender));
			return false;
		}

		if (!(valid.validEmailSimple(email.getText().toString(), getString(R.string.email)))){
			return false;
		}

		if (!(valid.validEmailSimple(confirm_email.getText().toString(), getString(R.string.confirm_email))))
		{
			return false;
		}else {
			if (!(valid.checkIfEqual(email.getText().toString(), getString(R.string.email), confirm_email.getText().toString(), getString(R.string.confirm_email))))
			{
				return false;
			}
		}

		if (valid.checkIfEmpty(password.getText().toString(), getString(R.string.password))){
			return false;
		}

		if (valid.checkIfEmpty(confirm_password.getText().toString(), getString(R.string.confirm_password))){
			return false;
		}else {
			if (!(valid.checkIfEqual(password.getText().toString(), getString(R.string.password), confirm_password.getText().toString(), getString(R.string.confirm_password)))){
				return false;
			}
		}

		if (!ownerRadio.isChecked() && !renterRadio.isChecked()){
			Utils.alert(RegistrationActivity.this,getString(R.string.register_owner));
//			Toast.makeText(RegistrationActivity.this,"Select Owner or Renter",Toast.LENGTH_LONG).show();
			return false;
		}

		if (renterRadio.isChecked()){
			/*if (selectedOwner == null){
				Toast.makeText(RegistrationActivity.this,"Select owner",Toast.LENGTH_LONG).show();
				return false;
			}*/
			if (valid.checkIfEmpty(first_name_owner, getString(R.string.owner_first_name))){
				return false;
			}

			if (valid.checkIfEmpty(last_name_owner, getString(R.string.owner_last_name))){
				return false;
			}

			if (valid.checkIfEmpty(phone_number_owner, getString(R.string.owner_phone_number))){
				return false;
			}

		}



		return true;
	}
	
}
