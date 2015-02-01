package com.ui.classes;

import xu.leon.patientdatabasemanager.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.method.classes.DataSource;
import com.method.classes.GetCalls;

public class SetPrescription extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_prescription);
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
			Intent intent = new Intent(this, PhysicianActions.class);
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
	 * Get the current visit's vid and from that set prescription information
	 */
	public void onClick(View view){
		EditText medText = (EditText) findViewById(R.id.meds);
		EditText instText = (EditText) findViewById(R.id.instructions);
		String medNames = medText.getText().toString();
		String instructions = instText.getText().toString();
		DataSource dataSource = new DataSource(this);
		dataSource.open();
		Intent intent = getIntent();
		String hcn = (String) intent.getExtras().get("hcnKey");
		long vid = GetCalls.getCurrentVisit(dataSource.getDB(), hcn).getId();
		dataSource.createPrescription(medNames, instructions, vid);
		dataSource.close();
	}
}
