package Demo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.MobileBy;

public class ApiDemosEmulator {
	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		//Gather Desire Capabilities
		UiAutomator2Options options = new UiAutomator2Options(); //set the enviroment
        options.setDeviceName("Emulator 16.0");
        options.setPlatformName("Android");
        options.setPlatformVersion("16"); 
        options.setAutomationName("UiAutomator2");
		options.setAppPackage("io.appium.android.apis"); //set the application
        options.setAppActivity("io.appium.android.apis.ApiDemos"); //adb shell, dumpsys window displays | grep -e 'mCurrentFocus'

        URL url = URI.create("http://127.0.0.1:4723/").toURL(); // make sure Appium Server run in port 4723
        AndroidDriver driver = new AndroidDriver(url, options);
		Thread.sleep(2000);
		System.out.println("Application Started");
		
		WebElement views = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Views\"]"));
		views.click();
		
		/*
		WebElement textFields = driver.findElement(MobileBy.AndroidUIAutomator( "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
			    + "new UiSelector().text(\"TextFields\"));"));
		*/
		WebElement textFields = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"TextFields\"]"));
		textFields.click();
		
		WebElement username = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"io.appium.android.apis:id/edit\"]"));
		username.sendKeys("225220003_username");
		
		WebElement password = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"io.appium.android.apis:id/edit1\"]"));
		password.sendKeys("225220003_password");
		
		WebElement loginsimulation = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"io.appium.android.apis:id/edit2\"]"));
		loginsimulation.sendKeys("LoginSimulation");
		
		WebElement NJUCS = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"io.appium.android.apis:id/edit3\"]"));
		NJUCS.sendKeys("NJUCS");
		
		WebElement thesis = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"io.appium.android.apis:id/edit4\"]"));
		thesis.sendKeys("Thesis");
		
		System.out.println("Pass");
		//driver.quit();
	}
}
