package com.jobPortal.slidingmenu;

import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jobPortal.activities.student.StudentHomeActivity;
import com.jobPortal.activities.student.StudentRegisterActivity;
import com.jobPortal.bean.StudentBean;
import com.jobPortal.library.Common;
import com.jobPortal.library.DatabaseHandler;
import com.jobPortal.library.UserFunctions;

public class StudentFragment extends Fragment {
	private EditText inputAdminUname;
	private EditText inputAdminPasswd;
	private Button btnAdminLogin;
	private ProgressBar loading;
	private TextView errorMsg;
	private TextView studRegister;
	UserFunctions userFunction;
	Context myContext;
	String msg = "";

	// JSON Response node names
	private static String KEY_SUCCESS = "success";

	public StudentFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = null;

		userFunction = new UserFunctions();
		myContext = getActivity().getApplicationContext();
		if (userFunction.isUserLoggedIn(myContext)) {
			userFunction.logoutUser(myContext);
			Intent login = new Intent(myContext, StudentHomeActivity.class);
			startActivity(login);
			// Closing dashboard screen
			getActivity().finish();
		} else {

			rootView = inflater.inflate(R.layout.fragment_student_login,
					container, false);

			userFunction = new UserFunctions();
			myContext = getActivity().getApplicationContext();

			// Importing all assets
			inputAdminUname = (EditText) rootView
					.findViewById(R.id.inputStudentUsername);
			inputAdminPasswd = (EditText) rootView
					.findViewById(R.id.inputStudentPassword);
			btnAdminLogin = (Button) rootView
					.findViewById(R.id.btnStudentLogin);
			loading = (ProgressBar) rootView
					.findViewById(R.id.progressStudentBar);
			errorMsg = (TextView) rootView
					.findViewById(R.id.textViewStudentErrorMsg);
			studRegister = (TextView) rootView
					.findViewById(R.id.textViewStudentRegister);
			errorMsg.setTextColor(Color.RED);

			inputAdminUname
					.setOnFocusChangeListener(new View.OnFocusChangeListener() {

						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							// TODO Auto-generated method stub
							if (hasFocus) {
								errorMsg.setText("");
							}
						}
					});

			btnAdminLogin.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					new AdminLoginTask().execute();
				}
			});

			studRegister.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent register = new Intent(myContext,
							StudentRegisterActivity.class);
					startActivity(register);
				}
			});
		}
		return rootView;

	}

	class AdminLoginTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {
			// code where data is processing

			String uName = inputAdminUname.getText().toString();
			String passd = inputAdminPasswd.getText().toString();
			String loginType = "student";
			Log.d("Button", "Login");

			//bypass raisoni.net
			if(!uName.contains("raisoni.net")){
				uName+="@raisoni.net";
			}
			
			// check for login response
			try {
				JSONObject json = userFunction.loginUser(uName, passd,
						loginType);

				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// user successfully logged in
						// Store user details in SQLite Database
						DatabaseHandler db = new DatabaseHandler(myContext);
						JSONObject json_user = json.getJSONObject("user");

						// Clear all previous data in database
						userFunction.logoutUser(myContext);
						db.addUser(
								json_user.getString(Common.STUDENT_FIRST_NAME),
								json_user
										.getString(Common.STUDENT_RAISONI_EMAIL),
								"uid", json_user
										.getString(Common.STUDENT_CREATED_AT));

						// Fill student object
						StudentBean student = new StudentBean();
						student.setStudent_id(json_user
								.getString(Common.STUDENT_ID));
						student.setRoll_no(json_user
								.getString(Common.STUDENT_ROLL_NO));
						student.setSection(json_user
								.getString(Common.STUDENT_SECTION));
						student.setFirstName(json_user
								.getString(Common.STUDENT_FIRST_NAME));
						student.setMiddleName(json_user
								.getString(Common.STUDENT_MIDDLE_NAME));
						student.setLastName(json_user
								.getString(Common.STUDENT_LAST_NAME));
						student.setDate_of_birth(json_user
								.getString(Common.STUDENT_DATE_OF_BIRTH));
						student.setGender(json_user
								.getString(Common.STUDENT_GENDER));
						student.setBranch(json_user
								.getString(Common.STUDENT_BRANCH));
						student.setSemester(json_user
								.getString(Common.STUDENT_SEMESTER));
						student.setSscMarks(json_user
								.getString(Common.STUDENT_SSC_MARKS));
						student.setHscMarks(json_user
								.getString(Common.STUDENT_HSC_MARKS));
						student.setDiplomaMarks(json_user
								.getString(Common.STUDENT_DIPLOMA_MARKS));
						student.setEnggMarks(json_user
								.getString(Common.STUDENT_ENGG_MARKS));
						student.setBacklogHistory(json_user
								.getString(Common.STUDENT_BACKLOG_HISTORY));
						student.setIsBacklogLive(json_user
								.getString(Common.STUDENT_IS_BACKLOGLIVE));
						student.setIsGapInEducation(json_user
								.getString(Common.STUDENT_IS_GAPEDUCATION));
						student.setGap_details(json_user
								.getString(Common.STUDENT_GAP_DETAILS));
						student.setMobile_number(json_user
								.getString(Common.STUDENT_MOBILE_NUMBER));
						student.setEmail(json_user
								.getString(Common.STUDENT_RAISONI_EMAIL));
						student.setTemp_address(json_user
								.getString(Common.STUDENT_TEMP_ADDRESS));
						student.setPer_address(json_user
								.getString(Common.STUDENT_PER_ADDRESS));
						student.setCertification(json_user
								.getString(Common.STUDENT_CERTIFICATION));
						student.setProject_details(json_user
								.getString(Common.STUDENT_PROJECT_DETAILS));
						student.setIsActive(json_user
								.getString(Common.STUDENT_IS_ACTIVE));
						student.setCreated_at(json_user
								.getString(Common.STUDENT_CREATED_AT));

						Intent dashboard = new Intent(myContext,
								StudentHomeActivity.class);

						// Close all views before launching Dashboard
						dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Common.STUDENT_EMAIL = student.getEmail();
						dashboard.putExtra("student", student);
						startActivity(dashboard);

						// Close Login Screen
						getActivity().finish();

					} else {
						// Error in login
						msg = "Incorrect username/password";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "Connection Error:" + e.toString();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			loading.setVisibility(View.INVISIBLE);
			inputAdminUname.setEnabled(true);
			inputAdminPasswd.setEnabled(true);
			btnAdminLogin.setEnabled(true);
			errorMsg.setText(msg);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading.setVisibility(View.VISIBLE);
			inputAdminUname.setEnabled(false);
			inputAdminPasswd.setEnabled(false);
			btnAdminLogin.setEnabled(false);
		}

	}
}
