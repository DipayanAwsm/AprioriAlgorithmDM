/*
	Author:Dipayan Das
	Roll:CS1726.
	Date:Aug 3, 2018
	Bhabatu Sarba Mangalam
*/

package com.dipayan.dataStructureChar;

public class CharDataStructure {
	

	private String data;
	
	 public CharDataStructure() {
		// TODO Auto-generated constructor stub
	}
	
	public CharDataStructure(String data) {
		this.data=data;
	}

	public boolean equals(String anObject) {
		return data.equals(anObject);
	}

	public String getData() {
		return data;
	}

	public boolean contains(String dataToCheck) {
		return data.contains(dataToCheck);
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String toString() {
		return this.data+"";
	}

	public int compareTo(String anotherString) {
		return data.compareTo(anotherString);
	}
	
	
	
	
	
	
}
