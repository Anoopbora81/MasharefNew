package com.sr.masharef.masharef.registration;

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

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.adapter.OwnerListAdapter;
import com.sr.masharef.masharef.common.DialogToolbarInterface;
import com.sr.masharef.masharef.model.AOwner;
import com.sr.masharef.masharef.utility.Log;

import java.util.ArrayList;

public abstract class OwnerListFragment extends DialogFragment implements DialogToolbarInterface {

	private RecyclerView ownerRecyclerView;
	@SuppressWarnings("rawtypes")
	private RecyclerView.Adapter ownerAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	ArrayList<AOwner> ownerList;

	Context context;

	Button backButton,doneButton;
	TextView title;
	boolean hadParcels;
	boolean hadRouteSelected;
	ProgressDialog progress;
	TextView emptyText;

	public OwnerListFragment(Context context,ArrayList<AOwner> ownerList) {
		this.context 	= context;
		this.ownerList 	= ownerList;
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
			
			title.setVisibility(View.VISIBLE);
			title.setText(getString(R.string.select_owner));
			
			initializeListener();
			initializeAdapter();
		}
		
		return rootView;

	}
	
	@Override
	public void onStart() {
		super.onStart();

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

		ownerAdapter = new OwnerListAdapter(getActivity(),ownerList) {
			@Override
			public void ownerSelected(AOwner owner) {
				OnOwnerSelect(owner);
			}
		};

		ownerRecyclerView.setAdapter(ownerAdapter);

	}



	@Override
	public void onBackPressed() {
		Log.e("onBackPressed OwnerListFragment");
	}
	
	@Override
	public void onDonePressed() {
		
	}

	public abstract void OnOwnerSelect(AOwner owner);


}
