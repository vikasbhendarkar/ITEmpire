/**
 * Author: Vikas Bhendarkar
 * */
package com.jobPortal.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

import com.jobPortal.bean.Company;
import com.jobPortal.bean.SearchFilterForCompany;
import com.jobPortal.bean.StudentBean;

public class UserFunctions {

	private JSONParser jsonParser;

	 private static String loginURL ="http://nure3rmsmms.org/job_portal_api/";
//	private static String loginURL = "http://10.0.0.2:80/job_portal_api/";
	 private static String registerURL="http://nure3rmsmms.org/job_portal_api/";
//	private static String registerURL = "http://10.0.0.2:80/job_portal_api/";

	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String users_tag = "db_users";
	private static String update_act = "update_act";
	private static String update_student = "update_student";
	private static String update_company = "update_company";
	private static String delete_user = "delete_user";
	private static String user_profile_update = "user_profile_update";
	private static String get_available_activated_companies = "get_available_activated_companies";
	private static String get_available_activated_students = "get_available_activated_students";
	private static String get_notifications = "get_notifications";
	private static String add_filter = "add_filter";
	private static String get_filter = "get_filter";

	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	/**
	 * function make Login Request
	 * 
	 * @param username
	 * @param password
	 * */
	public JSONObject loginUser(String username, String password,
			String loginType) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("type", loginType));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function make Login Request
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String email, String password,
			String type) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("type", type));

		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}

	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if (count > 0) {
			// user logged in
			return true;
		}
		return false;
	}

	/**
	 * Function to logout user Reset Database
	 * */
	public boolean logoutUser(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}

	/**
	 * function get All rows from database
	 * 
	 * @param username
	 * @param password
	 * */
	public JSONObject getAllvaluesFromDatabase(String table, String userType) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", users_tag));
		params.add(new BasicNameValuePair("table", table));
		params.add(new BasicNameValuePair("userType", userType));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function get All rows from database
	 * 
	 * @param username
	 * @param password
	 * */
	public JSONObject updateActivationStatus(String activation, String email) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", update_act));
		params.add(new BasicNameValuePair("activation", activation));
		params.add(new BasicNameValuePair("email", email));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function update student database
	 * 
	 * @param username
	 * @param password
	 * */
	public JSONObject updateStudentProfile(StudentBean student) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", update_student));
		params.add(new BasicNameValuePair("contact_number", student
				.getMobile_number()));
		params.add(new BasicNameValuePair("temp_address", student
				.getTemp_address()));
		params.add(new BasicNameValuePair("per_address", student
				.getPer_address()));
		params.add(new BasicNameValuePair("certification", student
				.getCertification()));
		params.add(new BasicNameValuePair("project_details", student
				.getProject_details()));
		params.add(new BasicNameValuePair("email", student.getEmail()));
		params.add(new BasicNameValuePair("isOptedout", student.getIsActive()));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	public JSONObject updateCompanyProfile(Company company) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", update_company));
		params.add(new BasicNameValuePair("cmny_type", company.getType()));
		params.add(new BasicNameValuePair("course", company.getCourse()));
		params.add(new BasicNameValuePair("date_of_placement", company
				.getDate_of_placement()));
		params.add(new BasicNameValuePair("salary_package", company
				.getSalary_package()));
		params.add(new BasicNameValuePair("email", company.getEmail()));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	public JSONObject deleteUser(String email, String userType) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", delete_user));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("userType", userType));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		return json;
	}

	public JSONObject getUserdetailForUpdation(String email, String userType) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", user_profile_update));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("userType", userType));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		return json;
	}

	public JSONObject getAllAvailableCompanies() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag",
				get_available_activated_companies));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		return json;
	}

	public JSONObject getAllAvailableStudents() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag",
				get_available_activated_students));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		return json;
	}
	
	public JSONObject getNotifications(String userType) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag",
				get_notifications));
		params.add(new BasicNameValuePair("userType",
				userType));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		return json;
	}
	
	public JSONObject addFilter(SearchFilterForCompany filter, String processType) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag",add_filter));
		params.add(new BasicNameValuePair("processType",processType));
		params.add(new BasicNameValuePair("email",filter.getEmail()));
		params.add(new BasicNameValuePair("ssc",filter.getSsc_marks()+"".trim()));
		params.add(new BasicNameValuePair("hsc",filter.getHsc_marks()+"".trim()));
		params.add(new BasicNameValuePair("diploma",filter.getDiploma_marks()+"".trim()));
		params.add(new BasicNameValuePair("be",filter.getBe_marks()+"".trim()));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		return json;
	}

	public JSONObject getFilterDetailsCompanyUsingEmail(String mail) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", get_filter));
		params.add(new BasicNameValuePair("email", mail));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		return json;
	}
}
