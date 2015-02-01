package com.method.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.base.classes.MySQLHelper;
import com.base.classes.Patient;
import com.base.classes.Prescription;
import com.base.classes.Status;
import com.base.classes.Treated;
import com.base.classes.Visit;

public class DataSource {
	private SQLiteDatabase db;
	private MySQLHelper dbHelper;
	
// Database Manger methods	
	public DataSource(Context context){
		dbHelper = new MySQLHelper(context);
	}
	public void open() throws SQLException{
		db = dbHelper.getWritableDatabase();
	}
	public void close(){
		dbHelper.close();
	}
	public SQLiteDatabase getDB(){
		return db;
	}

//Methods for Patient
	/**
	 * 
	 * @param HCN
	 * @param name
	 * @param DoB
	 * @return a Patient object with the information of the newly 
	 * inserted row
	 */
	public Patient createPatient(String HCN, String name, String DoB){
		String hcn = "'" + HCN + "'";
		
		ContentValues values = new ContentValues();
		values.put(MySQLHelper.P_COLUMN_HCN, HCN);
		values.put(MySQLHelper.P_COLUMN_Name, name);
		values.put(MySQLHelper.P_COLUMN_DOB, DoB);
		
		db.insert(MySQLHelper.TABLE_Patient, null, values);
		
		Cursor cursor = db.rawQuery("SELECT *" 
				+ " FROM " + MySQLHelper.TABLE_Patient
				+ " WHERE " + MySQLHelper.P_COLUMN_HCN + " = " + hcn  
				+ ";", null);
		cursor.moveToFirst();
		
		Patient newPatient = CursorTo.toPatient(cursor);
		
		cursor.close();
		
		return newPatient;
	}

	/**
	 * 
	 * @param HCN
	 * @return True if the Patient exists in the Patient table of the 
	 * Database. False otherwise.
	 */
	public boolean patientExists(String HCN){
		String hcn = "'" + HCN + "'";
		
		Cursor c = db.rawQuery("Select * "
				+ " FROM " + MySQLHelper.TABLE_Patient
				+ " WHERE " + MySQLHelper.P_COLUMN_HCN + " = " + hcn 
				+ ";", null);
		boolean exists = (c.getCount() > 0);
		c.close();
		return exists;
	}
	
	/**
	 * 
	 * @param db
	 * @return True if the Patient table of db
	 */
	public boolean patientTablePopulated(SQLiteDatabase db){
		Cursor c = db.query(MySQLHelper.TABLE_Patient, null, null, null, null, null, null);
		c.moveToFirst();
		boolean result = c.getCount() > 0;
		c.close();
		
		return result;
	}

// Methods for Prescription
	/**
	 * 
	 * @param name
	 * @param instructions
	 * @param vid
	 * updates visit table's prescription_id column
	 * @return the Prescription object with the information of the newly inserted row
	 */
	public Prescription createPrescription(String name, 
			String instructions, long vid){
		
		ContentValues values = new ContentValues();
		values.put(MySQLHelper.Pre_COLUMN_Name, name);
		values.put(MySQLHelper.Pre_COLUMN_Inst, instructions);
		
		long id = db.insert(MySQLHelper.TABLE_Prescription, null, values);
		
		Cursor c = db.query(MySQLHelper.TABLE_Prescription, null,
				MySQLHelper.Pre_COLUMN_ID + " = " + id, null, null, 
				null, null);
		c.moveToFirst();
		Prescription presc = CursorTo.toPresc(c);
		c.close();
		
	//updating visit table so it has the proper prescription id for pre_id column
		ContentValues visitValues = new ContentValues();
		visitValues.put(MySQLHelper.V_COLUMN_PreID, id);
		db.update(MySQLHelper.TABLE_Visit, visitValues, 
				MySQLHelper.V_COLUMN_ID + " = " + vid, null);
		
		return presc;
	}

// Methods for Status
	
