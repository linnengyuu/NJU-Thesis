package NEW.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.concurrent.TimeUnit;


public class ActivitySubPages {

	// ═══════════════════════════════════════════════════════════
	// CustomTitlePage  —  App → Activity → Custom Title
	// ═══════════════════════════════════════════════════════════
	
	public static class CustomTitlePage extends BasePage {
	
	    private static final String DESC_LEFT_FIELD  = "Left is best";
	    private static final String DESC_RIGHT_FIELD = "Right is always right";
	    private static final String DESC_CHANGE_LEFT  = "Change Left";
	    private static final String DESC_CHANGE_RIGHT = "Change Right";
	
	    public CustomTitlePage(AndroidDriver driver) {
	        super(driver);
	    }
	
	    
	    public void changeLeftTitle(String value) {
	        WebElement field = driver.findElement(By.xpath(
	                "//android.widget.EditText[@content-desc=\"" + DESC_LEFT_FIELD + "\"]"));
	        field.clear();
	        field.sendKeys(value);
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"" + DESC_CHANGE_LEFT + "\"]")).click();
	    }
	
	    
	    public void changeRightTitle(String value) {
	        WebElement field = driver.findElement(By.xpath(
	                "//android.widget.EditText[@content-desc=\"" + DESC_RIGHT_FIELD + "\"]"));
	        field.clear();
	        field.sendKeys(value);
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"" + DESC_CHANGE_RIGHT + "\"]")).click();
	    }
	
	    
	    public String getLeftTitle() {
	        return driver.findElement(By.xpath(
	                "//android.widget.TextView[@content-desc=\"" + DESC_LEFT_FIELD + "\"]"))
	                     .getText();
	    }
	
	    
	    public String getRightTitle() {
	        return driver.findElement(By.xpath(
	                "//android.widget.TextView[@content-desc=\"" + DESC_RIGHT_FIELD + "\"]"))
	                     .getText();
	    }
	}
	
	// ═══════════════════════════════════════════════════════════
	// HelloWorldPage  —  App → Activity → Hello World
	// ═══════════════════════════════════════════════════════════
	
	public static class HelloWorldPage extends BasePage {
		
	    public HelloWorldPage(AndroidDriver driver) {
	        super(driver);
	    }
	
	    public String getDisplayedText() {
	        return driver.findElement(By.xpath(
	                "//android.widget.TextView[@content-desc=\"Hello, World!\"]"))
	                     .getText();
	    }
	}
	
	// ═══════════════════════════════════════════════════════════
	// ReceiveResultPage  —  App → Activity → Receive Result
	// ═══════════════════════════════════════════════════════════
	
	public static class ReceiveResultPage extends BasePage {
	
	    private static final String ID_RESULTS = "io.appium.android.apis:id/results";
	
	    public ReceiveResultPage(AndroidDriver driver) {
	        super(driver);
	    }
	
	    public void clickGetResult() throws InterruptedException {
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"Get Result\"]")).click();
	        TimeUnit.SECONDS.sleep(1);
	    }
	
	    /** 点击 Corky 选项 */
	    public void clickCorky() {
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"Corky\"]")).click();
	    }
	
	    /** 点击 Violet 选项 */
	    public void clickViolet() {
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"Violet\"]")).click();
	    }
	
	    /** 读取结果文字（resource-id 定位） */
	    public String getResultText() {
	        return driver.findElement(By.xpath(
	                "//android.widget.TextView[@resource-id=\"" + ID_RESULTS + "\"]"))
	                     .getText();
	    }
	}
	
	// ═══════════════════════════════════════════════════════════
	// AlertDialogsPage  —  App → Activity → Alert Dialogs
	// ═══════════════════════════════════════════════════════════
	
	public static class AlertDialogsPage extends BasePage {
	
	    private static final String ID_MESSAGE  = "android:id/message";
	    private static final String ID_BTN_OK   = "android:id/button1";
	
	    public AlertDialogsPage(AndroidDriver driver) {
	        super(driver);
	    }
	
	    /** 点击「OK Cancel dialog with a long message」按钮 */
	    public void clickLongMessageDialog() {
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"OK Cancel dialog with a long message\"]"))
	              .click();
	    }
	
	    /** 读取弹窗中的长文字 */
	    public String getLongMessageText() {
	        return driver.findElement(By.xpath(
	                "//android.widget.TextView[@resource-id=\"" + ID_MESSAGE + "\"]"))
	                     .getText();
	    }
	
	    /** 点击弹窗的 OK 按钮 */
	    public void clickOk() {
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@resource-id=\"" + ID_BTN_OK + "\"]")).click();
	    }
	}
}