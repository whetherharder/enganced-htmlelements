package ru.peterservice.qa.selenium.pagefactory.interceptors;

import org.openqa.selenium.WebDriver;

import ru.peterservice.qa.selenium.pagefactory.locators.SwitchableLocator;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

public class HtmlElementListInterceptorFactory extends InterceptorFactory {
    private WebDriver driver;
    private int timeOutInSeconds;
    private Class<?> listElementClass;

    public <T extends HtmlElement> HtmlElementListInterceptorFactory(
            Class<T> listElementClass) {
        this.listElementClass = listElementClass;
    }

    @Override
    public Interceptor createInterceptor(SwitchableLocator locator) {
        // TODO Auto-generated method stub
        return new HtmlElementListInterceptor<HtmlElement>(locator, (Class<HtmlElement>) this.listElementClass, driver, timeOutInSeconds);
    }

}
