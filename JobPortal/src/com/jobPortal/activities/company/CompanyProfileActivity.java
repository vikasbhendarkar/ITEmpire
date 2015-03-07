package com.jobPortal.activities.company;

import java.util.Calendar;

import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.jobPortal.bean.Company;
import com.jobPortal.library.DatabaseHandler;
import com.jobPortal.library.UserFunctions;
import com.jobPortal.slidingmenu.R;

public class CompanyProfileActivity extends Activity implements OnClickListener {

	UserFunctions userFunctions;
	Context context;
	DatabaseHandler db;
	Company company = new Company();

	private RadioGroup radioCmpnyTypeGroup;
	private RadioButton radioCmnyTypeButton;
	private Spinner salary_pkg;
	private EditText date_of_placement;
	private CheckBox chkbCA, chkBsc, chkMsc, chkMca, chkBE, chkME, chkEE,
			chkCVE;
	String COMPANY_EMAIL = "";
	StringBuilder courses = new StringBuilder();

	private int year;
	private int month;
	private int day;
	static final int DATE_DIALOG_ID = 100;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_FAILURE_MSG = "error_msg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_profile);
		context = getApplicationContext();
		db = new DatabaseHandler(context);

		company = (Company) getIntent().getSerializableExtra("company");

		findViewById();
		getDatePicker();
	}

	private void getDatePicker() {
		date_of_placement = (EditText) findViewById(R.id.company_DOP);
		if (company.getDate_of_placement() != null) {
			date_of_placement.setText(company.getDate_of_placement());
		} else {
			final Calendar calendar = Calendar.getInstance();

			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);

			// set current date into textview
			date_of_placement.setText(new StringBuilder()
					// Month is 0 based, so you have to add 1
					.append(month + 1).append("-").append(day).append("-")
					.append(year).append(" "));
		}

		date_of_placement.addTextChangedListener(new TextWatcher() {

			@SuppressWarnings("deprecation")
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void findViewById() {
		radioCmpnyTypeGroup = (RadioGroup) findViewById(R.id.company_type);
		if (company.getType() != null
				&& company.getType().equalsIgnoreCase("public")) {
			radioCmpnyTypeGroup.check(R.id.radioPublic);
		}

		// checkbox items
		chkbCA = (CheckBox) findViewById(R.id.chkbCA);
		chkBsc = (CheckBox) findViewById(R.id.chkBsc);
		chkMsc = (CheckBox) findViewById(R.id.chkMsc);
		chkMca = (CheckBox) findViewById(R.id.chkMca);
		chkBE = (CheckBox) findViewById(R.id.chkBE);
		chkME = (CheckBox) findViewById(R.id.chkME);
		chkEE = (CheckBox) findViewById(R.id.chkEE);
		chkCVE = (CheckBox) findViewById(R.id.chkCVE);

		chkbCA.setOnClickListener(this);
		chkBsc.setOnClickListener(this);
		chkMsc.setOnClickListener(this);
		chkMca.setOnClickListener(this);
		chkBE.setOnClickListener(this);
		chkME.setOnClickListener(this);
		chkEE.setOnClickListener(this);
		chkCVE.setOnClickListener(this);

		if (company.getCourse() != null) {
			String[] corses = company.getCourse().split(",");
			for (String crse : corses) {
				if (crse.equals(chkbCA.getText())) {
					chkbCA.setChecked(true);
					courses.append(chkbCA.getText() + ",");
					continue;
				}
				if (crse.equals(chkBsc.getText())) {
					chkBsc.setChecked(true);
					courses.append(chkBsc.getText() + ",");
					continue;
				}
				if (crse.equals(chkMsc.getText())) {
					chkMsc.setChecked(true);
					courses.append(chkMsc.getText() + ",");
					continue;
				}
				if (crse.equals(chkMca.getText())) {
					chkMca.setChecked(true);
					courses.append(chkMca.getText() + ",");
					continue;
				}
				if (crse.equals(chkBE.getText())) {
					chkBE.setChecked(true);
					courses.append(chkBE.getText() + ",");
					continue;
				}
				if (crse.equals(chkME.getText())) {
					chkME.setChecked(true);
					courses.append(chkME.getText() + ",");
					continue;
				}
				if (crse.equals(chkEE.getText())) {
					chkEE.setChecked(true);
					courses.append(chkEE.getText() + ",");
					continue;
				}
				if (crse.equals(chkCVE.getText())) {
					chkCVE.setChecked(true);
					courses.append(chkCVE.getText() + ",");
					continue;
				}
			}
		}

		// qualification items
		salary_pkg = (Spinner) findViewById(R.id.spinner_company_salary_package);
		String[] qual_list = getResources().getStringArray(
				R.array.company_package_items);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, qual_list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		salary_pkg.setAdapter(dataAdapter);
		if (company.getSalary_package() != null) {
			@SuppressWarnings("unchecked")
			ArrayAdapter<String> myAdap = (ArrayAdapter<String>) salary_pkg
					.getAdapter(); // cast to an ArrayAdapter
			int spinnerPosition = myAdap.getPosition(company
					.getSalary_package());
			salary_pkg.setSelection(spinnerPosition);
		}
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(context, "First complete this profile !!",
				Toast.LENGTH_SHORT).show();
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into Text View
			date_of_placement.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	public void completeCompanyProfile(View view) {

		// get selected radio button from radioGroup
		int selectedId = radioCmpnyTypeGroup.getCheckedRadioButtonId();
		// find the radiobutton by returned id
		radioCmnyTypeButton = (RadioButton) findViewById(selectedId);
		String s = radioCmnyTypeButton.getText().toString();

		COMPANY_EMAIL = company.getEmail();
		Company company = new Company();
		company.setEmail(COMPANY_EMAIL);
		company.setType(s);
		company.setCourse(courses.toString());
		company.setDate_of_placement(date_of_placement.getText().toString());
		company.setSalary_package(String.valueOf(salary_pkg.getSelectedItem()));

		try {
			UserFunctions function = new UserFunctions();
			JSONObject json = function.updateCompanyProfile(company);
			if (json.getString(KEY_SUCCESS) != null
					&& json.getString(KEY_SUCCESS).equals("1")) {
				Intent intent = new Intent(this, CompanyHomeActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(context, json.getString(KEY_FAILURE_MSG),
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onClick(View v) {
		if (((CheckBox) v).isChecked()) {
			courses.append(((CheckBox) v).getText().toString() + ",");
		} else {
			String chkText = ((CheckBox) v).getText().toString();
			int startIndex = courses.indexOf(chkText);
			int endIndex = startIndex + chkText.length() + 1;
			courses.delete(startIndex, endIndex);
		}

	}
}
