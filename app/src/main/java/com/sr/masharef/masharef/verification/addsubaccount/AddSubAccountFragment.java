package com.sr.masharef.masharef.verification.addsubaccount;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.DialogToolbarInterface;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.model.subaccount.ASubAccountInfoModel;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.utility.Validation;

import java.util.ArrayList;
import java.util.List;

public abstract class AddSubAccountFragment extends DialogFragment implements DialogToolbarInterface {

	Context context;

	TextView title;

	Spinner relationSpinner;
	ProgressDialog progress;
	List<String> spinnerData;

	ImageView backButton;
	Button addBtn;

	EditText villa_number,first_name,last_name,email;

	RadioButton male_rdo,female_rdo;
	ImageView back_sub_image;

	public AddSubAccountFragment(Context context) {
		this.context 	= context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, R.style.AppTheme);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.add_sub, container, false);
		
		if (savedInstanceState ==  null) {

			backButton  	= (ImageView)rootView.findViewById(R.id.cancel_image_view);
			title       	= (TextView)rootView.findViewById(R.id.title);
			relationSpinner = (Spinner)rootView.findViewById(R.id.relation_spinner);
			addBtn 			= (Button) rootView.findViewById(R.id.add_btn);

			villa_number	= (EditText) rootView.findViewById(R.id.villa_number);
			first_name		= (EditText) rootView.findViewById(R.id.first_name);
			last_name		= (EditText) rootView.findViewById(R.id.last_name);
			email 			= (EditText) rootView.findViewById(R.id.email);

			male_rdo		= (RadioButton) rootView.findViewById(R.id.male_rdo);
			female_rdo		= (RadioButton) rootView.findViewById(R.id.female_rdo);
			back_sub_image  = (ImageView) rootView.findViewById(R.id.cancel_image_view);

			title.setVisibility(View.VISIBLE);
			title.setText(getString(R.string.add_sub_account));
			back_sub_image.setBackgroundResource(R.mipmap.ic_back);
			
			initializeListener();
			setAdapter();
			initializeView();
		}
		
		return rootView;

	}
	
	@Override
	public void onStart() {
		super.onStart();

	}

	void initializeView(){

		try {

			AUserDetail userInfo = StaticContext.getLoggedInUserDetail();

			Log.e("userInfo "+(userInfo == null));

			//=====Verify User Information
			if(userInfo != null && userInfo.villaNumber != null){
				villa_number.setText(userInfo.villaNumber);
			}

		}catch (Exception e){
			e.printStackTrace();
		}

	}
	void setAdapter(){
		prepareSpinner();

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, spinnerData);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		relationSpinner.setAdapter(dataAdapter);


	}

	private void prepareSpinner() {

		spinnerData = new ArrayList<String>();

		spinnerData.add("Select Relation");
		spinnerData.add("Father");
		spinnerData.add("Mother");
		spinnerData.add("Husband");
		spinnerData.add("Wife");
		spinnerData.add("Son");
		spinnerData.add("Daughter");
		spinnerData.add("other");

	}


	void initializeListener(){
		
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (validate()){
					onSubAccountAdded(getSubAccountModel());
				}
			}
		});
		
	}

	private boolean validate() {

		Validation valid	= new Validation(getActivity());
		valid.ShowAlerts(true);

		if (valid.checkIfEmpty(villa_number, getString(R.string.villa_no))){
			return false;
		}

		if (valid.checkIfEmpty(first_name, getString(R.string.first_name))){
			return false;
		}

		if (valid.checkIfEmpty(last_name, getString(R.string.last_name))){
			return false;
		}

		if (!male_rdo.isChecked() && !female_rdo.isChecked()){
			//Toast.makeText(getActivity(),getString(R.string.regsister_gender),Toast.LENGTH_LONG).show();
			Utils.alert(context, getString(R.string.regsister_gender));
			return false;
		}

		if (!(valid.validEmailSimple(email.getText().toString(), getString(R.string.email)))){
			return false;
		}

		if (relationSpinner.getSelectedItemPosition() == 0){
			//Toast.makeText(getActivity(),"Select relation",Toast.LENGTH_LONG).show();
			Utils.alert(context, getString(R.string.select_relation));
			return false;
		}



		return true;
	}

	ASubAccountInfoModel getSubAccountModel(){

		ASubAccountInfoModel model = new ASubAccountInfoModel();

		model.firstName = first_name.getText().toString();
		model.lastName 	= last_name.getText().toString();
		model.email 	= email.getText().toString();

		if (male_rdo.isChecked()){
			model.gender = "1";
		}else if (female_rdo.isChecked()){
			model.gender = "0";
		}

		model.relation = spinnerData.get(relationSpinner.getSelectedItemPosition());


		return  model;
	}


	@Override
	public void onBackPressed() {
		Log.e("onBackPressed AddSubAccountFragment");
	}
	
	@Override
	public void onDonePressed() {
		
	}

	public abstract void onSubAccountAdded(ASubAccountInfoModel subAccount);


}
