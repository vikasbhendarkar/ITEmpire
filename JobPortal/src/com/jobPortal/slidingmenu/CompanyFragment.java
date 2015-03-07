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

import com.jobPortal.activities.company.CompanyHomeActivity;
import com.jobPortal.activities.company.CompanyProfileActivity;
import com.jobPortal.activities.company.CompanyRegisterActivity;
import com.jobPortal.bean.Company;
import com.jobPortal.library.Common;
import com.jobPortal.library.DatabaseHandler;
import com.jobPortal.library.UserFunctions;

public class CompanyFragment extends Fragment {
	private EditText inputAdminUname;
	private EditText inputAdminPasswd;
	private Button btnAdminLogin;
	private ProgressBar loading;
	private TextView errorMsg;
	private TextView cmnyRegister;
	UserFunctions userFunction;
	Context myContext;
	String msg = "";

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	private static String KEY_ACTIVATION = "activation";
	private static String KEY_VISIT_COUNT = "visit_count";

	public CompanyFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = null;

		userFunction = new UserFunctions();
		myContext = getActivity().getApplicationContext();
		if (userFunction.isUserLoggedIn(myContext)) {
			userFunction.logoutUser(myContext);
			Intent login = new Intent(myContext, CompanyHomeActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing dashboard screen
			getActivity().finish();
		} else {

			rootView = inflater.inflate(R.layout.fragment_company_login,
					container, false);

			// Importing all assets
			inputAdminUname = (EditText) rootView
					.findViewById(R.id.inputCompanyUsername);
			inputAdminPasswd = (EditText) rootView
					.findViewById(R.id.inputCompanyPassword);
			btnAdminLogin = (Button) rootView
					.findViewById(R.id.btnCompanyLogin);
			loading = (ProgressBar) rootView
					.findViewById(R.id.progressCompanyBar);
			errorMsg = (TextView) rootView
					.findViewById(R.id.textViewCompanyErrorMsg);
			cmnyRegister = (TextView) rootView
					.findViewById(R.id.textViewCompanyRegister);
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

			cmnyRegister.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent register = new Intent(myContext,
							CompanyRegisterActivity.class);
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
			String loginType = "company";
			Log.d("Button", "Login");

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

						// check activation status
						if (json_user.getString(KEY_ACTIVATION) != null
								&& json_user.getString(KEY_ACTIVATION)
										.equalsIgnoreCase("false")) {
							msg = "Your Account has not activated yet, please contact your administrator.";
							return null;
						}

						// Clear all previous data in database
						userFunction.logoutUser(myContext);
						db.addUser(json_user.getString(KEY_NAME),
								json_user.getString(KEY_EMAIL),
								json.getString(KEY_UID),
								json_user.getString(KEY_CREATED_AT));

						//fill company object
						Company company = new Company();
						company.setName(json_user.getString(KEY_NAME));
						company.setEmail(json_user.getString(KEY_EMAIL));
						company.setCreated_at(json_user.getString(KEY_CREATED_AT));
						
						int visit_count = Integer.parseInt(json_user
								.getString(KEY_VISIT_COUNT));
						if (visit_count == 0) {
							// Launch Dashboard Screen
							Intent dashboard = new Intent(myContext,
									CompanyProfileActivity.class);

							// Close all views before launching Dashboard
							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							Common.STUDENT_EMAIL = company.getEmail();
							dashboard.putExtra("company", company);
							startActivity(dashboard);
						} else {
							Intent dashboard = new Intent(myContext,
									CompanyHomeActivity.class);

							// Close all views before launching Dashboard
							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							Common.STUDENT_EMAIL = company.getEmail();
							dashboard.putExtra("company", company);
							startActivity(dashboard);
						}

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
