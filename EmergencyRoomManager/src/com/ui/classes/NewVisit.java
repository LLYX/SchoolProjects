package com.ui.classes;

import xu.leon.patientdatabasemanager.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.method.classes.DataSource;
import com.method.classes.GetCalls;

public class NewVisit extends Activity {

	public String hcn;
	public DataSource dataSource;
	
	/**
	 * Set the text to represent the string form of the patient associated
	 * with the passed in health card number
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_visit);
		
		dataSource = new DataSource(this);
		dataSource.open();
		
		TextView personalInfoText = (TextView) findViewById(R.id.personal_info);
		Intent intent = getIntent();
		hcn = (String) intent.getExtras().get("hcnKey");
		
		personalInfoText.setText(GetCalls.getPatient(dataSource.getDB(), hcn).toString());
		
		Toast.makeText(this, "This patient is associated with the health card number, but not currently on waitlist.", Toast.LENGTH_LONG).show();
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
	 * If submit3 is pressed then call datasource.createVisit with values of the
	 * inputed string -> go to CurrentPatient
	 * If past_records is pressed -> go to PatientHistory
	 */
	public void onClick(View view){
		switch (view.getId()){
		case R.id.submit3:
			EditText curDateText = (EditText) findViewById(R.id.current_date);
			EditText curTimeText = (EditText) findViewById(R.id.current_time);
			String currentDate = curDateText.getText().toString();
			String currentTime = curTimeText.getText().toString();
			
			dataSource.createVisit(hcn, currentDate, currentTime);
			dataSource.close();
			
			Intent intent2 = new Intent(this, CurrentPatient.class);
			intent2.putExtra("hcnKey", hcn);
			startActivity(intent2);
			break;
		case R.id.past_records:
			Intent intent3 = new Intent(this, PatientHistory.class);
			intent3.putExtra("hcnKey", hcn);
			startActivity(intent3);
		}
	}
}
