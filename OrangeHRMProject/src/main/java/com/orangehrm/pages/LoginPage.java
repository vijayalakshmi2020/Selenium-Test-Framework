package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage 
{
private ActionDriver actiondriver;

//Pass the driver from your ActionDriver Class into this constructor
/*public LoginPage(WebDriver driver)
{
	this.actiondriver= new ActionDriver(driver);
}*/
public LoginPage(WebDriver driver)
{
	this.actiondriver= BaseClass.getActionDriver();
}
// Define locators by using by class
private By usernameid=By.name("username");
private By passwordid=By.name("password");
private By loginbutton= By.xpath("//button[text ()=' Login ']");
private By errormessage=By.xpath("//p[text()='Invalid credentials']");

//Method to perform login
public void login(String username , String password)
{
	actiondriver.enterText(usernameid, username);
	actiondriver.enterText(passwordid, password);
	actiondriver.click(loginbutton);
}

//Method to check if the error message is displayed
public boolean isErrorMessageDisplayed()
{
return actiondriver.isDisplayed(errormessage);
}

//Method to get the text from the error message
public String getTextErrorMessage()
{
return actiondriver.getText(errormessage);	
}

//Method to verify if the error message displayed is correct or not
public boolean  verifyErrorMessage(String Expectederror)
{
	return actiondriver.compareText(errormessage, Expectederror);
	}
}

