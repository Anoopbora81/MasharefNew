package com.sr.masharef.masharef.voting;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.utility.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VotingDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VotingDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VotingDetailFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private TextView textViewVotingTitleD, textViewVotingDescriptionD, textViewVotingStartDateD, textViewVotingEndDateD, textViewCandidateList;
    private Button buttonCancelVote, buttonVote;
    private VotingObject votingObject;
    private RecyclerView recyclerViewVotingMsgList;
    private ArrayList<OptionsObject> optionObjetList;
    int currentPosition = -1;
    int previousPosition = -1;
    LinearLayout layoutChart;
    PieChart pieChart;
    BarChart mBarChart;
    ImageView mBarChartUnSelected, mBarChartSelected, mPieChartUnSelected, mPieChartSelected;

    public VotingDetailFragment() {

    }


    public static VotingDetailFragment newInstance(String param1, String param2) {
        VotingDetailFragment fragment = new VotingDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            String votingJsonObject = bundle.getString("votingJsonObject");
            Type type = new TypeToken<VotingObject>() {
            }.getType();
            votingObject = new Gson().fromJson(votingJsonObject, type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voting_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewVotingTitleD = (TextView) getActivity().findViewById(R.id.textViewVotingTitleD);
        textViewVotingDescriptionD = (TextView) getActivity().findViewById(R.id.textViewVotingDescriptionD);
        textViewVotingStartDateD = (TextView) getActivity().findViewById(R.id.textViewVotingStartDateD);
        textViewVotingEndDateD = (TextView) getActivity().findViewById(R.id.textViewVotingEndDateD);
        textViewCandidateList = (TextView) getActivity().findViewById(R.id.textViewCandidateList);
        buttonCancelVote = (Button) getActivity().findViewById(R.id.buttonCancelVote);
        buttonVote = (Button) getActivity().findViewById(R.id.buttonVote);
        recyclerViewVotingMsgList = (RecyclerView) view.findViewById(R.id.recyclerViewOptionsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewVotingMsgList.setLayoutManager(mLayoutManager);
        recyclerViewVotingMsgList.setItemAnimator(new DefaultItemAnimator());
        layoutChart = (LinearLayout) getActivity().findViewById(R.id.layoutChart);
        pieChart = (PieChart) getActivity().findViewById(R.id.pieChart);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(10f);
        mBarChart = (BarChart) getActivity().findViewById(R.id.barChart);
        initializeBarChart();
        textViewVotingTitleD.setText(votingObject.getVoting_title());
        textViewVotingDescriptionD.setText(votingObject.getVoting_description());
        textViewVotingStartDateD.setText(votingObject.getVoting_start_date());
        textViewVotingEndDateD.setText(votingObject.getVoting_end_date());

        mBarChartSelected = (ImageView) view.findViewById(R.id.barChartImageView_selected);
        mBarChartUnSelected = (ImageView) view.findViewById(R.id.barChartImageView_unselected);
        mPieChartSelected = (ImageView) view.findViewById(R.id.pieChartImageView_selected);
        mPieChartUnSelected = (ImageView) view.findViewById(R.id.pieChartImageView_unselected);
        mPieChartUnSelected.setOnClickListener(this);
        mBarChartUnSelected.setOnClickListener(this);
        initializeAdapter();
        buttonCancelVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendJoinEventInfo(event_id, joiningStatus, memberGoing);

            }
        });
        buttonVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition == -1) {
                    Utils.alert(getActivity(), R.drawable.del_aa_common_error, R.string.voting_system, getActivity().getResources().getString(R.string.gallery_upload_success));
                } else {
                    com.sr.masharef.masharef.utility.Utils.decisionAlertOnMainThread(getActivity(), android.R.drawable.ic_dialog_info, R.string.voting_system,
                            Html.fromHtml(getString(R.string.voting_conform_dailog)),
                            getActivity().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    textViewCandidateList.setVisibility(View.GONE);
                                    recyclerViewVotingMsgList.setVisibility(View.GONE);
                                    buttonCancelVote.setVisibility(View.GONE);
                                    buttonVote.setVisibility(View.GONE);
                                    layoutChart.setVisibility(View.VISIBLE);
                                    pieChart.setVisibility(View.VISIBLE);
                                    showPieChart();
                                }
                            }, getActivity().getString(R.string.no), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                }
            }
        });

    }

    private void showPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(4f, "Name1", 0));
        entries.add(new PieEntry(8f, "Name2", 1));
        entries.add(new PieEntry(6f, "Name3", 2));
        entries.add(new PieEntry(12f, "Name4", 3));
        PieDataSet dataset = new PieDataSet(entries, "Voting Results");
        PieData data = new PieData(dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.animateY(4000);
        pieChart.invalidate();
    }

    void initializeBarChart() {
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawGridBackground(false);
        IAxisValueFormatter xAxisFormatter = new XaxisValueFormatter(mBarChart);
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
        Legend l = mBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

    }

    private void showBarChart(int count, float range) {

        float start = 1f;
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            if (Math.random() * 100 < 25) {
                yVals1.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.add_arrow)));
            } else {
                yVals1.add(new BarEntry(i, val));
            }
        }
        BarDataSet set1;
        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Voting Results");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            mBarChart.setData(data);
        }
    }

    private void initializeAdapter() {
        optionObjetList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            OptionsObject optionsObject = new OptionsObject("Name" + i, "detail" + i, null);
            optionObjetList.add(optionsObject);
        }
        OptionObjetListAdapter optionObjetListAdapter = new OptionObjetListAdapter(optionObjetList);
        recyclerViewVotingMsgList.setAdapter(optionObjetListAdapter);
        optionObjetListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pieChartImageView_unselected:
                pieChart.setVisibility(View.VISIBLE);
                mBarChart.setVisibility(View.GONE);
                mBarChartSelected.setVisibility(View.GONE);
                mBarChartUnSelected.setVisibility(View.VISIBLE);
                mPieChartUnSelected.setVisibility(View.GONE);
                mPieChartSelected.setVisibility(View.VISIBLE);
                //showPieChart();
                break;
            case R.id.barChartImageView_unselected:
                pieChart.setVisibility(View.GONE);
                mBarChart.setVisibility(View.VISIBLE);
                mBarChartSelected.setVisibility(View.VISIBLE);
                mBarChartUnSelected.setVisibility(View.GONE);
                mPieChartUnSelected.setVisibility(View.VISIBLE);
                mPieChartSelected.setVisibility(View.GONE);
                if (mBarChart.getData() == null)
                    showBarChart(5, 12);
                break;
        }
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


    @Override
    public void onResume() {
        super.onResume();
    }


    private class OptionObjetListAdapter extends RecyclerView.Adapter<OptionObjetListAdapter.OptionObjetViewHolder> {
        ArrayList<OptionsObject> optionObjetList;

        public OptionObjetListAdapter(ArrayList<OptionsObject> optionObjetList) {
            this.optionObjetList = optionObjetList;
        }

        @Override
        public OptionObjetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.voter_candidate_raw_list, parent, false);
            return new OptionObjetViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return optionObjetList.size();
        }

        @Override
        public void onBindViewHolder(final OptionObjetViewHolder holder, final int position) {
            OptionsObject optionsObject = optionObjetList.get(position);

            holder.textViewOptionName.setText(optionsObject.getOptionName());
            holder.textViewOptionDescription.setText(optionsObject.getOptionDesignation());
            // Here you apply the animation when the view is bound
            //     setAnimation(holder.itemView, position);

            if (previousPosition == position) {
                holder.optionVoterIconOff.setVisibility(View.VISIBLE);
                holder.optionVoterIconOn.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousPosition = currentPosition;
                    if (holder.optionVoterIconOn.getVisibility() == View.GONE) {
                        currentPosition = position;
                        holder.optionVoterIconOff.setVisibility(View.GONE);
                        holder.optionVoterIconOn.setVisibility(View.VISIBLE);
                    } else {
                        currentPosition = -1;
                        holder.optionVoterIconOff.setVisibility(View.VISIBLE);
                        holder.optionVoterIconOn.setVisibility(View.GONE);
                    }
                    if (previousPosition != -1)
                        notifyItemChanged(previousPosition);
                }
            });
        }

        public class OptionObjetViewHolder extends RecyclerView.ViewHolder {
            private final TextView textViewOptionName;
            private final TextView textViewOptionDescription;
            private final ImageView optionVoterIconOff;
            private final ImageView optionVoterIconOn;

            public OptionObjetViewHolder(View itemView) {
                super(itemView);
                textViewOptionName = (TextView) itemView.findViewById(R.id.textViewOptionName);
                textViewOptionDescription = (TextView) itemView.findViewById(R.id.textViewOptionDescription);
                optionVoterIconOff = (ImageView) itemView.findViewById(R.id.optionVoterIconOff);
                optionVoterIconOn = (ImageView) itemView.findViewById(R.id.optionVoterIconOn);
            }
        }
    }

    class XaxisValueFormatter implements IAxisValueFormatter {
        private BarLineChartBase<?> chart;

        public XaxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return "Name";
        }
    }


}
