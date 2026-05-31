package Demo;

import java.net.URI;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.NoSuchElementException;

public class allCase {

    static int sum = 0;

    static int totalCases = 0;
    static int passedCases = 0;
    static PrintWriter logWriter;
    static String failMessage = "";
    static String currentLogFileName = "";
    static boolean stoppedByElementNotFound = false;
    static String stoppedLine = "";
    static String stoppedMessage = "";

    interface TestStep {
        boolean run() throws Exception;
    }

    public static UiAutomator2Options setup_Emu(UiAutomator2Options options) {
        options.setDeviceName("Emulator 16.0");
        options.setPlatformName("Android");
        options.setPlatformVersion("16");
        options.setAutomationName("UiAutomator2");
        return options;
    }
    public static UiAutomator2Options setup_Real(UiAutomator2Options options) {
    	options.setDeviceName("OPPO CPH2025");
        options.setPlatformName("Android");
        options.setPlatformVersion("13"); 
        options.setAutomationName("UiAutomator2");
        options.setCapability("ignoreHiddenApiPolicyError", true);
        options.setNoReset(true); 
        return options;
    }
    

    public static UiAutomator2Options apidemos(UiAutomator2Options options) {
        options.setAppPackage("io.appium.android.apis");
        options.setAppActivity("io.appium.android.apis.ApiDemos");
        return options;
    }
    
    public static UiAutomator2Options mydemoapp(UiAutomator2Options options) {
        options.setAppPackage("com.saucelabs.mydemoapp.android");
        options.setAppActivity("com.saucelabs.mydemoapp.android/.view.activities.SplashActivity");
        return options;
    }

    // =========================
    // CSV Log Functions
    // =========================

    public static void startLog() throws IOException {
        currentLogFileName = getAvailableLogFileName();

        logWriter = new PrintWriter(new FileWriter(currentLogFileName, false));
        logWriter.print("\uFEFF");
        logWriter.println("TestTime,TestName,StepName,Status,Duration(ms),ErrorPoint,ErrorContent,PassedCases,TotalCases,SuccessRate(%)");
        logWriter.flush();

        System.out.println("Log file path: " + new java.io.File(currentLogFileName).getAbsolutePath());
    }
    
    
    public static String getAvailableLogFileName() {
        String folderPath = "F:\\PROJECT\\Mavenjava\\logfile";
        String baseName = "test_log";
        String extension = ".csv";

        java.io.File folder = new java.io.File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = folderPath + "\\" + baseName + extension;
        java.io.File file = new java.io.File(fileName);

        int index = 1;

        while (file.exists()) {
            fileName = folderPath + "\\" + baseName + "(" + index + ")" + extension;
            file = new java.io.File(fileName);
            index++;
        }

        return fileName;
    }

    public static String nowTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String csv(String text) {
        if (text == null) return "";
        text = text.replace("\"", "\"\"");
        text = text.replace("\r", " ");
        text = text.replace("\n", " ");
        return "\"" + text + "\"";
    }

    public static void writeLog(String testName, String stepName, String status,
                                long duration, String errorPoint, String errorContent) {
        double successRate = 0.0;
        if (totalCases > 0) {
            successRate = passedCases * 100.0 / totalCases;
        }

        logWriter.println(
                csv(nowTime()) + "," +
                csv(testName) + "," +
                csv(stepName) + "," +
                csv(status) + "," +
                duration + "," +
                csv(errorPoint) + "," +
                csv(errorContent) + "," +
                passedCases + "," +
                totalCases + "," +
                String.format("%.2f", successRate)
        );

        logWriter.flush();
    }

    public static boolean fail(String message) {
        failMessage = message;
        return false;
    }

