package com.jobPortal.activities.admin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jobPortal.bean.Company;
import com.jobPortal.bean.StudentBean;
import com.jobPortal.library.Common;
import com.jobPortal.library.DatabaseHandler;
import com.jobPortal.library.MiscellaneousData;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.slidingmenu.MainActivity;
import com.jobPortal.slidingmenu.R;

public class AdminHomeActivity extends Activity {

	Dialog dialogbox;
	UserFunctions functions;
	Context context;
	DatabaseHandler db;
	Intent login;
	Intent studentList;
	Intent companyList;
	ProgressBar progress;
	EditText notification_data;
	Button btn_cmny, btn_stud, btn_notification;
	CheckBox chk_cmny, chk_stud;
	TextView txt_message;
	String message;
	boolean isCmnyChecked = false;
	boolean isStudChecked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_home);
		functions = new UserFunctions();
		context = getApplicationContext();

		progress = (ProgressBar) findViewById(R.id.progressBarAdmin);
		progress.setMax(100);
		progress.setProgress(0);

		btn_cmny = (Button) findViewById(R.id.btnCompanyView);
		btn_stud = (Button) findViewById(R.id.btnStudentView);
		btn_notification = (Button) findViewById(R.id.buttonSendNotifications);
		notification_data = (EditText) findViewById(R.id.editTextSendNotificationToAll);
		
		txt_message = (TextView) findViewById(R.id.textViewStatusAdmin);
		
		chk_cmny = (CheckBox) findViewById(R.id.checkBoxCompany);
		chk_cmny.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				isCmnyChecked = isChecked;
			}
		});

		chk_stud = (CheckBox) findViewById(R.id.checkBoxStudent);
		chk_stud.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				isStudChecked = isChecked;
			}
		});

		db = new DatabaseHandler(context);
		login = new Intent(context, MainActivity.class);
		login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_home, menu);
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

	public void sendNotification(View view) {
		new SendNotifications().execute();
	}

	public void showStudentList(View view) {
		new ShowStudentData().execute();
	}

	public void showCompanyList(View view) {
		new ShowCompanyData().execute();
	}

	@Override
	public void onBackPressed() {

		MiscellaneousData data = new MiscellaneousData();
		data.logoutActivity(AdminHomeActivity.this, "ADMIN");
	}

	class SendNotifications extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			try {
				UserFunctions userFunction = new UserFunctions();
				if (notification_data.getText().toString() != null && (isCmnyChecked || isStudChecked)) {
					JSONObject jsn = userFunction.registerUser(notification_data.getText().toString(),
							isCmnyChecked + "".trim(), isStudChecked + "".trim(),
							"storeNotificationFromAdmin");
					progress.setProgress(50);
					Thread.sleep(1000);
					String response = jsn.getString(Common.KEY_SUCCESS);
					if (Integer.parseInt(response) == 1) {
						message="notifications has been sent !!";
					} else {
						message="sending notifications failed !!";
					}
				}
			} catch (Exception e) {
				message="sending notifications failed !!";
			}
		
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			txt_message.setText(message);
			progress.setProgress(100);
			btn_notification.setEnabled(true);
		}

		@Override
		protected void onPreExecute() {
			txt_message.setText("");
			progress.setProgress(10);
			btn_notification.setEnabled(false);
		}

	}
	
	class ShowCompanyData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			try {
				JSONObject json = functions.getAllvaluesFromDatabase("users",
						"company");
				progress.setProgress(50);
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
					companies.add(company);
				}
				progress.setProgress(80);
				Thread.sleep(2000);

				if (companies.size() > 0) {
					companyList = new Intent(AdminHomeActivity.this,
							CompanyListActivity.class);
					companyList.putExtra("companiesList", companies);
					startActivity(companyList);
				} else {
					Toast.makeText(getApplicationContext(),
							"No-one registered yet !!", Toast.LENGTH_LONG)
							.show();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progress.setProgress(100);
			btn_cmny.setEnabled(true);
		}

		@Override
		protected void onPreExecute() {
			progress.setProgress(10);
			txt_message.setText("");
			btn_cmny.setEnabled(false);
		}

	}

	class ShowStudentData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			try {
				UserFunctions functions = new UserFunctions();
				JSONObject json = functions.getAllAvailableStudents();
				progress.setProgress(50);
				JSONArray menuitemArray = json.getJSONArray("userDetails");
				ArrayList<StudentBean> students = new ArrayList<StudentBean>();
				for (int i = 0; i < menuitemArray.length(); i++) {
					JSONObject row = menuitemArray.getJSONObject(i);
					StudentBean student = new StudentBean();
					student.setStudent_id(row.getString(Common.STUDENT_ID));
					student.setRoll_no(row.getString(Common.STUDENT_ROLL_NO));
					student.setSection(row.getString(Common.STUDENT_SECTION));
					student.setFirstName(row
							.getString(Common.STUDENT_FIRST_NAME));
					student.setMiddleName(row
							.getString(Common.STUDENT_MIDDLE_NAME));
					student.setLastName(row.getString(Common.STUDENT_LAST_NAME));
					student.setDate_of_birth(row
							.getString(Common.STUDENT_DATE_OF_BIRTH));
					student.setGender(row.getString(Common.STUDENT_GENDER));
					student.setBranch(row.getString(Common.STUDENT_BRANCH));
					student.setSemester(row.getString(Common.STUDENT_SEMESTER));
					student.setSscMarks(row.getString(Common.STUDENT_SSC_MARKS));
					student.setHscMarks(row.getString(Common.STUDENT_HSC_MARKS));
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
					student.setIsActive(row.getString(Common.STUDENT_IS_ACTIVE));
					student.setCreated_at(row
							.getString(Common.STUDENT_CREATED_AT));
					students.add(student);
				}

				progress.setProgress(80);
				Thread.sleep(2000);

				if (students.size() > 0) {
					Intent companyList = new Intent(AdminHomeActivity.this,
							StudentListActivity.class);
					companyList.putExtra("studentsList", students);
					startActivity(companyList);
				} else {
					Toast.makeText(getApplicationContext(),
							"No-one registered yet !!", Toast.LENGTH_LONG)
							.show();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progress.setProgress(100);
			btn_stud.setEnabled(true);
		}

		@Override
		protected void onPreExecute() {
			progress.setProgress(10);
			txt_message.setText("");
			btn_stud.setEnabled(false);
		}

	}
}
