/*
	Author:Dipayan Das
	Roll:CS1726.
	Date:Aug 3, 2018
	Bhabatu Sarba Mangalam
*/

package com.dipayan.bean;

import java.util.ArrayList;
import java.util.List;

import com.dipayan.dataStructureChar.CharDataStructure;

public class TransactionBean {
	private List<List<CharDataStructure>> transactionDataBase;
	public TransactionBean() {
		this.transactionDataBase=new ArrayList<List<CharDataStructure>>();
	}
	public List<List<CharDataStructure>> getTransactionDataBase() {
		return this.transactionDataBase;
	}
	public void setTransactionDataBase(List<List<CharDataStructure>> transactionDataBase) {
		this.transactionDataBase = transactionDataBase;
	}
	
	
	
	
	public String toString() {
		String result="";
		for(List<CharDataStructure> temp:transactionDataBase) {
			for(CharDataStructure tempCharacter:temp) {
				result=result+" "+tempCharacter;
			}
			result=result+"\n";
		}
		
		
		return result;
		
	} 
	
	
	
}
