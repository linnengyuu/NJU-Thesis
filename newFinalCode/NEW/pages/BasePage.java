package NEW.pages;  // ← 改这里

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
// ...

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


 // All Page Object Base Class
public abstract class BasePage {

    protected AndroidDriver driver;

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
    }

    protected WebElement scrollToText(String text) {
        return driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                        + "new UiSelector().text(\"" + text + "\"));"));
    }

    /** 点击导航返回键 */
    public void navigateBack() {
        driver.navigate().back();
    }

    public void navigateBack(int times) {
        for (int i = 0; i < times; i++) {
            driver.navigate().back();
        }
    }

    protected WebElement findByDesc(String desc) {
        return driver.findElement(By.xpath(
                "//android.widget.TextView[@content-desc=\"" + desc + "\"]"));
    }
}
