package com.jobPortal.activities.student;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jobPortal.bean.Company;
import com.jobPortal.library.Common;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.slidingmenu.R;

public class CompanyDetail extends Activity {

	private TextView name;
	private TextView email;
	private TextView cmny_type;
	private TextView cmny_qualification;
	private TextView date_of_placement;
	private TextView salary_package;
	private TextView ssc_marks;
	private TextView hsc_marks;
	private TextView diploma_marks;
	private TextView be_marks;

	Company company;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_detail);
		findViewById();
		company = new Company();
		company = (Company) getIntent().getSerializableExtra("company");
		fillCompanyDetails(company);
	}

	private void fillCompanyDetails(Company cmny) {
		// TODO Auto-generated method stub
		name.setText(cmny.getName());
		email.setText(cmny.getEmail());
		cmny_type.setText(cmny.getType());
		cmny_qualification.setText(cmny.getCourse());
		date_of_placement.setText(cmny.getDate_of_placement());
		salary_package.setText(cmny.getSalary_package());
		UserFunctions userFunction = new UserFunctions();
			try {
				JSONObject json = userFunction.getFilterDetailsCompanyUsingEmail(cmny.getEmail());
				if (json.getString(Common.KEY_SUCCESS) != null) {
					String res = json.getString(Common.KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// Fill Student Object
						JSONObject json_user = json.getJSONObject("user");
						ssc_marks.setText(json_user.getString("ssc"));
						hsc_marks.setText(json_user.getString("hsc"));
						diploma_marks.setText(json_user.getString("diploma"));
						be_marks.setText(json_user.getString("be"));
						
					} 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		name = (TextView) findViewById(R.id.textViewCompanyName);
		email = (TextView) findViewById(R.id.textViewCompanyEmail);
		cmny_type = (TextView) findViewById(R.id.textViewCompanyType);
		cmny_qualification = (TextView) findViewById(R.id.textViewCompanyQualification);
		date_of_placement = (TextView) findViewById(R.id.textViewDOP);
		salary_package = (TextView) findViewById(R.id.textViewSalary_package);
		ssc_marks = (TextView) findViewById(R.id.textViewComnySSCPercent);
		hsc_marks = (TextView) findViewById(R.id.textViewComnyHSCPercent);
		diploma_marks = (TextView) findViewById(R.id.textViewCmnyDiplomaPercent);
		be_marks = (TextView) findViewById(R.id.textViewCmnyBEAggregate);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.company_detail, menu);
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
}
