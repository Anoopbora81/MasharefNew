package com.sr.masharef.masharef.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.verification.AVBaseFragment;


public class About extends AVBaseFragment {
	
	private TextView titleTxt;
	private TextView VersionTxt;
	private TextView defaultMail;
	private TextView commanWeb;
	
	private Button termsAndCondition;


	public About() {

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.about, container, false);
		
		if (savedInstanceState == null) {
			titleTxt	  		= (TextView)rootView.findViewById(R.id.title);
			VersionTxt	  		= (TextView)rootView.findViewById(R.id.version);
			commanWeb			= (TextView)rootView.findViewById(R.id.comman_web);
			defaultMail			= (TextView)rootView.findViewById(R.id.default_mail);

			setupUIComponent(rootView);
		}
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//HomeActivity.getInstance().setSliderTitle(getString(R.string.info));
	}
	
	private void setupUIComponent(View v){
		
		titleTxt.setText(getString(R.string.app_name));
		VersionTxt.setText(Utils.getAppVersionName(getActivity()));
		
	}
	


}