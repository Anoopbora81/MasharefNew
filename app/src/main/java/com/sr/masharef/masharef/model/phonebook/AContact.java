package com.sr.masharef.masharef.model.phonebook;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.model.APhone;
import com.sr.masharef.masharef.model.APhoneNumber;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zuhair on 25/01/17.
 */

public class AContact extends JsonObj {

    public String contact_id;
    public String firstName;
    public String lastName;
    public String work_place;
    public String rating;
    public String categeory_name;
    public APhone phone;
    public ACategory category;
    public APhoneNumber phoneNumber;
    public String countryCode, countryId;
    public String nationalNumber;
    public String i8nNumber;

    public String categeory_id;


    public AContact(JSONObject map) {
        super(map);
        if (!isEmpty) {
            this.contact_id = AJSONObject.optString(map, "ID");
            this.categeory_id =   (AJSONObject.optString(AJSONObject.optJSONObject(map, "phone_category"), "id")).toString();
            this.categeory_name =   (AJSONObject.optString(AJSONObject.optJSONObject(map, "phone_category"), "name")).toString();
            this.firstName  = AJSONObject.optString(map, "first_name","");
            this.lastName   = AJSONObject.optString(map, "last_name","");
            this.work_place = AJSONObject.optString(map, "work_place","");
            this.rating     = AJSONObject.optString(map, "rating","");

            phone			= new APhone(AJSONObject.optJSONObject(map, "phone"));
            phoneNumber     = new APhoneNumber(AJSONObject.optJSONObject(map, "phone"));
            category		= new ACategory(AJSONObject.optJSONObject(map, "phone_category"));

            phoneNumber     = new APhoneNumber();
       //   countryCode =  phoneNumber.countryCode =   (AJSONObject.optString(AJSONObject.optJSONObject(map, "phone"), "country_code")).toString();
         //   countryId =  phoneNumber.countryId =   (AJSONObject.optString(AJSONObject.optJSONObject(map, "phone"), "country_id")).toString();
            // phoneNumber.countryId = country_code;
//            phoneNumber.countryISN =  (AJSONObject.optString(AJSONObject.optJSONObject(map, "phone"), "country_ISN")).toString();
//            phoneNumber.countryName =(AJSONObject.optString(AJSONObject.optJSONObject(map, "phone"), "country_name")).toString();
            nationalNumber =  phoneNumber.nationalNumber =(AJSONObject.optString(AJSONObject.optJSONObject(map, "phone"), "national_number")).toString();
            i8nNumber =phoneNumber.interNationalNumber = (AJSONObject.optString(AJSONObject.optJSONObject(map, "phone"), "interNational_number")).toString();

            int contactId = Integer.parseInt(contact_id);
            int categeoryId = Integer.parseInt(categeory_id);

            MDatabaseManager.getInstance().insertContactList(contactId, categeoryId, categeory_name,firstName, lastName, null, work_place, rating, phoneNumber.countryName,
                    phoneNumber.countryCode, phoneNumber.countryISN, phoneNumber.nationalNumber, phoneNumber.interNationalNumber);
        }
    }


    public AContact() {
        super();
    }

    public  AContact(int contact_id, int categeory_id, String categeory_name, String first_name, String last_name, String occupation, String work_place, String rating, String country_name,
                     String country_code, String country_ISN, String national_number, String interNational_number)
    {
        this.contact_id = ""+contact_id;
        this.categeory_id  =  ""+categeory_id;
         this.categeory_name = categeory_name;
        this.firstName = first_name;
        this.lastName   = last_name;
        this.work_place = work_place;
        this.rating     = rating;
        phoneNumber     = new APhoneNumber();
        countryCode =  phoneNumber.countryCode = country_code;
       // phoneNumber.countryId = country_code;
        phoneNumber.countryISN = country_ISN;
        phoneNumber.countryName = country_name;
        nationalNumber = phoneNumber.nationalNumber = national_number;
       i8nNumber = phoneNumber.interNationalNumber = interNational_number;


    }

    public String getContactId() {
        return contact_id;
    }
    public String getContactName() {
        return firstName+""+lastName;
    }
    public String getContactWorkplace() {
        return work_place;
    }
    public String getContactRating() {
        return rating;
    }
    public String getContactOccupation() {
        return categeory_name;
    }

    public String getContactNo() {
        return phoneNumber.interNationalNumber;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        try{
            json.putOpt("first_name", firstName);
            json.putOpt("last_name", lastName);
            json.putOpt("work_place", work_place);
          //  json.putOpt("rating", rating);

            json.putOpt("category_id", categeory_id);
            json.putOpt("country_id", countryId);
            json.putOpt("national_number", nationalNumber);
            json.putOpt("interNational_number", i8nNumber);
            /*if (phone != null)
                json.putOpt("phone", phone.toJson());*/
          /*  if (phoneNumber != null)
                json.putOpt("phone", phoneNumber.toJson());
            if (category != null)
                json.putOpt("phone_category", category.toJson());*/
        }
        catch(JSONException e){
            Log.d(JSON_TAG,e.getMessage()+" at AContact toJson Module!!");
            e.printStackTrace();
        }
        return json;
    }
}