	/**
	 * 
	 * @param HCN
	 * @param date
	 * @param time
	 * @param temp
	 * @param bp_dia
	 * @param bp_sys
	 * @param hr
	 * @param symptoms
	 * @return a Status object with the information of the newly inserted row
	 */
	public Status createStatus(String HCN, String date, String time, double temp, 
			int bp_dia, int bp_sys, int hr, String symptoms){
		Patient patient = GetCalls.getPatient(db, HCN);
		long vid = GetCalls.getCurrentVisit(db, HCN).getId();
		
		int urgency = 0;
		if(patient.youngerThan2(date)){
			urgency += 1;
		}if(temp >= 39.0){
			urgency += 1;
		}if(bp_sys >= 100 || bp_dia >= 90){
			urgency += 1;
		}if(hr >= 100 || hr <= 50){
			urgency += 1;
		}
		
		ContentValues values = new ContentValues();
		values.put(MySQLHelper.S_COLUMN_Date, date);
		values.put(MySQLHelper.S_COLUMN_Time, time);
		values.put(MySQLHelper.S_COLUMN_Temp, temp);
		values.put(MySQLHelper.S_COLUMN_HR, hr);
		values.put(MySQLHelper.S_COLUMN_BP_Dia, bp_dia);
		values.put(MySQLHelper.S_COLUMN_BP_Sys, bp_sys);
		values.put(MySQLHelper.S_COLUMN_Symptoms, symptoms);
		values.put(MySQLHelper.S_COLUMN_Urgency, urgency);
		values.put(MySQLHelper.S_COLUMN_VID, vid);
		
		long id = db.insert(MySQLHelper.TABLE_Status, null, values);
		
		Cursor c = db.query(MySQLHelper.TABLE_Status, null, 
				MySQLHelper.S_COLUMN_ID + " = " + id, null, null, null, null);
		c.moveToFirst();
		
		Status status = CursorTo.toStatus(c);
		
		c.close();
		
		return status;
	}
	
	/**
	 * 
	 * @param vid
	 * @return True if there exists a row in the Status table in the database where
	 * the value of the vid column == the given vid. Else return False.
	 */
	public boolean lastStatusExists(long vid){	
		Cursor c = db.query(MySQLHelper.TABLE_Status, null, 
				MySQLHelper.S_COLUMN_VID + " = " + vid, 
				null, null, null, null);
		c.moveToFirst();
		boolean res = (c.getCount() > 0);
		c.close();
		return res;
	}

// Methods for Treated
	
	/**
	 * 
	 * @param HCN
	 * @param date
	 * @param time
	 * @return a Treated object with the information of the newly inserted row
	 */
	public Treated createTreated(String HCN, String date, String time){
		long vid = GetCalls.getCurrentVisit(db, HCN).getId();
		
		ContentValues values = new ContentValues();
		values.put(MySQLHelper.T_COLUMN_Date, date);
		values.put(MySQLHelper.T_COLUMN_Time, time);
		
		long tid = db.insert(MySQLHelper.TABLE_Treated, null, values);
				
		Cursor c = db.query(MySQLHelper.TABLE_Treated, null, 
				MySQLHelper.T_COLUMN_ID + " = " + tid, 
				null, null, null, null);
		c.moveToFirst();
		
		Treated treated = CursorTo.toTreated(c);
		
		c.close();
	//updating visit table so it has the proper treated id for the tid column
		ContentValues visitValues = new ContentValues();
		visitValues.put(MySQLHelper.V_COLUMN_TID, tid);
		db.update(MySQLHelper.TABLE_Visit, visitValues, 
				MySQLHelper.V_COLUMN_ID + " = " + vid, null);

		
		return treated;
		}	
	
// Methods for Visit
	
	/**
	 * 
	 * @param HCN
	 * @param date
	 * @param time
	 * @return a Visit object with the information of the newly inserted row
	 */
	public Visit createVisit(String HCN, String date, String time){
		ContentValues values = new ContentValues();
				
		values.put(MySQLHelper.V_COLUMN_Arrival_Date, date);
		values.put(MySQLHelper.V_COLUMN_Arrival_Time, time);
		
		values.put(MySQLHelper.V_COLUMN_HCN, HCN);
		// When patient has yet to be seen by doctor TID is 0
		values.put(MySQLHelper.V_COLUMN_TID, 0);
		values.put(MySQLHelper.V_COLUMN_PreID, 0);
				
		Long id = db.insert(MySQLHelper.TABLE_Visit, null, values);
		Cursor cursor = db.rawQuery("SELECT *" 
				+ " FROM " + MySQLHelper.TABLE_Visit
				+ " WHERE " + MySQLHelper.V_COLUMN_ID + " = " + id  
				+ ";", null);
		cursor.moveToFirst();
		
		Visit visit = CursorTo.toVisit(cursor);
		
		cursor.close();
		
		return visit;
	}
	
	/**
	 * 
	 * @param HCN
	 * @return True if there exists a row in Visit table in the database where
	 * the value of the HCN column == the given HCN and the value of the
	 * TID column == 0. False otherwise.
	 */
	public boolean isCurrentPatient(String HCN){
		String hcn = "'" + HCN + "'";

		Cursor cursor = db.rawQuery("SELECT *"
				+ " FROM " + MySQLHelper.TABLE_Visit + " AS v, " 
					+ MySQLHelper.TABLE_Patient + " AS p"
				+ " WHERE v." + MySQLHelper.V_COLUMN_HCN + " = " + hcn 
				+ " AND v." + MySQLHelper.V_COLUMN_TID + " = 0;", null);
		
		cursor.moveToFirst();
		
		boolean result = (cursor.getCount() > 0);
		
		cursor.close();
		
		return result;
	}
}
