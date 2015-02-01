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

public class SubmitHCN extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_hcn);
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
	 * If the submit button is clicked:
	 * 		if the inputed health card number != 6  -> show user message
	 * 		else if the inputed health card number does not exist in the database -> go to RegisterPatient
	 * 		else if patient not on wait list -> go to NewVisit
	 * 		else go to CurrentPatient
	 * If the sort_by_dt button or sort_by_urgency is pressed -> go to Waitlist
	 */
	public void onClick(View view){
		DataSource dataSource = new DataSource(this);
		dataSource.open();
		EditText hcnText = (EditText) findViewById(R.id.hcn);
		String hcn = hcnText.getText().toString();
		switch(view.getId()){
		case R.id.submit:
			if (hcn.length() == 6){
				boolean inDatabase = dataSource.patientExists(hcn);
				if (inDatabase){
					boolean onWaitlist = dataSource.isCurrentPatient(hcn);
					if (onWaitlist){
						dataSource.close();
						Intent intent = new Intent(this, CurrentPatient.class);
						intent.putExtra("hcnKey", hcn);
						startActivity(intent);
						}
					else {
						dataSource.close();
						Intent intent = new Intent(this, NewVisit.class);
						intent.putExtra("hcnKey", hcn);
						startActivity(intent);
					}
				}
				else {
					dataSource.close();
					Intent intent = new Intent(this, RegisterPatient.class);
					intent.putExtra("hcnKey", hcn);
					startActivity(intent);
				}
			}
			else {
				Toast.makeText(this, "Invalid health card number", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.sort_by_dt:
			dataSource.close();
			Intent intent = new Intent(this, Waitlist.class);
			intent.putExtra("hcnKey",  hcn);
			intent.putExtra("orderBy", "wait");
			startActivity(intent);
			break;
		case R.id.sort_by_urgency:
			dataSource.close();
			Intent intent2 = new Intent(this, Waitlist.class);
			intent2.putExtra("hcnKey",  hcn);
			intent2.putExtra("orderBy", "urgency");
			startActivity(intent2);
			break;
		}
	}
}
