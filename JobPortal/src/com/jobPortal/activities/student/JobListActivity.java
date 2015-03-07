package com.jobPortal.activities.student;

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

public class JobListActivity extends Activity implements
		OnItemLongClickListener {

	private ListView available_companies;
	private ArrayList<Object> companies = null;
	private ArrayList<Company> appliedCompanyList = null;
	ArrayList<Company> cmnies;
	StudentBean student;
	Button apply_job;
	boolean isFiltered = false;
	String message;
	CustomJobforStudentAdapter adapter;
	ProgressBar loading;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_list);

		appliedCompanyList = new ArrayList<Company>();
		cmnies = new ArrayList<Company>();
		apply_job = (Button) findViewById(R.id.apply_job);
		apply_job.setEnabled(false);
		loading = (ProgressBar) findViewById(R.id.progressBar_job_list);

		// get list for companies
		available_companies = (ListView) findViewById(R.id.available_joblist);

		student = new StudentBean();
		student = (StudentBean) getIntent().getSerializableExtra("student");

		companies = new ArrayList<Object>();
		companies = (ArrayList<Object>) getIntent().getSerializableExtra(
				"companiesList");
		adapter = new CustomJobforStudentAdapter(this, companies);
		available_companies.setAdapter(adapter);
		available_companies.setPadding(10, 20, 10, 15);
		available_companies.setFooterDividersEnabled(true);
		available_companies.setHeaderDividersEnabled(true);
		available_companies.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.job_list, menu);
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

	public void applyForJobs(View view) {
		new ApplyForCompany().execute();
	}

	public void applyFilter(MenuItem item) {
		new ApplyFilter().execute();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		Company company = (Company) parent.getItemAtPosition(position);
		Intent companyInfo = new Intent(this, CompanyDetail.class);
		companyInfo.putExtra("company", company);
		startActivity(companyInfo);
		return true;
	}

	class ApplyForCompany extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			try {
				UserFunctions userFunction = new UserFunctions();
				for (Company appCmny : appliedCompanyList) {
					if (appCmny.isSelected()) {
						JSONObject jsn = userFunction.registerUser(
								appCmny.getEmail(), student.getEmail(),
								appCmny.isSelected() + "".trim(),
								"applyForCompany");
						String response = jsn.getString(Common.KEY_SUCCESS);
						if (Integer.parseInt(response) == 1) {
							message = "company registration successfull !!";
						} else {
							message = "company registration failed !!";
							break;
						}
					}
				}
			} catch (Exception e) {
				message = "company registration failed !!";
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!message.isEmpty()) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {

		}
	}

	class ApplyFilter extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			// TODO Auto-generated method stub
			SearchFilterForCompany filter = null;
			double ssc_m = Double.parseDouble(student.getSscMarks()
					.replace("%", "").trim());
			double hsc_m = 110.0;
			if (!(student.getHscMarks().equals(""))) {
				hsc_m = Double.parseDouble(student.getHscMarks()
						.replace("%", "").trim());
			}
			double diploma_m = 110.0;
			if (!(student.getDiplomaMarks().equals(""))) {
				diploma_m = Double.parseDouble(student.getDiplomaMarks()
						.replace("%", "").trim());
			}
			double be_m = 0.0;
			if (!(student.getEnggMarks().equals(""))) {
				be_m = Double.parseDouble(student.getEnggMarks()
						.replace("%", "").trim());
			}
			for (Object kk : companies) {
				cmnies.add((Company) kk);
			}
			for (Company company : cmnies) {

				UserFunctions userFunction = new UserFunctions();
				try {
					JSONObject json = userFunction
							.getFilterDetailsCompanyUsingEmail(company
									.getEmail());
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
							filter.setDiploma_marks(Double
									.parseDouble(json_user.getString("diploma")));
							filter.setBe_marks(Double.parseDouble(json_user
									.getString("be")));

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (filter != null) {
					if (ssc_m < filter.getSsc_marks() && (hsc_m < filter.getHsc_marks() || diploma_m < filter.getDiploma_marks()) && be_m < filter.getBe_marks()) {
						companies.remove(company);
						continue;
					} 
				}
					
			}
			appliedCompanyList.clear();
			for (Object kk : companies) {
				UserFunctions userFunction = new UserFunctions();
				try {
					JSONObject json = userFunction.loginUser(
							((Company) kk).getEmail(), student.getEmail(),
							"getRegistrationStatus");
					if (json.getString(Common.KEY_SUCCESS) != null) {
						String res = json.getString(Common.KEY_SUCCESS);
						if (Integer.parseInt(res) == 1) {
							boolean isApplied = Boolean.parseBoolean(json
									.getString("isApplied"));
							if (isApplied) {
								((Company) kk).setRegistered("Registered");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					message = "Error while getting registration value";
				}
				appliedCompanyList.add((Company) kk);
			}
			if (appliedCompanyList.size() > 0) {
				message = "success";
			} else {
				message = "No more jobs are suitable for your profile";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.notifyDataSetChanged();
			loading.setIndeterminate(false);
			available_companies.setEnabled(true);
			if (!message.isEmpty()) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}
			apply_job.setEnabled(true);
			isFiltered = true;
			message = "";
		}

		@Override
		protected void onPreExecute() {
			loading.setIndeterminate(true);
			available_companies.setEnabled(false);
		}
	}
}
