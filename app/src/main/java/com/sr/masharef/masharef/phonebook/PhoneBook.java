package com.sr.masharef.masharef.phonebook;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.manager.ApplicationManager;
import com.sr.masharef.masharef.model.phonebook.ACategory;
import com.sr.masharef.masharef.model.phonebook.ACategoryTemplate;
import com.sr.masharef.masharef.model.phonebook.AContact;
import com.sr.masharef.masharef.model.phonebook.AContactTemplate;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.verification.AVBaseFragment;
import com.sr.masharef.masharef.webservice.api.PhonebookServices;

import java.util.ArrayList;

/**
 * Created by Kavya on 11/01/2017.
 */

public class PhoneBook extends AVBaseFragment {

    private RecyclerView recyclerView;
    private PhoneAdapter mAdapter;
    String actionBarName;
    ArrayList<ACategory> categoryList;
    LinearLayout addContact;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View phoneview  = inflater.inflate(R.layout.activity_phone, container, false);

        if (savedInstanceState == null){
            setHasOptionsMenu(true);
            final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.phonebook);
            recyclerView = (RecyclerView)phoneview.findViewById(R.id.recycler_view);
            addContact=(LinearLayout)phoneview.findViewById(R.id.addlayout);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);

            if (categoryList == null)
                getPhoneCategory();
            initializeAdapter();

            final Toolbar toolbar = (Toolbar)((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
            // toolbar.setNavigationIcon(R.drawable.cal_left_arrow_on);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setDisplayShowCustomEnabled(false);
                    actionBar.setDisplayShowTitleEnabled(true);
                    if(getActivity() != null)
                        getActivity().onBackPressed();

                }
            });
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                fragment=new AddNewContact(categoryList);
                ft.replace(R.id.content_frame, fragment,"phonetab").addToBackStack("del_phone").commit();
            }
        });

        return phoneview;
    }


    private void initializeAdapter() {
        mAdapter = new PhoneAdapter(categoryList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    void initializeAdapterOnMainThread(){
        if(getActivity()!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initializeAdapter();
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.phonebook, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      /*  switch (item.getItemId()){
            case R.id.phonebook_item:
            Fragment fragment;
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                fragment=new AddNewContact(categoryList);
                ft.replace(R.id.content_frame, fragment,"phonetab").addToBackStack("del_phone").commit();

        }*/
        return super.onOptionsItemSelected(item);
    }

    public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneViewHolder>{
        private ArrayList<ACategory> phoneList;

        public class PhoneViewHolder extends RecyclerView.ViewHolder {
            TextView phoneRowText;
            public PhoneViewHolder(View itemView) {
                super(itemView);
                phoneRowText=(TextView)itemView.findViewById(R.id.contacttext);

            }
        }


        public PhoneAdapter(ArrayList<ACategory> phoneList ){
            this.phoneList=phoneList;
        }

        @Override
        public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_row_list, parent, false);

            return new PhoneViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(PhoneViewHolder holder, final int position) {

            ACategory phoneCategory = phoneList.get(position);

            holder.phoneRowText.setText(""+phoneCategory.getCategoryName());
            actionBarName= (String) holder.phoneRowText.getText();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id=position;
               /*     Fragment lFragmentPhone = null;
                    FragmentManager fragmentManager =getActivity(). getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    lFragmentPhone=new PhoneBookOption();
                    Bundle bundle = new Bundle();
                    bundle.putString("con_id",phoneList.get(position).getCategoryId());
                    lFragmentPhone.setArguments(bundle);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(phoneList.get(position).getCategoryName());
                    ft.replace(R.id.content_frame, lFragmentPhone,"phonetab").addToBackStack("del_phone").commit();*/
                    getAContactList(phoneList.get(position).getCategoryId(), phoneList.get(position).getCategoryName());
                }
            });
        }

        @Override
        public int getItemCount() {

            if (phoneList == null)
                return 0;
            return phoneList.size();
        }


    }

    void getPhoneCategory(){

        final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.getting_pb_category));

        new Thread(new Runnable() {

            public void run() {


                PhonebookServices userServ = new PhonebookServices(ApplicationManager.context);

                boolean callStatus 	= userServ.getPhoneCategory(new PhonebookServices.PhonebookServiceListeners(){


                    @Override
                    public void onSuccess(ACategoryTemplate categoryTemplate) {
                        categoryList = MDatabaseManager.getInstance().getCategeoryList();
                        //categoryTemplate.categoryList;
                        if (categoryList != null)
                        {
                            Log.e("categoryList.size()categoryList.size()categoryList.size() = "+categoryList.size()+" XX");

                            initializeAdapterOnMainThread();
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void onSuccess() {
                        progress.dismiss();
                    }

                    @Override
                    public void onSuccess(AContactTemplate contactTemplate) {

                    }

                    @Override
                    public void onFailure(Exception error) {
                        Log.e(error.getMessage()+" At getPhoneCategory() of PhoneBook Class");
                        Utils.showAlertOnMainThread(getActivity(), error.getMessage());
                        progress.dismiss();
                    }

                    @Override
                    public void onForceLogout(Exception error) {
                        Log.e(error.getMessage()+" At getPhoneCategory() of PhoneBook Class");
                        progress.dismiss();
                        if(listener!=null){
                            listener.onLogoutEvent(error);
                        }
                    }

                });


                if(!callStatus)
                    progress.dismiss();

            }

        }).start();

    }



    void getAContactList(final String categoryId, final String CategeoryName){
        final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.getting_pb_contacklist));
        new Thread(new Runnable() {
            @Override
            public void run() {
                PhonebookServices userServ = new PhonebookServices(getActivity());
                boolean callResult=userServ.getPhoneContactList(categoryId, new PhonebookServices.PhonebookServiceListeners() {
                    @Override
                    public void onSuccess(AContactTemplate contactTemplate) {
                        ArrayList<AContact> contactList=new ArrayList<>();
                       // contactList = MDatabaseManager.getInstance().getContactList(categoryId);
                        contactList = contactTemplate.contactList;
                        if (contactList != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Log.e("categoryList.size()categoryList.size()categoryList.size() = " + contactList.size() + " XX");
                                    Fragment lFragmentPhone = null;
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction ft = fragmentManager.beginTransaction();
                                    lFragmentPhone = new PhoneBookOption();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("categoryId",categoryId);
                                    bundle.putString("CategeoryName", CategeoryName);
                                    lFragmentPhone.setArguments(bundle);
                                    ft.replace(R.id.content_frame, lFragmentPhone, "phonebookOption").addToBackStack("PhOption").commit();

                                }
                            });
                            progress.dismiss();
                        }
                        else
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(getActivity(), "There is not any contact added for this categeory !!", Toast.LENGTH_SHORT);
                                    Utils.alert(getContext(), getString(R.string.not_any_contact_added));
                                }
                                });

                        }
                    }

                    @Override
                    public void onSuccess() {
                        progress.dismiss();
                    }

                    @Override
                    public void onSuccess(ACategoryTemplate categoryTemplate) {

                    }

                    @Override
                    public void onFailure(Exception error) {
                        Log.e(error.getMessage()+" At getAContactList() of PhoneBook Class");
                        Utils.showAlertOnMainThread(getActivity(), error.getMessage());

                        progress.dismiss();
                    }

                    @Override
                    public void onForceLogout(Exception error) {
                        Log.e(error.getMessage()+" At getPhoneCategory() of PhoneBook Class");
                        progress.dismiss();
                    }


                });
                if(!callResult)
                    progress.dismiss();
            }


        }).start();
    }

}
