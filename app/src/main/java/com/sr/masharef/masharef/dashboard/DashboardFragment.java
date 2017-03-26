package com.sr.masharef.masharef.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sr.masharef.masharef.HomeActivity;
import com.sr.masharef.masharef.R;

/**
 * Created by systemrapid on 13/01/17.
 */

public class DashboardFragment extends Fragment {

    RelativeLayout eventCalenderLayout;
    RelativeLayout phonebookLayout;
    RelativeLayout chatLayout;
    RelativeLayout galleryLayout;
    RelativeLayout votingLayout;
    RelativeLayout quesAndFormLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.dashboard));
        View dashboard = inflater.inflate(R.layout.fragment_dashboard, container, false);

        if (savedInstanceState == null){
            eventCalenderLayout = (RelativeLayout)dashboard.findViewById(R.id.event_layout);
            phonebookLayout     = (RelativeLayout)dashboard.findViewById(R.id.phonebook_layout);
            chatLayout          = (RelativeLayout)dashboard.findViewById(R.id.chat_layout);
            galleryLayout       = (RelativeLayout)dashboard.findViewById(R.id.gallery_layout);
            votingLayout        = (RelativeLayout)dashboard.findViewById(R.id.voting_layout);
            quesAndFormLayout   = (RelativeLayout)dashboard.findViewById(R.id.que_n_form_layout);


         /*   if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)){
                //do nothing
            }else{*/
                initializeListener();
         //   }
        }

        return dashboard;
    }

    private void initializeListener() {

        eventCalenderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.getSingleton().setupFragment(HomeActivity.HomeFragmentTags.EVENT);
            }
        });

        phonebookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.getSingleton().setupFragment(HomeActivity.HomeFragmentTags.PHONEBOOK);
            }
        });

        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.getSingleton().setupFragment(HomeActivity.HomeFragmentTags.CHAT);
            }
        });

        galleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.getSingleton().setupFragment(HomeActivity.HomeFragmentTags.GALLARY);
            }
        });

        votingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.getSingleton().setupFragment(HomeActivity.HomeFragmentTags.VOTING);
            }
        });

        quesAndFormLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.getSingleton().setupFragment(HomeActivity.HomeFragmentTags.QUESTION_FORM);
            }
        });
    }
}
