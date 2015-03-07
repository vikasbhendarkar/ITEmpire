package com.jobPortal.activities.company;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jobPortal.bean.StudentBean;
import com.jobPortal.slidingmenu.R;

public class StudentDetails extends Activity {

	TextView name;
	TextView gender;
	TextView email;
	TextView date_of_birth;
	TextView branch;
	TextView semester;
	TextView marks_ssc;
	TextView marks_hsc;
	TextView marks_diploma;
	TextView marks_be;
	TextView backog_history;
	TextView live_backlogs;
	TextView gap_details;
	TextView address;
	TextView contact_number;
	TextView certification;
	TextView project_details;
	StudentBean student;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_details);
		student = (StudentBean) getIntent().getSerializableExtra("student");
		findViewById();
		if (student != null) {
			fillDetails();
		}
	}

	private void fillDetails() {
		// TODO Auto-generated method stub
		name.setText(student.getFirstName() + " " + student.getLastName());
		gender.setText(student.getGender());
		email.setText(student.getEmail());
		date_of_birth.setText(student.getDate_of_birth());
		branch.setText(student.getBranch());
		semester.setText(student.getSemester());
		marks_ssc.setText(student.getSscMarks());
		marks_hsc.setText(student.getHscMarks());
		marks_diploma.setText(student.getDiplomaMarks());
		marks_be.setText(student.getEnggMarks());
		backog_history.setText(student.getBacklogHistory());
		live_backlogs.setText(student.getIsBacklogLive());
		gap_details.setText(student.getIsGapInEducation());
		address.setText(student.getPer_address());
		contact_number.setText(student.getMobile_number());
		certification.setText(student.getCertification());
		project_details.setText(student.getProject_details());

	}

	private void findViewById() {
		// TODO Auto-generated method stub
		name = (TextView) findViewById(R.id.textViewStudName);
		gender = (TextView) findViewById(R.id.textViewStudGender);
		email = (TextView) findViewById(R.id.textViewStudEmail);
		date_of_birth = (TextView) findViewById(R.id.textViewStudDOB);
		branch = (TextView) findViewById(R.id.textViewStudBranch);
		semester = (TextView) findViewById(R.id.textViewStudSemester);
		marks_ssc = (TextView) findViewById(R.id.textViewStudSSC);
		marks_hsc = (TextView) findViewById(R.id.textViewStudHSC);
		marks_diploma = (TextView) findViewById(R.id.textViewStudDiploma);
		marks_be = (TextView) findViewById(R.id.textViewStudBE);
		backog_history = (TextView) findViewById(R.id.textViewStudBacklogHistory);
		live_backlogs = (TextView) findViewById(R.id.textViewStudBacklogLive);
		gap_details = (TextView) findViewById(R.id.textViewStudGapEducation);
		address = (TextView) findViewById(R.id.textViewStudAddress);
		contact_number = (TextView) findViewById(R.id.textViewStudContactNumber);
		certification = (TextView) findViewById(R.id.textViewStudCertification);
		project_details = (TextView) findViewById(R.id.textViewStudProjectDetail);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_details, menu);
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
