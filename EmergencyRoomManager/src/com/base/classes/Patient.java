package com.base.classes;

public class Patient {

	/**
	 * @param args
	 */
//NOTE: All dates are kept in the format: yyyy/MM/dd
	private String HCN;
	private String name;
	private String DoB;
	
	/**
	 * Get/Set for HCN
	 */
	public String getHCN() {
		return HCN;
	}
	public void setHCN(String hCN) {
		HCN = hCN;
	}

	/**
	 * Get/Set for name
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get/Set DoB
	 */
	public String getDoB() {
		return DoB;
	}
	public void setDoB(String doB) {
		DoB = doB;
	}
	
	/**
	 * Checks if the patient is younger than age 2
	 */
	public boolean youngerThan2(String currentDate){
		int currentYear = Integer.parseInt(currentDate.substring(0, 4));
		int currentMonth = Integer.parseInt(currentDate.substring(5, 7));
		int currentDay = Integer.parseInt(currentDate.substring(8));
		
		int birthYear = Integer.parseInt(DoB.substring(0, 4));
		int birthMonth = Integer.parseInt(DoB.substring(5, 7));
		int birthDay = Integer.parseInt(DoB.substring(8));
		
		if(currentYear - birthYear == 2){
			if(birthMonth == currentMonth){
				if(birthDay > currentDay){
					return false;
				}
			}else if(birthMonth > currentMonth){
				return false;
			}
		}else if (currentYear - birthYear < 2) {
			return false;
		}
		return true;
	}
	
	/**
	 * Converts object to a String representation
	 */
	@Override
	public String toString(){
		return "Health Card Number: " + HCN +
				"\nPatient Name: " + name
				+ "\nDate of Birth: " + DoB + "\n";
	}
}
