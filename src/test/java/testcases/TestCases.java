package testcases;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import pageobject.ProductPO;
import pageobject.CartPO;
import pageobject.HomeScreenPO;

public class TestCases extends BaseTest {

	@Test()
	public void checkoutCartTest() throws InterruptedException {
		//HomeScreenPO Obj creation
		HomeScreenPO homeScreenPO = new HomeScreenPO(androidDriver);		
		homeScreenPO.tapOnLoginScreenTextView("Argentina");
		//ProductPO Obj creation
		waitUtils.waitForPageLoad(1000);
		ProductPO productPO = new ProductPO(androidDriver);
		productPO.clickOnCartButton();		
		//CartPO Object creation
		waitUtils.waitForPageLoad(1000);
		CartPO cartPO = new CartPO(androidDriver);
		cartPO.visitWebsite();		
	}

	@Test
	public void addProductToCartTest() {
		//HomeScreenPO Obj creation
		HomeScreenPO homeScreenPO = new HomeScreenPO(androidDriver);
		//ProductPO Obj creation
		ProductPO productPO = new ProductPO(androidDriver);
		homeScreenPO.tapOnLoginScreenTextView("Argentina");
		productPO.clickOnCartButton();	
	}

	@BeforeMethod
	@Override
	public void setUpAppium() throws MalformedURLException {
		androidDriver = new AndroidDriver(new URL(APPIUM_SERVER_URL), setDesiredCapabilitiesForAndroid());
	}
}