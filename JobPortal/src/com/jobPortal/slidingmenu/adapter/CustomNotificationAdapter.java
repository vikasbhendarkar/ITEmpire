package com.jobPortal.slidingmenu.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.jobPortal.bean.NotificationBean;
import com.jobPortal.slidingmenu.R;

public class CustomNotificationAdapter extends BaseAdapter{

	private LayoutInflater inflater;

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}

	private ArrayList<NotificationBean> objects;

	private class ViewHolder {
		TextView from_user;
		TextView created_time;
		EditText message;
	}

	public CustomNotificationAdapter(Context context, ArrayList<NotificationBean> objects) {
		inflater = LayoutInflater.from(context);
		this.objects = objects;
	}

	public int getCount() {
		return objects.size();
	}

	public NotificationBean getItem(int position) {
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
			convertView = inflater.inflate(R.layout.notification_layout, null);
			holder.from_user = (TextView) convertView
			.findViewById(R.id.textView_from);
			holder.message = (EditText) convertView
					.findViewById(R.id.editTextNotificationMessage);
			holder.created_time = (TextView) convertView
					.findViewById(R.id.textViewCreatedAt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.from_user.setText(objects.get(position).getFrom_user()+" said");
		holder.message.setText(objects.get(position).getMessage());
		holder.created_time.setText("@ "+objects.get(position).getCreated_at());

		return convertView;
	}

}
