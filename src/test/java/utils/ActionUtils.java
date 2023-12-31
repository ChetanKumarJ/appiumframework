package utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import java.time.Duration;

import org.openqa.selenium.WebElement;

import static io.appium.java_client.touch.TapOptions.tapOptions;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofSeconds;

public class ActionUtils {
	
	   //Tap to an element.
    public void tapOnElement(WebElement androidElement, AndroidDriver driver) {
        new TouchAction(driver)
                .tap(tapOptions().withElement(element(androidElement)))
                .perform();
    }

    //Tap to an element for particular amount of time.
    public void tapOnElementForParticularDuration(WebElement androidElement, AndroidDriver driver, long millis) {
        new TouchAction(driver)
                .tap(tapOptions().withElement(element(androidElement)))
                .waitAction(waitOptions(Duration.ofMillis(millis))).perform();
    }

    //Tap on coordinates
    public void tapOnCoordinates(AndroidDriver driver, int x, int y) {
        new TouchAction(driver)
                .tap(point(x, y))
                .perform();
    }

    //Tap on coordinates for particular amount of time.
    public void tapOnCoordinates(AndroidDriver driver, int x, int y, long millis) {
        new TouchAction(driver)
                .tap(point(x, y))
                .waitAction(waitOptions(Duration.ofMillis(millis))).perform();
    }

}
