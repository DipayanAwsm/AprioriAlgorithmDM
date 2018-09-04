/*
 * 	Atuthor:Dipayan Das
 * 	Date:2-08-2018
 *  Bhabatu Marba Mangalam	
 * */

package com.dipayan.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dipayan.bean.TransactionBean;
import com.dipayan.constant.Constant;
import com.dipayan.dataStructureChar.CharDataStructure;

public class ReadFile {

	/*Its for check the weather I can read File or not*/
	public void read(String fileLocation) {
		File file=new File(fileLocation);
		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			BufferedReader br=new BufferedReader(fileReader);
			String temp;
			
			while((temp=br.readLine())!=null) {
				System.out.println(temp);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem IN BufferedReader");
			e.printStackTrace();
		} 
		
	}

	/*For this Line my assumption is each line is a transaction */
	public TransactionBean getListOftransaction(String fileLocation) {
		TransactionBean result=new TransactionBean();
		List<List<CharDataStructure>> transactionDataBase=result.getTransactionDataBase();
		List<CharDataStructure> characterList;
		CharDataStructure characterTemp;
		File file=new File(fileLocation);
		FileReader fileReader;
		String []tempArray;
		try {
			fileReader = new FileReader(file);
			BufferedReader br=new BufferedReader(fileReader);
			String temp;
			
			while((temp=br.readLine())!=null) {
				tempArray=temp.split(Constant.COMA_DELIMITER);
				characterList=new ArrayList<CharDataStructure>();
				for (String data : tempArray) {
					characterTemp=new CharDataStructure();
					characterTemp.setData(data);
					characterList.add(characterTemp);
				}
				transactionDataBase.add(characterList);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem IN BufferedReader");
			e.printStackTrace();
		}
		
		
		
		result.setTransactionDataBase(transactionDataBase);
		return result;
	}

}
