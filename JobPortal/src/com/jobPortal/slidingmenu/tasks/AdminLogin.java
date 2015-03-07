package com.jobPortal.slidingmenu.tasks;


import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jobPortal.activities.admin.AdminHomeActivity;
import com.jobPortal.library.DatabaseHandler;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.slidingmenu.R;

public class AdminLogin extends AsyncTask<Void, Void, Void> {

	UserFunctions userFunction;
	Context myContext;
	ProgressBar loading;
	Activity myAct;
	String uName;
	String passd;
	String loginType;
	
	// JSON Response node names
		private static String KEY_SUCCESS = "success";
		private static String KEY_UID = "uid";
		private static String KEY_NAME = "name";
		private static String KEY_EMAIL = "email";
		private static String KEY_CREATED_AT = "created_at";
		
	public AdminLogin(String uName, String pass, String type, Context myContext, Activity myAct){
		userFunction = new UserFunctions();
		loading = (ProgressBar) myAct.findViewById(R.id.progressBar);
		this.uName = uName;
		this.passd = pass;
		this.loginType = type;
		this.myContext = myContext;
		this.myAct = myAct;
	}
	
	 @Override
	    protected Void doInBackground(Void... args) {
	        // code where data is processing
		
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
							myContext.startActivity(dashboard);

							// Close Login Screen
							myAct.finish();
							
						} else {
							// Error in login
							Toast.makeText(
									myContext.getApplicationContext(),
									"Incorrect username/password",
									Toast.LENGTH_SHORT).show();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
//					Toast.makeText(
//							myContext.getApplicationContext(),
//							"Connection Error: "+e.toString(),
//							Toast.LENGTH_SHORT).show();
				} 
				return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {         
	        loading.setVisibility(View.INVISIBLE);
	    }

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        loading.setVisibility(View.VISIBLE);
	    }

}
