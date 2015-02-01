package com.ui.classes;

import xu.leon.patientdatabasemanager.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.base.classes.Status;
import com.method.classes.DataSource;
import com.method.classes.GetCalls;

public class UpdatePatient extends Activity {
	
	int oldUrgency;
	int newUrgency;
	String hcn;
	DataSource dataSource;
	Status status;
	long vid;
	
	/**
	 * Calculates the current urgency before the update button is pressed
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_patient);
		
		Intent intent = getIntent();
		hcn = intent.getExtras().getString("hcnKey");
		dataSource = new DataSource(this);
		dataSource.open();
		vid = GetCalls.getCurrentVisit(dataSource.getDB(), hcn).getId();
		if (dataSource.lastStatusExists(vid)){
			status = GetCalls.getLastStatus(dataSource.getDB(), vid);
			oldUrgency = status.getUrgency();
		}
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
	
	/**
	 * 
	 * @param view
	 * If the update button is created -> create new status with the given information,
	 * 										if no new information is given for a particular
	 * 										variable then use the information from the last status
	 * If update seen_by_doc_check is checked -> create row in treated and end the visit
	 */
	public void onClick(View view){
		EditText dateText = (EditText) findViewById(R.id.update_date);
		EditText timeText = (EditText) findViewById(R.id.update_time);
		EditText tempText = (EditText) findViewById(R.id.temperature);
		EditText hrText = (EditText) findViewById(R.id.heart_rate);
		EditText sbpText = (EditText) findViewById(R.id.systolic_bp);
		EditText dbpText = (EditText) findViewById(R.id.diastolic_bp);
		EditText sympText = (EditText) findViewById(R.id.symptoms);
		
		String date = dateText.getText().toString();
		String time = timeText.getText().toString();
		String tempStr = tempText.getText().toString();
		String hrStr = hrText.getText().toString();
		String sbpStr = sbpText.getText().toString();
		String dbpStr = dbpText.getText().toString();
		String symp = sympText.getText().toString();
		
		double temp;
		int hr;
		int sbp;
		int dbp;
		
		if (tempStr.isEmpty()){
			temp = status.getTemp();
		}
		else {
			temp = Double.parseDouble(tempStr);
		}
		if (hrStr.isEmpty()){
			hr = status.getHr();
		}
		else {
			hr = Integer.parseInt(hrStr);
		}
		if (sbpStr.isEmpty()){
			sbp = status.getBp_sys();
		}
		else {
			sbp = Integer.parseInt(sbpStr);
		}
		if (dbpStr.isEmpty()){
			dbp = status.getBp_dia();
		}
		else {
			dbp = Integer.parseInt(dbpStr);
		}
		
		switch(view.getId()){
		case R.id.update:
			newUrgency = dataSource.createStatus(hcn, date, time, temp, dbp, sbp, hr, symp).getUrgency();
			break;
		case R.id.triage:
			String urgencyText = "";
			
			if (newUrgency >= 3){
				urgencyText+= "Urgent, ";
			}
			else if (newUrgency == 2){
				urgencyText+= "Less Urgent, ";
			}
			else {
				urgencyText+= "Non Urgent, ";
			}
			
			if (newUrgency == oldUrgency){
				urgencyText+= "status unchanged.";
			}
			else if (newUrgency > oldUrgency){
				urgencyText+= "status worsening.";
			}
			else {
				urgencyText+= "status improving.";
			}
			
			TextView urgencyView = (TextView) findViewById(R.id.urgency);
			urgencyView.setText(urgencyText);
			status = GetCalls.getLastStatus(dataSource.getDB(), vid);
			oldUrgency = status.getUrgency();
			break;
		case R.id.seen_by_doc_check:
			CheckBox seenByDoc = (CheckBox) findViewById(R.id.seen_by_doc_check);
			if (seenByDoc.isChecked()){
				dataSource.createTreated(hcn, date, time);
			}
			break;
		}
	}
}
