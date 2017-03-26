package com.sr.masharef.masharef.event;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.model.event.EventObjects;
import com.sr.masharef.masharef.model.event.EventObjectsTemplate;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.verification.AVBaseFragment;
import com.sr.masharef.masharef.webservice.api.EventListServices;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by systemrapid on 13/01/17.
 */

public class EventFragment extends AVBaseFragment {
    ActionBar actionBar;
    ArrayList<EventObjects> eventsList;
    RecyclerView recyclerViewAllEventsList;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View dashboard = inflater.inflate(R.layout.fragment_event, container, false);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getContext());


        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        final TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.event_list);
        ImageView imageButtonShowCalendar = (ImageView) mCustomView.findViewById(R.id.imageButtonShowCalendar);
        imageButtonShowCalendar.setVisibility(View.VISIBLE);
//        ImageButton imageViewAddEvent = (ImageButton) mCustomView.findViewById(R.id.imageViewAddEvent);
//        imageViewAddEvent.setVisibility(View.VISIBLE);

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


        recyclerViewAllEventsList = (RecyclerView) dashboard.findViewById(R.id.recyclerViewAllEventsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAllEventsList.setLayoutManager(mLayoutManager);
        recyclerViewAllEventsList.setItemAnimator(new DefaultItemAnimator());
        // hideItem();
        if(eventsList == null)
             getEventList();
        initializeAdapter();
        initializeListeners(mCustomView);

        return dashboard;
    }
    private void hideItem()
    {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_view).setVisible(false);
    }
    private void initializeAdapter() {

        eventsList = MDatabaseManager.getInstance().getEventData();
        AllEventsListAdapter allEventsListAdapter = new AllEventsListAdapter(eventsList);
        recyclerViewAllEventsList.setAdapter(allEventsListAdapter);
        allEventsListAdapter.notifyDataSetChanged();
    }

    void initializeAdapterOnMainThread(){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initializeAdapter();
            }
        });
    }

    private void initializeListeners(View mCustomView) {
//        final ImageButton imageButtonAddEvent = (ImageButton) mCustomView.findViewById(R.id.imageViewAddEvent);
        final ImageView buttonShowCalendar = (ImageView) mCustomView.findViewById(R.id.imageButtonShowCalendar);

      /*  imageButtonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "imageViewAddEvent Clicked!",
//                        Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager =getActivity(). getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                CreateNewEventFragment newEventFragment=new CreateNewEventFragment();
                ft.replace(R.id.content_frame, newEventFragment,"CreateNewEventFragmentTab").addToBackStack("CreateNewEventFragment").commit();
            }
        });*/
        buttonShowCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(getContext(), "buttonShowCalendar Clicked!",
                  //      Toast.LENGTH_SHORT).show();
              /*  mTitleTextView.setText("Calendar");
                buttonShowCalendar.setVisibility(View.INVISIBLE);
                imageButtonAddEvent.setVisibility(View.INVISIBLE);*/
                CalendarViewFragment calendarViewFragment = new CalendarViewFragment();
                FragmentManager fragmentManager =getActivity(). getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, calendarViewFragment,"CalendarViewFragmentTab").addToBackStack("CalendarViewFragment").commit();
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      /*  MasharefDB eventsDB1= new MasharefDB(getContext());
        eventsDB1.open();
        ArrayList<EventObjects> mEvents = eventsDB1.getEventData();
        eventsDB1.close();*/

    }

    public class AllEventsListAdapter extends RecyclerView.Adapter<AllEventsListAdapter.AllEventListHolder> {

        private int lastPosition = -1;
        ArrayList<EventObjects> eventsList;
        public AllEventsListAdapter(ArrayList<EventObjects> eventsList)
        {
            this.eventsList = eventsList;
        }
        @Override
        public AllEventListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.all_events_row_list, parent, false);
            return new AllEventListHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AllEventListHolder holder, final int position) {
            EventObjects eventObjects = eventsList.get(position);
            Calendar eventCalendar = Calendar.getInstance();
            eventCalendar.setTime(eventObjects.getEventDate());
            int dayValue = eventCalendar.get(Calendar.DAY_OF_MONTH);
            int displayMonth = eventCalendar.get(Calendar.MONTH) + 1;
            int displayYear = eventCalendar.get(Calendar.YEAR);
            String eventDate = dayValue+"-"+displayMonth+"-"+displayYear;

            holder.textViewAllEventName.setText(eventObjects.getEvent_name());
            holder.textViewAllDescription.setText(eventObjects.getDescription());
            holder.textViewAllEventsDate.setText(eventDate);
            holder.textViewAllVanue.setText(eventObjects.getVenue());
            holder.textViewAllEventsTime.setText(eventObjects.getTime());

            holder.textViewAllGender.setText(eventObjects.getGender());
//            holder.textViewAllCost.setText(eventObjects.getCost() + "("+"AED"+")");
            holder.textViewAllCost.setText(eventObjects.getCost() +" ("+ eventObjects.getCurrency()+")");

            // Here you apply the animation when the view is bound
       //     setAnimation(holder.itemView, position);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<EventObjects>() {}.getType();
                    final String eventObjectJson = gson.toJson(eventsList.get(position), type);

                    EventDetailFragment eventDetailFragment = new EventDetailFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("eventObjectJson", eventObjectJson);
                    eventDetailFragment.setArguments(bundle);
                    ft.replace(R.id.content_frame, eventDetailFragment, "EventDetailFragmentTab");
                    ft.addToBackStack("EventDetailFragment");
                    ft.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return eventsList.size();
        }


        public class AllEventListHolder extends RecyclerView.ViewHolder {
            private final TextView textViewAllDescription;
            private final TextView textViewAllEventName;
            private final TextView textViewAllVanue;
            private final TextView textViewAllEventsDate;
            private final TextView textViewAllEventsTime;
            private final TextView textViewAllGender;
            private final TextView textViewAllCost;

            public AllEventListHolder(View itemView) {
                super(itemView);
                textViewAllDescription =   (TextView)itemView.findViewById(R.id.textViewAllDescription);
                textViewAllEventName =   (TextView)itemView.findViewById(R.id.textViewAllEventName);
                textViewAllVanue =   (TextView)itemView.findViewById(R.id.textViewAllVanue);
                textViewAllGender =   (TextView)itemView.findViewById(R.id.textViewAllGender);
                textViewAllCost =   (TextView)itemView.findViewById(R.id.textViewAllCost);
                textViewAllEventsDate =   (TextView)itemView.findViewById(R.id.textViewAllEventsDate);
                textViewAllEventsTime =   (TextView)itemView.findViewById(R.id.textViewAllEventsTime);

            }
        }
        private void setAnimation(View viewToAnimate, int position)
        {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition)
            {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
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

                 /*   getFragmentManager().popBackStack();
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
                    int totalBackStacks = getFragmentManager().getBackStackEntryCount();
                    android.util.Log.i("","totalBackStacks== "+totalBackStacks);
                    for(int i=0; i<totalBackStacks; i++) {
                        getFragmentManager().popBackStack();
                    }*/

                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setDisplayShowCustomEnabled(false);
                    actionBar.setDisplayShowTitleEnabled(true);
                    if(getActivity() != null)
                        getActivity().onBackPressed();

                    return true;
                }
                return false;
            }
        });

    }
    void getEventList(){

        final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.getting_event_list));

        new Thread(new Runnable() {

            public void run() {


                EventListServices userServ = new EventListServices(getActivity());

                boolean callStatus 	= userServ.getEventList(new EventListServices.EventListServiceListeners(){

                    @Override
                    public void onSuccess(EventObjectsTemplate eventObjectsTemplate) {
                        Log.i(getTag(), "Event json == "+eventObjectsTemplate);
                        // new EventObjectsTemplate(JSON);
                        ArrayList<EventObjects> eventsList= MDatabaseManager.getInstance().getEventData();//eventObjectsTemplate.eventList;
                        if (eventsList != null)
                        {
                            Log.e("eventObjList.size()eventObjList.size()eventObjList.size() = "+eventsList.size()+" XX");

                            initializeAdapterOnMainThread();
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void onSuccess(JSONObject jsonJoiningEvents) {
                        progress.dismiss();
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

}
