package com.sr.masharef.masharef.phonebook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.model.phonebook.AContact;
import com.sr.masharef.masharef.utility.PhoneServiceClass;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhoneBookOption.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhoneBookOption#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneBookOption extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String categoryId, CategeoryName ;


    private RecyclerView mRecyclerViewPhoneOption;
    private RecyclerView.Adapter mAdapterPhoneOption;
    private RecyclerView.LayoutManager mLayoutManagerPhoneOption;
    private List<PhoneServiceClass> phoneOptionList;
    ArrayList<AContact> contactList=new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public PhoneBookOption() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhoneBookOption.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneBookOption newInstance(String param1, String param2) {
        PhoneBookOption fragment = new PhoneBookOption();
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
            this.categoryId = getArguments().getString("categoryId");
            this.CategeoryName = getArguments().getString("CategeoryName");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View phoneOptionView=inflater.inflate(R.layout.phone_book_option, container, false);
        phoneOptionList=new ArrayList<>();
        mRecyclerViewPhoneOption = (RecyclerView)phoneOptionView.findViewById(R.id.my_recycler_view);
        mRecyclerViewPhoneOption.setHasFixedSize(true);
        mLayoutManagerPhoneOption = new LinearLayoutManager(getActivity());
        mRecyclerViewPhoneOption.setLayoutManager(mLayoutManagerPhoneOption);

        contactList = MDatabaseManager.getInstance().getContactList(categoryId);
//        contactList = AContactTemplate.contactList;
        contactListAdapter();

        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(CategeoryName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        final Toolbar toolbar = (Toolbar)((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
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
        return phoneOptionView;
    }


    private void contactListAdapter(){
        mAdapterPhoneOption = new PhoneOptionAdapter(getActivity(),getFragmentManager(),contactList);
        mRecyclerViewPhoneOption.setAdapter(mAdapterPhoneOption);
        mAdapterPhoneOption.notifyDataSetChanged();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
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


}
