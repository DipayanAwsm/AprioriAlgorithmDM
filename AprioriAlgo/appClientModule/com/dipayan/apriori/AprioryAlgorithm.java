/*
	Author:Dipayan Das
	Roll:CS1726.
	Date:Aug 3, 2018
	Bhabatu Sarba Mangalam
*/

package com.dipayan.apriori;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.dipayan.bean.TransactionBean;
import com.dipayan.constant.Constant;
import com.dipayan.dataStructureChar.CharDataStructure;

public class AprioryAlgorithm {

	/*Apriori Will output the recommended Strong Rules 
	 * 
	 * outpu be a list of rulses(String)
	 * eg:
	 *  a b->efg
	 *  p q->abc
	 *  .....
	 * */
	public List<String> getRules(TransactionBean transactionDataBase) {
		List<String> strongRules=new ArrayList<String>();
		
		/*Its creating the C1 or Ci*/
		Map<String, Integer> supportMap=createSupportMap(transactionDataBase.getTransactionDataBase());
		//printSupportCount(supportMap);
		/*Its creating the L1 or Li wit minimum support minimum support is in Constant.MINIMUM_SUPPORT*/
		Map<String, Integer> frequentItemSet=createFrequentIemSet(supportMap);

		/*24-08-2018*/
		
		Map<Set<CharDataStructure>, Integer> cTemp=createCTemp(supportMap);
		Map<Set<CharDataStructure>, Integer> lTemp=createLTemp(frequentItemSet);
		Map<Set<CharDataStructure>, Integer> lTempPrevious=lTemp;
		
		while(null!=cTemp && 0!=cTemp.size()) {
			lTempPrevious=lTemp;
			cTemp=createNextCWithPrevious(lTemp,transactionDataBase);
			lTemp=createNextLWithPreviousCsSupportCount(cTemp);
			if(null==lTemp || 0==lTemp.size()) {
				break;
			}
			
		}
		
		/*Generate StrongRules using lTempPrevious variable*/
		strongRules=createStrongRules(lTempPrevious,transactionDataBase);
		return strongRules;
	}



	private List<String> createStrongRules(Map<Set<CharDataStructure>, Integer> lTempPrevious,TransactionBean transactionDataBase) {
		
		List<String> result=new ArrayList<String>();
		Set<Set<CharDataStructure>> SetOfItems=lTempPrevious.keySet();
		Set<Set<String>> allRules=premuteTheSet(SetOfItems);
		
		for(Set<CharDataStructure>temp:SetOfItems) {
			temp.add(new CharDataStructure(Constant.RULE_DELIMETER));
		}
		
		//System.out.println(allRulesInCharDataStructure);
		
		double confidence=0;
		/*Now from the transaction data base get the confidence and  generate the rule*/
		for(Set<String> tempRuleSet:allRules) {
			for(String tempRule:tempRuleSet) {
				confidence=confidence(tempRule,transactionDataBase);
				if(confidence>=Constant.CONFIDENCE_IN_POINT) {
					result.add(tempRule);
				}
			}
		
			
			
		}
		result=removeduplicateRules(result);
		System.out.print(".");
		return result;
	}



	/*Duplicaztes look like 
	 *  e>ab
		ae>b
		e>ba
		be>a
		eb>a
		ea>b
	 * 
	 * */
	private List<String> removeduplicateRules(List<String> result) {
		// TODO Auto-generated method stub
		/**/
		List<String> resultRules=new ArrayList<String>();
		/*First part is left side and second part is right side and its size is always2*/
		String devidedParts[];
		char[] tempCharArrayLeft;
		char[] tempCharArrayRight;
		String leftValue;
		String rightValue;
		Map<String, String> ruleMap=new HashMap<String ,String>();
		for(String rulesTemp:result) {
			devidedParts=rulesTemp.split(Constant.RULE_DELIMETER);
			tempCharArrayLeft=devidedParts[0].toCharArray();
			tempCharArrayRight=devidedParts[1].toCharArray();
			
			
			
			Arrays.sort(tempCharArrayLeft);
			Arrays.sort(tempCharArrayRight);
			leftValue=new String(tempCharArrayLeft);
			rightValue=new String(tempCharArrayRight);
			ruleMap.put(leftValue, rightValue);
		}
		
		
		/*create   result without duplication*/
		for (Map.Entry<String,String> entry : ruleMap.entrySet()) {
			resultRules.add(entry.getKey()+"->"+entry.getValue());
		}
		
		return resultRules;
	}