    public static void runStep(String testName, String stepName, String errorPoint, TestStep step) {
    long start = System.currentTimeMillis();
    failMessage = "";
    totalCases++;

    try {
        boolean passed = step.run();
        long duration = System.currentTimeMillis() - start;

        if (passed) {
            passedCases++;
            writeLog(testName, stepName, "PASS", duration, "", "");
        } else {
            writeLog(testName, stepName, "FAIL", duration, errorPoint, failMessage);
        }

    } catch (Exception e) {
        long duration = System.currentTimeMillis() - start;

        StackTraceElement errorLine = e.getStackTrace()[0];
        String lineInfo = errorLine.getFileName() + ":" + errorLine.getLineNumber();

        writeLog(
                testName,
                stepName,
                "ERROR",
                duration,
                errorPoint,
                e.getClass().getSimpleName() + ": " + e.getMessage()
        );

        System.out.println(stepName + ": ERROR");
        System.out.println(e.getMessage());

        if (e instanceof NoSuchElementException) {
            stoppedByElementNotFound = true;
            stoppedLine = lineInfo;
            stoppedMessage = "Element not found. Test stopped at " + stoppedLine;

            throw new RuntimeException(stoppedMessage, e);
        }
    }
}

    public static boolean checkEquals(String stepName, String actual, String expected) {
        if (actual.equals(expected)) {
            System.out.println(stepName + ": Pass");
            sum++;
            return true;
        } else {
            System.out.println(stepName + ": Failed");
            return fail("Expected: " + expected + ", actual: " + actual);
        }
    }

    public static boolean checkContains(String stepName, String actual, String expectedKeyword) {
        if (actual.contains(expectedKeyword)) {
            System.out.println(stepName + ": Pass");
            sum++;
            return true;
        } else {
            System.out.println(stepName + ": Failed");
            return fail("Expected result contains: " + expectedKeyword + ", actual: " + actual);
        }
    }

    public static boolean checkContainsAll(String stepName, String actual, String keyword1, String keyword2) {
        if (actual.contains(keyword1) && actual.contains(keyword2)) {
            System.out.println(stepName + ": Pass");
            sum++;
            return true;
        } else {
            System.out.println(stepName + ": Failed");
            return fail("Expected result contains: " + keyword1 + " and " + keyword2 + ", actual: " + actual);
        }
    }

    public static void writeSummary(long totalDuration) {
        double successRate = 0.0;
        if (totalCases > 0) {
            successRate = passedCases * 100.0 / totalCases;
        }

        // 先写 Summary 总结行
        logWriter.println();
        logWriter.println(
                csv(nowTime()) + "," +
                csv("Summary") + "," +
                csv("All Test Cases") + "," +
                csv("FINISHED") + "," +
                totalDuration + "," +
                csv("") + "," +
                csv("Total Passed: " + passedCases + ", Total Cases: " + totalCases) + "," +
                passedCases + "," +
                totalCases + "," +
                String.format("%.2f", successRate)
        );

        // 如果是因为找不到元素导致测试停止，就在 CSV 最下面再写一行英文说明
        if (stoppedByElementNotFound) {
            logWriter.println(
                    csv(nowTime()) + "," +
                    csv("Final Error") + "," +
                    csv("Element Not Found") + "," +
                    csv("STOPPED") + "," +
                    totalDuration + "," +
                    csv("NoSuchElementException") + "," +
                    csv(stoppedMessage) + "," +
                    passedCases + "," +
                    totalCases + "," +
                    String.format("%.2f", successRate)
            );
        }

        logWriter.flush();

        System.out.println("=================================");
        System.out.println("Total Cases: " + totalCases);
        System.out.println("Passed Cases: " + passedCases);
        System.out.println("Success Rate: " + String.format("%.2f", successRate) + "%");
        System.out.println("Test Log Saved: " + currentLogFileName);

        if (stoppedByElementNotFound) {
            System.out.println(stoppedMessage);
        }

        System.out.println("=================================");
    }

    public static void closeLog() {
        if (logWriter != null) {
            logWriter.close();
        }
    }

    // =========================
    // Login Test
    // =========================

