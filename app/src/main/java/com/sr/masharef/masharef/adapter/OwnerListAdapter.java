package com.sr.masharef.masharef.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.model.AOwner;

import java.util.ArrayList;

import static com.sr.masharef.masharef.R.id.main_layout;

public abstract class OwnerListAdapter extends RecyclerView.Adapter<OwnerListAdapter.ViewHolder>{

	ArrayList<AOwner> ownerList;
	Context context;

	@SuppressWarnings("static-access")
	public OwnerListAdapter(Context context, ArrayList<AOwner> ownerList) {

		this.context	= context;
		this.ownerList 	= ownerList;

	}

	//==========
	@Override
	public int getItemCount() {

		if (ownerList == null)
			return 0;

		return ownerList.size();
	}

	//==========
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

		if (ownerList != null) {

			if (ownerList.get(position).getOwnerName() != null && !(ownerList.get(position).getOwnerName()).equalsIgnoreCase(""))
				viewHolder.ownerName.setText(ownerList.get(position).getOwnerName());
			else
				viewHolder.ownerName.setText("N/A");

			viewHolder.villaNumber.setText(ownerList.get(position).getVillaNumber());
			viewHolder.phaseNumber.setText(ownerList.get(position).getPhaseNumber());

			viewHolder.mainLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ownerSelected(ownerList.get(position));
				}
			});

		}
	}

	//==========
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_row_view, null);
		
		ViewHolder viewHolder = new ViewHolder(itemLayoutView);
		
		return viewHolder;
			
	}
	
	//==================================
	
	
	public class ViewHolder extends RecyclerView.ViewHolder{
		
		public TextView phaseNumber;
		public TextView ownerName;
		public TextView villaNumber;
		
		public ImageView ownerThumb;

		public RelativeLayout mainLayout;

		public ViewHolder(View itemLayoutView) {
			
			super(itemLayoutView);
			
			ownerName 	= (TextView) itemLayoutView.findViewById(R.id.owner_name);
			villaNumber = (TextView) itemLayoutView.findViewById(R.id.villa_number);
			phaseNumber = (TextView) itemLayoutView.findViewById(R.id.phase_number);
			
			ownerThumb	= (ImageView)itemLayoutView.findViewById(R.id.owner_thumbnail);

			mainLayout	= (RelativeLayout)itemLayoutView.findViewById(main_layout);

		}

	}
		
	// ====	Listener for Owner Selection
	public abstract void ownerSelected(AOwner owner);

}
