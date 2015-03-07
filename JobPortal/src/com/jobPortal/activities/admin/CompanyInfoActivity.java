package com.jobPortal.activities.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jobPortal.bean.Company;
import com.jobPortal.library.MiscellaneousData;
import com.jobPortal.slidingmenu.R;

public class CompanyInfoActivity extends Activity {

	private TextView name;
	private TextView email;
	private TextView cmny_type;
	private TextView cmny_qualification;
	private TextView date_of_placement;
	private TextView salary_package;
	private TextView registration_date;
	private TextView activation_status;
	Company company;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_company_info);

		findViewById();
		company = new Company();
		company = (Company) getIntent().getSerializableExtra("company");
		fillCompanyDetails(company);
	}

	private void fillCompanyDetails(Company cmny) {
		// TODO Auto-generated method stub
		name.setText(cmny.getName());
		email.setText(cmny.getEmail());
		registration_date.setText(cmny.getCreated_at());
		boolean status = Boolean.parseBoolean(cmny.getActivation());
		activation_status.setText(status ? "Activated" : "Deactivated");
		if (cmny.getType() != null) {
			cmny_type.setText(cmny.getType());
		}
		if (cmny.getCourse() != null) {
			cmny_qualification.setText(cmny.getCourse());
		}
		if (cmny.getDate_of_placement() != null) {
			date_of_placement.setText(cmny.getDate_of_placement());
		}
		if (cmny.getSalary_package() != null) {
			salary_package.setText(cmny.getSalary_package());
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
		registration_date = (TextView) findViewById(R.id.textViewCRegistrationDate);
		activation_status = (TextView) findViewById(R.id.textViewCActivationStatus);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.company_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		finish();
		return true;
	}

	public void deleteCompany(View view) {

		if (company != null) {
			MiscellaneousData data = new MiscellaneousData();
			data.deleteUser(this, company);

		}
	}
}
