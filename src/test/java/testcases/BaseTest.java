package testcases;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.PropertyUtils;
import utils.ScreenshotUtility;
import utils.TestUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 
 * An abstract base for all of the Android tests within this package Responsible
 * for setting up the Appium test Driver
 *
 * @project POM_Automation_Framework
 */

@Listeners({ ScreenshotUtility.class })
public abstract class BaseTest {
	/**
	 * As driver static it will be created only once and used across all of the test
	 * classes.
	 */
	public static AndroidDriver androidDriver;
	public static IOSDriver iosDriver;
	public static AppiumDriverLocalService server;
	public final static String APPIUM_SERVER_URL = PropertyUtils.getProperty("appium.server.url",
			"localhost:4723/wd/hub");
	public final static int IMPLICIT_WAIT = PropertyUtils.getIntegerProperty("implicitWait", 50);
	public static WaitUtils waitUtils = new WaitUtils();
	public static TestUtils utils = new TestUtils();
	protected static ThreadLocal<String> platform = new ThreadLocal<String>();
	protected static ThreadLocal<String> dateTime = new ThreadLocal<String>();
	protected static ThreadLocal<String> deviceName = new ThreadLocal<String>();

	/**
	 * It will set the DesiredCapabilities for the local execution
	 *
	 * @param desiredCapabilities
	 */
	protected UiAutomator2Options setDesiredCapabilitiesForAndroid() {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		String PLATFORM_NAME = PropertyUtils.getProperty("android.platform");
		String PLATFORM_VERSION = PropertyUtils.getProperty("android.platform.version");
		String APP_NAME = PropertyUtils.getProperty("android.app.name");
		String APP_RELATIVE_PATH = PropertyUtils.getProperty("android.app.location") + APP_NAME;
		String APP_PATH = getAbsolutePath(APP_RELATIVE_PATH);
		String DEVICE_NAME = PropertyUtils.getProperty("android.device.name");
		String APP_PACKAGE_NAME = PropertyUtils.getProperty("android.app.packageName");
		String APP_ACTIVITY_NAME = PropertyUtils.getProperty("android.app.activityName");
		String APP_FULL_RESET = PropertyUtils.getProperty("android.app.full.reset");
		String APP_NO_RESET = PropertyUtils.getProperty("android.app.no.reset");

		UiAutomator2Options options = new UiAutomator2Options().setDeviceName(DEVICE_NAME).setApp(APP_RELATIVE_PATH)
				.setAppWaitForLaunch(true).setAppWaitActivity(APP_ACTIVITY_NAME).setAutoGrantPermissions(true)
				.setFullReset(true).eventTimings();

		return options;
	}

