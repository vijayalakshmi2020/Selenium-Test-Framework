package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
 private static final String filePath= System.getProperty("user.dir") + "/src/test/resources/testdata/TestData.xlsx";
 
 @DataProvider(name="VerifyLoginWithValidData")
 public static Object[][] validLoginData()
 {
	 return getSheetData("ValidLoginData");
 }
 @DataProvider(name="VerifyLoginWithInValidData")
 public static Object[][] invalidLoginData()
 {
	 return getSheetData("InValidLoginData");
 }
 @DataProvider(name="emplVerification")
 public static Object[][]emplVerification ()
 {
	 return getSheetData("emplVerification");
 }
 /*Method to convert a List (which is flexible in size) into a 2D Object Array 
 (which is the specific format TestNG requires)*/
 
 private static Object[][] getSheetData(String sheetname)
 {
	List<String[]> sheetdata= ExcelReaderUtility.getSheetData(filePath, sheetname);
	Object[][]data=new Object[sheetdata.size()][sheetdata.get(0).length];
	for(int i=0; i<sheetdata.size();i++)
	{
		data[i]=sheetdata.get(i);
	}
	 return data;
 }
 
}
