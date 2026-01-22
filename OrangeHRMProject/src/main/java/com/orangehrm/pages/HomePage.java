package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
	private ActionDriver actiondriver;

//Initialize the ActionDriver Object by passing Web driver instance to the constructor
	/*
	 * public HomePage(WebDriver driver) { this.actiondriver=new
	 * ActionDriver(driver); }
	 */
	public HomePage(WebDriver driver) {
		this.actiondriver = BaseClass.getActionDriver();
	}

	// Define the locators
	private By adminTab = By.xpath("//span[text()='Admin']");
    private By orangeHrmImg = By.xpath("//div[@class='oxd-brand-banner']//img");
	//private By orangeHrmImg = By.xpath("//img[@alt='client brand banner']");
	private By arrowid = By.xpath("//*[@id='app']/div[1]/div[1]/header/div[1]/div[3]/ul/li/span/i");
	private By logOut = By.xpath("//a[text()='Logout']");
	private By pimTab = By.xpath("//span[text()='PIM']");
	private By employeeSearchbtn = By.xpath("//button[text()=' Search ']");
	private By employeenameSearch = By.xpath(
			"//*[@id='app']/div[1]/div[2]/div[2]/div[1]/div[1]/div[2]/form/div[1]/div[1]//div[1]//div//div[2]//div[1]/div/input");
	private By empfirstMiddleid = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private By employeeLastid = By.xpath("//div[@class='oxd-table-card']/div/div[4]");

//Methods to verify the page is loaded and then clicked logout
	public boolean adminIsVisible() {
		return actiondriver.isDisplayed(adminTab);
	}

	public boolean VerifyOrangeHRMIsVisible() {
		return actiondriver.isDisplayed(orangeHrmImg);
	}

	/*
	 * public boolean VerifyuserIDIsVisible() { return
	 * actiondriver.isDisplayed(userIdButton); }
	 */
	public void logout() {
		actiondriver.click(arrowid);
		actiondriver.click(logOut);
	}

//Method to navigate to PIM tab
	public void clickOnPIMTab() {
		actiondriver.click(pimTab);
	}

//Method for employee search
	public void employeeSearch(String name) {
		actiondriver.enterText(employeenameSearch, name);
		actiondriver.click(employeeSearchbtn);
		actiondriver.scrollToElement(empfirstMiddleid);

	}

//Verify employee first and middle name
	public boolean verifyemployeeFirstAndMiddlename(String emplFirstandMiddlenamefromDB) {
		return actiondriver.compareText(empfirstMiddleid, emplFirstandMiddlenamefromDB);
	}

// Verify employee Last name
	public boolean verifyemployeeLastName(String emplLastnamefromDB) {
		return actiondriver.compareText(employeeLastid, emplLastnamefromDB);
	}
}
