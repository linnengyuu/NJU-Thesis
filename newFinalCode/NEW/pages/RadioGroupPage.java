package NEW.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

// Views → Radio Group 

public class RadioGroupPage extends BasePage {

    // Locator
    private static final String RESULT_DESC    = "You have selected: (none)";
    private static final String ID_SNACK       = "io.appium.android.apis:id/snack";
    private static final String DESC_BREAKFAST = "Breakfast";
    private static final String DESC_LUNCH     = "Lunch";
    private static final String DESC_DINNER    = "Dinner";
    private static final String DESC_CLEAR     = "Clear";

    public RadioGroupPage(AndroidDriver driver) {
        super(driver);
    }

    public void navigateToRadioGroup() {
        scrollToText("Radio Group").click();
    }

    public void clickClear() {
        driver.findElement(By.xpath(
                "//android.widget.Button[@content-desc=\"" + DESC_CLEAR + "\"]")).click();
    }

    public void clickSnack() {
        driver.findElement(By.xpath(
                "//android.widget.RadioButton[@resource-id=\"io.appium.android.apis:id/snack\"]")).click();
    }

    public void clickBreakfast() {
        driver.findElement(By.xpath(
                "//android.widget.RadioButton[@content-desc=\"" + DESC_BREAKFAST + "\"]")).click();
    }

    public void clickLunch() {
        driver.findElement(By.xpath(
                "//android.widget.RadioButton[@content-desc=\"" + DESC_LUNCH + "\"]")).click();
    }

    public void clickDinner() {
        driver.findElement(By.xpath(
                "//android.widget.RadioButton[@content-desc=\"" + DESC_DINNER + "\"]")).click();
    }

    
    public String getSelectionResult() {
        WebElement result = driver.findElement(By.xpath(
                "//android.widget.TextView[@content-desc=\"" + RESULT_DESC + "\"]"));
        return result.getText();
    }
}
