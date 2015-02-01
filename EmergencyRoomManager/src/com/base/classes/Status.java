package com.base.classes;

public class Status {
	/**
	 * @param args
	 */
	private long id;
	private String date;
	private String time;
	private double temp;
	private int bp_dia;
	private int bp_sys;
	private int hr;
	private String symptoms;
	private int urgency;
//Foreign Key
	private long VID;
	
	/**
	 * Get/Set id 
	 */
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Get/Set date 
	 */
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * Get/Set time
	 */
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	/**
	 * Get/Set temp 
	 */
	public double getTemp() {
		return temp;
	}
	public void setTemp(double temp) {
		this.temp = temp;
	}
	
	/**
	 * Get/Set bp_dia
	 */
	public int getBp_dia() {
		return bp_dia;
	}
	public void setBp_dia(int bp_dia) {
		this.bp_dia = bp_dia;
	}
	
	/**
	 * Get/Set bp_sys
	 */
	public int getBp_sys() {
		return bp_sys;
	}
	public void setBp_sys(int bp_sys) {
		this.bp_sys = bp_sys;
	}
	
	/**
	 * Get/Set hr 
	 */
	public int getHr() {
		return hr;
	}
	public void setHr(int hr) {
		this.hr = hr;
	}
	
	/**
	 * Get/Set symptoms 
	 */
	public String getSymptoms() {
		return symptoms;
	}
	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}
	
	/**
	 * Get/Set urgency
	 */
	public int getUrgency() {
		return urgency;
	}
	public void setUrgency(int urgency) {
		this.urgency = urgency;
	}
	
	/**
	 * Get/Set VID
	 */
	public long getVID() {
		return VID;
	}
	public void setVID(long vID) {
		VID = vID;
	}
	
	/**
	 * Turns the object into a String representation 
	 */
	@Override
	public String toString(){
		String result =  "Date: " + date + "\n" 
						+ "Time: " + time + "\n";
		if(temp != 0){
			result += "Temperature: " + temp + "\n";
		}if(bp_dia != 0){
			result += "Diastolic Blood Pressure: " + bp_dia + "\n";
		}if(bp_sys != 0){
			result += "Systolic Blood Pressure: " + bp_sys + "\n";
		}if(hr != 0){
			result += "Heart Rate: " + hr + "\n";
		}
		if(symptoms != null && !symptoms.isEmpty()){
			 result += "Symptoms: " + symptoms + "\n\n";
		}
		return result;
	}

}
