package com.sr.masharef.masharef.phonebook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.country.list.CountryListDialog;
import com.sr.masharef.masharef.common.country.model.ACountryDetail;
import com.sr.masharef.masharef.countrylist.CountryListFragment;
import com.sr.masharef.masharef.model.APhoneNumber;
import com.sr.masharef.masharef.model.country.ACountry;
import com.sr.masharef.masharef.model.phonebook.ACategory;
import com.sr.masharef.masharef.model.phonebook.ACategoryTemplate;
import com.sr.masharef.masharef.model.phonebook.AContact;
import com.sr.masharef.masharef.model.phonebook.AContactTemplate;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.utility.Validation;
import com.sr.masharef.masharef.verification.AVBaseFragment;
import com.sr.masharef.masharef.webservice.api.PhonebookServices;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewContact.OnFragmentInteractionListener} interface
 * to handle interaction events.
// * Use the {@link AddNewContact#} factory method to
 * create an instance of this fragment.
 */

public class AddNewContact extends AVBaseFragment implements CountryListDialog.CountryListDialogListener, AVBaseFragment.AVBaseFragmentListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView addImage;
    private final int SELECT_FILE = 1;
    static ArrayList<ACategory> categoryList;
    ArrayList<String> occupationList;
    private OnFragmentInteractionListener mListener;
    EditText firstName,lastName,contactNo,workplace;
    Spinner occupationSpinner;
    Button addContact,countryCode;
    private boolean verifyNumberContactNo;
    public APhoneNumber phoneNo;
    InputMethodManager mgr;
    static private DialogFragment countryListDialog;
    private ArrayList<ACountry> countryListCache;

    @SuppressLint("ValidFragment")
    public AddNewContact(ArrayList<ACategory> categoryList) {
        this.categoryList = categoryList;
    }

    @SuppressLint("ValidFragment")
    public AddNewContact(){

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewContact.
     */
    // TODO: Rename and change types and number of parameters
//    public static AddNewContact newInstance(String param1, String param2) {
//        AddNewContact fragment = new AddNewContact(categoryList);
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       /* final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final Toolbar toolbar = (Toolbar)((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setDisplayHomeAsUpEnabled(false);
//                    actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                if(getActivity() != null)
                    getActivity().onBackPressed();

            }
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View addContactView=inflater.inflate(R.layout.add_contact, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.add_contact);

        addImage            = (ImageView) addContactView.findViewById(R.id.add_image);
        occupationSpinner   = (Spinner) addContactView.findViewById(R.id.occupation_spinner);

        firstName           = (EditText) addContactView.findViewById(R.id.first_name);
//        lastName            = (EditText) addContactView.findViewById(R.id.last_name);
        contactNo           = (EditText) addContactView.findViewById(R.id.contact_phone_number_text);
        workplace           = (EditText) addContactView.findViewById(R.id.workplace);

        addContact          = (Button) addContactView.findViewById(R.id.add_btn);
        countryCode         = (Button) addContactView.findViewById(R.id.contact_country_sel);

       /* mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(firstName.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(lastName.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(contactNo.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(workplace.getWindowToken(), 0);*/

        initializeListener();
        initializeAdapter();
        phoneNo = new APhoneNumber();
        verifyNumberContactNo = true;

        addListeners();
     /*   ACountryDetail detail = CountryListDialog.getDefaultCountry(getActivity());
        Log.e("detail is NULL "+(detail == null));
        if(detail!=null){
            setSelectedCountry(detail);
        }*/

        return addContactView;
    }

    private void addListeners(){

        countryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    new CountryListDialog(getActivity(), AddNewContact.this).show();

                String fragmentTag = "countryList";

                countryListDialog = new CountryListFragment(getActivity(),countryListCache) {
                    @Override
                    public void OnCountrySelect(ACountry country) {
                        setSelectedCountry(country);
                        verifyMobileNumber();
                        countryListDialog.dismiss();
                    }

                    @Override
                    public void CacheCountryList(ArrayList<ACountry> countryList) {
                        if (countryList != null)
                            countryListCache = countryList;
                    }
                };

                countryListDialog.show(getActivity().getFragmentManager(),fragmentTag);



            }
        });



        contactNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(verifyNumberContactNo)
                    verifyMobileNumber();
            }
        });

    }


    private void verifyMobileNumber(){

        String currentNumber = contactNo.getText().toString();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber myNumber = phoneUtil.parse(currentNumber, phoneNo.countryCode);
            if(myNumber!=null /*&& phoneUtil.isValidNumber(myNumber)*/){
                verifyNumberContactNo = false;
                contactNo.setText(phoneUtil.format(myNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
                verifyNumberContactNo = true;

                sendButtonEnable(true);

            }else{
                sendButtonEnable(false);
            }
        }
        catch (NumberParseException e) {
            e.printStackTrace();
            Log.e(e.getMessage()+" At verifyMobileNumber() module of MobileVerificationFragment.");
            sendButtonEnable(false);

        }

    }

    void sendButtonEnable(boolean enable){
        if (enable) {
            //sendNumberBtn.setBackgroundResource(R.color.blue_but_enable);
            addContact.setEnabled(true);
        }else {
            //sendNumberBtn.setBackgroundResource(R.color.blue_but_disable);
            addContact.setEnabled(true); //asb
        }
    }

    private void setSelectedCountry(ACountry country){
        Log.e("name: "+country.getCountry_name()+" code : "+country.getCountry_code()+" ISN : "+country.getCountryISN());
        phoneNo.countryId = country.getCountry_id();
        phoneNo.countryName = country.getCountry_name();
        phoneNo.countryCode = country.getCountry_code();
        phoneNo.countryISN  = country.getCountryISN();
        countryCode.setText(phoneNo.countryCode+" "+phoneNo.countryCodeWithLeadingPlus());
    }

    private void initializeAdapter() {

        occupationList = prepareCategoryList();

        ArrayAdapter<String> occupationAdapter=new ArrayAdapter<String>(getActivity(),/*android.R.layout.simple_dropdown_item_1line*/R.layout.phone_spinner_text, occupationList);
        occupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        occupationSpinner.setAdapter(occupationAdapter);

    }

    private ArrayList<String> prepareCategoryList() {

        ArrayList<String> occupationList = new ArrayList<>();
        occupationList.add(getString(R.string.select_occupation));
        if (categoryList != null)
        {
            for (int i = 0; i<categoryList.size();i++)
            {
                occupationList.add(categoryList.get(i).getCategoryName());
            }
        }

        return  occupationList;
    }

    private void initializeListener() {

        occupationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image*//*");
                startActivityForResult(intent, PICK_PHOTO);*/
                /*Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, PICK_PHOTO);*/

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
            }
        });


        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    addContact(getContactModel());
                }
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    private AContact getContactModel() {

        AContact contact = new AContact();

        contact.firstName   = firstName.getText().toString();
        contact.lastName    = firstName.getText().toString();
//        contact.contact_id  = contactNo.getText().toString();
        contact.work_place  = workplace.getText().toString();
        //contact.occupation = occupationList.get(occupationSpinner.getSelectedItemPosition());
        ACategory aCategory= categoryList.get(occupationSpinner.getSelectedItemPosition()-1);
        contact.categeory_id =   aCategory.getCategoryId();


        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber myNumber = phoneUtil.parse(contactNo.getText().toString(), phoneNo.countryCode);
            if(myNumber!=null/* && phoneUtil.isValidNumber(myNumber)*/){
                String internationalNumber	= phoneUtil.format(myNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                String nationalNumber		= phoneUtil.format(myNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
                phoneNo.nationalNumber		= nationalNumber;
                phoneNo.interNationalNumber	= internationalNumber;
            }
        }
        catch (NumberParseException e) {
            e.printStackTrace();
            Log.e(e.getMessage()+" At verifyMobileNumberOnTap() module of MobileVerificationFragment.");

            phoneNo.nationalNumber		= contactNo.getText().toString();
            phoneNo.interNationalNumber	= "+966"+contactNo.getText().toString();
        }
//        contact.phone = new APhone();
//        contact.phoneNumber=phoneNo;
        contact.countryId =   phoneNo.countryId;
        contact.nationalNumber = phoneNo.nationalNumber;
        contact.i8nNumber = phoneNo.interNationalNumber;
        return contact;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }

    }



    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addImage.setImageBitmap(bm);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void selectedCountryWithDetail(ACountryDetail countryDetail) {

    }

    @Override
    public void onLogoutEvent(Exception error) {

    }

    @Override
    public void onCloseEvent() {

    }

    @Override
    public void onSuccessEvent() {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    boolean validate(){

        Validation valid	= new Validation(getActivity());
        valid.ShowAlerts(true);

        if (valid.checkIfEmpty(firstName, getString(R.string.first_name))){
            return false;
        }

        if (valid.checkIfEmpty(lastName, getString(R.string.last_name))){
            return false;
        }

        if (valid.checkIfEmpty(contactNo, getString(R.string.contact_number))){
            return false;
        }


        if (valid.checkIfEmpty(workplace, getString(R.string.workplace))){
            return false;
        }



        if (occupationSpinner.getSelectedItemPosition() == 0){
            Toast.makeText(getActivity(), R.string.select_category,Toast.LENGTH_LONG).show();
            return false;
        }

        return  true;
    }

    void addContact(final AContact contact){


        final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.adding_contact));

        new Thread(new Runnable() {

            public void run() {


                PhonebookServices userServ = new PhonebookServices(getActivity());

                boolean callStatus 	= userServ.addContact(contact,new PhonebookServices.PhonebookServiceListeners(){


                    @Override
                    public void onSuccess(ACategoryTemplate categoryTemplate) {
                        categoryList = categoryTemplate.categoryList;
                        if (categoryList != null)
                        {
//                            progress.dismiss();
//                            getFragmentManager().popBackStack();
                            Log.e("categoryList.size()categoryList.size()categoryList.size() = "+categoryList.size()+" XX");
                        }

                    }

                    @Override
                    public void onSuccess() {

                        if (contact != null)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), R.string.reques_addding_contact, Toast.LENGTH_SHORT).show();
                                }
                            });
                            getFragmentManager().popBackStack();
                            Log.e("categoryList.size()categoryList.size()categoryList.size()firstName = "+contact.firstName+" XX");


                        }
                        if(progress.isShowing()) {
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onSuccess(AContactTemplate contactTemplate) {
                        Log.e("onSuccess AContactTemplate");
                        if(progress.isShowing()) {
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Exception error) {
                        Log.e(error.getMessage()+" At getPhoneCategory() of PhoneBook Class");
                        Utils.showAlertOnMainThread(getActivity(), error.getMessage());
                        if(progress.isShowing()) {
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onForceLogout(Exception error) {
                        Log.e(error.getMessage()+" At getPhoneCategory() of PhoneBook Class");
                        if(progress.isShowing()) {
                            progress.dismiss();
                        }
                        if(listener!=null){
                            listener.onLogoutEvent(error);
                        }
                    }


                });


                if(!callStatus)
                    if(progress.isShowing()) {
                        progress.dismiss();
                    }

            }

        }).start();

    }
}

