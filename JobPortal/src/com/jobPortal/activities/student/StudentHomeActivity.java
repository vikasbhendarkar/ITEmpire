package com.jobPortal.activities.student;

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

public class StudentHomeActivity extends Activity {

	TextView user_alias;
	Dialog dialogbox;
	StudentBean student;
	TextView user_status;
	ProgressBar loading;
	Button btn_applyCompany, btn_notification;
	String msg;
	ArrayList<NotificationBean> notifications;
	Set<NotificationBean> note;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_home);

		// set student details
		student = (StudentBean) getIntent().getSerializableExtra("student");
		user_alias = (TextView) findViewById(R.id.textViewStudentUser);
		user_status = (TextView) findViewById(R.id.textViewStatus);
		loading = (ProgressBar) findViewById(R.id.progressBar_student);
		btn_applyCompany = (Button) findViewById(R.id.btnApplytoCompany);

		notifications = new ArrayList<NotificationBean>();

		if (student != null) {
			new GetAllNotifications().execute();
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
		getMenuInflater().inflate(R.menu.student_home, menu);
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
		data.logoutActivity(StudentHomeActivity.this, "STUDENT");
	}

	public void updateUser(View view) {
		if (student != null) {
			Intent dashboard = new Intent(getApplicationContext(),
					StudentProfileActivity.class);

			// Close all views before launching Dashboard
			dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			dashboard.putExtra("student", student);
			startActivity(dashboard);
		}
	}

	public void showNotifications(View view) {
		if (note.size() > 0) {
			Intent notificationList = new Intent(StudentHomeActivity.this,
					Notifications.class);
			notificationList.putExtra("notificationList", notifications);
			startActivity(notificationList);
		} else {
			Toast.makeText(getApplicationContext(),
					"Notifications are unavailable !!", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void showCompanyList(View view) {
		new CompanyList().execute();
	}

	public void logout(View view) {
		MiscellaneousData data = new MiscellaneousData();
		data.logoutActivity(StudentHomeActivity.this, "STUDENT");
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
				Intent intent = new Intent(StudentHomeActivity.this,
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

	class CompanyList extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			if (student != null) {
				try {
					UserFunctions functions = new UserFunctions();
					JSONObject json = functions.getAllAvailableCompanies();
					JSONArray menuitemArray = json.getJSONArray("userDetails");
					ArrayList<Company> companies = new ArrayList<Company>();
					for (int i = 0; i < menuitemArray.length(); i++) {
						JSONObject row = menuitemArray.getJSONObject(i);
						Company company = new Company();
						company.setName(row.getString("name"));
						company.setEmail(row.getString("email"));
						company.setCreated_at(row.getString("created_at"));
						company.setActivation(row.getString("activation"));
						if (row.getString("type") != null) {
							company.setType(row.getString("type"));
						}
						if (row.getString("course") != null) {
							company.setCourse(row.getString("course"));
						}
						if (row.getString("date_of_placement") != null) {
							company.setDate_of_placement(row
									.getString("date_of_placement"));
						}
						if (row.getString("salary_package") != null) {
							company.setSalary_package(row
									.getString("salary_package"));
						}
						company.setSelected(false);
						company.setRegistered("Not Registered");
						companies.add(company);
					}
					if (companies.size() > 0) {
						Intent companyList = new Intent(
								StudentHomeActivity.this, JobListActivity.class);
						companyList.putExtra("companiesList", companies);
						companyList.putExtra("student", student);
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
			loading.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading.setVisibility(View.VISIBLE);
		}

	}

	class GetAllNotifications extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			if (student != null) {
				try {
					UserFunctions functions = new UserFunctions();
					JSONObject json = functions.getNotifications(student
							.getEmail());
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

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			user_alias.setText("Welcome " + student.getFirstName());
			user_status.setTextColor(Color.GREEN);
			user_status.setText("Active");
			Common.STUDENT_EMAIL = student.getEmail();

			// put list => set
			note = new HashSet<NotificationBean>(notifications);
			btn_notification = (Button) findViewById(R.id.btnNotificationStudent);
			btn_notification.setText("Notifications (" + note.size() + ")");
			loading.setIndeterminate(false);
		}

		@Override
		protected void onPreExecute() {
			user_status.setText("Loading ...");
			loading.setIndeterminate(true);
		}

	}

}
