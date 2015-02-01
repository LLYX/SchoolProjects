package com.base.classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLHelper extends SQLiteOpenHelper{

	/**
	 * @param args
	 */
	
	/**
	 *  Patient Table, keeps track of personal data.
	 */
	public static final String TABLE_Patient = "Patient";
		public static final String P_COLUMN_HCN = "health_card_number";
		public static final String P_COLUMN_Name = "name";
		public static final String P_COLUMN_DOB = "date_of_birth";
		
	/**
	 * Visit Table, takes the initial information of the visit. Linked to single row in
	 * Patient table.
	 */
	public static final String TABLE_Visit = "Visit";
		public static final String V_COLUMN_ID = "vid";
		public static final String V_COLUMN_Arrival_Date = "arrival_date";
		public static final String V_COLUMN_Arrival_Time = "arrival_time";
		// Foreign key
		public static final String V_COLUMN_HCN = "health_card_number";
		public static final String V_COLUMN_TID = "tid";
		public static final String V_COLUMN_PreID = "pid";
		
	/**
	 * Status table, keeps the vitals and/or symptoms and records the time they were 
	 * recorded. Linked to a single row in visit table.
	 */
	public static final String TABLE_Status = "Status";
		public static final String S_COLUMN_ID = "sid";
		public static final String S_COLUMN_Date = "date";
		public static final String S_COLUMN_Time = "time";
		public static final String S_COLUMN_Temp = "temperature";
		public static final String S_COLUMN_BP_Dia = "bp_Diastolic";
		public static final String S_COLUMN_BP_Sys = "bp_Systolic";
		public static final String S_COLUMN_HR = "heart_rate";
		public static final String S_COLUMN_Symptoms = "symptoms";
		public static final String S_COLUMN_Urgency = "urgency";
		// Foreign Key
		public static final String S_COLUMN_VID = V_COLUMN_ID;
		
	/**
	 * Treated table, tells when the patient has been seen by the doctor, if either 
	 * date or time is null then the patient had not been seen by doctor.
	 */
	public static final String TABLE_Treated = "Treated";
		public static final String T_COLUMN_ID = V_COLUMN_TID;
		public static final String T_COLUMN_Date = "seen_date";
		public static final String T_COLUMN_Time = "seen_time";
		
	/**
	 * Prescription table, tells the name of the medicine, the instructions of use and
	 * for who it was prescribed for
	 */
	public static final String TABLE_Prescription = "Prescription";
		public static final String Pre_COLUMN_ID = V_COLUMN_PreID;
		public static final String Pre_COLUMN_Name = "prescription_name";
		public static final String Pre_COLUMN_Inst = "instructions";
	
	private static final String DB_Name = "ERDatabase.db";
	private static final int DB_Ver = 1;
	
	/**
	 * SQL statements for creating Patient table
	 */
	private static final String create_table_Patient = 
			"create table " + TABLE_Patient + "("
			+ P_COLUMN_HCN + " text primary key, "
			+ P_COLUMN_Name + " text, "
			+ P_COLUMN_DOB + " text);";
	
	/**
	 * SQL statements for creating Visit table
	 */
	private static final String create_table_Visit =
			"create table " + TABLE_Visit + "("
			+ V_COLUMN_ID + " integer primary key autoincrement, "
			+ V_COLUMN_Arrival_Date + " text, "
			+ V_COLUMN_Arrival_Time + " text, "
			+ V_COLUMN_PreID + " integer references "  
					+ TABLE_Prescription + "(" + Pre_COLUMN_ID + "), "
			+ V_COLUMN_TID + " integer references " 
									+ TABLE_Treated + "(" + T_COLUMN_ID + "), "
			+ V_COLUMN_HCN + " text references " 
									+ TABLE_Patient + "(" + P_COLUMN_HCN + "));";
	
	/**
	 * SQL statements for creating status table
	 */
	private static final String create_table_Status = 
			"create table " + TABLE_Status + "("
			+ S_COLUMN_ID + " integer primary key autoincrement, "
			+ S_COLUMN_Date + " text, "
			+ S_COLUMN_Time + " text, "
			+ S_COLUMN_Temp + " double, "
			+ S_COLUMN_BP_Dia + " integer, "
			+ S_COLUMN_BP_Sys + " integer, "
			+ S_COLUMN_HR + " integer, "
			+ S_COLUMN_Symptoms + " text, "
			+ S_COLUMN_Urgency + " integer, "
			+ S_COLUMN_VID + " text references "
									+ TABLE_Visit + "(" + V_COLUMN_ID + "));";
	
	/**
	 * SQL statements for creating Treated table
	 */
	private static final String create_table_Treated =
			"create table " + TABLE_Treated + "("
			+ T_COLUMN_ID + " integer primary key autoincrement, "
			+ T_COLUMN_Date + " text, "
			+ T_COLUMN_Time + " text);";
	
	/**
	 * SQL statements for creating Prescription table
	 */
	private static final String create_table_Perscription =
			"create table " + TABLE_Prescription + "("
			+ Pre_COLUMN_ID + " integer primary key autoincrement, "
			+ Pre_COLUMN_Name + " text, "
			+ Pre_COLUMN_Inst + " text);";
	
	/**
	 * Constructor
	 */
	public MySQLHelper(Context context){
		super(context, DB_Name, null, DB_Ver);
	}

	/**
	 * Method to create table
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(create_table_Patient);
		db.execSQL(create_table_Visit);
		db.execSQL(create_table_Status);
		db.execSQL(create_table_Treated);
		db.execSQL(create_table_Perscription);
	}

	/**
	 * Method to update table
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_Patient);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_Treated);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_Prescription);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_Status);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_Visit);
		    onCreate(db);
	}

}
