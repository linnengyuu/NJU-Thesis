package NEW.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;


// ApiDemosMainPage 
public class ApiDemosMainPage extends BasePage {

    public ApiDemosMainPage(AndroidDriver driver) {
        super(driver);
    }

    /** 点击主页的 Views 入口 */
    public void clickViews() {
        WebElement views = driver.findElement(
                org.openqa.selenium.By.xpath("//android.widget.TextView[@content-desc=\"Views\"]"));
        views.click();
    }

    /** 点击主页的 App 入口 */
    public void clickApp() {
        WebElement app = driver.findElement(
                org.openqa.selenium.By.xpath("//android.widget.TextView[@content-desc=\"App\"]"));
        app.click();
    }
}