	/*If the confidence 
	 * 
	 * X   y    confidence
	 * ab  c      50%
	 * ac  B      50%
	 * bc  A      50%
	 * A   BC     50%
	 * B   CA     50%
	 * 
	 * confidennce=support(x,y)/support(x)
	 * 
	 * */
	private double confidence(String tempRule, TransactionBean transactionDataBase) {
		// TODO Auto-generated method stub
		String[] listOFData=tempRule.split(Constant.RULE_DELIMETER);
		String x=listOFData[0];
		String y=listOFData[1];
		Set<CharDataStructure> setX=new HashSet<CharDataStructure>();
		Set<CharDataStructure> setXY=new HashSet<CharDataStructure>();
		CharDataStructure tempCharDatStructure;
		double supportCountOfXY=0;
		double supportCountOfX=0;
		/*Setting X*/
		for(int i=0;i<x.length();i++) {
			
				tempCharDatStructure=new CharDataStructure();
				tempCharDatStructure.setData(x.charAt(i)+"");
				setX.add(tempCharDatStructure);
				setXY.add(tempCharDatStructure);
			
		}
		
		/*Setting y*/
		for(int i=0;i<y.length();i++) {
			
			tempCharDatStructure=new CharDataStructure();
			tempCharDatStructure.setData(y.charAt(i)+"");
			setXY.add(tempCharDatStructure);
		
	}
		
		supportCountOfX=support(setX,transactionDataBase);
		supportCountOfXY=support(setXY, transactionDataBase);
		System.out.print(".");
		return (supportCountOfXY/supportCountOfX);
	}



	private double support(Set<CharDataStructure> setX, TransactionBean transactionDataBase) {

		List<List<CharDataStructure>> transactions=transactionDataBase.getTransactionDataBase();
		int tempCount=0;
		CharDataStructure charTemp;
		Set<CharDataStructure>transactionSet;
		//dipayanCheckContainsAll();
		for(List<CharDataStructure> charList:transactions) {
			transactionSet=new HashSet<CharDataStructure>();
			for(CharDataStructure items:charList) {
				charTemp=new CharDataStructure();
				charTemp.setData(items.getData());
				transactionSet.add(charTemp);
			}
			if(dipayanCheckContainsAll(transactionSet,setX)) {
				tempCount++;
			}
		}
		
		
		return tempCount;
	}



	/*This method will return set of set of rules all rules*/
	private Set<Set<String>> premuteTheSet(Set<Set<CharDataStructure>> SetOfItems) {
		String data="";
		Set<String> allRulesOfONeRow;
		Set<String> allRulesOfONeRowForThisMethod=new HashSet<String>();
		Set<Set<String>> allRulus=new HashSet<Set<String>>();
		for(Set<CharDataStructure> tempSet:SetOfItems) {
			data="";
			for(CharDataStructure tempInner:tempSet) {
				data=data+tempInner.getData();
			}
			data=data+Constant.RULE_DELIMETER;
			allRulesOfONeRow= permutationFinder(data);
			allRulesOfONeRowForThisMethod=new HashSet<String>();
			for(String temp:allRulesOfONeRow) {
				if(!Constant.RULE_DELIMETER.equals(temp.charAt(0)+"")&& !Constant.RULE_DELIMETER.equals(temp.charAt(temp.length()-1)+"")) {
					allRulesOfONeRowForThisMethod.add(temp);
				}
			}
			allRulus.add(allRulesOfONeRowForThisMethod);
			//System.out.println("\nPermutations for " + data + " are: \n" + allRulesOfONeRowForThisMethod);
		}
		return allRulus;
	}
	
///////////////////////Generate All Possible Combination//////////////////////////////////////
///*IF you get a string which stats with'>' please dont work with that its rule delimeter*///
    public  Set<String> permutationFinder(String str) {
        Set<String> perm = new HashSet<String>();
        //Handling error scenarios
        if (str == null) {
            return null;
        } else if (str.length() == 0) {
            perm.add("");
            return perm;
        }
        char initial = str.charAt(0); // first character
        String rem = str.substring(1); // Full string without first character
        Set<String> words = permutationFinder(rem);
        for (String strNew : words) {
            for (int i = 0;i<=strNew.length();i++){
                perm.add(charInsert(strNew, initial, i));
            }
        }
        for(String temp:perm) {
        	if(Constant.RULE_DELIMETER.equals(temp.charAt(0)+"")|| Constant.RULE_DELIMETER.equals(temp.charAt(temp.length()-1)+"")) {
        		
        	}
        }
        return perm;
    }

