package com.sr.masharef.masharef.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContentUri {


	public static final class Event implements BaseColumns {
		private Event() {
		}
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ MProvider.AUTHORITY + "/" + MProvider.TABLE_NAME_EVENTS);
		public static final String EVENT_id = "event_id";
		public static final String EVENT_NAME = "event_name";
		public static final String DESCRIPTION = "description";
		public static final String VENUE = "venue";
		public static final String DATE = "date";
		public static final String TIME = "time";
		public static final String GENDER = "gender";
		public static final String COST = "cost";
		public static final String DEAD_LINE = "dead_line";
		public static final String STATUS = "status";
		public static final String NOTGOINGTOEVENT = "not_going_to_event";
		public static final String MAYBEGOINGTOEVENT = "may_be_gooing_to_event";
		public static final String GOINGTOEVENT = "going_to_event";
		public static final String CURRENCY = "currency";

	}

	public static final class PhoneCategeory implements BaseColumns {
		private PhoneCategeory() {
		}
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ MProvider.AUTHORITY + "/" + MProvider.TABLE_NAME_PHONE_CATEGEORY);
		public static final String CATEGEORY_id = "category_id";
		public static final String CATEGEORY_NAME = "categeory_name";
	}

	public static final class GalleryCategeory implements BaseColumns {
		private GalleryCategeory() {
		}
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ MProvider.AUTHORITY + "/" + MProvider.TABLE_NAME_GALLERY);
		public static final String GALLERY_IS_PRIVATE = "is_private";
		public static final String GALLERY_MEDIATYPE = "media_type";
		public static final String GALLERY_MEDIAIMAGE = "media_image";
	}

	public static final class GalleryList implements BaseColumns {
		private GalleryList() {
		}
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ MProvider.AUTHORITY + "/" + MProvider.TABLE_NAME_GALLERY);
		}

	public static final class ContactList implements BaseColumns {
		private ContactList() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ MProvider.AUTHORITY + "/" + MProvider.TABLE_NAME_CONTACT_LIST);

		public static final String CONTACT_ID = "contact_id";
		public static final String CATEGEORY_ID = "categeory_id";
		public static final String CATEGEORY_NAME = "categeory_name";
		public static final String FIRST_NAME = "first_name";
		public static final String LAST_NAME = "last_name";
		public static final String OCCUPATION = "occupation";
		public static final String WORKPLACE = "work_place";
		public static final String RATING = "rating";
		public static final String COUNTRY_NAME = "country_name";
		public static final String COUNTRY_CODE = "country_code";
		public static final String COUNTRY_ISN = "country_ISN";
		public static final String NATIONAL_NUMBER = "national_number";
		public static final String I8N_NUMBER = "interNational_number";
	}

}