    public static void loginTest(AndroidDriver driver) {
        System.out.println("loginTest Started");

        runStep("LoginTest", "Click Views", "Find or click Views", () -> {
            WebElement views = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Views\"]"));
            views.click();
            System.out.println("Clicked Views");
            return true;
        });

        runStep("LoginTest", "Click TextFields", "Find or click TextFields", () -> {
            WebElement textFields = driver.findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                            + "new UiSelector().text(\"TextFields\"));"));
            textFields.click();
            System.out.println("Clicked TextFields");
            return true;
        });

        runStep("LoginTest", "Input TextFields", "Input text fields", () -> {
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

            return checkContains("Login Test", "All text fields input completed", "completed");
        });
    }

    // =========================
    // Radio Group Test
    // =========================

    public static void RadioGroupTest(AndroidDriver driver) {
        System.out.println("RadioGroupTest Started");

        runStep("RadioGroupTest", "Back To Main Page", "Navigate back", () -> {
            driver.navigate().back();
            driver.navigate().back();
            return true;
        });

        runStep("RadioGroupTest", "Click Views", "Find or click Views", () -> {
            WebElement views = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Views\"]"));
            views.click();
            System.out.println("Clicked Views");
            return true;
        });

        runStep("RadioGroupTest", "Click Radio Group", "Find or click Radio Group", () -> {
            WebElement radioGroup = driver.findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                            + "new UiSelector().text(\"Radio Group\"));"));
            radioGroup.click();
            System.out.println("Clicked RadioGroups");
            return true;
        });

        runStep("RadioGroupTest", "Clear Button", "Clear button result check", () -> {
            WebElement clear = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Clear\"]"));
            clear.click();
            System.out.println("Clicked Clear");

            WebElement result = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"You have selected: (none)\"]"));
            String resultString = result.getText();

            return checkEquals("Clear Button", resultString, "You have selected: (none)");
        });

        runStep("RadioGroupTest", "Snack Button", "Snack button result check", () -> {
            WebElement snack = driver.findElement(By.xpath("//android.widget.RadioButton[@resource-id=\"io.appium.android.apis:id/snack\"]"));
            snack.click();
            System.out.println("Clicked Snack");

            WebElement result = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"You have selected: (none)\"]"));
            String resultString = result.getText();

            return checkEquals("Snack Button", resultString, "You have selected: 2131296760");
        });

        runStep("RadioGroupTest", "Breakfast Button", "Breakfast button result check", () -> {
            WebElement breakfast = driver.findElement(By.xpath("//android.widget.RadioButton[@content-desc=\"Breakfast\"]"));
            breakfast.click();
            System.out.println("Clicked Breakfast");

            WebElement result = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"You have selected: (none)\"]"));
            String resultString = result.getText();

            return checkEquals("Breakfast Button", resultString, "You have selected: 2131296352");
        });

        runStep("RadioGroupTest", "Lunch Button", "Lunch button result check", () -> {
            WebElement lunch = driver.findElement(By.xpath("//android.widget.RadioButton[@content-desc=\"Lunch\"]"));
            lunch.click();
            System.out.println("Clicked Lunch");

            WebElement result = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"You have selected: (none)\"]"));
            String resultString = result.getText();

            return checkEquals("Lunch Button", resultString, "You have selected: 2131296574");
        });

        runStep("RadioGroupTest", "Dinner Button", "Dinner button result check", () -> {
            WebElement dinner = driver.findElement(By.xpath("//android.widget.RadioButton[@content-desc=\"Dinner\"]"));
            dinner.click();
            System.out.println("Clicked Dinner");

            WebElement result = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"You have selected: (none)\"]"));
            String resultString = result.getText();

            return checkEquals("Dinner Button", resultString, "You have selected: 2131296432");
        });
        
        runStep("RadioGroupTest", "Back To Main Page", "Navigate back", () -> {
            driver.navigate().back();
            driver.navigate().back();
            return true;
        });
    }

    // =========================
    // Activity Test
    // =========================

    public static void activityTest(AndroidDriver driver) {
        System.out.println("ActivityTest Started");

        runStep("ActivityTest", "Click App", "Find or click App", () -> {
            WebElement app = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"App\"]"));
            app.click();
            System.out.println("Clicked App");
            return true;
        });

        runStep("ActivityTest", "Click Activity", "Find or click Activity", () -> {
            WebElement activity = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Activity\"]"));
            activity.click();
            System.out.println("Clicked Activity");
            return true;
        });

        runStep("ActivityTest", "Click Custom Title", "Find or click Custom Title", () -> {
            WebElement customTitle = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Custom Title\"]"));
            customTitle.click();
            System.out.println("Clicked CustomTitle");
            return true;
        });

        runStep("ActivityTest", "Change Left Title", "Left title result check", () -> {
            WebElement result = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Left is best\"]"));
            WebElement title = driver.findElement(By.xpath("//android.widget.EditText[@content-desc=\"Left is best\"]"));

            title.clear();
            title.sendKeys("NANJING");

            WebElement pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Change Left\"]"));
            pencet.click();
            System.out.println("Clicked ChangeLeft");

            String resultString = result.getText();

            return checkEquals("LeftTitle Button", resultString, "NANJING");
        });

        runStep("ActivityTest", "Change Right Title", "Right title result check", () -> {
            WebElement title = driver.findElement(By.xpath("//android.widget.EditText[@content-desc=\"Right is always right\"]"));

            title.clear();
            title.sendKeys("UNIVERSITY");

            WebElement pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Change Right\"]"));
            pencet.click();
            System.out.println("Clicked ChangeRight");

            WebElement result = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Right is always right\"]"));
            String resultString = result.getText();

            return checkEquals("RightTitle Button", resultString, "UNIVERSITY");
        });

        runStep("ActivityTest", "Back From Custom Title", "Navigate back from Custom Title", () -> {
            driver.navigate().back();
            return true;
        });

        runStep("ActivityTest", "Hello World", "Hello World result check", () -> {
            WebElement pencet = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Hello World\"]"));
            pencet.click();
            System.out.println("Clicked HelloWorld");

            WebElement result = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Hello, World!\"]"));
            String resultString = result.getText();

            return checkEquals("HelloWorld Button", resultString, "Hello, World!");
        });

        runStep("ActivityTest", "Back From Hello World", "Navigate back from Hello World", () -> {
            driver.navigate().back();
            return true;
        });

        runStep("ActivityTest", "Click Receive Result", "Find or click Receive Result", () -> {
            WebElement pencet = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Receive Result\"]"));
            pencet.click();
            System.out.println("Clicked ReceiveResult");
            return true;
        });

        runStep("ActivityTest", "Click Get Result For Corky", "Find or click Get Result", () -> {
            WebElement pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Get Result\"]"));
            pencet.click();
            System.out.println("Clicked GetResult");
            TimeUnit.SECONDS.sleep(1);
            return true;
        });

        runStep("ActivityTest", "Corky Button", "Corky result check", () -> {
            WebElement pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Corky\"]"));
            pencet.click();
            System.out.println("Clicked Corky");

            WebElement result = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"io.appium.android.apis:id/results\"]"));
            String resultString = result.getText();

            return checkContains("Corky Button", resultString, "Corky");
        });

        runStep("ActivityTest", "Click Get Result For Violet", "Find or click Get Result again", () -> {
            WebElement pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Get Result\"]"));
            pencet.click();
            System.out.println("Clicked GetResult");
            TimeUnit.SECONDS.sleep(1);
            return true;
        });

        runStep("ActivityTest", "Violet Button", "Violet result check", () -> {
            WebElement pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Violet\"]"));
            pencet.click();
            System.out.println("Clicked Violet");

            WebElement result = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"io.appium.android.apis:id/results\"]"));
            String resultString = result.getText();

            return checkContainsAll("Violet Button", resultString, "Corky", "Violet");
        });

        runStep("ActivityTest", "Back To Activity Page", "Navigate back twice", () -> {
            driver.navigate().back();
            driver.navigate().back();
            return true;
        });

        runStep("ActivityTest", "Click Alert Dialogs", "Find or click Alert Dialogs", () -> {
            WebElement pencet = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Alert Dialogs\"]"));
            pencet.click();
            System.out.println("Clicked AlertDialogs");
            return true;
        });

        runStep("ActivityTest", "Click Long Message", "Find or click long message dialog", () -> {
            WebElement pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"OK Cancel dialog with a long message\"]"));
            pencet.click();
            System.out.println("Clicked LongMessage");
            return true;
        });

        runStep("ActivityTest", "Long Message Check", "Long message text check", () -> {
            WebElement result = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/message\"]"));
            String resultString = result.getText();

            return checkContainsAll("LongMessage Button", resultString, "Plloaso", "Uhex");
        });

        runStep("ActivityTest", "Click OK", "Find or click OK button", () -> {
            WebElement pencet = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"android:id/button1\"]"));
            pencet.click();
            System.out.println("Clicked OK");
            return true;
        });

        System.out.println("ActivityTest Finished");
    }
    
    // =========================
    // MyDemoAppTest
    // =========================
    public static void MyDemoAppTest(AndroidDriver driver) {
        System.out.println("loginTest Started");

        runStep("Backpack", "Click Backpack", "Find or click Backpack", () -> {
            WebElement backpack = driver.findElement(By.xpath("(//android.widget.ImageView[@content-desc=\"Product Image\"])[1]"));
            backpack.click();
            System.out.println("Clicked backpack");
            return true;
        });

        runStep("Rating", "Click Rating", "Find or click Rating", () -> {
            WebElement rating = driver.findElement(By.xpath("//android.widget.ImageView[@resource-id=\"com.saucelabs.mydemoapp.android:id/start5IV\"]"));
            rating.click();
            System.out.println("Clicked rating");
            rating = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Closes review dialog\"]"));
            rating.click();
            System.out.println("Clicked continue");
            return true;
        });
        
        runStep("Add", "Click Add", "Find or click Add", () -> {
            WebElement pencet = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Increase item quantity\"]"));
            pencet.click();
            System.out.println("Clicked add");
            pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Tap to add product to cart\"]"));
            pencet.click();
            System.out.println("Clicked Add to cart");
            return true;
        });
        
        runStep("Cart", "Click Cart", "Find or click Cart", () -> {
            WebElement cart = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Displays number of items in your cart\"]"));
            cart.click();
            System.out.println("Clicked Cart");
            cart = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Confirms products for checkout\"]"));
            cart.click();
            System.out.println("Clicked Proceed to Checkout");
            return true;
        });
        
        runStep("Login", "Click Login", "Find or click Login", () -> {
            TimeUnit.SECONDS.sleep(2);
            WebElement pencet = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/nameET\"]"));
            pencet.sendKeys("225220003");
            System.out.println("Input Username");
            pencet = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/passwordET\"]"));
            pencet.sendKeys("NJUCS");
            System.out.println("Input Password");
            pencet = driver.findElement(By.xpath( "//android.widget.Button[@content-desc=\"Tap to login with given credentials\"]"));
            pencet.click();
            TimeUnit.SECONDS.sleep(2);
            return true;
        });
        
        runStep("Shipping", "Input Shipping Address", "Input the Address", () -> {
        	 WebElement pencet = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/fullNameET\"]"));
             pencet.sendKeys("LIN NENG YU");
             System.out.println("Input Full Name");
             pencet = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/address1ET\"]"));
             pencet.sendKeys("NANJING UNIVERSITY XIANLIN CAMPUS");
             System.out.println("Input Adress Line 1");
             pencet = driver.findElement(By.xpath( "//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/cityET\"]"));
             pencet.sendKeys("NANJING");
             System.out.println("Input City");
             pencet = driver.findElement(By.xpath( "//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/zipET\"]"));
             pencet.sendKeys("123456");
             System.out.println("Input ZipCode");
             pencet = driver.findElement(By.xpath( "//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/countryET\"]"));
             pencet.sendKeys("CHINA");
             System.out.println("Input Country");
             pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Saves user info for checkout\"]"));
             pencet.click();
             return true;
        });
        
        runStep("Payment", "Input Payment", "Input the Payment Method", () -> {
       	 	WebElement pencet = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/nameET\"]"));
            pencet.sendKeys("LIN NENG YU");
            System.out.println("Input Full Name");
            pencet = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/cardNumberET\"]"));
            pencet.sendKeys("1234123412341234");
            System.out.println("Input Card Number");
            pencet = driver.findElement(By.id("com.saucelabs.mydemoapp.android:id/expirationDateET"));
            pencet.sendKeys("0101");
            System.out.println("Input Expiration");
            pencet = driver.findElement(By.xpath( "//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/securityCodeET\"]"));
            pencet.sendKeys("123");
            System.out.println("Input Code");
            pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Saves payment info and launches screen to review checkout data\"]"));
            pencet.click();
            pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Completes the process of checkout\"]"));
            pencet.click();
            pencet= driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Tap to open catalog\"]"));
            pencet.click();
            System.out.println("Checkout Success");
            TimeUnit.SECONDS.sleep(5);
            return true;
        });
        
        runStep("WebView", "Click WebView", "Click or Find WebView", () -> {
       	 	WebElement pencet = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"View menu\"]"));
            pencet.click();
            System.out.println("Click Menu");
            pencet = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.saucelabs.mydemoapp.android:id/itemTV\" and @text=\"WebView\"]"));
            pencet.click();
            System.out.println("Click WebView");
            pencet = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/urlET\"]"));
            pencet.sendKeys("https://cs.nju.edu.cn");
            System.out.println("Input NJUCS Website");
            pencet = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Tap to view content of given url\"]"));
            pencet.click();
            System.out.println("Click Go To NJUCS");
            pencet = driver.findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                            + "new UiSelector().text(\"版权所有©南京大学计算机学院\"));"));
            if(pencet.getText().contains("南京大学计算机学院")) System.out.println("Correct Website"); else System.out.println("Wrong Website");
            return true;
       });
        
    }

    // =========================
    // Main
    // =========================
