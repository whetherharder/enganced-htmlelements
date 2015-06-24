package ru.peterservice.qa.selenium.pagefactory.interceptors;

import ru.peterservice.qa.selenium.pagefactory.locators.SwitchableLocator;

public class HtmlElementInterceptorFactory extends InterceptorFactory {

    @Override
    public Interceptor createInterceptor(SwitchableLocator locator) {
        return new HtmlElementInterceptor(locator);

    }
}
