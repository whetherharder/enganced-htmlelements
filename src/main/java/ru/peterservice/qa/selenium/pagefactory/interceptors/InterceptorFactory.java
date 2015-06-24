package ru.peterservice.qa.selenium.pagefactory.interceptors;

import ru.peterservice.qa.selenium.pagefactory.locators.SwitchableLocator;

/**
 * @author Viktor.Fomintsev Фабрика для создания интерцептора из переключаемого
 *         локатора
 */
public abstract class InterceptorFactory {

    public abstract Interceptor createInterceptor(SwitchableLocator locator);

}
