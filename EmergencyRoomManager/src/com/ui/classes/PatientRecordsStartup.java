package com.ui.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import xu.leon.patientdatabasemanager.R;

import android.content.Context;
import android.content.res.Resources;

import com.method.classes.DataSource;

public class PatientRecordsStartup {

	public DataSource dataSource;
	public InputStream inputStream;
	
	/**
	 * 
	 * @param context
	 * @param res
	 * @throws IOException
	 * Reads a file from raw folder called patient_records.txt, if tables are populated
	 * pass, else populate table from file.
	 */
	public PatientRecordsStartup(Context context, Resources res) throws IOException{
		dataSource = new DataSource(context);
		dataSource.open();
		inputStream = res.openRawResource(R.raw.patient_records);
		if (!dataSource.patientTablePopulated(dataSource.getDB())){
			populate(inputStream);
			inputStream.close();
		}
		else {
			inputStream.close();
		}
		dataSource.close();
	}
	
	/**
	 * 
	 * @param inputStream
	 * @throws IOException
	 * Inserts a new row into Patient table of the database for each line in the file
	 */
	public void populate(InputStream inputStream) throws IOException{
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        
        try {
            while ((line = reader.readLine()) != null) {
            	String[] personalData = line.split(",");
            	dataSource.createPatient(personalData[0], personalData[1], personalData[2]);
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        reader.close();
	}
}
