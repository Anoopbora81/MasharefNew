
//APhoneNumber.java

//Created By Zuhair on May 12, 2014


package com.sr.masharef.masharef.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class APhoneNumber extends JsonObj implements Parcelable {

	public String countryId;
	public String countryName;//India
	public String countryCode;//IN
	public String countryISN;//91
	public String nationalNumber;//98-232-33243
	public String interNationalNumber;//+919823233243
	public String verificationCode;
	
	public APhoneNumber() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	/**
    *
    * Called from the constructor to create this
    * object from a parcel.
    *
    * @param in parcel from which to re-create object.
    */

	public APhoneNumber(Parcel in) {
		super();
		
		try{
			countryId			= in.readString();
			countryName 		= in.readString();
			countryCode 		= in.readString();
			countryISN			= in.readString();
			nationalNumber 		= in.readString();
			interNationalNumber	= in.readString();
			verificationCode	= in.readString();
			
			isEmpty = false;
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e(e.getMessage()+" At APhoneNumber(...) of "+getClass().getName());
		}
	}

	public APhoneNumber(JSONObject map) {
		super(map);
		if(!isEmpty){
			countryId			= AJSONObject.optString(map, "country_id", "");
			verificationCode 	= AJSONObject.optString(map, "verificationCode", "");
			countryISN			= AJSONObject.optString(map, "countryCode", "");
			nationalNumber 		= AJSONObject.optString(map, "nationalNumber", "");
			interNationalNumber = AJSONObject.optString(map, "interNationalNumber", "");
		}
	}
	
	
	@Override
	public JSONObject toJson() {
		JSONObject json = super.toJson();
		try{

			json.putOpt("country_id", countryId);
			json.putOpt("national_number", nationalNumber);
			json.putOpt("interNational_number", interNationalNumber);
			json.putOpt("verificationCode", verificationCode);

			/*json.putOpt("country_code", countryISN);
			json.putOpt("country_ISN", countryISN);
			json.putOpt("country_name", countryName);*/
		}	
		catch(JSONException e){
			Log.d(JSON_TAG,e.getMessage()+" at APhoneNumber toJson Module!!");
			e.printStackTrace();
		}
		return json;
	}
	
	public String countryCodeWithLeadingPlus(){
		
		if(countryISN.startsWith("+")){
			return countryISN;
		}
		else{
			return "+ "+countryISN; 
		}
		
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		// We just need to write each field into the
        // parcel. When we read from parcel, they
        // will come back in the same order
		
		dest.writeString(countryName);
		dest.writeString(countryCode);
		dest.writeString(countryISN);
		dest.writeString(nationalNumber);
		dest.writeString(interNationalNumber);
		dest.writeString(verificationCode);
	}
	
	 /**
    *
    * This field is needed for Android to be able to
    * create new objects, individually or as arrays.
    *
    * This also means that you can use the default
    * constructor to create the object and use another
    * method to hyrdate it as necessary.
    */

	public static final Parcelable.Creator<APhoneNumber> CREATOR = new Parcelable.Creator<APhoneNumber>() {
		  
		public APhoneNumber createFromParcel(Parcel in) {
			return new APhoneNumber(in);
		}
		
		public APhoneNumber[] newArray(int size) {
			return new APhoneNumber[size];
		}
	    
	};

}
