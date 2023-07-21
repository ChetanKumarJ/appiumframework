package pageobject;

import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class CartPO extends BasePO{

	public CartPO(AndroidDriver driver) {
		super(driver);
	}
	
	WebElement visitWebsite = driver.findElement(AppiumBy.id("com.androidsample.generalstore:id/btnProceed"));
	
	public void visitWebsite() {
		waitUtils.waitForElementToBeVisible(visitWebsite, driver);
		visitWebsite.click();
	}
}