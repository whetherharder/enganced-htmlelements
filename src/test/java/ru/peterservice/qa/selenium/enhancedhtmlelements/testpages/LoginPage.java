package ru.peterservice.qa.selenium.enhancedhtmlelements.testpages;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.Image;
import ru.yandex.qatools.htmlelements.element.Radio;
import ru.yandex.qatools.htmlelements.element.TextInput;

public class LoginPage {
    private static final String ELEMENT_ID = "element";
    private static final String HTML_ELEMENT_ID = "html-element";
    private static final String TEXT_INPUT_ID = "text-input";
    private static final String BUTTON_ID = "button";
    private static final String RADIO_NAME = "radio";
    private static final String IMAGE_NAME = "image";
    private static final String IFRAME_HTML_ELEMENT_CSS = "iframe[src='frame-src']";

    @FindBy(id = ELEMENT_ID)
    private WebElement element;

    @FindBy(id = HTML_ELEMENT_ID)
    private HtmlElement htmlElement;

    @FindBy(css = IFRAME_HTML_ELEMENT_CSS)
    private HtmlElement frameHtmlelement;

    @FindBy(id = TEXT_INPUT_ID)
    private TextInput textInput;

    @FindBy(id = BUTTON_ID)
    private Button button;

    @FindBy(name = RADIO_NAME)
    private Radio radio;

    @FindBy(name = IMAGE_NAME)
    private Image image;

    @FindBy(id = TEXT_INPUT_ID)
    private List<TextInput> textInputList;

    @FindBy(id = HTML_ELEMENT_ID)
    private List<HtmlElement> htmlElementList;

    @FindBy(id = ELEMENT_ID)
    private List<WebElement> webElementList;



    public LoginPage() {
        // TODO Auto-generated constructor stub
    }

    WebDriver mockDriver() {
        WebDriver driver = mock(WebDriver.class);

        WebElement element = mock(WebElement.class);
        WebElement htmlElement = mock(WebElement.class);
        WebElement textInput = mock(WebElement.class);
        WebElement button = mock(WebElement.class);
        WebElement radioButton = mock(WebElement.class);
        WebElement image = mock(WebElement.class);
        List<WebElement> radioGroup = Arrays.asList(radioButton, radioButton, radioButton);
        List<WebElement> textInputList = Arrays.asList(textInput, textInput, textInput);
        List<WebElement> htmlElementList = Arrays.asList(htmlElement, htmlElement, htmlElement);
        List<WebElement> webElementList = Arrays.asList(element, element, element);

        when(driver.findElement(By.id(ELEMENT_ID))).thenReturn(element);
        when(driver.findElement(By.id(HTML_ELEMENT_ID))).thenReturn(htmlElement);
        when(driver.findElement(By.id(TEXT_INPUT_ID))).thenReturn(textInput);
        when(driver.findElement(By.id(BUTTON_ID))).thenReturn(button);
        when(driver.findElement(By.name(RADIO_NAME))).thenReturn(radioButton);
        when(driver.findElement(By.name(IMAGE_NAME))).thenReturn(image);

        when(driver.findElements(By.name(RADIO_NAME))).thenReturn(radioGroup);
        when(driver.findElements(By.id(TEXT_INPUT_ID))).thenReturn(textInputList);
        when(driver.findElements(By.id(HTML_ELEMENT_ID))).thenReturn(htmlElementList);
        when(driver.findElements(By.id(ELEMENT_ID))).thenReturn(webElementList);
        when(driver.findElement(By.cssSelector(IFRAME_HTML_ELEMENT_CSS))).thenReturn(frameHtmlelement);
        return driver;
    }

}