	/**
	 * It will get the DesiredCapabilities for the IOS
	 *
	 * @return DesiredCapabilities
	 */
	protected DesiredCapabilities getDesiredCapabilitiesForIOS() {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		String PLATFORM_NAME = PropertyUtils.getProperty("ios.platform");
		String PLATFORM_VERSION = PropertyUtils.getProperty("ios.platform.version");
		String APP_NAME = PropertyUtils.getProperty("ios.app.name");
		String APP_RELATIVE_PATH = PropertyUtils.getProperty("ios.app.location") + APP_NAME;
		String APP_PATH = getAbsolutePath(APP_RELATIVE_PATH);
		String DEVICE_NAME = PropertyUtils.getProperty("ios.device.name");
		String APP_FULL_RESET = PropertyUtils.getProperty("ios.app.full.reset");
		String APP_NO_RESET = PropertyUtils.getProperty("ios.app.no.reset");

		desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
		desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DEVICE_NAME);
		desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
		desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, PLATFORM_VERSION);
		desiredCapabilities.setCapability(MobileCapabilityType.APP, APP_PATH);
		desiredCapabilities.setCapability(MobileCapabilityType.FULL_RESET, APP_FULL_RESET);
		desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, APP_NO_RESET);
		desiredCapabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
		return desiredCapabilities;
	}

	// @BeforeSuite
	public void startServerUsingCMD() {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("cmd.exe /c start cmd.exe /k \"appium -a 127.0.0.1 -p 4723\"");
			Thread.sleep(30000);
			utils.log().info("Appium Server Started");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will run at the time of Test Suite creatopn so it will run at
	 * once through out the execution
	 * <p>
	 * Appium is a client - server model: So we need to set up appium client in
	 * order to connect to Device Farm's appium server.
	 * 
	 * @throws Exception
	 * @throws AppiumServerHasNotBeenStartedLocallyException
	 */
	@BeforeSuite
	public void beforesuite() throws AppiumServerHasNotBeenStartedLocallyException, Exception {
		ThreadContext.put("ROUTINGKEY", "ServerLogs");
		server = getAppiumService();
		if (!checkIfAppiumServerIsRunnning(4723)) {
			server.start();
			server.clearOutPutStreams(); // -> Comment this if you want to see server logs in the console
			utils.log().info("Appium Server Started");
		} else {
			utils.log().info("Appium server already running");
		}
	}

	/**
	 * At the end of the Test Suite(At last) this method would be called
	 */
	@AfterSuite(alwaysRun = true)
	public void tearDownAppium() {
		if (server.isRunning()) {
			server.stop();
			utils.log().info("Appium Server Stopped");
		}
	}

	// @AfterSuite
	public void stopServerUsingCMD() {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("taskkill /F /IM node.exe");
			utils.log().info("Appium Server Stopped");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public AppiumDriverLocalService getAppiumServerDefault() {
		return AppiumDriverLocalService.buildDefaultService();
	}

	public AppiumDriverLocalService getAppiumService() {
		HashMap<String, String> environment = new HashMap<String, String>();
		environment.put("PATH",
				"C:\\Program Files\\Java\\jdk-18.0.1.1\\bin;C:\\Program Files\\ApacheMaven\\apache-maven-3.8.6\\bin;C:\\Program Files\\dotnet\\;C:\\WINDOWS\\system32;C:\\WINDOWS;C:\\WINDOWS\\System32\\Wbem;C:\\WINDOWS\\System32\\WindowsPowerShell\\v1.0\\;C:\\WINDOWS\\System32\\OpenSSH\\;C:\\Program Files\\nodejs\\;C:\\Users\\chetan.kumar\\AppData\\Local\\Android\\Sdk\\platform-tools;C:\\Users\\chetan.kumar\\AppData\\Local\\Android\\Sdk\\cmdline-tools;C:\\Users\\chetan.kumar\\AppData\\Local\\Microsoft\\WindowsApps;C:\\Users\\chetan.kumar\\AppData\\Roaming\\npm;");
		return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
				.usingDriverExecutable(new File("C:/Program Files/nodejs/node.exe"))
				.withAppiumJS(
						new File("C:/Users/chetan.kumar/AppData/Roaming/npm/node_modules/appium/build/lib/main.js"))
				.usingPort(4723).withArgument(GeneralServerFlag.SESSION_OVERRIDE).withEnvironment(environment)
				.withLogFile(new File("ServerLogs/server.log")));
	}

	/**
	 * This method will be called everytime before your test runs
	 */
	@BeforeTest
	public void setUpPage() throws IOException {

	}

	@AfterTest(alwaysRun = true)
	public void afterTest() {
	}

	@BeforeMethod
	public abstract void setUpAppium() throws MalformedURLException;

	/**
	 * This method will always execute after each test case, This will quit the
	 * WebDriver instance called at the last
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(final ITestResult result) throws IOException {
		if (androidDriver != null) {
			androidDriver.quit();
		}
		String fileName = result.getTestClass().getName() + "_" + result.getName();
		System.out.println("Test Case: [" + fileName + "] executed..!");
	}

	/**
	 * This method will be called after class finishes the execution of all tests
	 */
	@AfterClass
	public void afterClass() {
	}

	public static WebDriver getScreenshotableWebDriver() {
		final WebDriver augmentedDriver = new Augmenter().augment(androidDriver);
		return augmentedDriver;
	}

	/**
	 * This method will be called when test case is getting failed or skipped.
	 */
	private void takeLocalScreenshot(String imageName) throws IOException {
		File scrFile = ((TakesScreenshot) androidDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File("failureScreenshots/" + imageName + ".png"));
	}

	/**
	 * This will set implicit wait
	 *
	 * @param driver
	 */
	private static void setTimeOuts(AppiumDriver driver) {
		// Use a higher value if your mobile elements take time to show up
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
	}

	private static String getAbsolutePath(String appRelativePath) {
		File file = new File(appRelativePath);
		return file.getAbsolutePath();
	}

	public static String getPlatform() {
		return PropertyUtils.getProperty("android.platform");
	}

	public static String getDeviceName() {
		return PropertyUtils.getProperty("android.device.name");
	}

	public static String getDateTime() {
		return dateTime.get();
	}

	public boolean checkIfAppiumServerIsRunnning(int port) throws Exception {
		boolean isAppiumServerRunning = false;
		ServerSocket socket;
		try {
			socket = new ServerSocket(port);
			socket.close();
		} catch (IOException e) {
			System.out.println("1");
			isAppiumServerRunning = true;
		} finally {
			socket = null;
		}
		return isAppiumServerRunning;
	}

	/**
	 * This will quite the android driver instance
	 */
	private void quitDriver() {
		try {
			this.androidDriver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Logger log() {
		return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
	}
}