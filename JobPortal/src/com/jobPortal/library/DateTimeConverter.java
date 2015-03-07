package com.jobPortal.library;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateTimeConverter {
	public static String longToDateConverter(long date) {
		java.text.DateFormat dateFormat = new SimpleDateFormat(
				"dd-MM-yyyy hh:mm:ss a");
		Date newdate = new Date(date);
		return dateFormat.format(newdate);
	}
}
