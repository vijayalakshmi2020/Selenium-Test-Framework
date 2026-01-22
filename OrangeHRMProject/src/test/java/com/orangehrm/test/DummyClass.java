package com.orangehrm.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;


 public class DummyClass extends BaseClass {
 @Test
	public void dummytest()
	{
	//ExtentManager.startTest("Dummy Test");This has been implemented in Test Listener
	String title=getDriver().getTitle();
	ExtentManager.logStep("Verifying the Title");
	assert title.equals("OrangeHRM"):"TestFailed";
	System.out.println("Test is passed");
	ExtentManager.logStep("Test is passed");
	//ExtentManager.logSkip("This test is skipped");
	//throw new SkipException("Skipping the test as part of testing");
	}
}
