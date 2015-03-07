package com.jobPortal.activities.student;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.jobPortal.library.DatabaseHandler;
import com.jobPortal.library.EditTextWithDeleteButton;
import com.jobPortal.library.EditTextWithDeleteButton.TextChangedListener;
import com.jobPortal.library.SendEmailAsyncTask;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.library.Validation;
import com.jobPortal.slidingmenu.R;

public class StudentRegisterActivity extends Activity {

	Button btnRegister;
	Button btnLinkToLogin;
	Button btnGenerateKey;
	EditTextWithDeleteButton inputGeneratedKey;
	EditTextWithDeleteButton inputEmail;
	EditTextWithDeleteButton inputPassword;
	EditTextWithDeleteButton inputRPassword;
	TextView registerErrorMsg;
	Context context;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	UserFunctions userFunction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_register);
		context = getApplicationContext();
		userFunction = new UserFunctions();
		// Common.minimizeKeyboard(StudentRegisterActivity.this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Importing all assets like buttons, text fields
		inputGeneratedKey = (EditTextWithDeleteButton) findViewById(R.id.textGeneratedKey);
		inputGeneratedKey.addTextChangedListener(editTextChanged());
		inputGeneratedKey.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_CLASS_NUMBER);

		inputEmail = (EditTextWithDeleteButton) findViewById(R.id.registerEmailStudent);
		inputEmail.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

		inputPassword = (EditTextWithDeleteButton) findViewById(R.id.registerPasswordStudent);
		inputPassword.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		inputPassword.setSelection(inputPassword.getText().length());

		inputRPassword = (EditTextWithDeleteButton) findViewById(R.id.registerRePasswordStudent);
		inputRPassword.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		inputRPassword.setSelection(inputPassword.getText().length());

		btnGenerateKey = (Button) findViewById(R.id.btnGenerateAutoKey);
		btnRegister = (Button) findViewById(R.id.btnRegisterStudent);
		btnRegister.setEnabled(true);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreenStudent);
		registerErrorMsg = (TextView) findViewById(R.id.register_error_student);

		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				boolean isNotValid = false;
				String name = inputGeneratedKey.getEditText();

				// check validation
				String email = inputEmail.getEditText();
				
				//bypass raisoni.net
				if(!email.contains("raisoni.net")){
					email+="@raisoni.net";
				}
				if (!Validation.isValidEmail(email,true)) {
					inputEmail.setError("Invalid Email");
					isNotValid = true;
				}

				if (!Validation.isValidActivationKey(email, inputGeneratedKey
						.getText().toString())) {
					inputGeneratedKey.setError("Invalid Activation Key");
					isNotValid = true;
				}

				String password = inputPassword.getEditText();
				String rPassword = inputRPassword.getEditText();
				if (!Validation.isValidPassword(password, rPassword)) {
					inputRPassword.setError("Password Mismatch");
					isNotValid = true;
				}

				String type = "student";
				if (!isNotValid) {
					// check for login response
					try {
						JSONObject json = userFunction.registerUser(name,
								email, password, type);
						if (json.getString(KEY_SUCCESS) != null) {
							btnRegister.setEnabled(false);
							registerErrorMsg.setText("");
							String res = json.getString(KEY_SUCCESS);
							if (Integer.parseInt(res) == 1) {
								// user successfully registred
								// Store user details in SQLite Database
								DatabaseHandler db = new DatabaseHandler(
										getApplicationContext());
								JSONObject json_user = json
										.getJSONObject("user");

								// Clear all previous data in database
								userFunction
										.logoutUser(getApplicationContext());
								db.addUser(json_user.getString(KEY_NAME),
										json_user.getString(KEY_EMAIL),
										json.getString(KEY_UID),
										json_user.getString(KEY_CREATED_AT));
								// Launch Dashboard Screen
								registerErrorMsg.setTextColor(Color.GREEN);
								registerErrorMsg
										.setText("Registration successful!! Please Login to start this app.");
							} else {
								// Error in registration
								registerErrorMsg.setText(json
										.getString(KEY_ERROR_MSG));
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						registerErrorMsg.setText("Connection error: "
								+ e.toString());
					}
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

	public void generateKey(View view) {
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		String email = inputEmail.getEditText();
		
		//bypass raisoni.net
		if(!email.contains("raisoni.net")){
			email+="@raisoni.net";
		}
		if (!Validation.isValidEmail(email,true)) {
			inputEmail.setError("Invalid Email");
			return;
		}
		try {
			JSONObject json = userFunction.loginUser(email, "",
					"isEmailAvailable");
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {

					Random r = new Random(System.currentTimeMillis());
					String act_code = ((1 + r.nextInt(2)) * 10000 + r
							.nextInt(10000)) + "";

					String message = "<!DOCTYPE html><html><head><meta charset=\"ISO-8859-1\"><title>Insert title here</title></head><body>"
							+ "<h3>Thanks for using Campus Recruitment android app !!</h3>Please find the Activation key below:<table border=\"2\" style=\"color:maroon;\">"
							+ "<tr><th>&nbsp;&nbsp;Registered mail id&nbsp;&nbsp;</th><th>&nbsp;Activation key&nbsp;</th></tr><tr align=\"center\">"
							+ "<td>"
							+ email
							+ "</td><td>"
							+ act_code
							+ "</td></tr></table><br><u>Use the above generated key in your android application to register yourself.</u>"
							+ "</body></html>";

					JSONObject jsn = userFunction.registerUser("", email,
							act_code, "storeActivationKey");
					String response = jsn.getString(KEY_SUCCESS);
					if (Integer.parseInt(response) == 1) {
						SendEmailAsyncTask mAuthTask = new SendEmailAsyncTask(
								context, message, StudentRegisterActivity.this,
								email, registerErrorMsg);
						// mAuthTask = new UserLoginTask(email,
						// userName,userContact);
						mAuthTask.execute();
					} else {
						registerErrorMsg.setTextColor(Color.RED);
						registerErrorMsg.setText(jsn.getString(KEY_ERROR_MSG));
					}

					btnGenerateKey.setEnabled(false);
				} else {
					// Error in registration
					registerErrorMsg.setTextColor(Color.RED);
					registerErrorMsg.setText("mail ID not found !!");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
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
		getMenuInflater().inflate(R.menu.student_register, menu);
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
