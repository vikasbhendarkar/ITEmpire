package com.jobPortal.slidingmenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AboutFragment extends Fragment {

	Button share_button;

	public AboutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_about, container,
				false);
		share_button = (Button) rootView.findViewById(R.id.shareApp);
		share_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareAppNow(v);
			}
		});
		return rootView;
	}

	public void shareAppNow(View view) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sharing App");
		intent.putExtra(
				"android.intent.extra.TEXT",
				"Hey , This app is really usefull for calculating Loan EMI and EMI Statistics.\n\nEMI CALCULATOR: https://play.google.com/store/apps/details?id=com.finance.emi.calculate");
		intent.setType("text/plain");
		getActivity().startActivity(
				Intent.createChooser(intent, "Share the app"));
	}
}
