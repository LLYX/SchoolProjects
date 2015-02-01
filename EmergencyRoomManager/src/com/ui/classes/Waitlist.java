package com.ui.classes;

import java.util.List;

import xu.leon.patientdatabasemanager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.method.classes.DataSource;
import com.method.classes.Sorts;

public class Waitlist extends Activity {
	
	/**
	 * Activate listView of all visits
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waitlist);
		
		ListView listView = (ListView)findViewById(R.id.list);
		DataSource dataSource = new DataSource(this);
		dataSource.open();
		Intent intent = getIntent();
		String orderBy = (String) intent.getExtras().get("orderBy");
		List<String> values;
		if (orderBy.equals("wait")){
			values = Sorts.sortedWaitTime(dataSource.getDB());
		}
		else {
			values = Sorts.sortedUrgency(dataSource.getDB());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
		listView.setAdapter(adapter);
		dataSource.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()){
		case R.id.menu_home:
			Intent intent = new Intent(this, SubmitHCN.class);
			startActivity(intent);
			return true;
		case R.id.menu_logout:
			Intent intent2 = new Intent(this, Login.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent2.putExtra("EXIT", true);
			startActivity(intent2);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
