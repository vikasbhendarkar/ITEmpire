package com.jobPortal.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.jobPortal.activities.student.StudentRegisterActivity;

public class SendEmailAsyncTask extends AsyncTask<Void, Void, Void> {
	GMailSender sender = new GMailSender("andro.crt@gmail.com", "itempire");
	Context context;
	String mailBody;
	StudentRegisterActivity screen;
	ProgressDialog mProgressDialog;
	String email;
	TextView registerErrorMsg;

	public SendEmailAsyncTask(Context cont, String mailBody,
			StudentRegisterActivity screen,
			String email, TextView registerErrorMsg) {
		this.context = cont;
		this.mailBody = mailBody;
		this.screen = screen;
		this.email = email;
		this.registerErrorMsg = registerErrorMsg;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = new ProgressDialog(screen);
		mProgressDialog.setTitle("Generating Activation key...");
		mProgressDialog
				.setMessage("This activattion key will be sent to your raisoni mail id. Grab it and register yourself");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {

			sender.sendMail("Campus Recruitment Andro App Student Activation Request",
					mailBody, "andro.crt@gmail.com", email);

		} catch (Exception e) {
			mProgressDialog.dismiss();
			registerErrorMsg.setTextColor(Color.RED);
			registerErrorMsg.setText("Failed: "+e.getMessage());
			Log.e("SendMail", e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// Set title into TextView
		mProgressDialog.dismiss();
		registerErrorMsg.setTextColor(Color.GREEN);
		registerErrorMsg.setText("Generated key has been sent to your raisoni mail ID");

	}
}
