package com.orangehrm.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod
	public void setUpPages() {
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}
@Test(dataProvider = "emplVerification", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameFromDataBase(String emp_id, String emp_name) 
{
	    SoftAssert softassert=getSoftAssert();
		ExtentManager.logStep("Login with Credentials");
		loginpage.login(prop.getProperty("Username"), prop.getProperty("Password"));
		ExtentManager.logStepWithScreenShot(BaseClass.getDriver(), "Entered Username and Password",
				"Logged in Successfully");
		ExtentManager.logStep("Click on PIM tab");
		homepage.clickOnPIMTab();
		ExtentManager.logStep("Enter the employee name and click on search");
		homepage.employeeSearch(emp_name);
		ExtentManager.logStep("Verify the UI and DB data");
		String employee_id = emp_id;

		// Fetch the data from DB
		Map<String, String> employeedetails = DBConnection.getEmployeeDetails(employee_id);
		// Check if the map is empty BEFORE trying to get strings from it
		Assert.assertFalse(employeedetails.isEmpty(), "No employee found in DB with ID: " + employee_id);
		String emplFirstName = employeedetails.get("firstName");
		String emplMiddleName = employeedetails.get("middleName");
		String emplLastName = employeedetails.get("lastName");
		String emplFirstandMiddleName = (emplFirstName + "" + emplMiddleName).trim();
		// Verify the first and middle name
		softassert.assertTrue(homepage.verifyemployeeFirstAndMiddlename(emplFirstandMiddleName),
				"First and MiddleName are not matching");
		ExtentManager.logStep("Verified the employee first and Middlename");
		// Verify the last name
		softassert.assertTrue(homepage.verifyemployeeLastName(emplLastName), "LastName are not matching");
		ExtentManager.logStep("Verified the employee lastname ");
		ExtentManager.logStep("DataBase Validation completed");
		ExtentManager.logStep("Logged Out successully");
		softassert.assertAll();
	}
}