/*
package com.sr.masharef.masharef.masharef.phonebook;

        import android.app.Activity;
        import android.app.DialogFragment;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.google.i18n.phonenumbers.NumberParseException;
        import com.google.i18n.phonenumbers.PhoneNumberUtil;
        import com.google.i18n.phonenumbers.Phonenumber;
        import com.sr.masharef.masharef.masharef.R;
        import JResponseError;
        import CountryListDialog;
        import ACountryDetail;
        import CountryListFragment;
        import APhone;
        import APhoneNumber;
        import ACountry;
        import ACategory;
        import ACategoryTemplate;
        import AContact;
        import AContactTemplate;
        import Log;
        import Utils;
        import Validation;
        import AVBaseFragment;
        import MobileVerificationFragment;
        import PhonebookServices;

        import java.io.IOException;
        import java.util.ArrayList;



public class AddNewContact extends AVBaseFragment implements CountryListDialog.CountryListDialogListener, AVBaseFragment.AVBaseFragmentListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView addImage;
    private final int SELECT_FILE = 1;
    static ArrayList<ACategory> categoryList;
    ArrayList<String> occupationList;
    private OnFragmentInteractionListener mListener;
    EditText firstName,lastName,contactNo,workplace;
    Spinner occupationSpinner;
    Button addContact,countryCode;
    private boolean verifyNumberContactNo;
    public APhoneNumber phoneNo;
    InputMethodManager mgr;
    private DialogFragment countryListDialog;
    private ArrayList<ACountry> countryListCache;

    public AddNewContact(ArrayList<ACategory> categoryList) {
        this.categoryList = categoryList;
    }

    */
