package com.jobPortal.activities.company;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jobPortal.bean.Company;
import com.jobPortal.bean.SearchFilterForCompany;
import com.jobPortal.bean.StudentBean;
import com.jobPortal.library.Common;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.slidingmenu.R;
import com.jobPortal.slidingmenu.adapter.CustomJobforStudentAdapter;

public class StudentListCmnyActivity extends Activity implements
		OnItemLongClickListener {

	private ListView available_students;
	private ArrayList<Object> students = null;
	private ArrayList<StudentBean> appliedStudentList = null;
	CustomJobforStudentAdapter adapter;
	Company company;
	ProgressBar loading;
	Button btn_apply_for_student;
	String message = "";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_list);

		// get list for companies
		appliedStudentList = new ArrayList<StudentBean>();
		company = new Company();
		available_students = (ListView) findViewById(R.id.available_studentlist);
		students = new ArrayList<Object>();
		loading = (ProgressBar) findViewById(R.id.progressBar_student_list);
		btn_apply_for_student = (Button) findViewById(R.id.apply_stud_filter);
		btn_apply_for_student.setEnabled(false);

		company = (Company) getIntent().getSerializableExtra("company");
		students = (ArrayList<Object>) getIntent().getSerializableExtra(
				"studentsList");
		adapter = new CustomJobforStudentAdapter(this, students);
		available_students.setAdapter(adapter);
		available_students.setPadding(10, 20, 10, 15);
		available_students.setFooterDividersEnabled(true);
		available_students.setHeaderDividersEnabled(true);
		available_students.setOnItemLongClickListener(this);
		new EligibleStudentsTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_list, menu);
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

	public void applyStudents(View view) {
		new ApplyForStudent().execute();
	}

	public void applyFilter(MenuItem item) {
		new FilterTask().execute();
	}

//	public void filterApplicableStudents() {
//		new EligibleStudentsTask().execute();
//	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		StudentBean student = (StudentBean) parent.getItemAtPosition(position);

		Intent companyInfo = new Intent(this, StudentDetails.class);
		companyInfo.putExtra("student", student);
		startActivity(companyInfo);
		return true;
	}

	class FilterTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			ArrayList<StudentBean> student = new ArrayList<StudentBean>();
			for (Object kk : students) {
				student.add((StudentBean) kk);
			}

			for (StudentBean std : student) {
				UserFunctions userFunction = new UserFunctions();
				try {
					JSONObject json = userFunction.loginUser(
							company.getEmail(), std.getEmail(),
							"getRegistrationStatus");
					if (json.getString(Common.KEY_SUCCESS) != null) {
						String res = json.getString(Common.KEY_SUCCESS);
						if (Integer.parseInt(res) == 1) {
							boolean isApplied = Boolean.parseBoolean(json
									.getString("isApplied"));
							boolean isRecruited = Boolean.parseBoolean(json
									.getString("isRecruited"));
							if (isApplied && isRecruited) {
								std.setRegistered("Recruited");
							} else {
								std.setRegistered("Eligible");
							}
						} 
					}
				} catch (Exception e) {
					e.printStackTrace();
					message = "Error while getting registration value";
				}
				appliedStudentList.add(std);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			adapter.notifyDataSetChanged();
			if (appliedStudentList.size() > 0) {
				message = "success";
				btn_apply_for_student.setEnabled(true);
			} else {
				message = "students has yet to registered !!";
			}

			if (!message.isEmpty()) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}

			message = "";
			loading.setIndeterminate(false);
			available_students.setEnabled(true);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading.setIndeterminate(true);
			available_students.setEnabled(false);
		}

	}

	class EligibleStudentsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			SearchFilterForCompany filter = null;

			UserFunctions userFunction = new UserFunctions();
			try {
				JSONObject json = userFunction
						.getFilterDetailsCompanyUsingEmail(company.getEmail());
				if (json.getString(Common.KEY_SUCCESS) != null) {
					String res = json.getString(Common.KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// Fill Student Object
						JSONObject json_user = json.getJSONObject("user");
						filter = new SearchFilterForCompany();
						filter.setSsc_marks(Double.parseDouble(json_user
								.getString("ssc")));
						filter.setHsc_marks(Double.parseDouble(json_user
								.getString("hsc")));
						filter.setDiploma_marks(Double.parseDouble(json_user
								.getString("diploma")));
						filter.setBe_marks(Double.parseDouble(json_user
								.getString("be")));

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			ArrayList<StudentBean> studs = new ArrayList<StudentBean>();
			for (Object kk : students) {
				studs.add((StudentBean) kk);
			}

			for (StudentBean stud : studs) {

				double ssc_m = Double.parseDouble(stud.getSscMarks()
						.replace("%", "").trim());
				double hsc_m = 110.0;
				if (!(stud.getHscMarks().equals(""))) {
					hsc_m = Double.parseDouble(stud.getHscMarks()
							.replace("%", "").trim());
				}
				double diploma_m = 110.0;
				if (!(stud.getDiplomaMarks().equals(""))) {
					diploma_m = Double.parseDouble(stud.getDiplomaMarks()
							.replace("%", "").trim());
				}
				double be_m = 0.0;
				if (!(stud.getEnggMarks().equals(""))) {
					be_m = Double.parseDouble(stud.getEnggMarks()
							.replace("%", "").trim());
				}
				if (filter != null) {
					if (ssc_m < filter.getSsc_marks() && (hsc_m < filter.getHsc_marks() || diploma_m < filter.getDiploma_marks()) && be_m < filter.getBe_marks()) {
						students.remove(stud);
						continue;
					} 
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.notifyDataSetChanged();
			message = "List of eligible candidates";
			if (!message.isEmpty()) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}
			loading.setIndeterminate(false);
			available_students.setEnabled(true);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading.setIndeterminate(true);
			available_students.setEnabled(false);
		}

	}

	class ApplyForStudent extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			try {
				UserFunctions userFunction = new UserFunctions();
				for (StudentBean appStud : appliedStudentList) {
					if (appStud.isSelected()) {
						JSONObject jsn = userFunction.registerUser(
								company.getEmail(), appStud.getEmail(),
								appStud.isSelected() + "".trim(),
								"applyForStudent");
						String response = jsn.getString(Common.KEY_SUCCESS);
						//send notification to each student
						JSONObject notification = userFunction.registerUser("Congratulation, you are selected !! \nPlease send your resume to "+company.getEmail(),
								company.getEmail(), appStud.getEmail(),
								"storeNotificationFromCompany");
						String responseNotification = notification.getString(Common.KEY_SUCCESS);
						if (Integer.parseInt(response) == 1 && Integer.parseInt(responseNotification) == 1) {
							message = "student recruited successfull !!";
						} else {
							message = "student recruited failed !!";
							break;
						}
					}
				}
			} catch (Exception e) {
				message = "student recruited failed !!";
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			loading.setIndeterminate(false);
			available_students.setEnabled(true);
			if (!message.isEmpty()) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			loading.setIndeterminate(true);
			available_students.setEnabled(false);
		}
	}
}
