package pageobject;

import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class HomeScreenPO extends BasePO{
	 /**
     * A base constructor that sets the page's driver
     * <p>
     * The page structure is being used within this test in order to separate the
     * page actions from the tests.
     * <p>
     * Please use the AppiumFieldDecorator class within the page factory. This way annotations
     * like @AndroidFindBy within the page objects.
     *
     * @param driver the appium driver created in the beforesuite method.
     */
    public HomeScreenPO(AndroidDriver driver) {
        super(driver);
    }

    WebElement selectCountrydropdown = driver.findElement(AppiumBy.id("com.androidsample.generalstore:id/spinnerCountry"));
    WebElement loginScreenTextView = driver.findElement(AppiumBy.id("com.androidsample.generalstore:id/nameField"));
    WebElement femalegenderradiobutton = driver.findElement(AppiumBy.id("com.androidsample.generalstore:id/radioFemale"));
    WebElement letshopbutton = driver.findElement(AppiumBy.id("com.androidsample.generalstore:id/btnLetsShop"));   

    /**
     * This method will click on Login Screen textview.
     */
    public void tapOnLoginScreenTextView(String dropdownvalue){    	
    	waitUtils.waitForElementToBeVisible(loginScreenTextView, driver);
    	femalegenderradiobutton.click();
        loginScreenTextView.sendKeys("JOHN");
        selectCountrydropdown.click();
        driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\""+dropdownvalue+"\"));")).click();
        letshopbutton.click();
    }
}