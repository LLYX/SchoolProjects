package com.method.classes;

import android.database.Cursor;

import com.base.classes.MySQLHelper;
import com.base.classes.Patient;
import com.base.classes.Prescription;
import com.base.classes.Status;
import com.base.classes.Treated;
import com.base.classes.Visit;

public class CursorTo {
	/**
	 * 
	 * @param cursor
	 * Cursor should only one row assigned to it
	 * @return a Patient object with the information stored in the cursor
	 */
	public static Patient toPatient(Cursor cursor){
		Patient patient = new Patient();

		patient.setName(cursor.getString(cursor.getColumnIndexOrThrow(
				MySQLHelper.P_COLUMN_Name)));
		patient.setDoB(cursor.getString(cursor.getColumnIndexOrThrow(
									MySQLHelper.P_COLUMN_DOB)));
		patient.setHCN(cursor.getString(cursor.getColumnIndexOrThrow(
									MySQLHelper.P_COLUMN_HCN)));
		
		return patient;
	}
	
	/**
	 * 
	 * @param cursor
	 * Cursor should only one row assigned to it
	 * @return a Prescription object with the information stored in the cursor
	 */
	public static Prescription toPresc(Cursor cursor){
		Prescription presc = new Prescription();
		
		presc.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLHelper.Pre_COLUMN_ID)));
		presc.setName(cursor.getString(cursor.getColumnIndexOrThrow(MySQLHelper.Pre_COLUMN_Name)));
		presc.setInstructions(cursor.getString(cursor.getColumnIndexOrThrow(MySQLHelper.Pre_COLUMN_Inst)));
		
		return presc;
	}
	
	/**
	 * 
	 * @param cursor
	 * Cursor should only one row assigned to it
	 * @return a Status object with the information stored in the cursor
	 */
	public static Status toStatus(Cursor cursor){
		Status status = new Status();
		
		status.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLHelper.S_COLUMN_ID)));
		status.setDate(cursor.getString(cursor.getColumnIndexOrThrow(MySQLHelper.S_COLUMN_Date)));
		status.setTime(cursor.getString(cursor.getColumnIndexOrThrow(MySQLHelper.S_COLUMN_Time)));
		status.setTemp(cursor.getDouble(cursor.getColumnIndexOrThrow(MySQLHelper.S_COLUMN_Temp)));
		status.setBp_dia(cursor.getInt(cursor.getColumnIndexOrThrow(MySQLHelper.S_COLUMN_BP_Dia)));
		status.setBp_sys(cursor.getInt(cursor.getColumnIndexOrThrow(MySQLHelper.S_COLUMN_BP_Sys)));
		status.setHr(cursor.getInt(cursor.getColumnIndexOrThrow(MySQLHelper.S_COLUMN_HR)));
		status.setSymptoms(cursor.getString(cursor.getColumnIndexOrThrow(MySQLHelper.S_COLUMN_Symptoms)));
		status.setUrgency(cursor.getInt(cursor.getColumnIndexOrThrow(MySQLHelper.S_COLUMN_Urgency)));
		status.setVID(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLHelper.S_COLUMN_VID)));
		
		return status;
	}
	
	/**
	 * 
	 * @param cursor
	 * Cursor should only one row assigned to it
	 * @return a Treated object with the information stored in the cursor
	 */
	public static Treated toTreated(Cursor cursor){
		Treated doctor = new Treated();

		doctor.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLHelper.T_COLUMN_ID)));
		doctor.setDate(cursor.getString(cursor.getColumnIndexOrThrow(MySQLHelper.T_COLUMN_Date)));
		doctor.setTime(cursor.getString(cursor.getColumnIndexOrThrow(MySQLHelper.T_COLUMN_Time)));
		
		return doctor;
	}
	
	/**
	 * 
	 * @param cursor
	 * Cursor should only one row assigned to it
	 * @return a Visit object with the information stored in the cursor
	 */
	public static Visit toVisit(Cursor cursor) {
		Visit visit = new Visit();
		
		visit.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLHelper.V_COLUMN_ID)));
		visit.setDate(cursor.getString(cursor.getColumnIndexOrThrow(MySQLHelper.V_COLUMN_Arrival_Date)));
		visit.setTime(cursor.getString(cursor.getColumnIndexOrThrow(MySQLHelper.V_COLUMN_Arrival_Time)));
		
		visit.setHCN(cursor.getString(cursor.getColumnIndexOrThrow(MySQLHelper.V_COLUMN_HCN)));
		visit.setTreated_id(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLHelper.V_COLUMN_TID)));
		visit.setPerscription_id(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLHelper.V_COLUMN_PreID)));
		
		return visit;
	}
}
