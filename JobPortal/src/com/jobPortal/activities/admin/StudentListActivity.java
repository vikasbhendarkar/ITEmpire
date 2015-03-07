package com.jobPortal.activities.admin;

import java.util.ArrayList;

import org.json.JSONArray;
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
import android.widget.ListView;
import android.widget.Toast;

import com.jobPortal.activities.company.StudentDetails;
import com.jobPortal.bean.Student;
import com.jobPortal.bean.StudentBean;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.slidingmenu.R;
import com.jobPortal.slidingmenu.adapter.CustomStudentAdapter;

public class StudentListActivity extends Activity implements
		OnItemLongClickListener {

	private ListView registered_students;
	private ArrayList<StudentBean> students = null;
	CustomStudentAdapter adapter;
	UserFunctions userFunctions;
	String statusChanges = "";
	String message="";
	MenuItem menuAppliedList;
	ArrayList<String> studNames;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_student_list);

		
		// get list for students
		registered_students = (ListView) findViewById(R.id.listView_students_list);
		students = new ArrayList<StudentBean>();
		students = (ArrayList<StudentBean>) getIntent().getSerializableExtra(
				"studentsList");
		adapter = new CustomStudentAdapter(this, students);
		registered_students.setAdapter(adapter);
		registered_students.setPadding(10, 20, 10, 15);
		registered_students.setFooterDividersEnabled(true);
		registered_students.setHeaderDividersEnabled(true);
		registered_students.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_main, menu);
		menuAppliedList = menu.findItem(R.id.menuItemgetRegisteredStudentList);
		menuAppliedList.setEnabled(true);
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

	public void applyOptedout(MenuItem item) {
		new OptedOutTask().execute();
	}

	public void getRegisteredStudentList(MenuItem item) {
		new GetRegisteredStudents().execute();
	}
	
	public ArrayList<Student> getAllStudentsList() {

		return null;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		StudentBean student = (StudentBean) parent.getItemAtPosition(position);

		Intent companyInfo = new Intent(this, StudentDetails.class);
		companyInfo.putExtra("student", student);
		startActivity(companyInfo);
		return true;
	}

	class OptedOutTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {
			ArrayList<StudentBean> temp = new ArrayList<StudentBean>();
			for (StudentBean t : students) {
				temp.add(t);
			}
			for (StudentBean stud : temp) {
				if (!Boolean.parseBoolean(stud.getIsActive())) {
					students.remove(stud);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.notifyDataSetChanged();
			menuAppliedList.setEnabled(false);
			if(students.size() == 0){
			Toast.makeText(getApplicationContext(),
					"no-one Student is opted out !!", Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(getApplicationContext(),
						"List of Opted out Students", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	}
	
	class GetRegisteredStudents extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

				UserFunctions userFunction = new UserFunctions();
				try {
					studNames = new ArrayList<String>();
					JSONObject json = userFunction.loginUser(
							"", "",
							"getRegistrationStatusForAdmin");							
							JSONArray menuitemArray = json.getJSONArray("userDetails");
							for (int i = 0; i < menuitemArray.length(); i++) {
								JSONObject row = menuitemArray.getJSONObject(i);
								studNames.add(row.getString("student_email"));
							}
					
							ArrayList<String> temp = new ArrayList<String>();
							for (String n : studNames) {
								temp.add(n);
							}
							
							ArrayList<StudentBean> stu = new ArrayList<StudentBean>();
							for (StudentBean x : students) {
								stu.add(x);
							}
							
							for (StudentBean st : stu) {
								if(!temp.contains(st.getEmail())){
									students.remove(st);
								}
							}
							message ="success";
					
				} catch (Exception e) {
					e.printStackTrace();
					message = "Error while getting registered student list";
				}
				
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.notifyDataSetChanged();
			Toast.makeText(getApplicationContext(),
					message, Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPreExecute() {
			
		}

	}

}
