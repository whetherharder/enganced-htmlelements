package ru.peterservice.qa.selenium.pagefactory.interceptors;

import ru.peterservice.qa.selenium.pagefactory.locators.SwitchableLocator;

/**
 * @author Viktor.Fomintsev Фабрика интерцепторов для объектов типа
 *         {@link TypifiedElement}
 * 
 *
 */
public class TypifiedElementInterceptorFactory extends InterceptorFactory {

    public TypifiedElementInterceptorFactory() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public Interceptor createInterceptor(SwitchableLocator locator) {
        // TODO Auto-generated method stub
        return new TypifiedElementInterceptor(locator);
    }

}
