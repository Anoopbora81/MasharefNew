package com.sr.masharef.masharef.verification.otherinformation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.user.AOtherInfoModel;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.utility.Validation;
import com.sr.masharef.masharef.verification.AVBaseFragment;
import com.sr.masharef.masharef.webservice.api.UserServices;

import java.util.ArrayList;


public class OtherInformationFragment extends AVBaseFragment {

	private TextView title;

	private ProgressBar progress;

	EditText landlineNo,workInstitute,workAddress,totalCar;
	Spinner occupationSpinner;
	String selectedOccupation = "";
	Button save;
	Button logoutBtn;

	public OtherInformationFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.other_information, container, false);
		
		if(savedInstanceState == null){

			occupationSpinner	= (Spinner) v.findViewById(R.id.occupation_spinner);

			landlineNo		= (EditText) v.findViewById(R.id.landline_no);
			workInstitute	= (EditText) v.findViewById(R.id.work_institute);
			workAddress		= (EditText) v.findViewById(R.id.work_address);
			totalCar		= (EditText) v.findViewById(R.id.total_car);

			progress	= (ProgressBar)v.findViewById(R.id.progress);
			title 		= (TextView)v.findViewById(R.id.title);

			save		= (Button) v.findViewById(R.id.save);
			logoutBtn	= (Button)v.findViewById(R.id.cancel);

			setUIComponents();
			addListeners();
		}	
		
		return v;
		
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void setUIComponents(){
		title.setText(getString(R.string.other_information));
		logoutBtn.setVisibility(View.VISIBLE);

		ArrayList<String> occupationList = new ArrayList<>();

		occupationList.add(getString(R.string.select_occupation));

		occupationList.add(getString(R.string.enployee));
		occupationList.add(getString(R.string.businessperson));
		occupationList.add(getString(R.string.retired));
		occupationList.add(getString(R.string.other));

		ArrayAdapter<String> occupationAdapter=new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, occupationList){

			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);

				((TextView) v).setTextSize(16);

				return v;

			}
		};

		occupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<String> villaAdapter=new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, occupationList);
		villaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		occupationSpinner.setAdapter(villaAdapter);

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

				if (validate()){
					addOtherInformation(getActivity(),getSelection());
				}

			}
		});

		occupationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				if (i == 0){
					selectedOccupation = "";
				}else {
					selectedOccupation = adapterView.getItemAtPosition(i).toString();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

	}

	boolean validate(){

		Validation valid	= new Validation(getActivity());
		valid.ShowAlerts(true);


		/*if (valid.checkIfEmpty(landlineNo, getString(R.string.landline_no))){
			return false;
		}

		if (valid.checkIfEmpty(occupation, getString(R.string.occupation))){
			return false;
		}

		if (valid.checkIfEmpty(workInstitute, getString(R.string.work_institute))){
			return false;
		}

		if (valid.checkIfEmpty(workAddress, getString(R.string.work_address))){
			return false;
		}

		if (valid.checkIfEmpty(totalCar, getString(R.string.total_car))){
			return false;
		}*/

		return true;
	}

	public void addOtherInformation(final Context context, final AOtherInfoModel otherInfo){

		final ProgressDialog progress = Utils.getProgress(context, context.getString(R.string.processing_number));

		new Thread(new Runnable() {

			public void run() {

				UserServices userServ = new UserServices();
				boolean callStatus 	= userServ.addOtherInformation(context, otherInfo, new UserServices.UserServiceListeners() {

					public void onSuccess(JsonObj model) {


						AUserDetail user = (AUserDetail)model;

						if(user == null || user.userActions.otherInformation){
							displayErrorOnMainThread();
						}
						else{
							StaticContext.setLoggedInUserDetail(user,null);
							if(listener!=null){
								listener.onSuccessEvent();
							}
						}

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

	AOtherInfoModel getSelection(){
		AOtherInfoModel otherInfo = new AOtherInfoModel();

		otherInfo.landlineNumber	= landlineNo.getText().toString();
		otherInfo.occupation 		= selectedOccupation;
		otherInfo.workInstitute 	= workInstitute.getText().toString();
		otherInfo.workAddress 		= workAddress.getText().toString();
		otherInfo.totalNoCars 		= totalCar.getText().toString();

		return otherInfo;
	}

	private void displayErrorOnMainThread() {

		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getActivity(),"displayErrorOnMainThread in member info \n user == null || user.userActions.otherInformation",Toast.LENGTH_LONG).show();
			}
		});
	}

}
