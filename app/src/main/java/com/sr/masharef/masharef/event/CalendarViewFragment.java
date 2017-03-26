package com.sr.masharef.masharef.event;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.model.event.EventObjects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class CalendarViewFragment extends Fragment implements OnClickListener
{
	private static final String tag = "CalendarViewActivity";

	private ImageView calendarToJournalButton;
	private Button selectedDayMonthYearButton;
	private TextView currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	private int month, year;
	private String eventDay;
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";

	private ArrayList<EventObjects> monthlyEventList;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		/*EventsDB eventsDB = new EventsDB(this);
		//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//	String date = sdf.format(new Date());
		eventsDB.open();
		String date = "2017-02-03";
		eventsDB.insertEventData(date,"Testing1");
		date = "2016-12-02";
		eventsDB.insertEventData(date,"Testing2");
		date = "2016-11-27";
		eventsDB.insertEventData(date,"Testing3");
		eventsDB.close();*/

	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


		View dashboard = inflater.inflate(R.layout.simple_calendar_view, container, false);

		final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		View mCustomView = inflater.inflate(R.layout.custom_actionbar, null);
		final TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
		ImageView imageButtonShowCalendar = (ImageView) mCustomView.findViewById(R.id.imageButtonShowCalendar);
		imageButtonShowCalendar.setVisibility(View.INVISIBLE);
//		ImageButton imageViewAddEvent = (ImageButton) mCustomView.findViewById(R.id.imageViewAddEvent);
//		imageViewAddEvent.setVisibility(View.INVISIBLE);
		mTitleTextView.setText(R.string.event_calendar);
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
		LayoutInflater mInflater = LayoutInflater.from(getContext());

		return dashboard;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar:= " + "Month: " + month + " " + "Year: " + year);

		prevMonth = (ImageView) getActivity().findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) getActivity().findViewById(R.id.currentMonth);
		currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

		nextMonth = (ImageView) getActivity().findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) getActivity().findViewById(R.id.calendarGridView);

		// Initialised
		setUpCalendarAdapter();

		/*calendarView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
			public void onSwipeTop() {
				Toast.makeText(getActivity(), "top", Toast.LENGTH_SHORT).show();
			}
			public void onSwipeRight() {
				Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
			}
			public void onSwipeLeft() {
				Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();
			}
			public void onSwipeBottom() {
				Toast.makeText(getActivity(), "bottom", Toast.LENGTH_SHORT).show();
			}
		});*/
	}

	/**
	 *
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year)
	{
		//adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
//		adapter.notifyDataSetChanged();
//		calendarView.setAdapter(adapter);
		setUpCalendarAdapter();
	}

	@Override
	public void onClick(View v)
	{
		if (v == prevMonth)
		{
			if (month <= 1)
			{
				month = 12;
				year--;
			}
			else
			{
				month--;
			}
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth)
		{
			if (month > 11)
			{
				month = 1;
				year++;
			}
			else
			{
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}

	}

	@Override
	public void onDestroy()
	{
		Log.d(tag, "Destroying View...");
		super.onDestroy();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener
	{
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final ArrayList<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		private final String[] months = new String[]{getString(R.string.jan), getString(R.string.feb), getString(R.string.march), getString(R.string.april), getString(R.string.may), getString(R.string.june), getString(R.string.jul), getString(R.string.aug), getString(R.string.sept), getString(R.string.oct), getString(R.string.nov), getString(R.string.dec)};
		private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		private final int month, year;
		private int daysInMonth, prevMonthDays;
		private int currentDayOfMonth;
		private int currentWeekDay;

		private ArrayList<EventObjects> allEvents;
		ArrayList<HashMap> eventsPerMonthMapList;

		private Button gridcell;
		private TextView num_events_per_day;
		private HashMap eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId, int month, int year, ArrayList<EventObjects> allEvents)
		{
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			this.month = month;
			this.year = year;
			this.allEvents = allEvents;
			eventsPerMonthMap = new HashMap();
			monthlyEventList =  new ArrayList<EventObjects>();

			this.eventsPerMonthMapList =  new ArrayList<HashMap>();

			Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());


			// Print Month
			printMonth(month, year);

		}
		private String getMonthAsString(int i)
		{
			return months[i];
		}

		private String getWeekDayAsString(int i)
		{
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i)
		{
			return daysOfMonth[i];
		}

		public String getItem(int position)
		{
			return list.get(position);
		}

		@Override
		public int getCount()
		{
			return list.size();
		}

		/**
		 * Prints Month
		 *
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy)
		{
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			// The number of days to leave blank at
			// the start of this month.
			int trailingSpaces = 0;
			int leadSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

			// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11)
			{
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			}
			else if (currentMonth == 0)
			{
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			}
			else
			{
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			}

			// Compute how much to leave before before the first day of the
			// month.
			// getDay() returns 0 for Sunday.
			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1)
			{
				++daysInMonth;
			}
			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++)
			{
				Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
				list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);

				Calendar eventCalendar = Calendar.getInstance();
				int dayValue = i;
				int displayMonth = cal.get(Calendar.MONTH) + 1;
				int displayYear = cal.get(Calendar.YEAR);
				boolean eventDayAdded = false;
				HashMap map = new HashMap<String, Integer>();
				for(int j = 0; j < allEvents.size(); j++){
					eventCalendar.setTime(allEvents.get(j).getEventDate());
					if((dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH)) && (displayMonth == eventCalendar.get(Calendar.MONTH) + 1)
							&& (displayYear == eventCalendar.get(Calendar.YEAR))){
						list.add(String.valueOf(i) + "-PINK" + "-" + getMonthAsString(currentMonth) + "-" + yy);

						monthlyEventList.add(allEvents.get(j));
						eventsPerMonthMap = findNumberOfEventsPerMonth(eventCalendar.getTime(), map);
						if(eventDayAdded)
						{
							int lastPosition = list.size()-1;
							list.remove(lastPosition);
						}
								eventDayAdded = true;
					}
				}
				if(eventsPerMonthMap != null && eventsPerMonthMap.size() >0)
				{
					eventsPerMonthMapList.add(eventsPerMonthMap);
				}

			if(!eventDayAdded ) {
				if (i == getCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
				}
			}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++)
			{
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
			}


		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * @return
		 */

		private HashMap findNumberOfEventsPerMonth(/*int year, int month,*/ Date dateCreated, HashMap map)
		{

						DateFormat dateFormatter2 = null;
						String day = null;
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.CUPCAKE) {
							dateFormatter2 = new DateFormat();
							 day = dateFormatter2.format("dd", dateCreated).toString();
						}


						 if (map.containsKey(day))
						 {
						 Integer val = (Integer) map.get(day) + 1;
						 map.put(day, val);
						 }
						 else
						 {
						 map.put(day, 1);
						 }
			return map;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{

			View row = convertView;
			if (row == null)
			{
				LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);

			gridcell.setOnClickListener(this);
			num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
			// ACCOUNT FOR SPACING

			Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			if(eventsPerMonthMapList !=null && eventsPerMonthMapList.size() >0) {
				for(HashMap eventsPerDayMap : eventsPerMonthMapList) {
					if ((!eventsPerDayMap.isEmpty()) && (eventsPerDayMap != null)) {
						int dayValue = Integer.parseInt(theday);
						String dayMatched = null;
						if (dayValue < 10) {
							dayMatched = "0" + String.valueOf(dayValue);
						} else {
							dayMatched = theday;
						}
						if (eventsPerDayMap.containsKey(dayMatched)) {

							Integer numEvents = (Integer) eventsPerDayMap.get(dayMatched);
							if((numEvents !=  null)/* && (!numEvents.toString().equalsIgnoreCase("1"))*/)
							num_events_per_day.setText(numEvents.toString());
						}
					}
				}

			}
			// Set the Day GridCell
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);
		if (day_color[1].equals("GREY"))
		{
			gridcell.setTextColor(Color.LTGRAY);
			num_events_per_day.setText("");
		}
		else if (day_color[1].equals("WHITE"))
		{
			gridcell.setTextColor(Color.BLACK);
		}
		else if(day_color[1].equals("PINK"))
		{
			gridcell.setTextColor(getResources().getColor(R.color.text_color_orange, null));
			eventDay = day_color[0];

//			SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
//			String sDate = formatter.format(cal.getTime());

		//	TextView textView =(TextView) findViewById(R.id.num_events_per_day);
		//	num_events_per_day.setText(list.get(position).toString());
		}
		else if (day_color[1].equals("BLUE"))
		{
			gridcell.setBackground(getResources().getDrawable(R.mipmap._ic_current_date, null));
		//	gridcell.setTextColor(Color.BLUE);
		}

			return row;
		}
		@Override
		public void onClick(View view)
		{
			String clicked_date_month_year = (String) view.getTag();
			String[] day_color = clicked_date_month_year.split("-");
			String choosenDate = day_color[0];

			//getCurrentDayOfMonth();
			ArrayList<EventObjects> eventObjectsesPerday = new ArrayList<EventObjects>();
			Log.i(tag,"monthlyEventList === "+monthlyEventList);
			Log.i(tag,"getCurrentDayOfMonth === "+getCurrentDayOfMonth());
			Calendar eventCalendar = Calendar.getInstance();
			int choosenDateValue = 0;
			if(choosenDate != null && choosenDate.length() >0)
				choosenDateValue = Integer.parseInt(choosenDate);

			String day = gridcell.getText().toString();
			HashMap map = new HashMap<String, Integer>();
			for(int j = 0; j < monthlyEventList.size(); j++) {
				eventCalendar.setTime(monthlyEventList.get(j).getEventDate());
				if ((choosenDateValue == eventCalendar.get(Calendar.DAY_OF_MONTH))) {
					eventObjectsesPerday.add(monthlyEventList.get(j));
				}
			}

			popUpPerDayEventListDialog(eventObjectsesPerday, clicked_date_month_year);
		}

		public int getCurrentDayOfMonth()
		{
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth)
		{
			this.currentDayOfMonth = currentDayOfMonth;
		}
