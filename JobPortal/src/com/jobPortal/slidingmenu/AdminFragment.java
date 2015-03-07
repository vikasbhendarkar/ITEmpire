package com.jobPortal.slidingmenu;

import org.json.JSONObject;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jobPortal.activities.admin.AdminHomeActivity;
import com.jobPortal.library.DatabaseHandler;
import com.jobPortal.library.UserFunctions;

public class AdminFragment extends Fragment {

	private EditText inputAdminUname;
	private EditText inputAdminPasswd;
	private Button btnAdminLogin;
	private ProgressBar loading;
	private TextView errorMsg;
	UserFunctions userFunction;
	Context myContext;
	String msg = "";

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	private int notificationID = 100;

	public AdminFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = null;

		userFunction = new UserFunctions();
		myContext = getActivity().getApplicationContext();
		if (userFunction.isUserLoggedIn(myContext)) {
			userFunction.logoutUser(myContext);
			Intent login = new Intent(myContext, AdminHomeActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing dashboard screen
			getActivity().finish();
		} else {
			rootView = inflater.inflate(R.layout.fragment_admin_login,
					container, false);
			// Importing all assets
			inputAdminUname = (EditText) rootView
					.findViewById(R.id.inputAdminUsername);
			inputAdminPasswd = (EditText) rootView
					.findViewById(R.id.inputAdminPassword);
			btnAdminLogin = (Button) rootView.findViewById(R.id.btnAdminLogin);
			loading = (ProgressBar) rootView.findViewById(R.id.progressBar);
			errorMsg = (TextView) rootView
					.findViewById(R.id.textViewAdminErrorMsg);
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

		}
		return rootView;
	}

	class AdminLoginTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {
			// code where data is processing

			String uName = inputAdminUname.getText().toString();
			String passd = inputAdminPasswd.getText().toString();
			String loginType = "admin";
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

						// Clear all previous data in database
						userFunction.logoutUser(myContext);
						db.addUser(json_user.getString(KEY_NAME),
								json_user.getString(KEY_EMAIL),
								json.getString(KEY_UID),
								json_user.getString(KEY_CREATED_AT));

						// Launch Dashboard Screen
						Intent dashboard = new Intent(myContext,
								AdminHomeActivity.class);

						// Close all views before launching Dashboard
						dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(dashboard);

						// create notification
//						NotificationCompat.Builder builder = new NotificationCompat.Builder(
//								myContext);
//						builder.setSmallIcon(R.drawable.ic_pages);
//						builder.setContentTitle("JOB PLACEMENT PORTAL");
//						builder.setContentText("Admin is currently logged in ...");
//						NotificationManager manager = (NotificationManager) getActivity()
//								.getSystemService(
//										Context.NOTIFICATION_SERVICE);
//						manager.notify(notificationID, builder.build());
						
						NotificationManager notificationManager = (NotificationManager) myContext.getSystemService(Context.NOTIFICATION_SERVICE);
					    Notification notification = new Notification(R.drawable.ic_pages, "",System.currentTimeMillis());

					    Intent notificationIntent = new Intent(myContext, AdminHomeActivity.class);

					    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					            | Intent.FLAG_ACTIVITY_SINGLE_TOP);

					    PendingIntent intent = PendingIntent.getActivity(myContext, 0,
					            notificationIntent, 0);

					    notification.setLatestEventInfo(myContext, "Job Recruitment Andro-App", "Admin is currently logged in ...", intent);
					    notification.flags |= Notification.FLAG_AUTO_CANCEL;
					    notificationManager.notify(0, notification);
					    
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
