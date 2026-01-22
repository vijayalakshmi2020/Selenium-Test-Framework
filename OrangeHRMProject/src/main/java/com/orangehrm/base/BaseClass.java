package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;

public class BaseClass {

	protected static Properties prop;
	// protected static WebDriver driver;
	// protected static ActionDriver actiondriver;
	private static ThreadLocal<WebDriver> driver = new ThreadLocal();
	private static ThreadLocal<ActionDriver> actiondriver = new ThreadLocal();
	public static final Logger logger = LogManager.getLogger(BaseClass.class);
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("Properties File loaded");
		// Start the Extent Reporter
		// ExtentManager.getReporter();//This has been implemented in Test Listener
	}

//Getter Method for softassert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	// Prop Getter Method
	public static Properties getProp() {
		return prop;
	}

	// WebDriver Getter Method
	public static WebDriver getDriver() {
		/*
		 * if (driver.get() == null) {
		 * System.out.println("WebDriver is not initialised"); throw new
		 * IllegalStateException("WebDriver is not initialised"); }
		 */
		// return getDriver();*/
		return driver.get();
	}

	// ActionDriver Getter Method
	public static ActionDriver getActionDriver() {
		if (actiondriver.get() == null) {
			System.out.println("ActionDriver is not initialised");
			throw new IllegalStateException("ActionDriver is not initialised");
		}
		return actiondriver.get();
	}

	// Driver Setter Method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	@BeforeMethod
	public synchronized void setup() {
		System.out.println("Setting up WebDriver for: " + this.getClass().getSimpleName());

		launchBrowser();
		ConfigBrowser();
		StaticWait(2);
		logger.info("WebDriver initialised and Browser window maximised");
		logger.trace("This is Trace Message");
		logger.error("This is Error Message");
		logger.debug("This is Debug Message");
		logger.fatal("This is Fatal message");
		logger.warn("This is a Warning mesage");
		// Initialize the action driver only once
		/*
		 * if (actiondriver == null) { actiondriver = new ActionDriver(driver);
		 * logger.info("Action Drive Instance is created:"+Thread.currentThread().getId(
		 * )); }
		 */

		// Initialize action driver for the current Thread
		actiondriver.set(new ActionDriver(getDriver()));
		logger.info("Action Driver initialised for Thread" + Thread.currentThread().getId());

	}

	private synchronized void launchBrowser() {
		// Initialise the webdriver instance based on the browser defined in
		// config.properties file
		String Browser = prop.getProperty("browser");
		if (Browser.equalsIgnoreCase("Chrome")) {
			// driver = new ChromeDriver();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless=new");
			// Try this specific argument for modern Headless Chrome
			options.addArguments("--screen-info={0,0 1920x1080}"); 
			options.addArguments("--window-size=1920,1080");
			options.addArguments("--force-device-scale-factor=1");
			options.addArguments("--disable-renderer-backgrounding");
			options.addArguments("--disable-backgrounding-occluded-windows");
			/*options.addArguments("--headless=new"); // 1. Run in Headless mode (optional)
			options.addArguments("--disable-gpu");// Disable GPU for headless mode
			options.addArguments("--window-size=1920,1080"); // Force Desktop resolution
			options.addArguments("--disable-notifications");// Disable Browser Notifications
			options.addArguments("--no-sandbox");// This flag allows Chrome to launch in these restricted environments.
			options.addArguments("--disable-dev-shm-usage");// Resolve issues in resource
			// Inside ChromeOptions setup
			options.setPageLoadStrategy(PageLoadStrategy.NORMAL); // Ensures full page load
			options.addArguments("--disable-blink-features=AutomationControlled"); // Helps bypass bot detection
			options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36");
			/*
			 * What it does: It forces Chrome to use the /tmp folder for its internal memory
			 * instead of the /dev/shm shared memory partition.Why use it: By default, Linux
			 * Docker containers only allocate 64MB of shared memory (/dev/shm). Chrome is a
			 * memory hog; once it hits that 64MB limit, the browser will crash ("Aw, Snap!"
			 * error). This flag tells Chrome to use the disk space instead of that tiny
			 * memory slice, preventing crashes during heavy page loads.
			 */
			driver.set(new ChromeDriver(options));// New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is created");
		} else if (Browser.equalsIgnoreCase("FireFox")) {
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless=new"); // 1. Run in Headless mode (optional)
			options.addArguments("--disable-gpu");// Disable GPU for headless mode
			options.addArguments("--disable-notifications");// Disable Browser Notifications
			options.addArguments("--no-sandbox");// This flag allows Chrome to launch in these restricted environments.
			options.addArguments("--disable-dev-shm-usage");//
			// Inside ChromeOptions setup
			options.setPageLoadStrategy(PageLoadStrategy.NORMAL); // Ensures full page load
			options.addArguments("--disable-blink-features=AutomationControlled"); // Helps bypass bot detection
			// driver = new FirefoxDriver();
			driver.set(new FirefoxDriver(options));// New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("FireFoxDriver Instance is created");
		} else if (Browser.equalsIgnoreCase("Edge")) {
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless=new"); // 1. Run in Headless mode (optional)
			options.addArguments("--disable-gpu");// Disable GPU for headless mode
			options.addArguments("--disable-notifications");// Disable Browser Notifications
			options.addArguments("--no-sandbox");// This flag allows Chrome to launch in these restricted environments.
			options.addArguments("--disable-dev-shm-usage");//
			// driver = new EdgeDriver();
			driver.set(new EdgeDriver(options));// New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver Instance is created");
		} else {
			throw new IllegalArgumentException("Undefined Browser:" + Browser);
		}

	}

	private void ConfigBrowser() {
		// maximize the browser
		getDriver().manage().window().maximize();
		// implicitwait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitwait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		// Navigate to URL
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to Navigate to the Url:" + e.getMessage());

		}
	}

	/*@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit the driver:" + e.getMessage());
			}
		}
		logger.info("WebDriver Instance is closed");
		// driver = null;
		// actiondriver = null;
		driver.remove();
		actiondriver.remove();
		softAssert.remove();
		// ExtentManager.endTest();//This has been implemented in Test Listener
	}*/
	@AfterMethod
	public synchronized void tearDown() {
	    // Small delay to allow the Listener to capture the final state 
	    // before we kill the session
	    StaticWait(1); 

	    if (getDriver() != null) {
	        try {
	            logger.info("Closing WebDriver Instance for Thread: " + Thread.currentThread().getId());
	            getDriver().quit();
	        } catch (Exception e) {
	            System.out.println("Unable to quit the driver: " + e.getMessage());
	        } finally {
	            driver.remove();
	            actiondriver.remove();
	            softAssert.remove();
	        }
	    }
	}

	public void StaticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	// Method to get element Description

	public String getElementDescription(By locator) {
		// Check whether the driver is null and locator is null to avoid Null Pointer
		// Exception
		if (driver == null)
			return "driver is null";
		if (locator == null)
			return "locator is null";

		try {
			// find the element using locator
			WebElement element = getDriver().findElement(locator);
			// Get Element description
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String classname = element.getDomAttribute("class");
			String placeholder = element.getDomAttribute("placeholder");
			String alt = element.getDomAttribute("alt");
			// Return the description based on element attributes
			if (isNotEmpty(name)) {
				return "Element with name:" + name;
			} else if (isNotEmpty(id)) {
				return "Element with name:" + id;
			} else if (isNotEmpty(text)) {
				return "Element with text:" + truncate(text, 50);
			} else if (isNotEmpty(classname)) {
				return "Element with class:" + classname;
			} else if (isNotEmpty(placeholder)) {
				return "Element with placeholder:" + placeholder;
			} else if (isNotEmpty(alt)) {
				return "Element with alt:" + alt;
			}

		} catch (Exception e) {
			logger.error("Unable to describe the element:" + e.getMessage());
		}
		return "Unable to describe the element";

	}

	// utility method to check if a string is not null or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();

	}

	// utility method to truncate long string
	private String truncate(String value, int maxlength) {
		if (value == null || value.length() <= maxlength) {
			return value;
		}
		return value.substring(0, maxlength) + "...";
	}

}
