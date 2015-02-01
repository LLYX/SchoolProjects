package com.method.classes;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.base.classes.MySQLHelper;
import com.base.classes.Patient;
import com.base.classes.Prescription;
import com.base.classes.Status;
import com.base.classes.Treated;
import com.base.classes.Visit;

public class GetCalls {
	
	/**
	 * 
	 * @param db
	 * @param HCN
	 * @return a specified row from the Patient table of the db who's 
	 * primary key == the given HCN and turns the information into a 
	 * Patient object
	 */
	public static Patient getPatient(SQLiteDatabase db, String HCN){
		String hcn = "'" + HCN + "'";

		Cursor cursor = db.query(MySQLHelper.TABLE_Patient, null, 
				MySQLHelper.P_COLUMN_HCN + " = " + hcn, null, null, null, null);
		cursor.moveToFirst();
		
		Patient patient = CursorTo.toPatient(cursor);
		
		cursor.close();
		
		return patient;
	}
	
	/**
	 * 
	 * @param db
	 * @param preId
	 * @return a specified row from the Prescription table of db 
	 * who's primary key == the given HCN and turns the information into a 
	 * Prescription object
	 */
	public static Prescription getPrescription(SQLiteDatabase db, long preId){
		Cursor c = db.query(MySQLHelper.TABLE_Prescription, null, 
				MySQLHelper.Pre_COLUMN_ID + " = " + preId, null, null, null, null);
		c.moveToFirst();
		
		Prescription presc = CursorTo.toPresc(c);
		
		c.close();
		
		return presc;
	}
	
	/**
	 * 
	 * @param db
	 * @param vid
	 * @return the last row of the Status table from db where the value at the
	 * VID Column == the given vid as a Status object
	 */
	public static Status getLastStatus(SQLiteDatabase db, long vid){
		Cursor c = db.rawQuery("SELECT *"
				+ " FROM " + MySQLHelper.TABLE_Status
				+ " WHERE " + MySQLHelper.S_COLUMN_VID + " = " + vid
				+ " ORDER BY " + MySQLHelper.S_COLUMN_ID
				+ ";", null);
	
		c.moveToLast();
		Status status = CursorTo.toStatus(c);
		c.close();
		
		return status;
	}
	
	/**
	 * 
	 * @param db
	 * @param vid
	 * @return all the rows from the Status table from db where the value of the 
	 * VID Column == the given vid as a List<Status>
	 */
	public static List<Status> getAllStatusforVisit(SQLiteDatabase db, long vid){
		List<Status> statuses = new ArrayList<Status>();
		
		Cursor c = db.rawQuery("SELECT *"
				+ " FROM " + MySQLHelper.TABLE_Status
				+ " WHERE " + MySQLHelper.S_COLUMN_VID + " = " + vid
				+ " ORDER BY " + MySQLHelper.S_COLUMN_ID
				+ ";", null);
		c.moveToFirst();
		
		while (!c.isAfterLast()){
			Status status = CursorTo.toStatus(c);
			statuses.add(status);
			c.moveToNext();
		}
		
		c.close();
		
		return statuses;
	}
	
	/**
	 * 
	 * @param db
	 * @param tid
	 * @return a specific row from the Treated table of the db 
	 * who's primary key == the given tid and turns the information into a 
	 * Treated object
	 */
	public static Treated getTreated(SQLiteDatabase db, long tid){
		Cursor c = db.query(MySQLHelper.TABLE_Treated, null,
				MySQLHelper.T_COLUMN_ID + " = " + tid, 
				null, null, null, null);
		c.moveToFirst();
		Treated treated = CursorTo.toTreated(c);
		c.close();
		
		return treated;
	}
	
	/**
	 * 
	 * @param db
	 * @param HCN
	 * @return a specific row from the Visit table of the db 
	 * who's value HCN Column == the given HCN and turns the information into a 
	 * Prescription object
	 */
	public static Visit getCurrentVisit(SQLiteDatabase db, String HCN){
		Cursor c = db.query(MySQLHelper.TABLE_Visit, null, 
				MySQLHelper.V_COLUMN_HCN + " = " + "'" + HCN + 
				"' AND " + MySQLHelper.V_COLUMN_TID + " = 0" , 
				null, null, null, null);
		c.moveToFirst();
		Visit visit = CursorTo.toVisit(c);
		c.close();
		return visit;
	}
	
	/**
	 * 
	 * @param db
	 * @param HCN
	 * @return all rows from the Visit table of db who's value in the 
	 * HCN Column == the given HCN as a List<Visit>
	 */
	public static List<Visit> getAllVisitsForPatient(SQLiteDatabase db, String HCN){
		List<Visit> visits = new ArrayList<Visit>();
		
		String hcn = "'" + HCN + "'";
		
		Cursor cursor = db.query(MySQLHelper.TABLE_Visit, null, 
				MySQLHelper.V_COLUMN_HCN + " = " + hcn, null, null, null, 
				MySQLHelper.V_COLUMN_ID);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()){
			Visit visit = CursorTo.toVisit(cursor);
			visits.add(visit);
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return visits;
	}
	
	/**
	 * 
	 * @param db
	 * @param HCN
	 * @return List<String> of all the rows from db that is in someway associated
	 * to the given HCN
	 */
	public static List<String> getPatientHistory(SQLiteDatabase db, String HCN){
		List<String> history = new ArrayList<String>();
		List<Visit> visits = getAllVisitsForPatient(db, HCN);
		
		for(Visit visit : visits){
			String visitInfo = "";
			long vid = visit.getId();
			if(visit.getTreated_id() != 0){
				visitInfo += getTreated(db, visit.getTreated_id()).toString();
			}else{
				visitInfo += "Has yet to been seen by the Doctor \n\n";
			}
			if(visit.getTreated_id() != 0){
				long preId = visit.getPerscription_id();
				visitInfo += getPrescription(db, preId).toString();
			}
			for(Status status : getAllStatusforVisit(db, vid)){
				visitInfo += status.toString();
			}
			history.add(visit.toString() + visitInfo);
		}
		return history;
	}
}
