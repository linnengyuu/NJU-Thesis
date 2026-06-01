package NEW.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;


 //App → Activity 列表页。

public class ActivityPage extends BasePage {

    public ActivityPage(AndroidDriver driver) {
        super(driver);
    }

    /** 点击 Activity 入口 */
    public void clickActivity() {
        driver.findElement(By.xpath(
                "//android.widget.TextView[@content-desc=\"Activity\"]")).click();
    }

    /** 点击 Custom Title */
    public void clickCustomTitle() {
        driver.findElement(By.xpath(
                "//android.widget.TextView[@content-desc=\"Custom Title\"]")).click();
    }

    /** 点击 Hello World */
    public void clickHelloWorld() {
        driver.findElement(By.xpath(
                "//android.widget.TextView[@content-desc=\"Hello World\"]")).click();
    }

    /** 点击 Receive Result */
    public void clickReceiveResult() {
        driver.findElement(By.xpath(
                "//android.widget.TextView[@content-desc=\"Receive Result\"]")).click();
    }

    /** 点击 Alert Dialogs */
    public void clickAlertDialogs() {
        driver.findElement(By.xpath(
                "//android.widget.TextView[@content-desc=\"Alert Dialogs\"]")).click();
    }
}
