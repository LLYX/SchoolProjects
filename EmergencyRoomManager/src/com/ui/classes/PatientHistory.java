package com.ui.classes;

import java.util.List;

import com.method.classes.DataSource;
import com.method.classes.GetCalls;

import xu.leon.patientdatabasemanager.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PatientHistory extends Activity {
	
	/**
	 * Make listView display the patient history of the given patient
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_history);
		
		ListView listView1 = (ListView)findViewById(R.id.listView1);
		DataSource dataSource = new DataSource(this);
		dataSource.open();
		Intent intent = getIntent();
		String hcn = (String) intent.getExtras().get("hcnKey");
		List<String> values;
		values = GetCalls.getPatientHistory(dataSource.getDB(), hcn);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
		listView1.setAdapter(adapter);
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
			return true;
		case R.id.menu_logout:
			Intent intent = new Intent(this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
