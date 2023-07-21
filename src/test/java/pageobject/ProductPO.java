package pageobject;

import java.util.List;

import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class ProductPO extends BasePO {

	public ProductPO(AndroidDriver driver) {
		super(driver);
	}

	List<WebElement> addToCart = driver.findElements(AppiumBy.id("com.androidsample.generalstore:id/productAddCart"));
	WebElement cartbutton = driver.findElement(AppiumBy.id("com.androidsample.generalstore:id/appbar_btn_cart"));
	
	public void clickOnCartButton() {
	 waitUtils.waitForElementToBeVisible(addToCart.get(1), driver);
	 addToCart.get(1).click();
	 cartbutton.click();
	}
}
