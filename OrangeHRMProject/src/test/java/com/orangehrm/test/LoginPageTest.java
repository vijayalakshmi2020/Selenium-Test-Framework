package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {
	private LoginPage loginpage;
	private HomePage homepage;

//Initialize the objects by extending the Base Class

	@BeforeMethod
	public void setupPages() {
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}

//Method to verify Login
	@Test(dataProvider = "VerifyLoginWithValidData", dataProviderClass = DataProviders.class)
	public void verifyValidLogin(String username, String password) {
		System.out.println("Running testmethod1 on thread: " + Thread.currentThread().getId());
		// ExtentManager.startTest("Login Page Test-Valid Login Test");//This has been
		// implemented in Test Listener
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginpage.login(username, password);
		ExtentManager.logStep("Verifying admin tab is visibe or not");
		Assert.assertTrue(homepage.adminIsVisible(), "Admin tab should be visible after successful login");
		ExtentManager.logStep("Validation successsful");
		homepage.logout();
		ExtentManager.logStep("Logged Out successfully");
		StaticWait(2);
	}

	@Test(dataProvider = "VerifyLoginWithInValidData", dataProviderClass = DataProviders.class)
	public void verifyinvalidLogin(String username, String password) {
		// ExtentManager.startTest("Login Page Test:In-Valid Login Test");//This has
		// been implemented in Test Listener
		System.out.println("Running testmethod2 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginpage.login(username,password);
		ExtentManager.logStep("Verifying the error message in the popupwindow");
		String Expectederror = "Invalid credentials";
		Assert.assertTrue(loginpage.verifyErrorMessage(Expectederror), "Error Message shown on the UI is incorrect");
		ExtentManager.logStep("Validation successsful");
		ExtentManager.logStep("Logged Out successfully");
	}

}
