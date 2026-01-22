package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtility 
{

	//Method to get the sheet data- to open an Excel file, go to a specific sheet, and pull all the rows into a List
	public static List<String[]> getSheetData(String filePath,String sheetname)
	{
		//Data variable is defined as a array of strings
		List<String[]> data= new ArrayList();
		try(FileInputStream fis= new FileInputStream(filePath); Workbook workbook=new XSSFWorkbook(fis)){
			Sheet sheet= workbook.getSheet(sheetname);
			if(sheet==null) {
				throw new IllegalArgumentException("Sheet"+sheetname+"does not exist");
			}
			//Iterate through rows
			for(Row row:sheet)
			{
				if(row.getRowNum()==0)
				{
					continue;
				}
				//Read All the Cells
				List <String> rowData=new ArrayList();
				for(Cell cell:row)
				{
					rowData.add(getCellValue(cell));
				}
				//convert rowData to Array
				data.add(rowData.toArray(new String[0]));
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return data;
		}
	
	
	//Method to update the cell to string (proper value)
	public static String getCellValue(Cell cell)
	{
		/*Before doing anything, it checks if the cell is empty. 
		 * If it is, it returns an empty piece of text ("") so the program doesn't crash with a NullPointerException
		 */
		if(cell==null) {
			return"";
		}
		/*The code looks at the "Type" of the cell and decides what to do:
Case STRING: If the cell contains text (like "Name" or "Address"), it simply reads it and returns it.
Case NUMERIC (The Tricky Part):
Dates: In Excel, dates are actually stored as numbers. The code checks: "Is this number actually a date?" If yes, it converts the date to text.
Regular Numbers: If it’s just a number (like 101), it converts it to an Integer first (to remove decimals like .0) and then turns it into text.
Case BOOLEAN: If the cell is TRUE or FALSE, it converts that value into the text "true" or "false".
Default: If the cell is something else (like an error or a formula it doesn't recognize), it just returns an empty string.*/
		switch(cell.getCellType())
		{
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if(DateUtil.isCellDateFormatted(cell)){
				return cell.getDateCellValue().toString();
			}
			return String.valueOf((int)cell.getNumericCellValue());
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return "";
		}
		}
}