/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewContact.
     *//*

    // TODO: Rename and change types and number of parameters
    public static AddNewContact newInstance(String param1, String param2) {
        AddNewContact fragment = new AddNewContact(categoryList);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       */
/* final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final Toolbar toolbar = (Toolbar)((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setDisplayHomeAsUpEnabled(false);
//                    actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                if(getActivity() != null)
                    getActivity().onBackPressed();

            }
        });*//*

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View addContactView=inflater.inflate(R.layout.add_contact, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Contact");

        addImage            = (ImageView) addContactView.findViewById(R.id.add_image);
        occupationSpinner   = (Spinner) addContactView.findViewById(R.id.occupation_spinner);

        firstName           = (EditText) addContactView.findViewById(R.id.first_name);
//        lastName            = (EditText) addContactView.findViewById(R.id.last_name);
        contactNo           = (EditText) addContactView.findViewById(R.id.contact_phone_number_text);
        workplace           = (EditText) addContactView.findViewById(R.id.workplace);

        addContact          = (Button) addContactView.findViewById(R.id.add_btn);
        countryCode         = (Button) addContactView.findViewById(R.id.contact_country_sel);

       */
/* mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(firstName.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(lastName.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(contactNo.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(workplace.getWindowToken(), 0);*//*


        initializeListener();
        initializeAdapter();
        phoneNo = new APhoneNumber();
        verifyNumberContactNo = true;

        addListeners();
     */
/*   ACountryDetail detail = CountryListDialog.getDefaultCountry(getActivity());
        Log.e("detail is NULL "+(detail == null));
        if(detail!=null){
            setSelectedCountry(detail);
        }*//*


        return addContactView;
    }

    private void addListeners(){

        countryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    new CountryListDialog(getActivity(), AddNewContact.this).show();

                String fragmentTag = "countryList";

                countryListDialog = new CountryListFragment(getActivity(),countryListCache) {
                    @Override
                    public void OnCountrySelect(ACountry country) {
                        setSelectedCountry(country);
                        verifyMobileNumber();
                        countryListDialog.dismiss();
                    }

                    @Override
                    public void CacheCountryList(ArrayList<ACountry> countryList) {
                        if (countryList != null)
                            countryListCache = countryList;
                    }
                };

                countryListDialog.show(getActivity().getFragmentManager(),"");



            }
        });



        contactNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(verifyNumberContactNo)
                    verifyMobileNumber();
            }
        });

    }


    private void verifyMobileNumber(){

        String currentNumber = contactNo.getText().toString();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber myNumber = phoneUtil.parse(currentNumber, phoneNo.countryCode);
            if(myNumber!=null && phoneUtil.isValidNumber(myNumber)){
                verifyNumberContactNo = false;
                contactNo.setText(phoneUtil.format(myNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
                verifyNumberContactNo = true;

                sendButtonEnable(true);

            }else{
                sendButtonEnable(false);
            }
        }
        catch (NumberParseException e) {
            e.printStackTrace();
            Log.e(e.getMessage()+" At verifyMobileNumber() module of MobileVerificationFragment.");
            sendButtonEnable(false);

        }

    }

    void sendButtonEnable(boolean enable){
        if (enable) {
            //sendNumberBtn.setBackgroundResource(R.color.blue_but_enable);
            addContact.setEnabled(true);
        }else {
            //sendNumberBtn.setBackgroundResource(R.color.blue_but_disable);
            addContact.setEnabled(false);
        }
    }

    private void setSelectedCountry(ACountry country){
        Log.e("name: "+country.getCountry_name()+" code : "+country.getCountry_code()+" ISN : "+country.getCountryISN());
        phoneNo.countryId = country.getCountry_id();
        phoneNo.countryName = country.getCountry_name();
        phoneNo.countryCode = country.getCountry_code();
        phoneNo.countryISN  = country.getCountryISN();
        countryCode.setText(phoneNo.countryCode+" "+phoneNo.countryCodeWithLeadingPlus());
    }

    private void initializeAdapter() {

        occupationList = prepareCategoryList();

        ArrayAdapter<String> occupationAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, occupationList);
        occupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        occupationSpinner.setAdapter(occupationAdapter);

    }

    private ArrayList<String> prepareCategoryList() {

        ArrayList<String> occupationList = new ArrayList<>();
        occupationList.add("Select Category");
        if (categoryList != null)
        {
            for (int i = 0; i<categoryList.size();i++)
            {
                occupationList.add(categoryList.get(i).getCategoryName());
            }
        }

        return  occupationList;
    }

    private void initializeListener() {

        occupationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                */
/*Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image*//*
*/
/*");
                startActivityForResult(intent, PICK_PHOTO);*//*

                */
/*Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, PICK_PHOTO);*//*


                Intent intent = new Intent();
                intent.setType("image*/
/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
            }
        });


        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    addContact(getContactModel());
                }
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    private AContact getContactModel() {

        AContact contact = new AContact();

        contact.firstName   = firstName.getText().toString();
        contact.lastName    = firstName.getText().toString();
//        contact.contact_id  = contactNo.getText().toString();
        contact.work_place  = workplace.getText().toString();
        //contact.occupation = occupationList.get(occupationSpinner.getSelectedItemPosition());
        ACategory aCategory= categoryList.get(occupationSpinner.getSelectedItemPosition()-1);
        contact.categeory_id =   aCategory.getCategoryId();


        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber myNumber = phoneUtil.parse(contactNo.getText().toString(), phoneNo.countryCode);
            if(myNumber!=null && phoneUtil.isValidNumber(myNumber)){
                String internationalNumber	= phoneUtil.format(myNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                String nationalNumber		= phoneUtil.format(myNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
                phoneNo.nationalNumber		= nationalNumber;
                phoneNo.interNationalNumber	= internationalNumber;
            }
        }
        catch (NumberParseException e) {
            e.printStackTrace();
            Log.e(e.getMessage()+" At verifyMobileNumberOnTap() module of MobileVerificationFragment.");
        }
//        contact.phone = new APhone();
//        contact.phoneNumber=phoneNo;
        contact.countryId =   phoneNo.countryId;
        contact.nationalNumber = phoneNo.nationalNumber;
        contact.i8nNumber = phoneNo.interNationalNumber;
        return contact;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }

    }



    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addImage.setImageBitmap(bm);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        */
