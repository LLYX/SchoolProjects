package com.ui.classes;

import xu.leon.patientdatabasemanager.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.method.classes.DataSource;
import com.method.classes.GetCalls;

public class CurrentPatient extends Activity {
	
	public DataSource dataSource;
	public Intent intent;
	public String hcn;
	
	/**
	 * Set the text view to represent the Patient object associated with the
	 * inputed health card number
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_patient);
		
		TextView personalInfoText = (TextView) findViewById(R.id.personal_information);
		dataSource = new DataSource(this);
		dataSource.open();
		intent = getIntent();
		hcn = (String) intent.getExtras().get("hcnKey");
		
		personalInfoText.setText(GetCalls.getPatient(dataSource.getDB(), hcn).toString());
		
		Toast.makeText(this, "This patient is currently on the waitlist.", Toast.LENGTH_LONG).show();
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
	 * If patient_history button pressed -> go to PatientHistory
	 * If update_patient button pressed -> go to UpdatePatient
	 */
	public void onClick(View view){
		switch(view.getId()){
		case R.id.patient_history:
			dataSource.close();
			Intent intent2 = new Intent(this, PatientHistory.class);
			intent2.putExtra("hcnKey", hcn);
			startActivity(intent2);
			break;
		case R.id.update_patient:
			dataSource.close();
			Intent intent3 = new Intent(this, UpdatePatient.class);
			intent3.putExtra("hcnKey", hcn);
			startActivity(intent3);
			break;
		}
	}
}
