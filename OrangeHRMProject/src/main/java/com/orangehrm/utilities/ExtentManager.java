package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> drivermap = new HashMap();

	// Initialize the Extent Report
	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReports/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRMReport");
			spark.config().setTheme(Theme.DARK);
			extent = new ExtentReports();
			extent.attachReporter(spark);
			// Adding System Information
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java version", System.getProperty("java.name"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
		}
		return extent;
	}

//Start the Test
	public synchronized static ExtentTest startTest(String TestName) {
		ExtentTest extent = getReporter().createTest(TestName);// Start a new entry in the report with this specific
																// name
		// This stores the test object in a ThreadLocal variable so you can access it
		// from anywhere in your code without passing it as a parameter.
		test.set(extent);
		// This allows you to immediately use the object if you need to perform an
		// action right after starting it.
		return extent;
	}

//End the Test
	public static void endTest() {
		getReporter().flush();
	}

// Get Current Thread's test
	public synchronized static ExtentTest getTest() {
		return test.get();
	}

//Method to get the name of the current Test
	public synchronized static String getTestName() {
		ExtentTest currenttest = getTest();
		if (currenttest != null) {
			return currenttest.getModel().getName();
		} else {
			return "No test is currently active in thread";
		}

	}

//Log a step
	public static void logStep(String logmessage) {
		getTest().info(logmessage);
	}

//Log step pass validation with screenshot
	public static void logStepWithScreenShot(WebDriver driver, String logmessage, String screenshotmessage) {
		getTest().pass(logmessage);
		// screenshot method
		attachScreenShot(driver,screenshotmessage);
	}

//Log step fail validation with screenshot
	public static void logFailWithScreenshot(WebDriver driver, String logmessage, String screenshotmessage) {
		String colormessage=String.format("<span style='color:red'>%s</span>", logmessage);
		getTest().fail(colormessage);
		// screenshot method
		attachScreenShot(driver,screenshotmessage);
		}
	
//Log Failure for API testing
	public static void logFailurewithAPI(String logmessage) {
		String colormessage=String.format("<span style='color:red'>%s</span>", logmessage);
		getTest().fail(colormessage);
	}
	
//Log Success for API testing
		public static void logStepwithAPI(String logmessage)
		{
			getTest().pass(logmessage);
		}
//Log Skip
	public static void logSkip(String logmessage) {
		String colormessage=String.format("<span style='color:orange'>%s</span>", logmessage);
		getTest().skip(colormessage);
	}

//Register web driver for current Thread
	public static void registerDriver(WebDriver driver) {
		drivermap.put(Thread.currentThread().getId(), driver);
	}
	
	// Take the screenshot by appending date and time
	/*public synchronized static String takeScreenShot(WebDriver driver, String screenshotname)
	{
		TakesScreenshot ts=(TakesScreenshot)driver;
		File src=ts.getScreenshotAs(OutputType.FILE);
		//Format date and time for the file
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());
		//Saving the screenshot to a  File
		//String DestPath=System.getProperty("user.dir") + "/src/test/resources/screenshots" +screenshotname+ "_ "+timeStamp+ ".png";
		String DestPath = System.getProperty("user.dir") +"/src/test/resources/screenshots/"+screenshotname+ "_" + timeStamp + ".png";
		File finalpath=new File(DestPath);
		try {
			FileUtils.copyFile(src, finalpath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Convert screenshot to Base64 for embedding in the extent Report
		String Base64Format=convertToBase64(src);
		return Base64Format;
	}*/
	public synchronized static String takeScreenShot(WebDriver driver, String screenshotname) {
	    if (driver == null) {
	        return ""; // Return empty if driver is gone
	    }
	    try {
	        TakesScreenshot ts = (TakesScreenshot) driver;
	        File src = ts.getScreenshotAs(OutputType.FILE);
	        // ... rest of your existing logic ...
	        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());
	        String DestPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + screenshotname + "_" + timeStamp + ".png";
	        FileUtils.copyFile(src, new File(DestPath));
	        return convertToBase64(src);
	    } catch (Exception e) {
	        System.out.println("Exception while taking screenshot: " + e.getMessage());
	        return "";
	    }
	}
//Method to convert screenshot to Base64 format
	public  static String convertToBase64(File screenShotFile)
	{
		String base64Format="";
		try {
			byte[] FileContent = FileUtils.readFileToByteArray(screenShotFile);
			 base64Format=Base64.getEncoder().encodeToString(FileContent);
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//convert the byte array to Base 64 string
		return base64Format;
		}
	
//Attach screenshot using Base64
	public synchronized static void attachScreenShot(WebDriver driver, String Message)
	{
		try {
			String screenshotBase64=takeScreenShot(driver,getTestName());
			getTest().info(Message,com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed to attach the screenshot:"+Message);
			e.printStackTrace();
		}
	}
}
