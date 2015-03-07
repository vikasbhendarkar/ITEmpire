package com.jobPortal.library;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Common {
	public static String STUDENT_EMAIL = "";
	public static String COMPANY_EMAIL = "";

	//JSON for Student
	public static String STUDENT_ID = "student_id";
	public static String STUDENT_ROLL_NO = "roll_no";
	public static String STUDENT_SECTION = "section";
	public static String STUDENT_FIRST_NAME = "first_name";
	public static String STUDENT_MIDDLE_NAME = "middle_name";
	public static String STUDENT_LAST_NAME = "last_name";
	public static String STUDENT_DATE_OF_BIRTH = "date_of_birth";
	public static String STUDENT_GENDER = "gender";
	public static String STUDENT_BRANCH = "branch";
	public static String STUDENT_SEMESTER = "semester";
	public static String STUDENT_SSC_MARKS = "ssc_marks";
	public static String STUDENT_HSC_MARKS = "hsc_marks";
	public static String STUDENT_DIPLOMA_MARKS = "diploma_marks";
	public static String STUDENT_ENGG_MARKS = "engg_marks";
	public static String STUDENT_BACKLOG_HISTORY = "backlog_history";
	public static String STUDENT_IS_BACKLOGLIVE = "isBacklogLive";
	public static String STUDENT_IS_GAPEDUCATION = "isGapInEducation";
	public static String STUDENT_GAP_DETAILS = "gap_details";
	public static String STUDENT_MOBILE_NUMBER = "mobile_number";
	public static String STUDENT_RAISONI_EMAIL = "email";
	public static String STUDENT_TEMP_ADDRESS = "temp_address";
	public static String STUDENT_PER_ADDRESS = "per_address";
	public static String STUDENT_CERTIFICATION = "certification";
	public static String STUDENT_PROJECT_DETAILS = "project_details";
	public static String STUDENT_IS_ACTIVE = "isActive";
	public static String STUDENT_CREATED_AT = "created_at";
	public static String KEY_SUCCESS = "success";	
	
	//json for notification
	public static String NOTIFICATION_CREATED_AT = "created_at";
	public static String NOTIFICATION_FROM_USER = "from_user";
	public static String NOTIFICATION_TYPE_USER = "type_user";
	public static String NOTIFICATION_MESSAGE = "message";
	
	public static void minimizeKeyboard(Activity activity) {
		 // Check if no view has focus:
	    View view = activity.getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
}