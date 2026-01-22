package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyser implements IRetryAnalyzer {
	private int retryCount = 0;// number of Retries
	private static final int retryMaxLength = 1;// Maximum number of retries

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < retryMaxLength) {
			retryCount++;
			return true;// Retry the test
		}
		return false;
	}

}
