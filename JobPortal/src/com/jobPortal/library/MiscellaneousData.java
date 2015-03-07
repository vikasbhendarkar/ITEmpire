package com.jobPortal.library;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jobPortal.activities.admin.AdminHomeActivity;
import com.jobPortal.bean.Company;
import com.jobPortal.bean.Student;
import com.jobPortal.slidingmenu.MainActivity;
import com.jobPortal.slidingmenu.R;

public class MiscellaneousData {
	private Dialog dialogbox = null;
	private DatabaseHandler db;
	private Activity activity;
	Intent login;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_FAILURE_MSG = "error_msg";

	public void logoutActivity(Activity act, String portal) {

		// TODO Auto-generated method stub
		dialogbox = new Dialog(act);
		dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogbox.setContentView(R.layout.custom_dialog_exit);
		dialogbox.setTitle("");
		dialogbox.setCancelable(true);

		Context context = act.getApplicationContext();
		db = new DatabaseHandler(context);
		activity = act;
		login = new Intent(context, MainActivity.class);
		login.addCategory(Intent.CATEGORY_HOME);
		login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		Button yes = (Button) dialogbox.findViewById(R.id.mYesExitDialog);
		Button no = (Button) dialogbox.findViewById(R.id.mNoExitDialog);
		TextView msg = (TextView) dialogbox
				.findViewById(R.id.mExitDialogMessage);
		msg.setText("Do you really want to logout " + portal + " PORTAL ?");
		yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.resetTables();
				activity.finish();
				activity.startActivity(login);
			}
		});
		no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialogbox.dismiss();

			}
		});
		dialogbox.show();

	}

	public void deleteUser(Activity act, Object object) {
		Student student = new Student();
		Company company = new Company();
		final String user_email, userType;
		if (object instanceof Student) {
			student = (Student) object;
			user_email = student.getEmail();
			userType = "student";
		} else {
			company = (Company) object;
			user_email = company.getEmail();
			userType = "company";
		}
		dialogbox = new Dialog(act);
		dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogbox.setContentView(R.layout.custom_dialog_exit);
		dialogbox.setTitle("");
		dialogbox.setCancelable(true);

		final Context context = act.getApplicationContext();
		activity = act;
		login = new Intent(context, AdminHomeActivity.class);
		login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		Button yes = (Button) dialogbox.findViewById(R.id.mYesExitDialog);
		Button no = (Button) dialogbox.findViewById(R.id.mNoExitDialog);
		TextView msg = (TextView) dialogbox
				.findViewById(R.id.mExitDialogMessage);
		msg.setText("Do you really want to delete?");
		yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					UserFunctions userFunctions = new UserFunctions();
					JSONObject json = userFunctions.deleteUser(user_email, userType);
					if (json.getString(KEY_SUCCESS) != null
							&& json.getString(KEY_SUCCESS).equals("1")) {
						activity.finish();
						activity.startActivity(login);
					} else {
						Toast.makeText(context,
								json.getString(KEY_FAILURE_MSG),
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
		no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialogbox.dismiss();

			}
		});
		dialogbox.show();

	}
}
