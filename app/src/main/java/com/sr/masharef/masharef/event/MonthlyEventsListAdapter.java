package com.sr.masharef.masharef.event;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.model.event.EventObjects;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by welcome on 1/18/2017.
 */

    public class MonthlyEventsListAdapter extends RecyclerView.Adapter<MonthlyEventsListAdapter.MonthlyEventListHolder> {
    private int lastPosition = -1;
    ArrayList<EventObjects> eventsList;
    Context context;
    FragmentManager fragmentManager;
    Dialog dialog;

    public MonthlyEventsListAdapter(ArrayList<EventObjects> eventsList, Context context, FragmentManager fragmentManager, Dialog dialog) {
        this.context = context;
        this.eventsList = eventsList;
        this.fragmentManager = fragmentManager;
        this.dialog = dialog;
    }

    @Override
    public MonthlyEventsListAdapter.MonthlyEventListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.monthly_events_row_list, parent, false);
        return new MonthlyEventsListAdapter.MonthlyEventListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MonthlyEventsListAdapter.MonthlyEventListHolder holder, final int  position) {
        EventObjects eventObjects = eventsList.get(position);

        Calendar eventCalendar = Calendar.getInstance();
        eventCalendar.setTime(eventObjects.getEventDate());
        int dayValue = eventCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = eventCalendar.get(Calendar.MONTH) + 1;
        int displayYear = eventCalendar.get(Calendar.YEAR);
        String eventDate = dayValue + "-" + displayMonth + "-" + displayYear;

        holder.textViewMonthlyEventName.setText(eventObjects.getEvent_name());
        holder.textViewMonthlyEventsDate.setText(eventDate);
        holder.textViewMonthlyVanue.setText(eventObjects.getVenue());
        holder.textViewMonthlyGender.setText(eventObjects.getGender());
        holder.textViewMonthlyCost.setText(eventObjects.getCost() +" ("+eventObjects.getCurrency()+")");
        holder.textViewMonthlyEventsTime.setText(eventObjects.getTime());
        // Here you apply the animation when the view is bound
      //  setAnimation(holder.itemView, position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Type type = new TypeToken<EventObjects>() {}.getType();
                final String eventObjectJson = gson.toJson(eventsList.get(position), type);
                EventDetailFragment eventDetailFragment = new EventDetailFragment();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("eventObjectJson", eventObjectJson);
                eventDetailFragment.setArguments(bundle);

                ft.replace(R.id.content_frame, eventDetailFragment, "CalendarViewFragmentTab");
                ft.addToBackStack("CalendarViewFragment");
                ft.commit();
                if(dialog != null)
                    dialog.dismiss();
                dialog = null;
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class MonthlyEventListHolder extends RecyclerView.ViewHolder {
        private final TextView textViewMonthlyGender;
        private final TextView textViewMonthlyEventName;
        private final TextView textViewMonthlyVanue;
        private final TextView textViewMonthlyEventsDate;
        private final TextView textViewMonthlyEventsTime;
        private final TextView textViewMonthlyCost;

        public MonthlyEventListHolder(View itemView) {
            super(itemView);

            textViewMonthlyEventName = (TextView) itemView.findViewById(R.id.textViewMonthlyEventName);
            textViewMonthlyVanue = (TextView) itemView.findViewById(R.id.textViewMonthlyVanue);
            textViewMonthlyGender = (TextView) itemView.findViewById(R.id.textViewMonthlyGender);
            textViewMonthlyCost = (TextView) itemView.findViewById(R.id.textViewMonthlyCost);
            textViewMonthlyEventsDate = (TextView) itemView.findViewById(R.id.textViewMonthlyEventsDate);
            textViewMonthlyEventsTime = (TextView) itemView.findViewById(R.id.textViewMonthlyEventsTime);

        }
    }


        private void setAnimation(View viewToAnimate, int position)
        {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition)
            {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

    }

