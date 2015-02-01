package com.base.classes;

public class Visit {
	/**
	 * @param args
	 */
	private long id;
	private String date;
	private String time;
//Foreign keys
	private String HCN;
	private long treated_id;
	private long perscription_id;

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

//Foreign Keys
	
	/**
	 * Get/Set HCN
	 */
	public String getHCN() {
		return HCN;
	}
	public void setHCN(String hCN) {
		HCN = hCN;
	}
	
	/**
	 * Get/Set treated_id
	 */
	public long getTreated_id() {
		return treated_id;
	}
	public void setTreated_id(long treated_id) {
		this.treated_id = treated_id;
	}
	
	/**
	 * Get/Set perscription_id
	 */
	public long getPerscription_id() {
		return perscription_id;
	}
	public void setPerscription_id(long perscription_id) {
		this.perscription_id = perscription_id;
	}
	
	/**
	 * Return the object as a String representation
	 */
	@Override
	public String toString(){
		return "Arrived on: " + date + " at " + time + "\n";
	}

}
