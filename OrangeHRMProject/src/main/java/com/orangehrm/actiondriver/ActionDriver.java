package com.orangehrm.actiondriver;

import java.time.Duration;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver extends BaseClass {
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

// Pass the driver from your BaseClass/Test into this constructor
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitwait = Integer.parseInt(BaseClass.getProp().getProperty("explicitwait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitwait));
		logger.info("WebDriver Instance is Created");
	}

//Method to click an element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			applyBorder(by, "green");
			waitForElementToBeVisible(by);
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked the Element:" + elementDescription);
			logger.info("Element is clicked-->" + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to click:" + e.getMessage());
			ExtentManager.logFailWithScreenshot(BaseClass.getDriver(), "Unable to Click the Element:",
					"Unable to Click the Element:" + elementDescription);
			logger.error("Element is not clicked-->" + elementDescription);

		}
	}

//Method to enter an element in an inputfield

	public void enterText(By by, String value) {
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered the element: " + getElementDescription(by) + " " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to enter element" + e.getMessage());
		}
	}

//Method to get the text from an input field
	public String getText(By by) {
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get the text:" + e.getMessage());
			return "";
		}
	}

//Method to compare text
	public boolean compareText(By by, String expectedText) {
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equalsIgnoreCase(actualText)) {
				applyBorder(by, "green");
				logger.info("Text are matching:" + actualText + "equals" + expectedText);
				ExtentManager.logStepWithScreenShot(BaseClass.getDriver(), "Compare Test success",
						"Test Verified Successfully!" + actualText + "equals" + expectedText);
				return true;
			} else {
				applyBorder(by, "red");
				logger.error("Text are not matching:" + actualText + "not equals" + expectedText);
				ExtentManager.logFailWithScreenshot(BaseClass.getDriver(), "Compare Test not success",
						"Tests are not matching!" + actualText + "notequals" + expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to compare the text:" + e.getMessage());
			return false;
		}
	}

//Method to check if an element is displayed
	/*
	 * public boolean isDisplayed(By by) { try { //waitForElementToBeClickable(by);
	 * // Change from Clickable to Visible for display checks
	 * waitForElementToBeVisible(by); boolean IsDisplayed =
	 * driver.findElement(by).isDisplayed(); if (IsDisplayed) {
	 * applyBorder(by,"green"); logger.info("Element is displayed:"
	 * +getElementDescription(by));
	 * ExtentManager.logStepWithScreenShot(BaseClass.getDriver()
	 * ,"Element is displayed::","Element is displayed:"+getElementDescription(by));
	 * //ExtentManager.logStep("Element is displayed:"+getElementDescription(by));
	 * return true; } //else { //applyBorder(by,"red"); return false; //} } catch
	 * (Exception e) { applyBorder(by,"red");
	 * logger.error("Element is not dispayed:" + e.getMessage());
	 * ExtentManager.logFailWithScreenshot(BaseClass.getDriver()
	 * ,"Element is not displayed","Element is not displayed:"+getElementDescription
	 * (by)); return false; } }
	 */
	public boolean isDisplayed(By by) {
	    try {
	        // 1. Wait for global loaders to finish
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("oxd-loading-spinner")));

	        // 2. Wait for the element to exist
	        wait.until(ExpectedConditions.presenceOfElementLocated(by));
	        WebElement element = driver.findElement(by);

	        // 3. Animation Sync: Wait until the element's position stops changing
	        // This is the secret to fixing "partial" or "blurry" screenshots
	        waitForAnimation(element);

	        // 4. Force scroll so it's centered in the viewport
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
	        
	        applyBorder(by, "green");
	        // Essential: Allow 500ms for the border color to actually render
	        Thread.sleep(500); 

	        ExtentManager.logStepWithScreenShot(BaseClass.getDriver(), "Visibility Check", "Verified: " + getElementDescription(by));
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}

	// Helper method to ensure the element isn't moving (animating)
	private void waitForAnimation(WebElement element) {
	    long lastWidth = -1;
	    for (int i = 0; i < 10; i++) { // Poll for max 1 second
	        long currentWidth = element.getSize().getWidth();
	        if (currentWidth > 0 && currentWidth == lastWidth) break;
	        lastWidth = currentWidth;
	        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
	    }
	}
//Method to scroll to an element
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
			// js.executeScript("arguments[0].scrollIntoView[false];", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to locate element:" + e.getMessage());
		}

	}

//Method to wait for the page to load
	public void waitForPageToBeLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded successfully");
		} catch (Exception e) {
			logger.error("Page did not load within :" + timeOutInSec + "seconds.Exception:" + e.getMessage());
		}
	}

//wait for element to be click-able
	public void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
			applyBorder(by, "green");
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("element is not clickable:" + e.getMessage());
		}
	}

//wait for Element to be Visible
	public void waitForElementToBeVisible(By by) {
		try {
			applyBorder(by, "green");
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("element is not visible:" + e.getMessage());
		}
	}

//Utility method to border an element 
	public void applyBorder(By by, String color) {
		try {
			// Locate the element
			WebElement element = driver.findElement(by);
			// Java script to apply the border
			// String script="arguments[0].style.border='3px solid "+color+"'";
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied the border with color" + color + "to element" + getElementDescription(by));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("Failed to apply the border to an element" + getElementDescription(by));
		}
	}

}
