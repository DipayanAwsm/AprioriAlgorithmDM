/*
	Author:Dipayan Das
	Roll:CS1726.
	Date:Aug 2, 2018
	Bhabatu Sarba Mangalam
*/

package com.dipayan.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.dipayan.constant.Constant;
import com.dipayan.apriori.AprioryAlgorithm;
import com.dipayan.bean.TransactionBean;
import com.dipayan.reader.ReadFile;

/*
 * A-priori algo Used Here
 * 1.Get each Transactions in a Transaction DataBaseList fromcsv lines
 * 
 * */

public class AaprioriMainDipayan {
	public static void main(String[] args) {
		ReadFile reader=new ReadFile();

		System.out.println("--------Welcome To Apriori Algo--------");
		fixminimumSupport();
		fixConfidence();
		TransactionBean transactionDataBase=reader.getListOftransaction(args[0]);
		//System.out.println(transactionDataBase.toString());
		
		AprioryAlgorithm apriory=new AprioryAlgorithm();
		
		 List<String> strongRules=apriory.getRules(transactionDataBase);
		 printRules(strongRules);
	}

	/*Confidence is in Constant its less than 1 and greater than 0*/
	private static void fixConfidence() {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please Enter Confidence(**It must in(0,1)):");
		double confidnce;
		try {
			confidnce=Double.parseDouble(br.readLine());
				if(confidnce>0 && confidnce<=1) {
					Constant.CONFIDENCE_IN_POINT=confidnce;
				}else {
					fixConfidence();
				}
				System.out.println("Minimum Confidence:"+Constant.CONFIDENCE_IN_POINT);
		}catch(Exception e) {
				System.out.println("You have Done Some some thing Wrong");
				fixConfidence();
		}
		
	}

	/*Its fixing up the support generally 1-10 but totally depends on context of input*/
	private static void fixminimumSupport() {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please Enter minimux Support(**It must be an Integer):");
		int minimumSupport;
		try {
				minimumSupport=Integer.parseInt(br.readLine());
				if(minimumSupport>0) {
					Constant.MINIMUM_SUPPORT=minimumSupport;
					System.out.println("Minimum Support:"+Constant.MINIMUM_SUPPORT);
				}else {
					fixminimumSupport();
				}
		}catch(Exception e) {
				System.out.println("You have Done Some some thing Wrong");
				fixminimumSupport();
		}
		
	}

	private static void printRules(List<String> strongRules) {
		
		System.out.println("\n================Recomended Strong Rules================");
		if(0<strongRules.size()) {
			
			for(String temp:strongRules) {
				System.out.println(temp);
			}
			
		}else {
			System.out.println("There is no recomended Strong Rules");
			
		}
		System.out.println("=======================================================");
	}

	/*this is to get the file name from user*/
	@SuppressWarnings("unused")
	private static String readFileName() {
		String result="";
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
		try {
			System.out.println("Provide file location:");
			result=br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem in getting the file location");
			e.printStackTrace();
		}
		
		return result;
	}
}
