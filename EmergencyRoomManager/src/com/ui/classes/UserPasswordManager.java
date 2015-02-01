package com.ui.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

import xu.leon.patientdatabasemanager.R;

import android.content.res.Resources;

public class UserPasswordManager {
	
	public TreeMap<String, String[]> Users;
	
	/**
	 * 
	 * @param res
	 * @throws IOException
	 * Created a Map of accepted passwords from passwords.txt where the username is
	 * the key and the corresponding password is the value
	 */
	public UserPasswordManager(Resources res) throws IOException {
		this.Users = new TreeMap<String, String[]>();
		InputStream inputStream = res.openRawResource(R.raw.passwords);
		populate(inputStream);
		inputStream.close();
	}
	
	private void populate(InputStream inputStream) throws IOException {
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        
        try {
            while ((line = reader.readLine()) != null) {
            	String[] uPC = line.split(",");
	        	String[] pC = new String[2];
	        	pC[0] = uPC[1];
	        	pC[1] = uPC[2];
	        	Users.put(uPC[0], pC);
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        reader.close();
	}
}
