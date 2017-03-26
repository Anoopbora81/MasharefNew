package com.sr.masharef.masharef.event;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.model.event.EventObjects;
import com.sr.masharef.masharef.model.event.EventObjectsTemplate;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.api.EventListServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CreateEventFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String select ;
    String[] noOfMembers ;
    private OnFragmentInteractionListener mListener;
   // private static TextView editTextEventDate,editTextDeadLine, editTextTime;

    private   TextView textViewEventName, textViewDescription, textViewVenue, textViewEventDate,textViewDeadLine, textViewTime, textViewGender,
            textViewNewCost, textViewStatus, textViewJoiningEvent, textViewMayBeJoiningEvent, textViewNotJoiningEvent;
    private Button buttonMaybe, buttonNo, buttonGoing;
    int year, month, day, hour, minute;
    private DatePickerDialog datePickerDialog;
    private static boolean isEventDate;
    private int positiion;
    private EventObjects eventObject;
    private String spinner_item;
    private int event_id ;
    private int joiningStatus = 0;
    private int memberGoing = 0;
    private int selectedPositionOfMemberList = 0;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNewEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventDetailFragment newInstance(String param1, String param2) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        select = ""+getString(R.string.select_caps).toString();
        noOfMembers = new String[]{select, "1", "2","3", "4","5", "6","7", "8","9","10","11","12","13", "14","15", "16","17", "18","19","20"};

        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
            Bundle bundle  = getArguments();
            String eventObjectJson = bundle.getString("eventObjectJson");
            Type type = new TypeToken<EventObjects>() {}.getType();
            eventObject = new Gson().fromJson(eventObjectJson, type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View mCustomView = inflater.inflate(R.layout.custom_actionbar, null);
        final TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        ImageView imageButtonShowCalendar = (ImageView) mCustomView.findViewById(R.id.imageButtonShowCalendar);
        imageButtonShowCalendar.setVisibility(View.INVISIBLE);

        mTitleTextView.setText(R.string.event_details);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
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

        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        textViewEventName = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewEventName);
        textViewDescription = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewDescription);
        textViewVenue = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewVanue);
        textViewEventDate = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewEventsDate);
        textViewTime = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewEventsTime);
        textViewNewCost = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewCost);
        textViewGender = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewGender);

//        textViewStatus = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewStatus);
//        textViewDeadLine = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewDeadLine);
//        textViewJoiningEvent = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewJoiningEvent);
//        textViewMayBeJoiningEvent = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewMayBeJoiningEvent);
//        textViewNotJoiningEvent = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.textViewNotJoiningEvent);

        buttonMaybe = (Button) ((AppCompatActivity)getActivity()).findViewById(R.id.buttonMaybe);
        buttonNo = (Button) ((AppCompatActivity)getActivity()).findViewById(R.id.buttonNo);
        buttonGoing = (Button) ((AppCompatActivity)getActivity()).findViewById(R.id.buttonGoing);


        java.util.Calendar eventCalendar = java.util.Calendar.getInstance();
        eventCalendar.setTime(eventObject.getEventDate());
        int dayValue = eventCalendar.get(java.util.Calendar.DAY_OF_MONTH);
        int displayMonth = eventCalendar.get(java.util.Calendar.MONTH) + 1;
        int displayYear = eventCalendar.get(java.util.Calendar.YEAR);
        String eventDate = dayValue + "-" + displayMonth + "-" + displayYear;
        String   day =  ""+dayValue;
        String  month = ""+displayMonth;
        if(dayValue <10) {
            day =  "0" + dayValue;
        }
        if(displayMonth <10) {
            month = "0" + displayMonth;
        }

         eventDate = day + "-" + month + "-" + displayYear;
        eventCalendar.setTime(eventObject.getDeadlineDate());
         dayValue = eventCalendar.get(java.util.Calendar.DAY_OF_MONTH);
         displayMonth = eventCalendar.get(java.util.Calendar.MONTH) + 1;
         displayYear = eventCalendar.get(java.util.Calendar.YEAR);
        String deadLineDate = dayValue+"-"+displayMonth+"-"+displayYear;

        event_id = eventObject.getId();

        textViewEventName.setText(eventObject.getEvent_name());
        textViewDescription.setText(eventObject.getDescription());
        textViewVenue.setText(eventObject.getVenue());
        textViewEventDate.setText(eventDate);
        textViewTime.setText(eventObject.getTime());


        textViewGender.setText(eventObject.getGender());
        textViewNewCost.setText(""+eventObject.getCost() +" ("+eventObject.getCurrency()+")");

//        textViewStatus.setText(""+eventObject.getStatus());
//        textViewDeadLine.setText(deadLineDate);
//        textViewJoiningEvent.setText(""+eventObject.getGoingToEvent());
//        textViewMayBeJoiningEvent.setText(""+eventObject.getMayBeGoingToEvent());
//        textViewNotJoiningEvent.setText(""+eventObject.getNotGoingToEvent());



        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joiningStatus =0;
                memberGoing = 0;
                sendJoinEventInfo(event_id, joiningStatus, memberGoing);

            }
        });


        buttonMaybe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joiningStatus= 1;
                popUpSpinnerDialog(joiningStatus);
            }
        });
        buttonGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joiningStatus = 2;
                popUpSpinnerDialog(joiningStatus);
            }
        });

    }

    private void sendJoinEventInfo(int event_id, int joiningStatus, int memberGoing) {

        JSONObject joiningDetail =  new JSONObject();
        JSONObject joinEventsParams = new JSONObject();
        try {
            joinEventsParams.put("event_id", event_id);
            joinEventsParams.put("join_status", joiningStatus);
            joinEventsParams.put("member_going", memberGoing);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(e.getMessage()+" at verifyCodeForMobileNumber Module!!");
        }

        membersJoiningInEvent(joinEventsParams);
    }

    private void popUpSpinnerDialog(final int joining_status) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.row_spinner);
        dialog.setCancelable(false);

        // set the custom dialog components - text, image and button
        final Spinner spinner = (Spinner) dialog
                .findViewById(R.id.spinner1);
