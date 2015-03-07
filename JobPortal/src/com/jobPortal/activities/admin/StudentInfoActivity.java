package com.jobPortal.activities.admin;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jobPortal.bean.Student;
import com.jobPortal.library.MiscellaneousData;
import com.jobPortal.slidingmenu.R;

public class StudentInfoActivity extends Activity {

	private TextView name;
	private TextView gender;
	private TextView email;
	private TextView date_of_birth;
	private TextView branch;
	private TextView semester;
	private TextView registration_date;
	private TextView activation_status;
	Student student;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_student_info);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		findViewById();
		student = new Student();
		student = (Student) getIntent().getSerializableExtra("student");
		fillStudentDetails(student);
	}

	private void fillStudentDetails(Student stud) {
		// TODO Auto-generated method stub
		name.setText(stud.getName());
		email.setText(stud.getEmail());
		registration_date.setText(stud.getCreated_at());
		boolean status = Boolean.parseBoolean(stud.getActivation());
		activation_status.setText(status ? "Activated" : "Deactivated");
		if (stud.getGender() != null) {
			gender.setText(stud.getGender());
		}
		if (stud.getDate_of_birth() != null) {
			date_of_birth.setText(stud.getDate_of_birth());
		}
		if (stud.getBranch() != null) {
			branch.setText(stud.getBranch());
		}
		if (stud.getSemester() != null) {
			semester.setText(stud.getSemester());
		}
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		name = (TextView) findViewById(R.id.textViewStudentName);
		gender = (TextView) findViewById(R.id.textViewStudentGender);
		email = (TextView) findViewById(R.id.textViewStudentEmail);
		date_of_birth = (TextView) findViewById(R.id.textViewDOB);
		branch = (TextView) findViewById(R.id.textViewBranch);
		semester = (TextView) findViewById(R.id.textViewSemester);
		registration_date = (TextView) findViewById(R.id.textViewRegistrationDate);
		activation_status = (TextView) findViewById(R.id.textViewActivationStatus);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}

	public void deleteStudent(View view) {

		if (student != null) {
			MiscellaneousData data = new MiscellaneousData();
			data.deleteUser(this, student);

		}
	}

}
