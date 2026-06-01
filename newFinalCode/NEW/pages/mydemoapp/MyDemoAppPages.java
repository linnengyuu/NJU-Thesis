package NEW.pages.mydemoapp;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import NEW.pages.BasePage;
import java.util.concurrent.TimeUnit;

public class MyDemoAppPages {

	// ═══════════════════════════════════════════════════════════
	// MyDemoMainPage
	// ═══════════════════════════════════════════════════════════
	
	public static class MyDemoMainPage extends BasePage {
	
	    public MyDemoMainPage(AndroidDriver driver) {
	        super(driver);
	    }
	
	    public void clickFirstProduct() {
	        driver.findElement(By.xpath(
	                "(//android.widget.ImageView[@content-desc=\"Product Image\"])[1]")).click();
	    }
	
	    public void openMenu() {
	        driver.findElement(By.xpath(
	                "//android.widget.ImageView[@content-desc=\"View menu\"]")).click();
	    }
	
	    public void clickWebViewMenu() {
	        driver.findElement(By.xpath(
	                "//android.widget.TextView[@resource-id=\"com.saucelabs.mydemoapp.android:id/itemTV\""
	                + " and @text=\"WebView\"]")).click();
	    }
	}
	
	// ═══════════════════════════════════════════════════════════
	// ProductPage
	// ═══════════════════════════════════════════════════════════
	
	public static class ProductPage extends BasePage {
	
	    public ProductPage(AndroidDriver driver) {
	        super(driver);
	    }
	
	    public void clickFiveStar() {
	        driver.findElement(By.xpath(
	                "//android.widget.ImageView[@resource-id=\"com.saucelabs.mydemoapp.android:id/start5IV\"]"))
	              .click();
	    }
	
	    public void closeReviewDialog() {
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"Closes review dialog\"]")).click();
	    }
	
	    public void increaseQuantity() {
	        driver.findElement(By.xpath(
	                "//android.widget.ImageView[@content-desc=\"Increase item quantity\"]")).click();
	    }
	
	    public void addToCart() {
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"Tap to add product to cart\"]")).click();
	    }
	}
	
	// ═══════════════════════════════════════════════════════════
	// CartPage
	// ═══════════════════════════════════════════════════════════
	
	public static class CartPage extends BasePage {
	
	    public CartPage(AndroidDriver driver) {
	        super(driver);
	    }
	

	    public void openCart() {
	        driver.findElement(By.xpath(
	                "//android.widget.ImageView[@content-desc=\"Displays number of items in your cart\"]"))
	              .click();
	    }
	
	    public void proceedToCheckout() {
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"Confirms products for checkout\"]")).click();
	    }
	}
	
	// ═══════════════════════════════════════════════════════════
	// MyDemoLoginPage
	// ═══════════════════════════════════════════════════════════
	
	public static class MyDemoLoginPage extends BasePage {
	
	    private static final String ID_NAME     = "com.saucelabs.mydemoapp.android:id/nameET";
	    private static final String ID_PASSWORD = "com.saucelabs.mydemoapp.android:id/passwordET";
	    private static final String DESC_LOGIN  = "Tap to login with given credentials";
	
	    public MyDemoLoginPage(AndroidDriver driver) {
	        super(driver);
	    }
	

	    public void login(String username, String password) throws InterruptedException {
	        TimeUnit.SECONDS.sleep(2);
	        driver.findElement(By.xpath(
	                "//android.widget.EditText[@resource-id=\"" + ID_NAME + "\"]"))
	              .sendKeys(username);
	        driver.findElement(By.xpath(
	                "//android.widget.EditText[@resource-id=\"" + ID_PASSWORD + "\"]"))
	              .sendKeys(password);
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"" + DESC_LOGIN + "\"]")).click();
	        TimeUnit.SECONDS.sleep(2);
	    }
	}
	
	// ═══════════════════════════════════════════════════════════
	// ShippingPage
	// ═══════════════════════════════════════════════════════════
	
	public static class ShippingPage extends BasePage {
	
	    private static final String PKG = "com.saucelabs.mydemoapp.android:id/";
	
	    public ShippingPage(AndroidDriver driver) {
	        super(driver);
	    }
	
	    public void fillShippingInfo(String fullName, String address, String city,
	                                  String zip, String country) {
	        find(PKG + "fullNameET").sendKeys(fullName);
	        find(PKG + "address1ET").sendKeys(address);
	        find(PKG + "cityET").sendKeys(city);
	        find(PKG + "zipET").sendKeys(zip);
	        find(PKG + "countryET").sendKeys(country);
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"Saves user info for checkout\"]")).click();
	    }
	
	    private WebElement find(String resourceId) {
	        return driver.findElement(By.xpath(
	                "//android.widget.EditText[@resource-id=\"" + resourceId + "\"]"));
	    }
	}
	
	// ═══════════════════════════════════════════════════════════
	// PaymentPage
	// ═══════════════════════════════════════════════════════════
	
	public static class PaymentPage extends BasePage {
	
	    private static final String PKG = "com.saucelabs.mydemoapp.android:id/";
	
	    public PaymentPage(AndroidDriver driver) {
	        super(driver);
	    }
	
	    /** 填写支付信息、提交结账，并返回目录 */
	    public void fillPaymentAndCheckout(String name, String cardNumber,
	                                        String expiry, String cvv) {
	        find(PKG + "nameET").sendKeys(name);
	        find(PKG + "cardNumberET").sendKeys(cardNumber);
	        // expiry 用 By.id 定位（与原代码保持一致）
	        driver.findElement(By.id(PKG + "expirationDateET")).sendKeys(expiry);
	        find(PKG + "securityCodeET").sendKeys(cvv);
	
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"Saves payment info and launches screen to review checkout data\"]"))
	              .click();
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"Completes the process of checkout\"]")).click();
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"Tap to open catalog\"]")).click();
	    }
	
	    private WebElement find(String resourceId) {
	        return driver.findElement(By.xpath(
	                "//android.widget.EditText[@resource-id=\"" + resourceId + "\"]"));
	    }
	}
	
	// ═══════════════════════════════════════════════════════════
	// WebViewPage
	// ═══════════════════════════════════════════════════════════
	
	public static class WebViewPage extends BasePage {
	
	    private static final String ID_URL_INPUT = "com.saucelabs.mydemoapp.android:id/urlET";
	    private static final String TARGET_TEXT  = "版权所有©南京大学计算机学院";
	
	    public WebViewPage(AndroidDriver driver) {
	        super(driver);
	    }
	
	    public void goToUrl(String url) {
	        driver.findElement(By.xpath(
	                "//android.widget.EditText[@resource-id=\"" + ID_URL_INPUT + "\"]"))
	              .sendKeys(url);
	        driver.findElement(By.xpath(
	                "//android.widget.Button[@content-desc=\"Tap to view content of given url\"]")).click();
	    }
	
	    public String getCopyrightText() {
	        WebElement el = driver.findElement(MobileBy.AndroidUIAutomator(
	                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
	                        + "new UiSelector().text(\"" + TARGET_TEXT + "\"));"));
	        return el.getText();
	    }
	}

}
