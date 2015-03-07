package com.jobPortal.activities.admin;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.jobPortal.bean.Company;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.slidingmenu.R;
import com.jobPortal.slidingmenu.adapter.CustomCompanyAdapter;

public class CompanyListActivity extends Activity implements
		OnItemClickListener, OnItemLongClickListener {

	private ListView registered_companies;
	private ArrayList<Company> companies = null;
	CustomCompanyAdapter adapter;
	UserFunctions userFunctions;
	String statusChanges = "";

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_FAILURE_MSG = "error_msg";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_company_list);

		// get list for students
		registered_companies = (ListView) findViewById(R.id.listView_companies_list);
		companies = new ArrayList<Company>();
		companies = (ArrayList<Company>) getIntent().getSerializableExtra(
				"companiesList");
		adapter = new CustomCompanyAdapter(this, companies);
		registered_companies.setAdapter(adapter);
		registered_companies.setPadding(10, 20, 10, 15);
		registered_companies.setFooterDividersEnabled(true);
		registered_companies.setHeaderDividersEnabled(true);
		registered_companies.setOnItemClickListener(this);
		registered_companies.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.company_list, menu);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Company company = (Company) parent.getItemAtPosition(position);
		// customView = adapter.getView(position, view, parent);
		final Context context = view.getContext();
		final String email = company.getEmail();
		Switch act_switch = (Switch) view.findViewById(R.id.active_btn);
		act_switch.setEnabled(true);
		act_switch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						statusChanges = isChecked+"";
						userFunctions = new UserFunctions();
						try {
							JSONObject json = userFunctions
									.updateActivationStatus(statusChanges,
											email);
							if (json.getString(KEY_SUCCESS) != null
									&& json.getString(KEY_SUCCESS).equals("1")) {
								Toast.makeText(context, "Updation Success.",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(context,
										json.getString(KEY_FAILURE_MSG),
										Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		Company company = (Company) parent.getItemAtPosition(position);
		if (statusChanges != null && !statusChanges.isEmpty()) {
			company.setActivation(statusChanges);
		}
		Intent companyInfo = new Intent(this, CompanyInfoActivity.class);
		companyInfo.putExtra("company", company);
		startActivity(companyInfo);
		return true;
	}
}
