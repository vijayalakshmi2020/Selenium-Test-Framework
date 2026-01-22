package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;

public class DBConnection {
	
private static final String DB_URL="jdbc:mysql://localhost:3306/orangehrm";
private static final String DB_UserName="root";
private static final String DB_PassWord="";
public static final Logger logger = LogManager.getLogger(BaseClass.class);

//Method to get the DB connected using DriverManagerclass and connection Interface
public static Connection getDBConnection()
{
	System.out.println("Starting the DB Connection");
	try {
		Connection conn=DriverManager.getConnection(DB_URL, DB_UserName, DB_PassWord);
		logger.info("DB connection successful");
		return conn;
	} catch (SQLException e) {
		logger.error("Error while establishing the DB connection");
		e.printStackTrace();
		return null;
	}
}

//Get the employee details in a DB and store in a map
public static Map<String,String> getEmployeeDetails(String employee_id)
{
	String query = "SELECT emp_firstname, emp_middle_name, emp_lastname FROM hs_hr_employee WHERE employee_id = '" + employee_id + "'";
	//String query="SELECT emp_firstname,emp_middle_name,emp_lastname from hs_hr_employee where employee_id=1"+employee_id;
	Map<String,String> employeedetails=new HashMap();
	try(Connection conn=getDBConnection(); 
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(query)){
		logger.info("Executing the Query:"+query);
		if(rs.next())
		{
		String firstName	=rs.getString("emp_firstname");
		String middleName=rs.getString("emp_middle_name");
		String lastName	=rs.getString("emp_lastname");
		//store in a map
		employeedetails.put("firstName", firstName);
		employeedetails.put("middleName",middleName!=null?middleName:"");
		employeedetails.put("lastName", lastName);
		logger.info("Query Executed successfully");
		logger.info("Employee Data fetched"+employeedetails);
		}
		else
		{
			logger.info("Employee not found");
		}
	} catch (SQLException e) {
		logger.error("Error while executing query");
		e.printStackTrace();
	}
	return employeedetails;
	}

}
