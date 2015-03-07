package com.jobPortal.slidingmenu;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.jobPortal.bean.NotificationBean;
import com.jobPortal.slidingmenu.adapter.CustomNotificationAdapter;

public class Notifications extends Activity {

	ArrayList<NotificationBean> notifications;
	CustomNotificationAdapter adapter;
	ListView notifications_List;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		
		notifications = (ArrayList<NotificationBean>) getIntent().getSerializableExtra("notificationList");
		adapter = new CustomNotificationAdapter(getApplicationContext(), notifications);
		notifications_List = (ListView)findViewById(R.id.listViewNotifications);
		notifications_List.setAdapter(adapter);
		notifications_List.setPadding(10, 50, 10, 50);
		notifications_List.setFooterDividersEnabled(true);
		notifications_List.setHeaderDividersEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notifications, menu);
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
