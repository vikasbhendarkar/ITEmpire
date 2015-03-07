package com.jobPortal.slidingmenu.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jobPortal.bean.Company;
import com.jobPortal.bean.StudentBean;
import com.jobPortal.slidingmenu.R;

public class CustomJobforStudentAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	Activity context;

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}

	private ArrayList<Object> objects;

	private class ViewHolder {
		TextView textView1;
		TextView textView2;
		ImageView iconImageView;
		CheckBox box;
	}

	public CustomJobforStudentAdapter(Activity context,
			ArrayList<Object> objects) {
		this.context = context;
		inflater = LayoutInflater.from(context.getApplicationContext());
		this.objects = objects;
	}

	public int getCount() {
		return objects.size();
	}

	public Object getItem(int position) {
		return objects.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.job_student_layout, null);
			holder.textView1 = (TextView) convertView
					.findViewById(R.id.student_company_name);
			holder.textView2 = (TextView) convertView
					.findViewById(R.id.date_of_placement_student_company);
			holder.iconImageView = (ImageView) convertView
					.findViewById(R.id.iconImageView_Student);
			holder.box = (CheckBox) convertView
					.findViewById(R.id.checkBox1_jobs);
			holder.box
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							int getPosition = (Integer) buttonView.getTag();
							if (objects.get(getPosition) instanceof StudentBean) {
								((StudentBean) objects.get(getPosition))
										.setSelected(buttonView.isChecked());
							}
							if (objects.get(getPosition) instanceof Company) {
								((Company) objects.get(getPosition))
										.setSelected(buttonView.isChecked());
							}
						}
					});
			convertView.setTag(holder);
			convertView.setTag(R.id.checkBox1_jobs, holder.box);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String nameString = "";
		String name = "";
		String dateOfplacement = "";
		holder.box.setTag(position);
		if (objects.get(position) instanceof Company) {
			name = ((Company) (objects.get(position))).getName();
			dateOfplacement = ((Company) (objects.get(position)))
					.getRegistered();
			holder.iconImageView.setImageResource(R.drawable.company);
			holder.box.setChecked(((Company) objects.get(position))
					.isSelected());
		} else if (objects.get(position) instanceof StudentBean) {
			name = ((StudentBean) (objects.get(position))).getFirstName() + " "
					+ ((StudentBean) (objects.get(position))).getLastName();
			dateOfplacement = ((StudentBean) (objects.get(position)))
					.getRegistered();
			if (dateOfplacement.length() > 15) {
				dateOfplacement = dateOfplacement.substring(0, 14) + "...";
			}

			holder.iconImageView.setImageResource(R.drawable.student);
			holder.box.setChecked(((StudentBean) objects.get(position))
					.isSelected());
		}
		if (name.length() > 20) {
			nameString = name.substring(0, 19) + "...";
		} else {
			nameString = name;
		}
		holder.textView1.setText("\t" + nameString);
		holder.textView2.setText("\t" + dateOfplacement);

		return convertView;
	}

}
