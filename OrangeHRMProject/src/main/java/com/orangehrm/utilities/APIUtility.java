package com.orangehrm.utilities;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIUtility {
//Method to send the GET Request
	public static Response sendGetRequest(String endpoint)
	{
		return RestAssured.get(endpoint);
	}
	
//Method to send the POST Request
	public static Response sendPostRequest(String endpoint, String payload)
	{
	return RestAssured.given().header("Content-Type","application/json")
	                   .body(payload).post(endpoint);
	}
	
//Method to validate the Status Code
	public static Boolean validateStatusCode(Response response,int statusCode)
	{
		return response.getStatusCode() == statusCode;
	}
//Method to extract value from JSON Response
	public static String getJsonValue(Response response, String value)
	{
		return response.jsonPath().getString(value);
	}
	
}
