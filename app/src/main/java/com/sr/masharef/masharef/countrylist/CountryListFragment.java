package com.sr.masharef.masharef.countrylist;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sr.masharef.masharef.adapter.CountryListAdapter;
import com.sr.masharef.masharef.common.DialogToolbarInterface;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.model.country.ACountry;
import com.sr.masharef.masharef.model.country.ACountryTemplate;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.api.CountryServices;

import java.util.ArrayList;

public abstract class CountryListFragment extends DialogFragment implements DialogToolbarInterface {

	private RecyclerView ownerRecyclerView;
	@SuppressWarnings("rawtypes")
	private RecyclerView.Adapter countryAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	ArrayList<ACountry> countryList;

	Context context;

	Button backButton;
	TextView title;

	ProgressDialog progress;
	TextView emptyText;

	public CountryListFragment(Context context, ArrayList<ACountry> countryList) {
		this.context 		= context;
		this.countryList	= countryList;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, R.style.AppTheme);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.owner_list, container, false);
		
		if (savedInstanceState ==  null) {
		
			ownerRecyclerView = (RecyclerView) rootView.findViewById(R.id.owner_recycler_view);
			ownerRecyclerView.setHasFixedSize(true);
			
			mLayoutManager = new LinearLayoutManager(getActivity());
			
			ownerRecyclerView.setLayoutManager(mLayoutManager);
			
			backButton  = (Button)rootView.findViewById(R.id.cancel);

			emptyText	= (TextView)rootView.findViewById(R.id.empty_text);
			title       = (TextView)rootView.findViewById(R.id.title);
			
			emptyText.setVisibility(View.GONE);
			backButton.setVisibility(View.VISIBLE);
			
			title.setVisibility(View.VISIBLE);
			title.setText(getString(R.string.select_country));
			
			initializeListener();
			initializeAdapter();


		}
		
		return rootView;

	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (countryList == null) {
			getCountryList(getActivity());
		}

	}

	void showHideEmptyView(final int count){
		
		emptyText.post(new Runnable() {
			
			@Override
			public void run() {
				emptyText.setVisibility((count<=0)? View.VISIBLE: View.GONE);
			}
		});
	}


	void initializeListener(){
		
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
	}
	
	void initializeAdapter(){

		countryAdapter = new CountryListAdapter(getActivity(),countryList) {
			@Override
			public void ownerSelected(ACountry country) {
				OnCountrySelect(country);
			}
		};

		ownerRecyclerView.setAdapter(countryAdapter);

	}



	@Override
	public void onBackPressed() {
		Log.e("onBackPressed OwnerListFragment");
	}
	
	@Override
	public void onDonePressed() {
		
	}

	public void getCountryList(final Context context) {

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progress = Utils.getProgress(context, context.getString(R.string.getting_country_list));
			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {

				CountryServices service = new CountryServices(context);

				boolean callStatus 	= service.getCountryList(new CountryServices.CountryServiceListeners() {
					@Override
					public void onSuccess(final ACountryTemplate countryTemplate) {
						if (countryTemplate != null)
							countryList = countryTemplate.countryList;


						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								CacheCountryList(countryTemplate.countryList);
								initializeAdapter();
							}
						});


						progress.dismiss();
					}

					@Override
					public void onSuccess(ACountry country) {

						progress.dismiss();
					}

					@Override
					public void onFailure(Exception error) {
						Log.e(error.getMessage()+" At getCountryList() of CountryListFragment Class");
						Utils.showAlertOnMainThread(context, error.getMessage());
						progress.dismiss();
					}

					@Override
					public void onForceLogout(Exception error) {
						progress.dismiss();
					}
				});

				if(!callStatus)
					progress.dismiss();
			}
		}).start();

	}

	public abstract void OnCountrySelect(ACountry country);
	public abstract void CacheCountryList(ArrayList<ACountry> countryList);

}
