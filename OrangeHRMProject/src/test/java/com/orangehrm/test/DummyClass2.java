package com.orangehrm.test;
import org.testng.annotations.Test;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

	public class DummyClass2 extends BaseClass {
	 @Test
		public void dummytest2()
		{
		//ExtentManager.startTest("Dummy Test 2");This has been implemented in Test Listener
		ExtentManager.logStep("Verifying the Title");
		String title=getDriver().getTitle();
		assert title.equals("OrangeHRM"):"TestFailed";
		System.out.println("Test is passed");
		ExtentManager.logStep("Validation successsful");
		}
	}


