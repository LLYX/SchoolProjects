package com.ui.classes;

import xu.leon.patientdatabasemanager.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.method.classes.DataSource;

public class PhysicianActions extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_physician_actions);
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
			Intent intent2 = new Intent(this, Login.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent2.putExtra("EXIT", true);
			startActivity(intent2);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 
	 * @param view
	 * If submit4 is clicked:
	 * 		if the length of inputed health card number != 6 -> return message
	 * 		else if the health card number doesn't exist in the database -> return message
	 * 		else go to PatientHistory
	 * If prescription is pressed:
	 * 		if length of inputed health card number != 6 -> return message
	 * 		else if health card number doesn't exist in the database -> return message
	 * 		else go to SetPrescription
	 */
	public void onClick(View view){
		DataSource dataSource = new DataSource(this);
		dataSource.open();
		EditText hcnText = (EditText) findViewById(R.id.hcn);
		String hcn = hcnText.getText().toString();
		switch(view.getId()){
		case R.id.submit4:
			if (hcn.length() == 6){
				boolean inDatabase = dataSource.patientExists(hcn);
				if (inDatabase){
					dataSource.close();
					Intent intent = new Intent(this, PatientHistory.class);
					intent.putExtra("hcnKey", hcn);
					startActivity(intent);
				}
				else {
					Toast.makeText(this, "No patient associated with this health card number.", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(this, "Invalid health card number", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.prescription:
			if (hcn.length() == 6){
				boolean inDatabase = dataSource.patientExists(hcn);
				if (inDatabase){
					dataSource.close();
					Intent intent = new Intent(this, SetPrescription.class);
					intent.putExtra("hcnKey",  hcn);
					startActivity(intent);
				}
				else {
					Toast.makeText(this, "No patient associated with this health card number.", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(this, "Invalid health card number", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
}
