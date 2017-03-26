package com.sr.masharef.masharef.verification.addsubaccount;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sr.masharef.masharef.adapter.SubAccountListAdapter;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.model.subaccount.ASubAccountInfoModel;
import com.sr.masharef.masharef.model.subaccount.ASubAccountModel;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.verification.AVBaseFragment;
import com.sr.masharef.masharef.webservice.api.UserServices;
import com.sr.masharef.masharef.webservice.api.UserServices.UserServiceListeners;

import java.util.ArrayList;

import static com.sr.masharef.masharef.R.id.button_layout;

public class AddSubAccountMainFragment extends AVBaseFragment {

	private TextView title;
	private Button skipBtn;
	private Button finishBtn;

	DialogFragment addSubAccountDialog;
	ArrayList<ASubAccountInfoModel> subAccountList;

	private RecyclerView.Adapter subAccountAdapter;

	private RecyclerView subAccountRecycler;
	private RecyclerView.LayoutManager mLayoutManager;

	LinearLayout emptyMessageLayout;

	LinearLayout buttonLayout;
//	ImageView back_submain_image;

	public AddSubAccountMainFragment() {
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_add_sub_account, container, false);
		
		if(savedInstanceState == null){

			title 			= (TextView)v.findViewById(R.id.title);
			finishBtn 		= (Button)v.findViewById(R.id.finish);
			skipBtn 		= (Button)v.findViewById(R.id.skip);
			buttonLayout	= (LinearLayout) v.findViewById(button_layout);

			emptyMessageLayout	= (LinearLayout) v.findViewById(R.id.empty_message_layout);
			subAccountRecycler 	= (RecyclerView) v.findViewById(R.id.sub_account_recycler);
//			back_submain_image  = (ImageView)v.findViewById(R.id.cancel_image_view);

			subAccountRecycler.setHasFixedSize(true);

			subAccountList = new ArrayList<ASubAccountInfoModel>();
			mLayoutManager = new LinearLayoutManager(getActivity());

			subAccountRecycler.setLayoutManager(mLayoutManager);

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
		title.setText(getString(R.string.add_sub_account));
		enableDisableFinish(false);
		manageEmptyMessage(true);
//		back_submain_image.setBackgroundResource(R.mipmap.ic_back);
	}
	
	private void addListeners(){

		buttonLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				String fragmentTag = "addSubAccount";

				addSubAccountDialog = new  AddSubAccountFragment(getActivity()){
					@Override
					public void onBackPressed() {
						Log.e("onBackPressed addSubAccountDialog");
						addSubAccountDialog.dismiss();
					}

					@Override
					public void onDonePressed() {
						addSubAccountDialog.dismiss();
					}

					@Override
					public void onSubAccountAdded(ASubAccountInfoModel subAccount) {

						subAccountList.add(subAccount);
						addSubAccountDialog.dismiss();
						initializeAdapter();
					}
				};

				addSubAccountDialog.show(getActivity().getFragmentManager(),"");

			}
		});

		finishBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addSubAccounts(getModel(false));
			}
		});
		
		skipBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addSubAccounts(getModel(true));
			}
		});
		
	}

	void initializeAdapter(){

		subAccountAdapter = new SubAccountListAdapter(getActivity(),subAccountList) {
			@Override
			public void deleteSubAccount(int position) {
				subAccountList.remove(position);
				subAccountAdapter.notifyDataSetChanged();
			}
		};

		subAccountRecycler.setAdapter(subAccountAdapter);
		subAccountAdapter.notifyDataSetChanged();

		if (subAccountList.size() > 0)
		{
			enableDisableFinish(true);
			manageEmptyMessage(false);
		}

	}
	
	private void enableDisableFinish(final boolean enable){
		finishBtn.post(new Runnable() {
			
			@Override
			public void run() {
				finishBtn.setEnabled(enable);
			}
		});
	}
	

	private void addSubAccounts(final ASubAccountModel model){

		final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.updating_profile));
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				UserServices jobService = new UserServices();
				boolean callStatus = jobService.addSubAccount(getActivity(),model, new UserServiceListeners() {
					
					@Override
					public void onSuccess(JsonObj model) {
						
						AUserDetail userDetail = (AUserDetail)model;

						StaticContext.setLoggedInUserDetail(userDetail,null);
						
						if(listener!=null){
							listener.onSuccessEvent();
						}
						
						progress.dismiss();
						
					}
					
					@Override
					public void onForceLogout(Exception error) {
						Log.e(error.getMessage()+" At AddSubAccountAction() of AddSubAccountMainFragment Class");
						progress.dismiss();
						if(listener!=null){
							listener.onLogoutEvent(error);
						}

					}
					
					@Override
					public void onFailure(Exception error) {
						Log.e(error.getMessage()+" At AddSubAccountAction() of AddSubAccountMainFragment Class");
						Utils.showAlertOnMainThread(getActivity(), error.getMessage());
						progress.dismiss();
					}
					
				});
				
				if(!callStatus)
					progress.dismiss();
				
			}
			
		}).start();
		
	}

	ASubAccountModel getModel(boolean skip){

		if (skip){
			return new ASubAccountModel(skip);
		}else {
			return new ASubAccountModel(skip,subAccountList);
		}

	}

	void manageEmptyMessage(boolean show){

		if (show){
			emptyMessageLayout.setVisibility(View.VISIBLE);
			subAccountRecycler.setVisibility(View.INVISIBLE);
		}else {
			emptyMessageLayout.setVisibility(View.INVISIBLE);
			subAccountRecycler.setVisibility(View.VISIBLE);
		}

	}


}
