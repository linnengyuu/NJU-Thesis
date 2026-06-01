package NEW.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

// Views → TextFields

public class TextFieldsPage extends BasePage {

    // Locator
    private static final String ID_USERNAME   = "io.appium.android.apis:id/edit";
    private static final String ID_PASSWORD   = "io.appium.android.apis:id/edit1";
    private static final String ID_LOGIN_SIM  = "io.appium.android.apis:id/edit2";
    private static final String ID_SCHOOL     = "io.appium.android.apis:id/edit3";
    private static final String ID_THESIS     = "io.appium.android.apis:id/edit4";

    public TextFieldsPage(AndroidDriver driver) {
        super(driver);
    }

    public void navigateToTextFields() {
        scrollToText("TextFields").click();
    }

    public void enterUsername(String value) {
        driver.findElement(By.xpath(
                "//android.widget.EditText[@resource-id=\"" + ID_USERNAME + "\"]"))
              .sendKeys(value);
    }

    public void enterPassword(String value) {
        driver.findElement(By.xpath(
                "//android.widget.EditText[@resource-id=\"" + ID_PASSWORD + "\"]"))
              .sendKeys(value);
    }

    public void enterLoginSimulation(String value) {
        driver.findElement(By.xpath(
                "//android.widget.EditText[@resource-id=\"" + ID_LOGIN_SIM + "\"]"))
              .sendKeys(value);
    }

    public void enterSchool(String value) {
        driver.findElement(By.xpath(
                "//android.widget.EditText[@resource-id=\"" + ID_SCHOOL + "\"]"))
              .sendKeys(value);
    }

    public void enterThesis(String value) {
        driver.findElement(By.xpath(
                "//android.widget.EditText[@resource-id=\"" + ID_THESIS + "\"]"))
              .sendKeys(value);
    }

    //Inputting
    public void fillLoginForm(String username, String password,
                               String loginSim, String school, String thesis) {
        enterUsername(username);
        enterPassword(password);
        enterLoginSimulation(loginSim);
        enterSchool(school);
        enterThesis(thesis);
    }
}
