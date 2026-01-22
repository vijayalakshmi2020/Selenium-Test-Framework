package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.APIUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyser;

import io.restassured.response.Response;

public class APIVerificationTest 
{
@Test(retryAnalyzer=RetryAnalyser.class)
public void verifyGetUserAPI()
{
	//Step-1 define api endpoint
	SoftAssert softAssert = new SoftAssert();
	String endPoint="http://localhost:8080/dashboard/api.php";
	ExtentManager.logStep("API endpoint: "+endPoint);
	//Step 2- Send GET Request
	ExtentManager.logStep("Sending Post Request to the API");
	Response response = APIUtility.sendPostRequest(endPoint, "{\"username\": \"Viji\", \"email\": \"viji@testmail.com\"}");
	//Response response=APIUtility.sendGetRequest(endPoint);
	//Step 3- Validate Status Code
	ExtentManager.logStep("Validating API Response Status Code");
	Boolean isStatusCodeValid=APIUtility.validateStatusCode(response, 200);
	softAssert.assertTrue(isStatusCodeValid,"Status Code is not expected");
	
	if(isStatusCodeValid)
	{
		ExtentManager.logStep("Status Code is passed:"+isStatusCodeValid);
		ExtentManager.logStepwithAPI("Test Passed: Status Code is matching");
	}else {
		ExtentManager.logFailurewithAPI("Status code is not matching:Failed");
	}
	//Step4- Validate UserName
	ExtentManager.logStep("Validating Response Body for username");
	//String userName=APIUtility.getJsonValue(response,"username");
	// Step 1: Send the POST request (Crucial for getting data back)
	// Step 2: Use your existing method with the DOT NOTATION path
	String userName = APIUtility.getJsonValue(response, "received_info.username");
	String userEmail = APIUtility.getJsonValue(response, "received_info.email");

	System.out.println("DEBUG: Username is " + userName); // Should now show [Viji]
	//String userName = APIUtility.getJsonValue(response, "received_info.username");

	// DEBUG: Print the actual value to the Eclipse console
	System.out.println("FULL RESPONSE: " + response.asPrettyString());
	System.out.println("DEBUG: The Username from API is: [" + userName + "]"); 
	boolean isUserNameValid="Viji".equals(userName);
	Assert.assertTrue(isUserNameValid,"username is not valid");
	if (isUserNameValid)
	{
		ExtentManager.logStep("Username Validation passed:"+isUserNameValid);
		ExtentManager.logStepwithAPI("Test Passed: Username is matching");
	}else {
		ExtentManager.logFailurewithAPI("Username is not matching:Failed");
	}
	//Step5- Validate Email
	ExtentManager.logStep("Validating Response Body for username");
	//String userEmail=APIUtility.getJsonValue(response,"username");
	//String userEmail = APIUtility.getJsonValue(response, "received_info.email");
	boolean isUserEmailValid="viji@testmail.com".equals(userEmail);
	softAssert.assertTrue(isUserEmailValid,"username is not valid");
	if (isUserEmailValid)
	{
		ExtentManager.logStep("Username Validation passed:"+isUserEmailValid);
		ExtentManager.logStepwithAPI("Test Passed: UserEmail is matching");
	}else {
		ExtentManager.logFailurewithAPI("UserEmail is not matching:Failed");
	}
	softAssert.assertAll();
}
}
