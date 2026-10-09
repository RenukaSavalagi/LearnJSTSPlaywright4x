package com.example.salesforce.tests;

import java.time.Duration;

import com.example.salesforce.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.SkipException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ValidLoginTest {
    private static final String LOGIN_URL = "https://login.salesforce.com/?locale=in";

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeClass
    public void setUp() {
        String username = System.getenv("SF_USERNAME");
        String password = System.getenv("SF_PASSWORD");
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new SkipException("Set SF_USERNAME and SF_PASSWORD to run the valid-login test");
        }

        try {
            ChromeOptions options = new ChromeOptions();
            if (Boolean.getBoolean("headless")) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--window-size=1440,1000", "--disable-notifications");
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            loginPage = new LoginPage(driver);
            loginPage.open(LOGIN_URL);
        } catch (WebDriverException exception) {
            closeDriverAfterSetupFailure();
            throw new IllegalStateException("Unable to start the browser for valid login", exception);
        }
    }

    @Test
    public void acceptsValidCredentialsAndNavigates() {
        loginPage.doLogin(System.getenv("SF_USERNAME"), System.getenv("SF_PASSWORD"));
        Assert.assertTrue(loginPage.isAuthenticationAccepted(),
            "Valid credentials should advance beyond password entry without a login error");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        closeDriverAfterSetupFailure();
    }

    private void closeDriverAfterSetupFailure() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (WebDriverException exception) {
                throw new IllegalStateException("Unable to close the browser", exception);
            } finally {
                driver = null;
            }
        }
    }
}