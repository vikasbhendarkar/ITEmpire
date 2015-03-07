package com.jobPortal.activities.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jobPortal.bean.Company;
import com.jobPortal.bean.NotificationBean;
import com.jobPortal.bean.StudentBean;
import com.jobPortal.library.Common;
import com.jobPortal.library.DatabaseHandler;
import com.jobPortal.library.MiscellaneousData;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.slidingmenu.MainActivity;
import com.jobPortal.slidingmenu.Notifications;
import com.jobPortal.slidingmenu.R;

public class CompanyHomeActivity extends Activity {

	TextView user_alias;
	Dialog dialogbox;
	Company company;
	String msg;
	TextView user_status;
	ProgressBar loading;
	Button btn_student,btn_notification;

	ArrayList<NotificationBean> notifications;
	Set<NotificationBean> note;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_ACTIVATION = "activation";
	private static String KEY_COURSE = "course";
	private static String KEY_TYPE = "cmny_type";
	private static String KEY_DATE_OF_PLACEMENT = "date_of_placement";
	private static String KEY_SALARY_PACKAGE = "salary_package";
	private static String KEY_CREATED_AT = "created_at";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_main);
		
		user_status = (TextView) findViewById(R.id.textViewCmnyStatus);
		loading = (ProgressBar) findViewById(R.id.progressBar_company);
		btn_student = (Button) findViewById(R.id.btnApplytoStudent);
		
		
		notifications = new ArrayList<NotificationBean>();
		

		// tacking action based on stored user
		if (!Common.STUDENT_EMAIL.isEmpty()) {
			new GetUserDetailsTask().execute();
		} else {
			user_status.setTextColor(Color.RED);
			user_status.setText("Expired");
			Toast.makeText(getApplicationContext(),
					"Session expired, re-login the user !! ", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.company_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		MiscellaneousData data = new MiscellaneousData();
		data.logoutActivity(CompanyHomeActivity.this, "COMPANY");

	}

	public void updateUser(View view) {
		if (!Common.STUDENT_EMAIL.isEmpty()) {
			Intent dashboard = new Intent(getApplicationContext(),
					CompanyProfileActivity.class);

			// Close all views before launching Dashboard
			dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			dashboard.putExtra("company", company);
			startActivity(dashboard);
		}
	}

	public void showNotifications(View view) {
		if (note.size() > 0) {
			Intent notificationList = new Intent(CompanyHomeActivity.this, Notifications.class);
			notificationList.putExtra("notificationList", notifications);
			startActivity(notificationList);
		} else {
			Toast.makeText(getApplicationContext(),
					"Notifications are unavailable !!",
					Toast.LENGTH_LONG).show();
		}
	}

	public void saveSearchFilter(View view) {
		if (!Common.STUDENT_EMAIL.isEmpty()) {
			Intent dashboard = new Intent(getApplicationContext(),
					FilterForStudentsActivity.class);

			// Close all views before launching Dashboard
			dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			dashboard.putExtra("company", company);
			startActivity(dashboard);
		}
	}

	public void showStudentsList(View view) {
		new StudentList().execute();
	}

	public void logout(View view) {
		MiscellaneousData data = new MiscellaneousData();
		data.logoutActivity(CompanyHomeActivity.this, "COMPANY");
	}

	public void logoutAndQuit(View view) {

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.resetTables();

		// TODO Auto-generated method stub
		dialogbox = new Dialog(this);
		dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogbox.setContentView(R.layout.custom_dialog_exit);
		dialogbox.setTitle("");
		dialogbox.setCancelable(true);

		Button yes = (Button) dialogbox.findViewById(R.id.mYesExitDialog);
		Button no = (Button) dialogbox.findViewById(R.id.mNoExitDialog);
		yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(CompanyHomeActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("Exit me", true);
				startActivity(intent);
				finish();

			}
		});
		no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialogbox.dismiss();

			}
		});
		dialogbox.show();

	}

	class GetUserDetailsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {
			// code where data is processing
			UserFunctions userFunction = new UserFunctions();
			try {
				JSONObject json = userFunction.getUserdetailForUpdation(
						Common.STUDENT_EMAIL, "company");
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// Fill Student Object
						JSONObject json_user = json.getJSONObject("user");
						company = new Company();
						company.setName(json_user.getString(KEY_NAME));
						company.setEmail(json_user.getString(KEY_EMAIL));
						company.setCourse(json_user.getString(KEY_COURSE));
						company.setActivation(json_user
								.getString(KEY_ACTIVATION));
						company.setType(json_user.getString(KEY_TYPE));
						company.setDate_of_placement(json_user
								.getString(KEY_DATE_OF_PLACEMENT));
						company.setSalary_package(json_user
								.getString(KEY_SALARY_PACKAGE));
						company.setCreated_at(json_user
								.getString(KEY_CREATED_AT));
						company.setSelected(false);
						msg = "user details are available now !!";
						getAllNotifications();
					} else {
						// Error in login
						msg = "User details not found !!";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "Connection Error:" + e.toString();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			user_alias = (TextView) findViewById(R.id.textViewCompanyUser);
			user_alias.setText("Welcome " + company.getName());
			user_status.setTextColor(Color.GREEN);
			user_status.setText("Active");
			loading.setIndeterminate(false);
			Log.e("Company Home Screen Error", msg);
			
			Common.COMPANY_EMAIL = company.getEmail();
			
			//put list => set
			note = new HashSet<NotificationBean>(notifications);
			btn_notification = (Button) findViewById(R.id.btnNotificationCompany);
			btn_notification.setText("Notifications ("+note.size()+")");
		}

		@Override
		protected void onPreExecute() {
			user_status.setText("Loading ...");
			loading.setIndeterminate(true);
		}

	}

	class StudentList extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			if (company != null) {
				try {
					UserFunctions functions = new UserFunctions();
					JSONObject json = functions.getAllAvailableStudents();
					JSONArray menuitemArray = json.getJSONArray("userDetails");
					ArrayList<StudentBean> students = new ArrayList<StudentBean>();
					for (int i = 0; i < menuitemArray.length(); i++) {
						JSONObject row = menuitemArray.getJSONObject(i);
						StudentBean student = new StudentBean();
						student.setStudent_id(row.getString(Common.STUDENT_ID));
						student.setRoll_no(row
								.getString(Common.STUDENT_ROLL_NO));
						student.setSection(row
								.getString(Common.STUDENT_SECTION));
						student.setFirstName(row
								.getString(Common.STUDENT_FIRST_NAME));
						student.setMiddleName(row
								.getString(Common.STUDENT_MIDDLE_NAME));
						student.setLastName(row
								.getString(Common.STUDENT_LAST_NAME));
						student.setDate_of_birth(row
								.getString(Common.STUDENT_DATE_OF_BIRTH));
						student.setGender(row.getString(Common.STUDENT_GENDER));
						student.setBranch(row.getString(Common.STUDENT_BRANCH));
						student.setSemester(row
								.getString(Common.STUDENT_SEMESTER));
						student.setSscMarks(row
								.getString(Common.STUDENT_SSC_MARKS));
						student.setHscMarks(row
								.getString(Common.STUDENT_HSC_MARKS));
						student.setDiplomaMarks(row
								.getString(Common.STUDENT_DIPLOMA_MARKS));
						student.setEnggMarks(row
								.getString(Common.STUDENT_ENGG_MARKS));
						student.setBacklogHistory(row
								.getString(Common.STUDENT_BACKLOG_HISTORY));
						student.setIsBacklogLive(row
								.getString(Common.STUDENT_IS_BACKLOGLIVE));
						student.setIsGapInEducation(row
								.getString(Common.STUDENT_IS_GAPEDUCATION));
						student.setGap_details(row
								.getString(Common.STUDENT_GAP_DETAILS));
						student.setMobile_number(row
								.getString(Common.STUDENT_MOBILE_NUMBER));
						student.setEmail(row
								.getString(Common.STUDENT_RAISONI_EMAIL));
						student.setTemp_address(row
								.getString(Common.STUDENT_TEMP_ADDRESS));
						student.setPer_address(row
								.getString(Common.STUDENT_PER_ADDRESS));
						student.setCertification(row
								.getString(Common.STUDENT_CERTIFICATION));
						student.setProject_details(row
								.getString(Common.STUDENT_PROJECT_DETAILS));
						student.setIsActive(row
								.getString(Common.STUDENT_IS_ACTIVE));
						student.setCreated_at(row
								.getString(Common.STUDENT_CREATED_AT));
						student.setSelected(false);
						student.setRegistered("Not Recruited");
						students.add(student);
					}
					if (students.size() > 0) {
						Intent companyList = new Intent(
								CompanyHomeActivity.this,
								StudentListCmnyActivity.class);
						companyList.putExtra("studentsList", students);
						companyList.putExtra("company", company);
						startActivity(companyList);
					} else {
						Toast.makeText(getApplicationContext(),
								"No-one registered yet !!", Toast.LENGTH_LONG)
								.show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			btn_student.setEnabled(true);
			loading.setIndeterminate(false);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			btn_student.setEnabled(false);
			loading.setIndeterminate(true);
		}

	}

	private void getAllNotifications() {

		if (company != null) {
			try {
				UserFunctions functions = new UserFunctions();
				JSONObject json = functions
						.getNotifications(company.getEmail());
				JSONArray menuitemArray = json.getJSONArray("userDetails");

				for (int i = 0; i < menuitemArray.length(); i++) {
					JSONObject row = menuitemArray.getJSONObject(i);
					NotificationBean notification = new NotificationBean();
					notification.setFrom_user(row
							.getString(Common.NOTIFICATION_FROM_USER));
					notification.setType_user(row
							.getString(Common.NOTIFICATION_TYPE_USER));
					notification.setMessage(row
							.getString(Common.NOTIFICATION_MESSAGE));
					notification.setCreated_at(row
							.getString(Common.NOTIFICATION_CREATED_AT));
					if (i == 0) {
						notifications.add(notification);
					} else {
						for (NotificationBean nt : notifications) {
							if (nt != null) {
								if (!(nt.getFrom_user().equalsIgnoreCase(
										notification.getFrom_user()))
										&& !(nt.getMessage()
												.equalsIgnoreCase(
														notification
																.getMessage()))) {
									notifications.add(notification);
								}
							}
						}
					}
					
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
