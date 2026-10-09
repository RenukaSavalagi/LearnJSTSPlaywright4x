package com.example.salesforce.tests;

import java.time.Duration;

import com.example.salesforce.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class InvalidLoginTest {
    private static final String LOGIN_URL = "https://login.salesforce.com/?locale=in";

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeTest
    public void setUp() {
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
            throw new IllegalStateException("Unable to start the browser for invalid login", exception);
        }
    }

    @Test
    public void rejectsInvalidCredentialsWithAnErrorMessage() {
        loginPage.doLogin("invalid.user@example.com", "invalid-password");
        Assert.assertFalse(loginPage.getLoginError().isBlank(),
                "Salesforce should display a non-empty error for invalid credentials");
    }

    @AfterTest(alwaysRun = true)
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