package com.sr.masharef.masharef.event;

import android.support.v4.app.Fragment;

public class CreateNewEventFragment extends Fragment {
    /*// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CreateEventFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static TextView editTextEventDate,editTextDeadLine, editTextTime;

    private   TextView editTextEventName, editTextDescription,editTextVenue, editTextCost;
    private RadioGroup radioGroupGender;
    private Button buttonGetApproval;
    int year, month, day, hour, minute;
    DatePickerDialog datePickerDialog;
    private static boolean isEventDate;


    public CreateNewEventFragment() {
        // Required empty public constructor
    }

    *//**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNewEventFragment.
     *//*
    // TODO: Rename and change types and number of parameters
    public static CreateNewEventFragment newInstance(String param1, String param2) {
        CreateNewEventFragment fragment = new CreateNewEventFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View mCustomView = inflater.inflate(R.layout.custom_actionbar, null);
        final TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Create Event");
        ImageButton imageButtonShowCalendar = (ImageButton) mCustomView.findViewById(R.id.imageButtonShowCalendar);
        imageButtonShowCalendar.setVisibility(View.INVISIBLE);
        ImageButton imageViewAddEvent = (ImageButton) mCustomView.findViewById(R.id.imageViewAddEvent);
        imageViewAddEvent.setVisibility(View.INVISIBLE);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        editTextEventName = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.editTextEventName);
        editTextDescription = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.editTextDescription);
        editTextVenue = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.editTextVenue);
        editTextEventDate = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.editTextEventDate);
        editTextTime = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.editTextTime);
        editTextCost = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.editTextCost);
        editTextDeadLine = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.editTextDeadLine);

        radioGroupGender = (RadioGroup) ((AppCompatActivity)getActivity()).findViewById(R.id.radioGroupGender);
        buttonGetApproval = (Button) ((AppCompatActivity)getActivity()).findViewById(R.id.buttonGetApproval);

        Calendar calendar = Calendar.getInstance();
         year = calendar.get(Calendar.YEAR);
         month = calendar.get(Calendar.MONTH);
         day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);


        eventDate(year, month+1, day);
        deadLineDate(year, month+1, day);
        eventTime(hour, minute);

        editTextEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     setDate(v);
                InputMethodManager imm = (InputMethodManager)((AppCompatActivity)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((AppCompatActivity)getActivity()).getCurrentFocus().getWindowToken(), 0);
                isEventDate = true;
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "Select Event Date");
              //  eventDate(year, month+1, day);
            }
        });

        editTextDeadLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)((AppCompatActivity)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((AppCompatActivity)getActivity()).getCurrentFocus().getWindowToken(), 0);
                isEventDate = false;
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "Select Deadline Date");
              //  deadLineDate(year, month+1, day);
            }
        });
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)((AppCompatActivity)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((AppCompatActivity)getActivity()).getCurrentFocus().getWindowToken(), 0);

                DialogFragment newTimeFragment = new SelectTimeFragment();
                newTimeFragment.show(getFragmentManager(), "Select Event Time");
            }
        });
        buttonGetApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventName =  editTextEventName.getText().toString();
                String description =  editTextDescription.getText().toString();
                String venue =  editTextVenue.getText().toString();
                String eventDate =  editTextEventDate.getText().toString();
                String eventTime =  editTextTime.getText().toString();
                int eventCost = 0;
                if(editTextCost.getText().toString().length() >0) {
                    eventCost = Integer.parseInt(editTextCost.getText().toString());
                }
                String deadLineDate =  editTextDeadLine.getText().toString();

                int selectedId=radioGroupGender.getCheckedRadioButtonId();

                RadioButton radioSexButton=(RadioButton)getActivity().findViewById(selectedId);
                String gender = radioSexButton.getText().toString();
                Log.i("TAG", "eventName "+eventName+ ",description "+description+ ",venue "+venue+ ",eventDate "+ eventDate+ ",eventTime "+ eventTime+ ",gender "+ gender+ ",eventCost "+ eventCost+ ",deadLineDate "+ deadLineDate);

                if(eventName.length() > 0 && description.length()>0 && venue.length()>0 && eventDate.length() >0 && eventTime.length() >0
                        && eventCost >0 && deadLineDate.length() >0) {
                 *//*   MasharefDB eventsDB = new MasharefDB(getContext());
                    eventsDB.open();
                   eventsDB.insertEventData(eventName, description, venue, eventDate, eventTime, gender, eventCost, deadLineDate, "Not Approved");
                    //Toast.makeText(getContext(), "Event Successfully submitted  == " , Toast.LENGTH_SHORT).show();
                    eventsDB.close();*//*
                  //  MDatabaseManager.getInstance().insertEventData(1, eventName, description, venue, eventDate, eventTime, gender, eventCost, deadLineDate, "Not Approved");

                    getFragmentManager().popBackStack();
                }
                else
                {
                    Toast.makeText(getContext(), "Please fill all the fields ", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
        } *//*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*//*
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**
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

    private static void eventDate(int year, int month, int day) {
        if(day < 10)
        {
            editTextEventDate.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-0").append(day));
        }
        else
        {
            editTextEventDate.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
    }
    private static void deadLineDate(int year, int month, int day) {
        if(day < 10)
        {
            editTextDeadLine.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-0").append(day));
        }
        else
        {
            editTextDeadLine.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
    }

    private static void eventTime(int hour, int minute) {
        editTextTime.setText(new StringBuilder().append(hour).append(" : ")
                .append(minute));
        if(hour <10)
        {
            editTextTime.setText(new StringBuilder().append("0"+hour).append(" : ")
                    .append(minute));
        }
        if(minute <10)
        {
            editTextTime.setText(new StringBuilder().append(hour).append(" :  0")
                    .append(minute));
        }

    }
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            if(isEventDate) {
                eventDate(year, month, day);
            }
            else
            {
                deadLineDate(year, month, day);
            }
        }

    }
    public static class SelectTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(),this,hour, minute, true);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            editTextTime.setText(new StringBuilder().append(hourOfDay).append(" : ")
//                    .append(minute));
            eventTime(hourOfDay, minute);
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
                    // handle back button's click listener
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }*/
}
