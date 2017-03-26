package com.sr.masharef.masharef.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.model.subaccount.ASubAccountInfoModel;

import java.util.ArrayList;

public abstract class SubAccountListAdapter extends RecyclerView.Adapter<SubAccountListAdapter.ViewHolder>{

	ArrayList<ASubAccountInfoModel> subAccountList;
	Context context;

	@SuppressWarnings("static-access")
	public SubAccountListAdapter(Context context, ArrayList<ASubAccountInfoModel> subAccountList) {

		this.context			= context;
		this.subAccountList 	= subAccountList;

	}

	//==========
	@Override
	public int getItemCount() {

		if (subAccountList == null)
			return 0;

		return subAccountList.size();
	}

	//==========
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

		if (subAccountList != null) {
			
			viewHolder.firstName.setText(subAccountList.get(position).firstName);
			viewHolder.lastName.setText(subAccountList.get(position).lastName);
			viewHolder.email.setText(subAccountList.get(position).email);


			if ((subAccountList.get(position).gender) != null)
				if ((subAccountList.get(position).gender).equalsIgnoreCase("1")){
					viewHolder.gender.setText("Male");
				}else if ((subAccountList.get(position).gender).equalsIgnoreCase("0")){
					viewHolder.gender.setText("Female");
				}
			viewHolder.relation.setText(subAccountList.get(position).relation);

			viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					deleteSubAccount(position);
				}
			});

		}
	}

	//==========
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_account_row, null);
		
		ViewHolder viewHolder = new ViewHolder(itemLayoutView);
		
		return viewHolder;
			
	}
	
	//==================================
	
	
	public class ViewHolder extends RecyclerView.ViewHolder{
		
		public TextView firstName;
		public TextView lastName;
		public TextView email;
		public TextView gender;
		public TextView relation;
		RelativeLayout deleteBtn;

		public ViewHolder(View itemLayoutView) {
			
			super(itemLayoutView);
			
			firstName 	= (TextView) itemLayoutView.findViewById(R.id.first_name);
			lastName	= (TextView) itemLayoutView.findViewById(R.id.last_name);
			email 		= (TextView) itemLayoutView.findViewById(R.id.email);
			gender 		= (TextView) itemLayoutView.findViewById(R.id.gender);
			relation 	= (TextView) itemLayoutView.findViewById(R.id.relation);
			deleteBtn	= (RelativeLayout)itemLayoutView.findViewById(R.id.button_layout);

		}

	}

	public abstract void deleteSubAccount(int position);

}
