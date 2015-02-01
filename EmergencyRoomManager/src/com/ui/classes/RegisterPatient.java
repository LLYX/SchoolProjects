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

public class RegisterPatient extends Activity {

	DataSource dataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_patient);
		
		dataSource = new DataSource(this);
		dataSource.open();
		
		Toast.makeText(this, "The inputted health card number is not associated with any patient in the database.", Toast.LENGTH_LONG).show();
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
	 * Create patient in the database -> go to NewVisit
	 */
	public void onClick(View view){
		EditText nameText = (EditText) findViewById(R.id.name);
		EditText dobText = (EditText) findViewById(R.id.dob);
		Intent intent = getIntent();
		String hcn = (String) intent.getExtras().get("hcnKey");
		String name = nameText.getText().toString();
		String dob = dobText.getText().toString();
		
		dataSource.createPatient(hcn, name, dob);
		dataSource.close();
		
		Intent intent2 = new Intent(this, NewVisit.class);
		intent2.putExtra("hcnKey", hcn);
		startActivity(intent2);
	}
}
