package com.ui.classes;

import java.io.FileNotFoundException;
import java.io.IOException;

import xu.leon.patientdatabasemanager.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	
	Resources res;
	
	/**
	 * Initialize patient record startup
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		res = getResources();
		
		try {
			PatientRecordsStartup init = new PatientRecordsStartup(this, res);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	
	/**
	 * 
	 * @param view
	 * @throws FileNotFoundException
	 * @throws IOException
	 * If the inputed username doesn't match that of the passwords.txt file then
	 * show user "Invalid password"
	 * If the inputed password doesn't match that of the password.txt file then
	 * show user "Invalid username"
	 * If both the inputed username and password matches that of the passwords.txt file
	 * then go to SubmitHCN
	 */
	public void onClick(View view) throws FileNotFoundException, IOException{
		EditText usernameText = (EditText) findViewById(R.id.username);
		EditText passwordText = (EditText) findViewById(R.id.password);
		String username = usernameText.getText().toString();
		String password = passwordText.getText().toString();
		UserPasswordManager uPM = new UserPasswordManager(res);
		
		if (uPM.Users.containsKey(username)){
			if (password.equals(uPM.Users.get(username)[0])){
				if (uPM.Users.get(username)[1].equals("physician")){
					Intent intent = new Intent(this, PhysicianActions.class);
					startActivity(intent);
				}
				else {
					Intent intent = new Intent(this, SubmitHCN.class);
					startActivity(intent);
				}
			}
			else {
				Toast.makeText(this, "Invalid password.", Toast.LENGTH_SHORT).show();
			}
		}
		else {
			Toast.makeText(this, "Invalid username.", Toast.LENGTH_SHORT).show();
		}
	}
}