//                final EditText edittext = (EditText) dialog
//                        .findViewById(R.id.editText1);
        Button buttonDone = (Button) dialog.findViewById(R.id.buttonDone);
        Button buttonCancelF = (Button) dialog.findViewById(R.id.buttonCancelF);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext());
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                // TODO Auto-generated method stub
                selectedPositionOfMemberList = position;
                spinner_item = noOfMembers[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(selectedPositionOfMemberList == 0)
                {
                    Utils.alert(getContext(),getString(R.string.select_member_msg));
                }
                else
                {
                    int numberOfMembersGoing = Integer.parseInt(spinner_item);
                    sendJoinEventInfo(event_id, joining_status, numberOfMembersGoing);
                    dialog.dismiss();
                }

            }
        });

        buttonCancelF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } /*else {
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

    private  void eventDate(int year, int month, int day) {
        if(day < 10)
        {
            textViewEventDate.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-0").append(day));
        }
        else
        {
            textViewEventDate.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
    }
    private  void deadLineDate(int year, int month, int day) {
        if(day < 10)
        {
            textViewDeadLine.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-0").append(day));
        }
        else
        {
            textViewDeadLine.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
    }

    private  void eventTime(int hour, int minute) {

        String hourS = ""+hour;
        String minuteS =""+minute;;
        if(hour <10)
        {
            hourS = "0"+hour;
        }
        if(minute <10)
        {
            minuteS = "0"+minute;
        }

        textViewTime.setText(new StringBuilder().append(hourS).append(" :  ")
                .append(minuteS));
    }


    public class SpinnerAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater mInflater;

        public SpinnerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return noOfMembers.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ListContentHolder holder;
            View v = convertView;
            if (v == null) {
                mInflater = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                v = mInflater.inflate(R.layout.row_textview_spinner, null);
                holder = new ListContentHolder();
                holder.text = (TextView) v.findViewById(R.id.textViewSpinner);

                v.setTag(holder);
            } else {

                holder = (ListContentHolder) v.getTag();
            }

            holder.text.setText(noOfMembers[position]);

            return v;
        }
    }

    static class ListContentHolder {

        TextView text;
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
                    // handle back button's click listener
                    //Toast.makeText(getActivity(), "Back press", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

   private void membersJoiningInEvent(final JSONObject jsonMembersJoiningInfo){

        final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.updating_going_status));

        new Thread(new Runnable() {

            public void run() {


                EventListServices eventListService = new EventListServices(getActivity());

                boolean callStatus 	= eventListService.joinEvent(jsonMembersJoiningInfo, new EventListServices.EventListServiceListeners(){

                    @Override
                    public void onSuccess(EventObjectsTemplate jsonArrayOfEvents) {

                    }

                    @Override
                    public void onSuccess(JSONObject jsonJoiningEvents) {

//                        int event_id = Integer.parseInt(AJSONObject.optString(jsonMembersJoiningInfo, "event_id").toString());
                        String event_id = AJSONObject.optString(jsonMembersJoiningInfo, "event_id").toString();
                        JSONObject memberGoingJson = AJSONObject.optJSONObject(jsonJoiningEvents, "member_going");

                        int notGoing  =  Integer.parseInt(AJSONObject.optString(memberGoingJson, "not_going").toString());
                        int mayBeGoing  =  Integer.parseInt(AJSONObject.optString(memberGoingJson, "maybe").toString());
                        int going  =  Integer.parseInt(AJSONObject.optString(memberGoingJson, "going").toString());

                        MDatabaseManager.getInstance().setMembersJoiningToEvent(event_id, notGoing, mayBeGoing, going);

                        // getFragmentManager().popBackStack();
                        progress.dismiss();

                        ((Activity)getActivity()).runOnUiThread(new Runnable() {
                            public void run() {
                                // TODO Auto-generated method stub
                               // Utils.alert(getActivity(), R.drawable.ic_menu_send, R.string.success, "Successfully uploaded your request!!", true, null);
                                showThanksDialog();
                            }
                        });

                    }

                    @Override
                    public void onFailure(Exception error) {
                        Log.e(error.getMessage()+" At getEventList() of PhoneBook Class");
                        Utils.showAlertOnMainThread(getActivity(), error.getMessage());
                        progress.dismiss();
                    }

                    @Override
                    public void onForceLogout(Exception error) {
                        Log.e(error.getMessage()+" At getEventList() of PhoneBook Class");
                        progress.dismiss();
                    /*    if(listener!=null){
                            listener.onLogoutEvent(error);
                        }*/
                    }
                });
                if(!callStatus)
                    progress.dismiss();

            }

        }).start();

    }


    public  void showThanksDialog() {

        final Dialog dialog = new Dialog((Activity)getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.thanks_dialog_layout);
        dialog.setCancelable(false);

        Button buttonDone = (Button) dialog.findViewById(R.id.buttonDone);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
