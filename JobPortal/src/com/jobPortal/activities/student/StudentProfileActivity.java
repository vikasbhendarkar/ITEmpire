package com.jobPortal.activities.student;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.jobPortal.bean.StudentBean;
import com.jobPortal.library.DatabaseHandler;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.library.Validation;
import com.jobPortal.slidingmenu.R;

public class StudentProfileActivity extends Activity {

	UserFunctions userFunctions;
	Context context;
	DatabaseHandler db;
	StudentBean student = new StudentBean();
	private EditText contact_number;
	private EditText temp_address;
	private EditText permanant_address;
	private EditText certification;
	private EditText project_details;
	private CheckBox isSameAsTemp;
	private CheckBox isOptedOut;
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_FAILURE_MSG = "error_msg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_profile_new);
		context = getApplicationContext();
		db = new DatabaseHandler(context);
		student = (StudentBean) getIntent().getSerializableExtra("student");		
		findViewById();
	}

	private void findViewById() {
		contact_number = (EditText) findViewById(R.id.editTextContactNumber);
		if (student.getMobile_number() != null) {
			contact_number.setText(student.getMobile_number());
		}

		temp_address = (EditText) findViewById(R.id.editTextTempAddress);
		if (student.getTemp_address() != null) {
			temp_address.setText(student.getTemp_address());
		}

		permanant_address = (EditText) findViewById(R.id.editTextPermanantAddress);
		if (student.getPer_address() != null) {
			permanant_address.setText(student.getPer_address());
		}

		certification = (EditText) findViewById(R.id.editTextCertification);
		if (student.getCertification() != null) {
			certification.setText(student.getCertification());
		}

		project_details = (EditText) findViewById(R.id.editTextProjectDetails);
		if (student.getProject_details() != null) {
			project_details.setText(student.getProject_details());
		}

		isSameAsTemp = (CheckBox) findViewById(R.id.checkBoxIsSameAsTemp);
		isSameAsTemp
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							permanant_address.setText(temp_address.getText());
							permanant_address.setEnabled(false);
						} else {
							permanant_address.setText(student.getPer_address());
							permanant_address.setEnabled(true);

						}
					}
				});
		
		isOptedOut = (CheckBox) findViewById(R.id.checkBoxIsOptedOut);
		isOptedOut.setChecked(Boolean.parseBoolean(student.getIsActive()));
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_info, menu);
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
		Toast.makeText(context, "First complete this profile !!",
				Toast.LENGTH_SHORT).show();
	}

	public void completeStudentProfile(View view) {

		student.getEmail();
//		StudentBean student = new StudentBean();
//		student.setEmail(STUDENT_EMAIL);
		student.setTemp_address(temp_address.getText().toString());
		student.setPer_address(permanant_address.getText().toString());
		student.setCertification(certification.getText().toString());
		student.setProject_details(project_details.getText().toString());

		String mobile = contact_number.getText().toString();
		if (mobile.isEmpty() || !Validation.isValidContactNumber(mobile)) {
			contact_number.setError("Enter valid 10 digit number !!");
			return;
		}
		student.setMobile_number(mobile);
		String status = isOptedOut.isChecked()+"".trim();
		student.setIsActive(status);
		try {
			UserFunctions function = new UserFunctions();
			JSONObject json = function.updateStudentProfile(student);
			if (json.getString(KEY_SUCCESS) != null
					&& json.getString(KEY_SUCCESS).equals("1")) {
				Intent intent = new Intent(this, StudentHomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("student", student);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(context, json.getString(KEY_FAILURE_MSG),
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
