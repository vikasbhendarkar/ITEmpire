package com.jobPortal.activities.company;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jobPortal.library.DatabaseHandler;
import com.jobPortal.library.EditTextWithDeleteButton;
import com.jobPortal.library.EditTextWithDeleteButton.TextChangedListener;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.library.Validation;
import com.jobPortal.slidingmenu.R;

public class CompanyRegisterActivity extends Activity {

	Button btnRegister;
	Button btnLinkToLogin;
	EditTextWithDeleteButton inputFullName;
	EditTextWithDeleteButton inputEmail;
	EditTextWithDeleteButton inputPassword;
	EditTextWithDeleteButton inputRPassword;
	TextView registerErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	boolean isNotValid = false;
	String name, email, password, info_message;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_register);

		// Importing all assets like buttons, text fields
		inputFullName = (EditTextWithDeleteButton) findViewById(R.id.registerNameCompany);
		inputFullName.addTextChangedListener(editTextChanged());

		inputEmail = (EditTextWithDeleteButton) findViewById(R.id.registerEmailCompany);
		inputEmail.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

		inputPassword = (EditTextWithDeleteButton) findViewById(R.id.registerPasswordCompany);
		inputPassword.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		inputPassword.setSelection(inputPassword.getText().length());

		inputRPassword = (EditTextWithDeleteButton) findViewById(R.id.registerRePasswordCompany);
		inputRPassword.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		inputRPassword.setSelection(inputPassword.getText().length());

		btnRegister = (Button) findViewById(R.id.btnRegisterCompany);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreenCompany);
		registerErrorMsg = (TextView) findViewById(R.id.register_error_company);

		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				name = inputFullName.getEditText();

				// check validation
				email = inputEmail.getEditText();
				if (!Validation.isValidEmail(email, false)) {
					inputEmail.setError("Invalid Email");
					isNotValid = true;
				}

				password = inputPassword.getEditText();
				String rPassword = inputRPassword.getEditText();
				if (!Validation.isValidPassword(password, rPassword)) {
					inputRPassword.setError("Password Mismatch");
					isNotValid = true;
				}
				if (!isNotValid) {
					registerErrorMsg.setText("");
					registerErrorMsg.setTextColor(Color.MAGENTA);
					btnRegister.setEnabled(false);
					new CompanyRegistrationTask().execute();
				}
			}
		});

		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				finish();
			}
		});
	}

	private TextChangedListener editTextChanged() {
		return new TextChangedListener() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.company_register, menu);
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

	class CompanyRegistrationTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			String type = "company";
			UserFunctions userFunction = new UserFunctions();

			// check for login response
			try {
				JSONObject json = userFunction.registerUser(name, email,
						password, type);
				if (json.getString(KEY_SUCCESS) != null) {

					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// user successfully registred
						// Store user details in SQLite Database
						DatabaseHandler db = new DatabaseHandler(
								getApplicationContext());
						JSONObject json_user = json.getJSONObject("user");

						// Clear all previous data in database
						userFunction.logoutUser(getApplicationContext());
						db.addUser(json_user.getString(KEY_NAME),
								json_user.getString(KEY_EMAIL),
								json.getString(KEY_UID),
								json_user.getString(KEY_CREATED_AT));
						// Launch Dashboard Screen

						info_message = "Registration successful!! Please contact your administrator to activate your account.";
					} else {
						// Error in registration
						info_message = json.getString(KEY_ERROR_MSG);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				info_message = "Connection error: " + e.toString();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			registerErrorMsg.setText(info_message);
			btnRegister.setEnabled(true);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

	}
}
