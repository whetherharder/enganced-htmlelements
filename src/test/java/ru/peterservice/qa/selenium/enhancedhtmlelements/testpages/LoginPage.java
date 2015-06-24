package ru.peterservice.qa.selenium.enhancedhtmlelements.testpages;

import static org.mockito.Mockito.mock;

import org.openqa.selenium.WebDriver;

public class LoginPage {

    public LoginPage() {
        // TODO Auto-generated constructor stub
    }

    WebDriver mockDriver() {
        WebDriver driver = mock(WebDriver.class);
        return driver;
    }

}
