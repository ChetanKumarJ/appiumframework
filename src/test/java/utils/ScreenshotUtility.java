package utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import reports.ExtentReport;
import testcases.BaseTest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtility implements ITestListener {
	TestUtils utils = new TestUtils();
	byte[] encoded = null;
	
	// This method will execute only on the event of fail test.
    public void onTestFailure(ITestResult result) {  	
    	String screenShotPath = captureScreenShot(result, "fail");
    	//ExtentReport.getTest().log(Status.FAIL, "Test Failure");
    	ExtentReport.getTest().fail("Test Failed", MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
    	//ExtentReport.getTest().fail("Test Failed", MediaEntityBuilder.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build()));
    	ExtentReport.getTest().fail(result.getThrowable());
    }
    
    // This method will execute before the main test start (@Test)
    public void onTestStart(ITestResult result) {
    	ExtentReport.startTest(result.getName(), result.getMethod().getDescription())
    	.assignCategory(BaseTest.getPlatform() + "_" +BaseTest.getDeviceName())
    	.assignAuthor("Chetan Kumar");
    }
    
    // This method will execute only when the test is pass.
    public void onTestSuccess(ITestResult result) {
    	//captureScreenShot(result, "pass");
    	ExtentReport.getTest().log(Status.PASS, "Test Passed");
    }
    
    // This method will execute only if any of the main test(@Test) get skipped
    public void onTestSkipped(ITestResult result) {
    	ExtentReport.getTest().log(Status.SKIP, "Test Skipped");    	
    }
    
    // This method will execute before starting of Test suite.
    public void onStart(ITestContext result) {

    }

    // This method will execute, Once the Test suite is finished.
    public void onFinish(ITestContext result) {
    	ExtentReport.getReporter().flush();
    }
    
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    // Function to capture screenshot.
    public String captureScreenShot(ITestResult result, String status) {
        final WebDriver augmentedDriver = BaseTest.getScreenshotableWebDriver();
        String destDir = "";
        String filePath ="";
        String passfailMethod = result.getMethod().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
        // To capture screenshot.
        File scrFile = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
        /*byte[] encoded = null;
        try {
        	encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(scrFile));
        }catch (IOException e1){
        	e1.printStackTrace();
        }*/
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
        // If status = fail then set folder name "screenshots/Failures"
        if (status.equalsIgnoreCase("fail")) {
            destDir = "screenshots/Failures";
        }
        // If status = pass then set folder name "screenshots/Success"
        else if (status.equalsIgnoreCase("pass")) {
            destDir = "screenshots/Success";
        }

        // To create folder to store screenshots
        new File(destDir).mkdirs();
        // Set file name with combination of test class name + date time.
        String destFile = passfailMethod + " - " + dateFormat.format(new Date()) + ".png";

        try {
            // Store file at destination folder location
        	filePath = destDir + "/" + destFile;
            FileUtils.copyFile(scrFile, new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}