//		public int[] getEventDaysOfMonth()
//		{
//			return eventDaysList;
//		}
		public void setCurrentWeekDay(int currentWeekDay)
		{
			this.currentWeekDay = currentWeekDay;
		}
		public int getCurrentWeekDay()
		{
			return currentWeekDay;
		}
	}

	private void setUpCalendarAdapter(){


	/*	MasharefDB eventsDB1= new MasharefDB(getContext());
		eventsDB1.open();
		ArrayList<EventObjects> mEvents = eventsDB1.getEventData();
		eventsDB1.close();*/
		ArrayList<EventObjects> mEvents = MDatabaseManager.getInstance().getEventData();

		final int MAX_CALENDAR_COLUMN = 42;
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		List<Date> dayValueInCells = new ArrayList<Date>();
		Calendar mCal = (Calendar)cal.clone();
		mCal.set(Calendar.DAY_OF_MONTH, 1);
		int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
		mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
		while(dayValueInCells.size() < MAX_CALENDAR_COLUMN){
			dayValueInCells.add(mCal.getTime());
			mCal.add(Calendar.DAY_OF_MONTH, 1);
		}


		adapter = new GridCellAdapter(getActivity().getApplicationContext(), R.id.calendar_day_gridcell, month, year, mEvents);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);

		MonthlyEventsListAdapter monthlyEventsListAdapter = null;
		RecyclerView recyclerViewMonthlyEvents = (RecyclerView) getActivity().findViewById(R.id.recyclerViewMonthlyEvents);
		if(monthlyEventList != null && monthlyEventList.size() >0) {
			recyclerViewMonthlyEvents.setVisibility(View.VISIBLE);
			monthlyEventsListAdapter = new MonthlyEventsListAdapter(monthlyEventList, getContext(), getFragmentManager(), null);
			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
			recyclerViewMonthlyEvents.setLayoutManager(mLayoutManager);
			recyclerViewMonthlyEvents.setItemAnimator(new DefaultItemAnimator());
			recyclerViewMonthlyEvents.setAdapter(monthlyEventsListAdapter);
			monthlyEventsListAdapter.notifyDataSetChanged();
		}
		else
		{
			recyclerViewMonthlyEvents.setVisibility(View.INVISIBLE);
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
	}


	private void popUpPerDayEventListDialog(ArrayList<EventObjects> eventObjectsesPerday, String clicked_date_month_year) {

		Dialog dialog = new Dialog(getActivity());
		//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		if(recyclerViewDailyEvents != null && recyclerViewDailyEvents.getParent()!=null)
//			((ViewGroup)recyclerViewDailyEvents.getParent()).removeView(recyclerViewDailyEvents); // <- fix

		dialog.setContentView(R.layout.dialog_recyclerview_daily_event);
		dialog.setCancelable(true);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
		RecyclerView recyclerViewDailyEvents = (RecyclerView) dialog.findViewById(R.id.recyclerViewDailyEvents);
		TextView dailyEventTitle = (TextView) dialog.findViewById(R.id.dailyEventTitle);
		dailyEventTitle.setText(clicked_date_month_year);
		MonthlyEventsListAdapter eventObjectsesPerdayAdapter = null;
		if(eventObjectsesPerday != null && eventObjectsesPerday.size() >0) {
			recyclerViewDailyEvents.setVisibility(View.VISIBLE);
			eventObjectsesPerdayAdapter = new MonthlyEventsListAdapter(eventObjectsesPerday, getContext(), getFragmentManager(), dialog);
			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
			recyclerViewDailyEvents.setLayoutManager(mLayoutManager);
			recyclerViewDailyEvents.setItemAnimator(new DefaultItemAnimator());
			recyclerViewDailyEvents.setAdapter(eventObjectsesPerdayAdapter);
			eventObjectsesPerdayAdapter.notifyDataSetChanged();
			dialog.show();
		}
		else
		{
			recyclerViewDailyEvents.setVisibility(View.INVISIBLE);
		}

	}
}
