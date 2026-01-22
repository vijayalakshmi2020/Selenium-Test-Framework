package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;
	
@BeforeMethod	
public void setUpPages()
{
	loginpage= new LoginPage(getDriver()); 
	homepage= new HomePage(getDriver());
}
@Test(dataProvider = "VerifyLoginWithValidData", dataProviderClass = DataProviders.class)
public void verifyOrangeHRMLogo(String username, String password)
{
	//ExtentManager.startTest("Home Page Test-Verify Orange Logo");This has been implemented in Test Listener
	ExtentManager.logStep("Navigating to Login Page entering username and password");
	loginpage.login(username, password);
	ExtentManager.logStep("Verifying Orange Logo is visible");
	Assert.assertTrue(homepage.VerifyOrangeHRMIsVisible(), "Logo is not visible");
	ExtentManager.logStep("Validation successsful");
	homepage.logout();
	ExtentManager.logStep("Logged Out successfully");
}
}
