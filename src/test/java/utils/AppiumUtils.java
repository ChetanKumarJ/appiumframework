package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;

import java.util.List;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;

/**
 * 
 * This class contains custom appium/selenium methods for Webelement
 *
 */
public class AppiumUtils {
	public static WaitUtils waitUtils = new WaitUtils();

	/**
	 * This will check whether element is displayed on UI or not
	 *
	 * @param element
	 * @return
	 */
	public static boolean isElementDisplayed(WebElement element) {
		waitUtils.staticWait(500);
		boolean isPresent = false;
		try {
			element.isDisplayed();
			isPresent = true;
		} catch (Exception e) {
			isPresent = false;
		}
		return isPresent && element.isDisplayed();
	}
	/*
		*//**
			 * It will scroll down to particular element
			 *
			 * @param element
			 * @param driver
			 */
	/*
	 * public static void scrollToElement(WebElement element, AppiumDriver driver) {
	 * int i = 0; do { i++; Action swipe = new
	 * TouchAction(driver).press(PointOption.point(200, 500))
	 * .waitAction(waitOptions(ofSeconds(2))).moveTo(PointOption.point(200,
	 * -100)).release(); swipe.perform(); } while (!isElementDisplayed((WebElement)
	 * element)); // (i==2); }
	 * 
	 *//**
		 * This will scroll up until element is visible
		 *
		 * @param element
		 * @param driver
		 */
	/*
	 * public static void scrollUpToElement(WebElement element, AppiumDriver driver)
	 * { Dimension size = driver.manage().window().getSize(); int start_Y = (int)
	 * (size.getHeight() * 0.80); int end_Y = (int) (size.getHeight() * 0.20); int
	 * start_end_X = (int) (size.getWidth() * 0.50); while
	 * (!isElementDisplayed((WebElement) element)) { TouchAction swipe = new
	 * TouchAction(driver).press(PointOption.point(start_end_X, end_Y))
	 * .waitAction(waitOptions(ofSeconds(2))).moveTo(PointOption.point(start_end_X,
	 * start_Y)).release() .perform(); } }
	 * 
	 *//**
		 * This method will use to scroll vertically
		 *
		 * @param driver
		 */
	/*
	 * public static void verticalScroll(AppiumDriver driver) { Dimension size =
	 * driver.manage().window().getSize(); int start_Y = (int) (size.getHeight() *
	 * 0.75); int end_Y = (int) (size.getHeight() * 0.10); int start_end_X = (int)
	 * (size.getWidth() * 0.50);
	 * 
	 * TouchAction swipe = new
	 * TouchAction(driver).press(PointOption.point(start_end_X, start_Y))
	 * .waitAction(waitOptions(ofSeconds(2))).moveTo(PointOption.point(start_end_X,
	 * end_Y)).release() .perform(); }
	 * 
	 *//**
		 * This method will use to scroll vertically from given starting point to ending
		 * point
		 *
		 * @param driver
		 */
	/*
	 * public static void verticalScroll(AppiumDriver driver, int startY, int endY)
	 * { Dimension size = driver.manage().window().getSize(); // int start_Y = (int)
	 * (size.getHeight() * 0.95); // int end_Y = (int) (size.getHeight() * 0.10);
	 * int start_end_X = (int) (size.getWidth() * 0.50);
	 * 
	 * TouchAction swipe = new
	 * TouchAction(driver).press(PointOption.point(start_end_X, startY))
	 * .waitAction(waitOptions(ofSeconds(6))).moveTo(PointOption.point(start_end_X,
	 * endY)).release().perform(); }
	 * 
	 *//**
		 * This method would swipe on Screen, you need to pass 2 locations: 1) Start
		 * Location(startX, startY) 2) End Location(endX, endY)
		 *
		 * @param startX
		 * @param startY
		 * @param endX
		 * @param endY
		 */
	/*
	 * public static void doSwipe(int startX, int startY, int endX, int endY,
	 * AppiumDriver driver) { TouchAction swipe = new
	 * TouchAction(driver).press(PointOption.point(startX, startY))
	 * .waitAction(waitOptions(ofMillis(800))).moveTo(PointOption.point(endX,
	 * endY)).release().perform(); }
	 * 
	 *//**
		 * This method will click at particular point+
		 *
		 * @param x
		 * @param y
		 * @param driver
		 *//*
			 * public static void clickOnPoint(int x, int y, AppiumDriver driver) { new
			 * TouchAction(driver).press(PointOption.point(x, y)).release().perform(); }
			 */

	/**
	 * This method is used to generate 4 digit randomNumber
	 *
	 * @return
	 */
	public static String getRandomNumber() {
		return String.valueOf(System.currentTimeMillis() % 100000);
	}

	/**
	 * Return whether Alert is displayed or not
	 */
	public static boolean isAlertPresent(AppiumDriver driver) {
		boolean alertIsPresent = false;
		try {
			driver.switchTo().alert();
			alertIsPresent = true;
		} catch (Exception ex) {
			alertIsPresent = false;
		}
		return alertIsPresent;
	}

	/**
	 * This method will return the Element using Element Text from Element List
	 *
	 * @param driver
	 * @param elementList
	 * @param textOfElement
	 * @return
	 */
	public static WebElement getElementFromText(AppiumDriver driver, List<WebElement> elementList,
			String textOfElement) {
		for (WebElement el : elementList) {
			if (textOfElement.equals(el.getText())) {
				return el;
			}
		}
		return null;
	}

}