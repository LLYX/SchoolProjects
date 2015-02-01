package com.method.classes;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.base.classes.MySQLHelper;
import com.base.classes.Patient;
import com.base.classes.Visit;

public class Sorts {
	
	/**
	 * 
	 * @param db
	 * @return List<String> of all the patients that are currently
	 * visiting the hospital sorted by their arrival time. (Earliest
	 * arrival times at top)
	 */
	public static List<String> sortedWaitTime(SQLiteDatabase db){
		List<String> result = new ArrayList<String>();
		
		Cursor c = db.rawQuery(
				"SELECT *"
				+ " FROM " + MySQLHelper.TABLE_Patient + " AS p LEFT JOIN " 
					+ MySQLHelper.TABLE_Visit + " AS v"
				+ " ON p." + MySQLHelper.P_COLUMN_HCN + " = v." + MySQLHelper.V_COLUMN_HCN
				+ " WHERE " + "v." + MySQLHelper.V_COLUMN_TID + " = 0"
				+ " ORDER BY " + "v." + MySQLHelper.V_COLUMN_Arrival_Date + ","
					+ " v." + MySQLHelper.V_COLUMN_Arrival_Time + "," 
					+ " p." + MySQLHelper.P_COLUMN_Name + " DESC"
				+ ";", null);
		c.moveToFirst();
		
		while (!c.isAfterLast()){
			Patient patient = CursorTo.toPatient(c);
			Visit visit = CursorTo.toVisit(c);
			result.add(patient.toString() + visit.toString());
			c.moveToNext();
		}
		
		c.close();
		
		return result;
	}
	
	/**
	 * 
	 * @param db
	 * @return List<String> of all the patients that are currently visiting the
	 * hospital sorted by their urgency. (The most urgent at the top)
	 */
	public static List<String> sortedUrgency(SQLiteDatabase db){
		List<String> result = new ArrayList<String>();
		
		String cmd = "SELECT "
				/**
				 * Select columns HCN, name, and DoB from Patient Table and urgency from Status Table
				 * call Max() on urgency column from Status Table as "latest_urgency"
				 */
				+ "p." + MySQLHelper.P_COLUMN_HCN
				+ ", p." + MySQLHelper.P_COLUMN_Name
				+ ", p." + MySQLHelper.P_COLUMN_DOB
				+ ", Max(s." + MySQLHelper.S_COLUMN_ID
				+ ") AS latest_urgency" +
			" FROM "
			/**
			 * Join tables Patient p, Visit v and Status s, but only the rows
			 * that have p.HCN = v.HCN and v.id = s.id
			 */
				+ MySQLHelper.TABLE_Patient + " AS p LEFT JOIN " + MySQLHelper.TABLE_Visit
					+ " AS v ON p." + MySQLHelper.P_COLUMN_HCN + " = v." + MySQLHelper.V_COLUMN_HCN
				+ " LEFT JOIN " + MySQLHelper.TABLE_Status 
					+ " AS s ON v." + MySQLHelper.V_COLUMN_ID + " = s." + MySQLHelper.S_COLUMN_VID +
			" WHERE "
			/**
			 * Where only rows with v.tid = 0 i.e. haven't been seen by doctor  
			 */
				+ "v." + MySQLHelper.V_COLUMN_TID + " = 0" + 
			 " GROUP BY "
			/**
			 * Group by column HCN, Name, and DoB from Patient Table
			 */
				+ "p." + MySQLHelper.P_COLUMN_HCN
				+ ", p." + MySQLHelper.P_COLUMN_Name
				+ ", p." + MySQLHelper.P_COLUMN_DOB +
			" ORDER BY "
			/**
			 * order by column urgency from status table in descending order and
			 * by column name from name from Patient table in ascending order
			 */
				+ "s." + MySQLHelper.S_COLUMN_Urgency + " DESC, "
				+ "p." + MySQLHelper.P_COLUMN_Name;
		Cursor c = db.rawQuery(cmd + ";", null);
		c.moveToFirst();
		while (!c.isAfterLast()){
			Patient patient = CursorTo.toPatient(c);
			Visit visit = GetCalls.getCurrentVisit(db, patient.getHCN());
			int urgency = GetCalls.getLastStatus(db, visit.getId()).getUrgency();
			
			result.add(patient.toString() + visit.toString() + "Urgency: " + urgency);

			c.moveToNext();
		}
		
		c.close();
		return result;
	}
}
