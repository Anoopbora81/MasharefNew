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
import com.sr.masharef.masharef.common.ImageLoader;
import com.sr.masharef.masharef.model.country.ACountry;

import java.util.ArrayList;

import static com.sr.masharef.masharef.R.id.country_name;

public abstract class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder>{

	ArrayList<ACountry> countryList;
	Context context;
	ImageLoader loader;

	@SuppressWarnings("static-access")
	public CountryListAdapter(Context context, ArrayList<ACountry> countryList) {

		this.context	= context;
		this.countryList 	= countryList;
		loader			= new ImageLoader(context, 200, 230);


	}

	//==========
	@Override
	public int getItemCount() {

		if (countryList == null)
			return 0;

		return countryList.size();
	}

	//==========
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

		if (countryList != null) {

			viewHolder.countryName.setText(countryList.get(position).getCountry_name());

			viewHolder.countryCode.setText("(+"+countryList.get(position).getCountryISN()+")");

			loader.loadImage(countryList.get(position).flag_icon, R.drawable.del_ic_flag, viewHolder.countryThumb);

			viewHolder.mainLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ownerSelected(countryList.get(position));
				}
			});

		}
	}

	//==========
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_row, null);
		
		ViewHolder viewHolder = new ViewHolder(itemLayoutView);
		
		return viewHolder;
			
	}
	
	//==================================
	public class ViewHolder extends RecyclerView.ViewHolder{
		
		public TextView countryName;
		public TextView countryCode;

		public ImageView countryThumb;
		RelativeLayout mainLayout;

		public ViewHolder(View itemLayoutView) {
			
			super(itemLayoutView);

			countryName 	= (TextView) itemLayoutView.findViewById(country_name);
			countryCode 	= (TextView) itemLayoutView.findViewById(R.id.country_code);
			countryThumb	= (ImageView)itemLayoutView.findViewById(R.id.flag_icon);
			mainLayout		= (RelativeLayout) itemLayoutView.findViewById(R.id.main_layout);
		}

	}
		
	// ====	Listener for Country Selection
	public abstract void ownerSelected(ACountry country);

}
