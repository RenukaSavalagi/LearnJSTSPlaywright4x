package com.example.salesforce.pages;

import java.time.Duration;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//input[@id='username']")
    private WebElement username;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement password;

    @FindBy(xpath = "//input[@id='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//input[@id='rememberUn']")
    private WebElement rememberMe;

    @FindBy(xpath = "//div[@id='error']")
    private WebElement loginError;

    public LoginPage(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver must not be null");
        }
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        PageFactory.initElements(driver, this);
    }

    public void open(String url) {
        try {
            driver.get(url);
            wait.until(ExpectedConditions.visibilityOf(username));
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to open the Salesforce login page", exception);
        }
    }

    public void setRememberMe(boolean selected) {
        try {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(rememberMe));
            if (checkbox.isSelected() != selected) {
                checkbox.click();
            }
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to update the remember-me setting", exception);
        }
    }

    public void doLogin(String user, String pass) {
        if (user == null || user.isBlank() || pass == null || pass.isBlank()) {
            throw new IllegalArgumentException("Username and password must not be blank");
        }
        try {
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOf(username));
            usernameField.clear();
            usernameField.sendKeys(user);
            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOf(password));
            passwordField.clear();
            passwordField.sendKeys(pass);

            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to submit Salesforce login credentials", exception);
        }
    }

    public String getLoginError() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(loginError)).getText().trim();
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Salesforce login error message was not displayed", exception);
        }
    }

    public boolean isAuthenticationAccepted() {
        try {
            wait.until(driver -> !isDisplayed(password)
                    || !driver.getCurrentUrl().contains("login.salesforce.com")
                    || isDisplayed(loginError));
            return !isDisplayed(loginError)
                    && (!isDisplayed(password) || !driver.getCurrentUrl().contains("login.salesforce.com"));
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to verify the Salesforce authentication result", exception);
        }
    }

    private boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        }
    }
}