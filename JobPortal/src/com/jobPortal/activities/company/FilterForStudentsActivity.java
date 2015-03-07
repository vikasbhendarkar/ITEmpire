package com.jobPortal.activities.company;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jobPortal.bean.Company;
import com.jobPortal.bean.SearchFilterForCompany;
import com.jobPortal.library.Common;
import com.jobPortal.library.DatabaseHandler;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.slidingmenu.R;

public class FilterForStudentsActivity extends Activity {

	Company company;
	private EditText ssc_marks;
	private EditText hsc_marks;
	private EditText diploma_marks;
	private EditText be_marks;
	DatabaseHandler db;
	boolean isFilterUpdatable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_for_students);
		isFilterUpdatable = false;
		company = new Company();
		company = (Company) getIntent().getSerializableExtra("company");
		db = new DatabaseHandler(getApplicationContext());
		findViewById();
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		ssc_marks = (EditText) findViewById(R.id.editTextSSCPercent);
		hsc_marks = (EditText) findViewById(R.id.editTextHSCPercent);
		diploma_marks = (EditText) findViewById(R.id.editTextDiplomaPercent);
		be_marks = (EditText) findViewById(R.id.editTextBEPercent);
			UserFunctions userFunction = new UserFunctions();
			try {
				JSONObject json = userFunction.getFilterDetailsCompanyUsingEmail(company.getEmail());
				if (json.getString(Common.KEY_SUCCESS) != null) {
					String res = json.getString(Common.KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// Fill Student Object
						isFilterUpdatable = true;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter_for_students, menu);
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

	public void saveFilterDetails(View view) {
		String ssc = ssc_marks.getText().toString();
		String hsc = hsc_marks.getText().toString();
		String diploma = diploma_marks.getText().toString();
		String be = be_marks.getText().toString();
		boolean isValidationSuccess = true;
		if (ssc.isEmpty()
				|| (Float.parseFloat(ssc) < 0 || Float.parseFloat(ssc) > 100)) {
			ssc_marks.setError("Invalid percentage");
			isValidationSuccess = false;
		}
		if (hsc.isEmpty()
				|| (Float.parseFloat(hsc) < 0 || Float.parseFloat(hsc) > 100)) {
			hsc_marks.setError("Invalid percentage");
			isValidationSuccess = false;
		}
		if (diploma.isEmpty()
				|| (Float.parseFloat(diploma) < 0 || Float.parseFloat(diploma) > 100)) {
			diploma_marks.setError("Invalid percentage");
			isValidationSuccess = false;
		}
		if (be.isEmpty()
				|| (Float.parseFloat(be) < 0 || Float.parseFloat(be) > 10)) {
			be_marks.setError("Invalid percentage");
			isValidationSuccess = false;
		}
		if (isValidationSuccess) {
			SearchFilterForCompany filter = new SearchFilterForCompany();
			filter.setEmail(company.getEmail());
			filter.setSsc_marks(Float.parseFloat(ssc));
			filter.setHsc_marks(Float.parseFloat(hsc));
			filter.setDiploma_marks(Float.parseFloat(diploma));
			filter.setBe_marks(Float.parseFloat(be));
			UserFunctions functions = new UserFunctions();
			try {
				JSONObject json = null;
				if (isFilterUpdatable) {
					json = functions.addFilter(filter, "update");
					if (json.getString(Common.KEY_SUCCESS) != null) {
						String res = json.getString(Common.KEY_SUCCESS);
						if (Integer.parseInt(res) == 1) {
							Toast.makeText(getApplicationContext(),
									"Updated successfully", Toast.LENGTH_SHORT)
									.show();
						}
					}
				} else {

					json = functions.addFilter(filter, "create");
					if (json.getString(Common.KEY_SUCCESS) != null) {
						String res = json.getString(Common.KEY_SUCCESS);
						if (Integer.parseInt(res) == 1) {
							Toast.makeText(getApplicationContext(),
									"Save successfully", Toast.LENGTH_SHORT)
									.show();
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}
}
