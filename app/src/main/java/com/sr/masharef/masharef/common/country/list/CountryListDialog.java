package com.sr.masharef.masharef.common.country.list;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import com.sr.masharef.masharef.common.country.model.ACountryDetail;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.customwidget.IndexableListView;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.StringMatcher;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class CountryListDialog extends Dialog {

	public interface CountryListDialogListener {
		public void selectedCountryWithDetail(ACountryDetail countryDetail);
	}

	private Context context;
	private IndexableListView countryListView;
	private CountryListDialogListener delegate;
	private CountryListAdapter adapter;
	private ArrayList<ACountryDetail> countries;
	private Button cancel;
	
	public CountryListDialog(Context context, CountryListDialogListener delegate) {
		super(context,android.R.style.Theme_Translucent_NoTitleBar);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.delegate = delegate;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_country_list);
		setUIComponents();
		addListeners();
		attachAdapter();
		//new AttachAdapter().execute();
	}
	
	private void setUIComponents(){
		
		TextView title = (TextView)findViewById(R.id.title);
		cancel = (Button)findViewById(R.id.cancel);
		countryListView = (IndexableListView)findViewById(R.id.country_list);
		
		title.setText(context.getString(R.string.select_country));
		
		TextView done  = (TextView)findViewById(R.id.done);
		done.setVisibility(View.INVISIBLE);
		cancel.setVisibility(View.VISIBLE);
		
	}
	
	private void addListeners(){
		
		cancel.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		countryListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
									long arg3) {
				// TODO Auto-generated method stub
				if(delegate!=null){
					delegate.selectedCountryWithDetail(countries.get(index));
					dismiss();
				}
			}
		});

	}

	private void attachAdapter(){

		countries = new ArrayList<ACountryDetail>();
		try{

			NSArray countryArray = countryList(context);
			for (int i = 0; i < countryArray.count(); i++) {
				NSDictionary row = (NSDictionary)countryArray.objectAtIndex(i);
				ACountryDetail country = new ACountryDetail(row.get("fullname").toString(), row.get("ianacode").toString(), row.get("itucode").toString());
				countries.add(country);
			}
		}
		catch(Exception ex) {
			Log.e(ex.getMessage()+ " at attach adapter..");
		}

		adapter = new CountryListAdapter(context, countries);
		countryListView.setAdapter(adapter);
		countryListView.setFastScrollEnabled(true);

	}

	private static NSArray countryList(Context context){
		NSArray countryArray = new NSArray(0);
		try {
			InputStream is = context.getResources().openRawResource(R.raw.itudb);
			countryArray = (NSArray)PropertyListParser.parse(is);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PropertyListFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return countryArray;
	}

	public static ACountryDetail getDefaultCountry(Context context){

		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String countryIso = "";
		ACountryDetail defaultCountry = null;

		countryIso = tm.getSimCountryIso();
		Log.e("Telephony ISO : "+countryIso);

		/*if(countryIso.isEmpty()){
			countryIso = Locale.getDefault().getCountry();
			Log.e("Locale ISO : "+countryIso);
		}*/

		if(countryIso.isEmpty()){
			//TODO nothing as it is not possible to find the country
		}
		else{
			NSArray countryArray = countryList(context);
			for (int i = 0; i < countryArray.count(); i++) {
				NSDictionary row = (NSDictionary)countryArray.objectAtIndex(i);
				String countryCode = row.get("ianacode").toString();

				if(countryCode.equalsIgnoreCase(countryIso)){
					defaultCountry = new ACountryDetail(row.get("fullname").toString(), row.get("ianacode").toString(), row.get("itucode").toString());
					break;
				}
			}
		}

		return defaultCountry;
	}

//===========================================================================================

	/*private class AttachAdapter extends AsyncTask<Object, Object, Object>{

		private ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = Utils.getProgress(context, context.getString(R.string.fetch_country_list_msg));
			countries = new ArrayList<ACountryDetail>();
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try{
				InputStream is = context.getResources().openRawResource(R.raw.itudb);
				NSArray countryArray = (NSArray)PropertyListParser.parse(is);
				for (int i = 0; i < countryArray.count(); i++) {
					NSDictionary row = (NSDictionary)countryArray.objectAtIndex(i);
					ACountryDetail country = new ACountryDetail(row.get("fullname").toString(), row.get("itucode").toString(), row.get("ianacode").toString());
					countries.add(country);
				}

			}
			catch(Exception ex) {
				Log.e(ex.getMessage()+ " at attach adapter..");
			}
			return null;
		}

		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			adapter = new CountryListAdapter(context, countries);
			countryListView.setAdapter(adapter);
			countryListView.setFastScrollEnabled(true);
			progress.dismiss();
		};

	}*/

//===========================================================================================

	private  class CountryListAdapter extends BaseAdapter implements SectionIndexer {

  		private Context context;
  		private ArrayList<ACountryDetail> data;
  		private android.view.View.OnClickListener listener;
  		
  		public CountryListAdapter(Context context, ArrayList<ACountryDetail> data){
  			
  			this.context = context;
  			this.data = data;
  			
  		}
  		
  		public int getCount() {
  			// TODO Auto-generated method stub
  			return data.size();
  		}

  		public Object getItem(int position) {
  			// TODO Auto-generated method stub
  			return data.get(position);
  		}

  		public long getItemId(int position) {
  			// TODO Auto-generated method stub
  			return position;
  		}

  		public View getView(int position, View convertView, ViewGroup parent) {
  			// TODO Auto-generated method stub
  			
  			if(convertView == null){
  				convertView = LayoutInflater.from(context).inflate(R.layout.country_list_row, parent, false);
  			}
  			
  			TextView countryName = (TextView)convertView.findViewById(R.id.country_name);
  			TextView countryCode = (TextView)convertView.findViewById(R.id.country_code);
  			
  			ACountryDetail detail = data.get(position);
  			
  			countryName.setText(detail.countryName);
  			countryCode.setText("("+detail.countryISN+")");
  			
  			convertView.setTag(position);
  			convertView.setOnClickListener(listener);
  			
  			return convertView;
  		}
  		
  		private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	
		@Override
		public int getPositionForSection(int section) {
			// If there is no item for current section, previous section will be selected
			for (int i = section; i >= 0; i--) {
				for (int j = 0; j < getCount(); j++) {
					ACountryDetail country = (ACountryDetail) getItem(j);
					if (i == 0) {
						// For numeric section
						for (int k = 0; k <= 9; k++) {
							if (StringMatcher.match(String.valueOf(country.countryName.charAt(0)), String.valueOf(k)))
								return j;
						}
					} 
					else {
						if (StringMatcher.match(String.valueOf(country.countryName.charAt(0)), String.valueOf(mSections.charAt(i))))
							return j;
					}
				}
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		@Override
		public Object[] getSections() {
			String[] sections = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++)
				sections[i] = String.valueOf(mSections.charAt(i));
			return sections;
		}
  		
  	}

}
