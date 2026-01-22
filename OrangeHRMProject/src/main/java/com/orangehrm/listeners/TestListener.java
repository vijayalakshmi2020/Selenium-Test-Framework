package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyser;

public class TestListener implements ITestListener,IAnnotationTransformer {
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		// TODO Auto-generated method stub
		annotation.setRetryAnalyzer(RetryAnalyser.class);
		}

	// Trigger when the test starts
	@Override
	public void onTestStart(ITestResult result) {
		// Start the ExtentReport by capturing the testname using getmetod()
		String testName = result.getMethod().getMethodName();
		// Start logging in extentReport
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test Started: " + testName);

	}

	// Trigger when the test success
	/*@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		//ExtentManager.logStepWithScreenShot(BaseClass.getDriver(), "TEST PASSED SUCCESSFULLY",
			//	"Test End:" + testName + "TestPassed");
		if (!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logStepWithScreenShot(BaseClass.getDriver(), "TEST PASSED SUCCESSFULLY",
					"Test End:" + testName + "TestPassed");
		} else {
			ExtentManager.logStepwithAPI("Test End:\" + testName + \"TestPassed");
		}
	}*/
	@Override
	public void onTestSuccess(ITestResult result) {
	    String testName = result.getMethod().getMethodName();
	    if (!result.getTestClass().getName().toLowerCase().contains("api")) {
	        WebDriver driver = BaseClass.getDriver();
	        if (driver != null) {
	            // Force a quick sync to ensure the page hasn't gone blank
	            ExtentManager.logStepWithScreenShot(driver, "TEST PASSED SUCCESSFULLY",
	                    "Final State: " + testName);
	        }
	    } else {
	        ExtentManager.logStepwithAPI("Test Passed: " + testName);
	    }
	}

	// Trigger when the test fails
	
	/*@Override 
	 public void onTestFailure(ITestResult result) 
	 { String testName =result.getMethod().getMethodName();
	 String FailureMessage =result.getThrowable().getMessage(); 
	 ExtentManager.logStep(FailureMessage);
	 ExtentManager.logFailWithScreenshot(BaseClass.getDriver(), "TEST FAILED",
	  "Test End:" + testName + "Test Failed");
	 }*/
	 
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		// Get the driver immediately before it's closed by AfterMethod
		
		if (!result.getTestClass().getName().toLowerCase().contains("api")) 
		{
		WebDriver driver = BaseClass.getDriver();
		if (driver != null) 
		{
			String failureMessage = result.getThrowable().getMessage();
			ExtentManager.logStep("Failure Reason: " + failureMessage);
			ExtentManager.logFailWithScreenshot(driver, "TEST FAILED", "Screenshot on Failure: " + testName);
		} 
		else {
			// Fallback if the driver was already closed by another thread
			ExtentManager.logStep("Test Failed, but WebDriver was already closed. Could not take screenshot.");
		}
		} 
		else {
			ExtentManager.logFailurewithAPI("Test End:\" + testName + \"TestFailed");
		}
		
	}

	// Trigger when the test skips
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test is Skipped: " + testName);
	}

	// Trigger when a suite starts
	@Override
	public void onStart(ITestContext context) {
		// Initialize the Extent Report
		ExtentManager.getReporter();
	}

	// Trigger when a suite ends
	@Override
	public void onFinish(ITestContext context) {
		// Flush the extent report
		ExtentManager.endTest();
	}

}
