package com.base.classes;

public class Treated {
	/**
	 * @param args 
	 */
	private long id;
	private String date;
	private String time;
	
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
	 * Return the object in a string representation 
	 */
	@Override
	public String toString(){
		return "Seen by Doctor on: " + date + ", (" + time + ")\n";
	}

}