    public  String charInsert(String str, char c, int j) {
        String begin = str.substring(0, j);
        String end = str.substring(j);
        return begin + c + end;
    }
	
/////////////////////Generate All Possible Combination/////////////////////////////////
	
	



	private Map<Set<CharDataStructure>, Integer> createNextLWithPreviousCsSupportCount(
			Map<Set<CharDataStructure>, Integer> cTemp) {
		Map<Set<CharDataStructure>, Integer> result=new HashMap<Set<CharDataStructure>, Integer>();
		
		Set<CharDataStructure> tempSet;
		int count=0;
		for (Map.Entry<Set<CharDataStructure>, Integer> entry : cTemp.entrySet()) { 
			tempSet=entry.getKey();
			count=entry.getValue();
			if(Constant.MINIMUM_SUPPORT<=count) {
				result.put(tempSet, count);
			}
		}
		return result;
	}



	private Map<Set<CharDataStructure>, Integer> createNextCWithPrevious(Map<Set<CharDataStructure>, Integer> cTemp,
			TransactionBean transactionDataBase) {

		Map<Set<CharDataStructure>, Integer> result=new HashMap<Set<CharDataStructure>, Integer>();
		Set<CharDataStructure> tempSet;
		Set<CharDataStructure> tempInnerSet;
		Set<CharDataStructure> resultSet;
		for (Map.Entry<Set<CharDataStructure>, Integer> entry : cTemp.entrySet()) { 
			//tempSet=entry.getKey();
			
			
			for (Map.Entry<Set<CharDataStructure>, Integer> entryInner : cTemp.entrySet()) { 
				tempSet=entry.getKey();
				resultSet=new HashSet<CharDataStructure>();
				for(CharDataStructure keyTemp:tempSet) {
					resultSet.add(keyTemp);
				}
				if(tempSet!=entryInner.getKey()) {
					tempInnerSet=entryInner.getKey();
					
					for(CharDataStructure tempCharDataStructure:tempInnerSet) {
						if(!tempSet.contains(tempCharDataStructure)) {
							
							resultSet.add(tempCharDataStructure);						}
					}
					//System.out.println("Key = " + entryInner.getKey() +", Value = " + entryInner.getValue());
					
					/*All data initialized at 0*/
					result.put(resultSet, 0);
					System.out.print(".");
				}
	        }
            
			
			
			
			//System.out.println("Key = " + entry.getKey() +", Value = " + entry.getValue());
        }
		
		
		
		/*Here All Data will get its count*/
		result=setTheTransactionCounts(result,transactionDataBase);
		
		return result;
	}



	private Map<Set<CharDataStructure>, Integer> setTheTransactionCounts(Map<Set<CharDataStructure>, Integer> result,
			TransactionBean transactionDataBase) {

		Set<CharDataStructure> tempSet;
		Map<Set<CharDataStructure>, Integer> resultNew=new HashMap<Set<CharDataStructure>, Integer>();
		List<List<CharDataStructure>>  db=transactionDataBase.getTransactionDataBase();
		int tempCount=0;
		Set<CharDataStructure> tempSetList;
		for (Map.Entry<Set<CharDataStructure>, Integer> entry : result.entrySet()) { 
			tempCount=0;
			tempSet=entry.getKey();
			
			for (List<CharDataStructure> transactionTemp : db) { 
				
				tempSetList=new HashSet<>();
				tempSetList.addAll(transactionTemp);
				if(dipayanCheckContainsAll(tempSetList,tempSet)) {
					tempCount++;
				}	
			}
			if(tempCount>0) {
				resultNew.put(tempSet, tempCount);
			}else if(tempCount==0 ) {
				
			}
		}
		
		
		return resultNew;
	}