/*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*//*

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void selectedCountryWithDetail(ACountryDetail countryDetail) {

    }

    @Override
    public void onLogoutEvent(Exception error) {

    }

    @Override
    public void onCloseEvent() {

    }

    @Override
    public void onSuccessEvent() {

    }


    */
/**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    boolean validate(){

        Validation valid	= new Validation(getActivity());
        valid.ShowAlerts(true);

        if (valid.checkIfEmpty(firstName, getString(R.string.first_name))){
            return false;
        }

        if (valid.checkIfEmpty(lastName, getString(R.string.last_name))){
            return false;
        }

        if (valid.checkIfEmpty(contactNo, getString(R.string.contact_number))){
            return false;
        }


        if (valid.checkIfEmpty(workplace, getString(R.string.workplace))){
            return false;
        }



        if (occupationSpinner.getSelectedItemPosition() == 0){
            Toast.makeText(getActivity(),"Select Category",Toast.LENGTH_LONG).show();
            return false;
        }

        return  true;
    }

    void addContact(final AContact contact){


        final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.adding_contact));

        new Thread(new Runnable() {

            public void run() {


                PhonebookServices userServ = new PhonebookServices(getActivity());

                boolean callStatus 	= userServ.addContact(contact,new PhonebookServices.PhonebookServiceListeners(){


                    @Override
                    public void onSuccess(ACategoryTemplate categoryTemplate) {
                        categoryList = categoryTemplate.categoryList;
                        if (categoryList != null)
                        {
//                            progress.dismiss();
//                            getFragmentManager().popBackStack();
                            Log.e("categoryList.size()categoryList.size()categoryList.size() = "+categoryList.size()+" XX");
                        }

                    }

                    @Override
                    public void onSuccess() {

                        if (contact != null)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), "Contact added Successfully!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            getFragmentManager().popBackStack();
                            Log.e("categoryList.size()categoryList.size()categoryList.size()firstName = "+contact.firstName+" XX");


                        }
                        if(progress.isShowing()) {
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onSuccess(AContactTemplate contactTemplate) {
                        Log.e("onSuccess AContactTemplate");
                        if(progress.isShowing()) {
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Exception error) {
                        Log.e(error.getMessage()+" At getPhoneCategory() of PhoneBook Class");
                        Utils.showAlertOnMainThread(getActivity(), error.getMessage());
                        if(progress.isShowing()) {
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onForceLogout(Exception error) {
                        Log.e(error.getMessage()+" At getPhoneCategory() of PhoneBook Class");
                        if(progress.isShowing()) {
                            progress.dismiss();
                        }
                        if(listener!=null){
                            listener.onLogoutEvent(error);
                        }
                    }


                });


                if(!callStatus)
                    if(progress.isShowing()) {
                        progress.dismiss();
                    }

            }

        }).start();

    }


}
*/




