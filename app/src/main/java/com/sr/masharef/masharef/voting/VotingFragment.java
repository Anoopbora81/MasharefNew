package com.sr.masharef.masharef.voting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sr.masharef.masharef.R;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Created by Anoop on 01/03/17.
 */

public class VotingFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerViewVotingMsgList;
    private ArrayList<VotingObject> votingMsgList;
    private ArrayList<VotingObject> votingMsgListCancelled;
    private int mVotingStatusId = -1;
    private View mVotingTabs;
    TextView mTextViewCompleted, mTextViewCancelled;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dashboard = inflater.inflate(R.layout.fragment_voting, container, false);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        final TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mVotingTabs = dashboard.findViewById(R.id.layout_tabs);
        mTextViewCompleted = (TextView)dashboard.findViewById(R.id.text_view_voting_status_completed);
        mTextViewCancelled = (TextView)dashboard.findViewById(R.id.text_view_voting_status_cancelled);
        mTextViewCompleted.setOnClickListener(this);
        mTextViewCancelled.setOnClickListener(this);

        mTitleTextView.setText(R.string.voting_system);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayHomeAsUpEnabled(true);
        final Toolbar toolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });
        recyclerViewVotingMsgList = (RecyclerView) dashboard.findViewById(R.id.recyclerViewVotingMsgList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewVotingMsgList.setLayoutManager(mLayoutManager);
        recyclerViewVotingMsgList.setItemAnimator(new DefaultItemAnimator());

        votingMsgList = new ArrayList<>();
        votingMsgListCancelled = new ArrayList<>();
        for (int i = 1; i < 3; i++) {

            VotingObject votingObject = new VotingObject("Voting Title " + i, i + ") Description of Voting Indian Financial System Code (IFSC)." +
                    " It is used for electronic payment applications like National Electronic Funds Transfer (NEFT)," +
                    " Immediate Payment Service,(IMPS), and Centralised Funds Management System (CFMS)" +
                    " developed by Reserve Bank of India (RBI). Code has eleven characters \"Alpha Numeric\" in nature. First four characters represent bank," +
                    " fifth character is default \"0\" left for future use and last six ) Voting" + i, i + "/03/2017", i + 15 + "/04/2017", 1, "Completed");
            votingMsgList.add(votingObject);
        }
        for (int i = 1; i < 3; i++) {

            VotingObject votingObject = new VotingObject("Voting Title " + i, i + ") Description of Voting Indian Financial System Code (IFSC)." +
                    " It is used for electronic payment applications like National Electronic Funds Transfer (NEFT)," +
                    " Immediate Payment Service,(IMPS), and Centralised Funds Management System (CFMS)" +
                    " developed by Reserve Bank of India (RBI). Code has eleven characters \"Alpha Numeric\" in nature. First four characters represent bank," +
                    " fifth character is default \"0\" left for future use and last six ) Voting" + i, i + "/03/2017", i + 15 + "/04/2017", 0, "Processing");
            votingMsgList.add(votingObject);
        }
        for (int i = 1; i < 3; i++) {

            VotingObject votingObject = new VotingObject("Voting Title " + i, i + ") Description of Voting Indian Financial System Code (IFSC)." +
                    " It is used for electronic payment applications like National Electronic Funds Transfer (NEFT)," +
                    " Immediate Payment Service,(IMPS), and Centralised Funds Management System (CFMS)" +
                    " developed by Reserve Bank of India (RBI). Code has eleven characters \"Alpha Numeric\" in nature. First four characters represent bank," +
                    " fifth character is default \"0\" left for future use and last six ) Voting" + i, i + "/03/2017", i + 15 + "/04/2017", 2, "Cancelled");
           // votingMsgListCancelled.add(votingObject);
        }
        if (votingMsgListCancelled.size() > 0) {
            mVotingTabs.setVisibility(View.VISIBLE);
        }
//        if(votingMsgList == null)
//            getVotingMsgList();

        initializeAdapter(1);
        //  initializeListeners(mCustomView);

        return dashboard;
    }

    private void initializeAdapter(int option) {
        //   eventsList = MDatabaseManager.getInstance().getEventData();
        VotingMsgListAdapter votingMsgListAdapter = new VotingMsgListAdapter(option == 1 ? votingMsgList : votingMsgListCancelled);
        recyclerViewVotingMsgList.setAdapter(votingMsgListAdapter);
        votingMsgListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_view_voting_status_completed:
               initializeAdapter(1);
                mTextViewCompleted.setBackgroundResource(R.color.colorPrimaryDark);
                mTextViewCancelled.setBackgroundResource(R.color.colorPrimary);

                break;
            case R.id.text_view_voting_status_cancelled:
                initializeAdapter(2);
                mTextViewCompleted.setBackgroundResource(R.color.colorPrimary);
                mTextViewCancelled.setBackgroundResource(R.color.colorPrimaryDark);
                break;
        }
    }

    private class VotingMsgListAdapter extends RecyclerView.Adapter<VotingMsgListAdapter.VotingViewHolder> {
        ArrayList<VotingObject> votingObjectList;

        public VotingMsgListAdapter(ArrayList<VotingObject> votingMsgList) {
            this.votingObjectList = votingMsgList;
        }

        @Override
        public VotingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.voting_msg_list, parent, false);
            return new VotingViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return votingObjectList.size();
        }

        @Override
        public void onBindViewHolder(VotingViewHolder holder, final int position) {
            VotingObject votingObject = votingObjectList.get(position);

            holder.textViewVotingTitle.setText(votingObject.getVoting_title());
            holder.textViewVotingDescription.setText(votingObject.getVoting_description());
            holder.textViewVotingStartDate.setText(votingObject.getVoting_start_date());
            holder.textViewVotingEndDate.setText(votingObject.getVoting_end_date());
            if (mVotingStatusId != votingObject.getVoting_status()) {
                holder.textViewVotingStatus.setVisibility(View.VISIBLE);
                mVotingStatusId = votingObject.getVoting_status();
                holder.textViewVotingStatus.setText(votingObject.getVoting_status_name());
            } else {
                holder.textViewVotingStatus.setVisibility(View.GONE);
            }

            // Here you apply the animation when the view is bound
            //     setAnimation(holder.itemView, position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<VotingObject>() {
                    }.getType();
                    final String votingJsonObject = gson.toJson(votingObjectList.get(position), type);

                    VotingDetailFragment votingDetailFragment = new VotingDetailFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("votingJsonObject", votingJsonObject);
                    votingDetailFragment.setArguments(bundle);
                    ft.replace(R.id.content_frame, votingDetailFragment, "VotingDetailFragmentTab");
                    ft.addToBackStack("VotingDetailFragmentTab");
                    ft.commit();
                }
            });
        }

        public class VotingViewHolder extends RecyclerView.ViewHolder {
            private final TextView textViewVotingTitle;
            private final TextView textViewVotingDescription;
            private final TextView textViewVotingStartDate;
            private final TextView textViewVotingEndDate;
            private final TextView textViewVotingStatus;

            public VotingViewHolder(View itemView) {
                super(itemView);
                textViewVotingTitle = (TextView) itemView.findViewById(R.id.textViewVotingTitle);
                textViewVotingDescription = (TextView) itemView.findViewById(R.id.textViewVotingDescription);
                textViewVotingStartDate = (TextView) itemView.findViewById(R.id.textViewVotingStartDate);
                textViewVotingEndDate = (TextView) itemView.findViewById(R.id.textViewVotingEndDate);
                textViewVotingStatus = (TextView) itemView.findViewById(R.id.text_view_voting_status);
            }
        }
    }


}
