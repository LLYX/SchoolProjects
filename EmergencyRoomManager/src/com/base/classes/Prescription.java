package com.base.classes;

/**
 * @param args
 */
public class Prescription {
	private long id;
	private String name;
	private String instructions;

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
	 * Get/Set name
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get/Set instructions
	 */
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	/**
	 * Turns the object to a String representation 
	 */
	@Override
	public String toString(){
		return "Was prescribed: \n" +
				"Medication name: " + name + "\n"
				+ "Instructions: " + instructions + "\n\n";
	}
	
}
