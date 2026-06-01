package NEW;

import java.net.URI;
import java.net.URL;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.NoSuchElementException;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;


import NEW.pages.ApiDemosMainPage;
import NEW.pages.TextFieldsPage;
import NEW.pages.RadioGroupPage;
import NEW.pages.ActivityPage;
import NEW.pages.ActivitySubPages.CustomTitlePage;
import NEW.pages.ActivitySubPages.HelloWorldPage;
import NEW.pages.ActivitySubPages.ReceiveResultPage;
import NEW.pages.ActivitySubPages.AlertDialogsPage;
import NEW.pages.mydemoapp.MyDemoAppPages.MyDemoMainPage;
import NEW.pages.mydemoapp.MyDemoAppPages.ProductPage;
import NEW.pages.mydemoapp.MyDemoAppPages.CartPage;
import NEW.pages.mydemoapp.MyDemoAppPages.MyDemoLoginPage;
import NEW.pages.mydemoapp.MyDemoAppPages.ShippingPage;
import NEW.pages.mydemoapp.MyDemoAppPages.PaymentPage;
import NEW.pages.mydemoapp.MyDemoAppPages.WebViewPage;


public class allCase {

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

    // ── CSV log
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
        if (!folder.exists()) folder.mkdirs();
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
        text = text.replace("\"", "\"\"").replace("\r", " ").replace("\n", " ");
        return "\"" + text + "\"";
    }

    public static void writeLog(String testName, String stepName, String status,
                                long duration, String errorPoint, String errorContent) {
        double successRate = totalCases > 0 ? passedCases * 100.0 / totalCases : 0.0;
        logWriter.println(
                csv(nowTime()) + "," + csv(testName) + "," + csv(stepName) + "," +
                csv(status) + "," + duration + "," + csv(errorPoint) + "," +
                csv(errorContent) + "," + passedCases + "," + totalCases + "," +
                String.format("%.2f", successRate));
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
            writeLog(testName, stepName, "ERROR", duration, errorPoint,
                     e.getClass().getSimpleName() + ": " + e.getMessage());
            System.out.println(stepName + ": ERROR\n" + e.getMessage());
            if (e instanceof NoSuchElementException) {
                stoppedByElementNotFound = true;
                stoppedLine = lineInfo;
                stoppedMessage = "Element not found. Test stopped at " + stoppedLine;
                throw new RuntimeException(stoppedMessage, e);
            }
        }
    }

    public static boolean checkEquals(String stepName, String actual, String expected) {
        if (actual.equals(expected)) { System.out.println(stepName + ": Pass"); return true; }
        System.out.println(stepName + ": Failed");
        return fail("Expected: " + expected + ", actual: " + actual);
    }

    public static boolean checkContains(String stepName, String actual, String keyword) {
        if (actual.contains(keyword)) { System.out.println(stepName + ": Pass"); return true; }
        System.out.println(stepName + ": Failed");
        return fail("Expected contains: " + keyword + ", actual: " + actual);
    }

    public static boolean checkContainsAll(String stepName, String actual, String k1, String k2) {
        if (actual.contains(k1) && actual.contains(k2)) { System.out.println(stepName + ": Pass"); return true; }
        System.out.println(stepName + ": Failed");
        return fail("Expected contains: " + k1 + " and " + k2 + ", actual: " + actual);
    }

    public static void writeSummary(long totalDuration) {
        double rate = totalCases > 0 ? passedCases * 100.0 / totalCases : 0.0;
        logWriter.println();
        logWriter.println(csv(nowTime()) + "," + csv("Summary") + "," + csv("All Test Cases") + "," +
                csv("FINISHED") + "," + totalDuration + "," + csv("") + "," +
                csv("Total Passed: " + passedCases + ", Total Cases: " + totalCases) + "," +
                passedCases + "," + totalCases + "," + String.format("%.2f", rate));
        if (stoppedByElementNotFound) {
            logWriter.println(csv(nowTime()) + "," + csv("Final Error") + "," +
                    csv("Element Not Found") + "," + csv("STOPPED") + "," + totalDuration + "," +
                    csv("NoSuchElementException") + "," + csv(stoppedMessage) + "," +
                    passedCases + "," + totalCases + "," + String.format("%.2f", rate));
        }
        logWriter.flush();
        System.out.println("=================================");
        System.out.println("Total Cases: " + totalCases);
        System.out.println("Passed Cases: " + passedCases);
        System.out.println("Success Rate: " + String.format("%.2f", rate) + "%");
        System.out.println("Test Log Saved: " + currentLogFileName);
        if (stoppedByElementNotFound) System.out.println(stoppedMessage);
        System.out.println("=================================");
    }

    public static void closeLog() {
        if (logWriter != null) logWriter.close();
    }

    // ═══════════════════════════════════════════════════════════════
    //  LoginTest
    // ═══════════════════════════════════════════════════════════════

    public static void loginTest(AndroidDriver driver) {
        System.out.println("loginTest Started");

        // 实例化需要用到的 Page 对象
        ApiDemosMainPage mainPage     = new ApiDemosMainPage(driver);
        TextFieldsPage   textFields   = new TextFieldsPage(driver);

        runStep("LoginTest", "Click Views", "Find or click Views", () -> {
            mainPage.clickViews();          // ← 操作委托给 Page 对象
            return true;
        });

        runStep("LoginTest", "Click TextFields", "Find or click TextFields", () -> {
            textFields.navigateToTextFields();
            return true;
        });

        runStep("LoginTest", "Input TextFields", "Input text fields", () -> {
            // 调用 Page 对象的组合方法
            textFields.fillLoginForm(
                "225220003_username",
                "225220003_password",
                "LoginSimulation",
                "NJUCS",
                "Thesis"
            );
            return checkContains("Login Test", "All text fields input completed", "completed");
        });
    }

    // ═══════════════════════════════════════════════════════════════
    //  RadioGroupTest
    // ═══════════════════════════════════════════════════════════════

    public static void RadioGroupTest(AndroidDriver driver) {
        System.out.println("RadioGroupTest Started");

        ApiDemosMainPage mainPage  = new ApiDemosMainPage(driver);
        RadioGroupPage   radioPage = new RadioGroupPage(driver);

        runStep("RadioGroupTest", "Back To Main Page", "Navigate back", () -> {
            radioPage.navigateBack(2);
            return true;
        });

        runStep("RadioGroupTest", "Click Views", "Find or click Views", () -> {
            mainPage.clickViews();
            return true;
        });

        runStep("RadioGroupTest", "Click Radio Group", "Find or click Radio Group", () -> {
            radioPage.navigateToRadioGroup();
            return true;
        });

        runStep("RadioGroupTest", "Clear Button", "Clear button result check", () -> {
            radioPage.clickClear();
            return checkEquals("Clear Button",
                    radioPage.getSelectionResult(),
                    "You have selected: (none)");
        });

        runStep("RadioGroupTest", "Snack Button", "Snack button result check", () -> {
            radioPage.clickSnack();
            return checkEquals("Snack Button",
                    radioPage.getSelectionResult(),
                    "You have selected: 2131296760");
        });

        runStep("RadioGroupTest", "Breakfast Button", "Breakfast button result check", () -> {
            radioPage.clickBreakfast();
            return checkEquals("Breakfast Button",
                    radioPage.getSelectionResult(),
                    "You have selected: 2131296352");
        });

        runStep("RadioGroupTest", "Lunch Button", "Lunch button result check", () -> {
            radioPage.clickLunch();
            return checkEquals("Lunch Button",
                    radioPage.getSelectionResult(),
                    "You have selected: 2131296574");
        });

        runStep("RadioGroupTest", "Dinner Button", "Dinner button result check", () -> {
            radioPage.clickDinner();
            return checkEquals("Dinner Button",
                    radioPage.getSelectionResult(),
                    "You have selected: 2131296432");
        });

        runStep("RadioGroupTest", "Back To Main Page", "Navigate back", () -> {
            radioPage.navigateBack(2);
            return true;
        });
    }

    // ═══════════════════════════════════════════════════════════════
    //  ActivityTest
    // ═══════════════════════════════════════════════════════════════

    public static void activityTest(AndroidDriver driver) {
        System.out.println("ActivityTest Started");

        ApiDemosMainPage  mainPage    = new ApiDemosMainPage(driver);
        ActivityPage      actPage     = new ActivityPage(driver);
        CustomTitlePage   titlePage   = new CustomTitlePage(driver);
        HelloWorldPage    helloPage   = new HelloWorldPage(driver);
        ReceiveResultPage receivePage = new ReceiveResultPage(driver);
        AlertDialogsPage  alertPage   = new AlertDialogsPage(driver);

        runStep("ActivityTest", "Click App", "Find or click App", () -> {
            mainPage.clickApp();
            return true;
        });

        runStep("ActivityTest", "Click Activity", "Find or click Activity", () -> {
            actPage.clickActivity();
            return true;
        });

        runStep("ActivityTest", "Click Custom Title", "Find or click Custom Title", () -> {
            actPage.clickCustomTitle();
            return true;
        });

        runStep("ActivityTest", "Change Left Title", "Left title result check", () -> {
            titlePage.changeLeftTitle("NANJING");
            return checkEquals("LeftTitle Button", titlePage.getLeftTitle(), "NANJING");
        });

        runStep("ActivityTest", "Change Right Title", "Right title result check", () -> {
            titlePage.changeRightTitle("UNIVERSITY");
            return checkEquals("RightTitle Button", titlePage.getRightTitle(), "UNIVERSITY");
        });

        runStep("ActivityTest", "Back From Custom Title", "Navigate back", () -> {
            titlePage.navigateBack();
            return true;
        });

        runStep("ActivityTest", "Hello World", "Hello World result check", () -> {
            actPage.clickHelloWorld();
            return checkEquals("HelloWorld Button", helloPage.getDisplayedText(), "Hello, World!");
        });

        runStep("ActivityTest", "Back From Hello World", "Navigate back", () -> {
            helloPage.navigateBack();
            return true;
        });

        runStep("ActivityTest", "Click Receive Result", "Find or click Receive Result", () -> {
            actPage.clickReceiveResult();
            return true;
        });

        runStep("ActivityTest", "Click Get Result For Corky", "Find or click Get Result", () -> {
            receivePage.clickGetResult();   // 内部含 sleep(1)
            return true;
        });

        runStep("ActivityTest", "Corky Button", "Corky result check", () -> {
            receivePage.clickCorky();
            return checkContains("Corky Button", receivePage.getResultText(), "Corky");
        });

        runStep("ActivityTest", "Click Get Result For Violet", "Find or click Get Result again", () -> {
            receivePage.clickGetResult();
            return true;
        });

        runStep("ActivityTest", "Violet Button", "Violet result check", () -> {
            receivePage.clickViolet();
            return checkContainsAll("Violet Button", receivePage.getResultText(), "Corky", "Violet");
        });

        runStep("ActivityTest", "Back To Activity Page", "Navigate back twice", () -> {
            actPage.navigateBack(2);
            return true;
        });

        runStep("ActivityTest", "Click Alert Dialogs", "Find or click Alert Dialogs", () -> {
            actPage.clickAlertDialogs();
            return true;
        });

        runStep("ActivityTest", "Click Long Message", "Find or click long message dialog", () -> {
            alertPage.clickLongMessageDialog();
            return true;
        });

        runStep("ActivityTest", "Long Message Check", "Long message text check", () -> {
            return checkContainsAll("LongMessage Button",
                    alertPage.getLongMessageText(), "Plloaso", "Uhex");
        });

        runStep("ActivityTest", "Click OK", "Find or click OK button", () -> {
            alertPage.clickOk();
            return true;
        });

        System.out.println("ActivityTest Finished");
    }

    // ═══════════════════════════════════════════════════════════════
    //  MyDemoAppTest
    // ═══════════════════════════════════════════════════════════════

    public static void MyDemoAppTest(AndroidDriver driver) {
        System.out.println("MyDemoAppTest Started");

        MyDemoMainPage  mainPage    = new MyDemoMainPage(driver);
        ProductPage     productPage = new ProductPage(driver);
        CartPage        cartPage    = new CartPage(driver);
        MyDemoLoginPage loginPage   = new MyDemoLoginPage(driver);
        ShippingPage    shipping    = new ShippingPage(driver);
        PaymentPage     payment     = new PaymentPage(driver);
        WebViewPage     webView     = new WebViewPage(driver);

        runStep("Backpack", "Click Backpack", "Find or click Backpack", () -> {
            mainPage.clickFirstProduct();
            return true;
        });

        runStep("Rating", "Click Rating", "Find or click Rating", () -> {
            productPage.clickFiveStar();
            productPage.closeReviewDialog();
            return true;
        });

        runStep("Add", "Click Add", "Find or click Add", () -> {
            productPage.increaseQuantity();
            productPage.addToCart();
            return true;
        });

        runStep("Cart", "Click Cart", "Find or click Cart", () -> {
            cartPage.openCart();
            cartPage.proceedToCheckout();
            return true;
        });

        runStep("Login", "Click Login", "Find or click Login", () -> {
            loginPage.login("225220003", "NJUCS");
            return true;
        });

        runStep("Shipping", "Input Shipping Address", "Input the Address", () -> {
            shipping.fillShippingInfo(
                "LIN NENG YU",
                "NANJING UNIVERSITY XIANLIN CAMPUS",
                "NANJING",
                "123456",
                "CHINA"
            );
            return true;
        });

        runStep("Payment", "Input Payment", "Input the Payment Method", () -> {
            payment.fillPaymentAndCheckout(
                "LIN NENG YU",
                "1234123412341234",
                "0101",
                "123"
            );
            TimeUnit.SECONDS.sleep(5);
            return true;
        });

        runStep("WebView", "Click WebView", "Click or Find WebView", () -> {
            mainPage.openMenu();
            mainPage.clickWebViewMenu();
            webView.goToUrl("https://cs.nju.edu.cn");
            // 断言留在测试层，Page 只返回文本
            String text = webView.getCopyrightText();
            return checkContains("WebView NJU", text, "南京大学计算机学院");
        });

        System.out.println("MyDemoAppTest Finished");
    }

    // ═══════════════════════════════════════════════════════════════
    //  Main
    // ═══════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        long suiteStart = System.currentTimeMillis();
        AndroidDriver driver = null;

        try {
            startLog();

            UiAutomator2Options options = new UiAutomator2Options();
            options = setup_Emu(options);
            // options = setup_Real(options);

            options = apidemos(options);
            // options = mydemoapp(options);

            URL url = URI.create("http://127.0.0.1:4723/").toURL();
            driver = new AndroidDriver(url, options);
            Thread.sleep(9000);
            System.out.println("Application Started");

            //TestCase
            
            loginTest(driver);
            RadioGroupTest(driver);
            activityTest(driver);
            // MyDemoAppTest(driver);

        } catch (Exception e) {
            totalCases++;
            if (logWriter != null) {
                writeLog("Main", "Unexpected Error", "ERROR",
                         System.currentTimeMillis() - suiteStart,
                         "Main Execution",
                         e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            System.out.println("Main Execution: ERROR\n" + e.getMessage());

        } finally {
            long totalDuration = System.currentTimeMillis() - suiteStart;
            if (logWriter != null) { writeSummary(totalDuration); closeLog(); }
            if (driver != null) driver.quit();
        }
    }
}