public static void main(String[] args) {
        long suiteStart = System.currentTimeMillis();
        AndroidDriver driver = null;

        try {
            startLog();

            UiAutomator2Options options = new UiAutomator2Options();
            
            options = setup_Emu(options);
            //options = setup_Real(options);
            
            //options = apidemos(options);
            options = mydemoapp(options);

            URL url = URI.create("http://127.0.0.1:4723/").toURL();

            driver = new AndroidDriver(url, options);
            Thread.sleep(9000);

            System.out.println("Application Started");

            //TestCase
            //loginTest(driver);
            //RadioGroupTest(driver);
            //activityTest(driver);
            //MyDemoAppTest(driver);

        } catch (Exception e) {
            totalCases++;

            if (logWriter != null) {
                writeLog(
                        "Main",
                        "Unexpected Error",
                        "ERROR",
                        System.currentTimeMillis() - suiteStart,
                        "Main Execution",
                        e.getClass().getSimpleName() + ": " + e.getMessage()
                );
            }

            System.out.println("Main Execution: ERROR");
            System.out.println(e.getMessage());

        } finally {
            long totalDuration = System.currentTimeMillis() - suiteStart;

            if (logWriter != null) {
                writeSummary(totalDuration);
                closeLog();
            }

            if (driver != null) {
                driver.quit();
            }
        }
    }
}
