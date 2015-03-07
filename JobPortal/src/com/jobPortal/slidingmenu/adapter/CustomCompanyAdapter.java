package com.jobPortal.slidingmenu.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.jobPortal.bean.Company;
import com.jobPortal.slidingmenu.R;

public class CustomCompanyAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}

	private ArrayList<Company> objects;

	private class ViewHolder {
		TextView textView1;
		TextView textView2;
		TextView textView3;
		ImageView iconImageView;
		Switch activation_switch;
	}

	public CustomCompanyAdapter(Context context, ArrayList<Company> objects) {
		inflater = LayoutInflater.from(context);
		this.objects = objects;
	}

	public int getCount() {
		return objects.size();
	}

	public Company getItem(int position) {
		return objects.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.student_admin_layout, null);
			holder.textView1 = (TextView) convertView
					.findViewById(R.id.student_name);
			holder.textView2 = (TextView) convertView
					.findViewById(R.id.qualification);
			holder.textView3 = (TextView) convertView
					.findViewById(R.id.duration);
			holder.iconImageView = (ImageView) convertView
					.findViewById(R.id.iconImageView);
			holder.activation_switch = (Switch) convertView
					.findViewById(R.id.active_btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView1.setText("\t" + objects.get(position).getName());
		String emailString = "";
		if (objects.get(position).getEmail().length() > 10) {
			emailString = objects.get(position).getEmail().substring(0, 9)
					+ "...";
		} else {
			emailString = objects.get(position).getEmail();
		}
		holder.textView2.setText("\t" + emailString);
		holder.textView3.setText("\t" + objects.get(position).getCreated_at());
		holder.activation_switch.setChecked(Boolean.parseBoolean(objects.get(
				position).getActivation()));
		holder.iconImageView.setImageResource(R.drawable.company);

		return convertView;
	}

}