	/*Te*/
	private boolean dipayanCheckContainsAll(Set<CharDataStructure> largeList, Set<CharDataStructure> smallList) {
		// TODO Auto-generated method stub
		boolean result =true;
		boolean tempBoolean=false;
		boolean[] trueFalseResult=new boolean[smallList.size()];
		int i=0;
		for(CharDataStructure smallTemp:smallList) {
			tempBoolean=false;
			for(CharDataStructure largeTemp:largeList) {
				if(smallTemp.getData().equals(largeTemp.getData())) {
					tempBoolean=true;
				}
			}
			trueFalseResult[i]=tempBoolean;
			i++;
		}
		
		for(boolean temp:trueFalseResult) {
			if(false==temp) {
				return false;
			}
		}
		return result;
	}



	private Map<Set<CharDataStructure>, Integer> createLTemp(Map<String, Integer> frequentItemSet) {
		Map<Set<CharDataStructure>, Integer> result=new HashMap<Set<CharDataStructure>, Integer>();
		Set<CharDataStructure>tempSet;
		int countTemp;
		CharDataStructure charDataTemp;
		for (Map.Entry<String,Integer> entry : frequentItemSet.entrySet()) { 
			tempSet=new HashSet<CharDataStructure>();
			charDataTemp=new CharDataStructure();
			charDataTemp.setData(entry.getKey());
			countTemp=entry.getValue();
			
			tempSet.add(charDataTemp);
			result.put(tempSet, countTemp);
			
            //System.out.println("Key = " + entry.getKey() +", Value = " + entry.getValue());
        }
		
		return result;
	}



	private Map<Set<CharDataStructure>, Integer> createCTemp(Map<String, Integer> supportMap) {

		Map<Set<CharDataStructure>, Integer> result=new HashMap<Set<CharDataStructure>, Integer>();
		Set<CharDataStructure>tempSet;
		int countTemp;
		CharDataStructure charDataTemp;
		for (Map.Entry<String,Integer> entry : supportMap.entrySet()) { 
			tempSet=new HashSet<CharDataStructure>();
			charDataTemp=new CharDataStructure();
			charDataTemp.setData(entry.getKey());
			countTemp=entry.getValue();
			
			tempSet.add(charDataTemp);
			result.put(tempSet, countTemp);
			
            //System.out.println("Key = " + entry.getKey() +", Value = " + entry.getValue());
        }
		
		return result;
	}



	/*ITS creating minimum support map L1*/
	private Map<String, Integer> createFrequentIemSet(Map<String, Integer> supportMap) {

		Map<String, Integer> minimumSupportMap=new HashMap<String ,Integer>();
		for(Map.Entry<String, Integer> tempSupportMap:supportMap.entrySet()) {
			if(tempSupportMap.getValue()>=Constant.MINIMUM_SUPPORT) {
				minimumSupportMap.put(tempSupportMap.getKey(),tempSupportMap.getValue());
			}
		}
		
		return minimumSupportMap;
	}

	/*Printing the support vector*/
	@SuppressWarnings("unused")
	private static void printSupportCount(Map<String, Integer> supportMap) {
		System.out.println("=============================================================================");
		for (Map.Entry<String,Integer> entry : supportMap.entrySet()) {
            System.out.println( "|"+entry.getKey() + "|" + entry.getValue()+"|");
		}
		System.out.println("=============================================================================");
		
	}

	/*This will create the support map
	 * eg:
	 * ==================
	 * Item:count
	 *   a   5   
	 *   b   7
	 *   c   8
	 * ==================  
	 *  Then return the Map
	 * */
	private Map<String, Integer> createSupportMap(List<List<CharDataStructure>> transactionDataBase) {
		Map<String ,Integer> supportMap=new HashMap<String ,Integer>();
		int itemCount=0;
		for(List<CharDataStructure> tempCharList:transactionDataBase) {
			for(CharDataStructure tempCharacter:tempCharList) {
				if(null==supportMap.get(tempCharacter.getData())) {
					itemCount=1;
				}else {
					itemCount=supportMap.get(tempCharacter.getData())+1;
				}
							
				supportMap.put(tempCharacter.getData(), itemCount);
			}
		}
		return supportMap;
	}

}
