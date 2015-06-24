package ru.peterservice.qa.selenium.pagefactory;

import java.lang.reflect.Field;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import ru.peterservice.qa.selenium.pagefactory.interceptors.Interceptor;
import ru.peterservice.qa.selenium.pagefactory.interceptors.InterceptorFactory;
import ru.peterservice.qa.selenium.pagefactory.locators.FrameLocator;
import ru.peterservice.qa.selenium.pagefactory.locators.Switchable;
import ru.peterservice.qa.selenium.pagefactory.locators.SwitchableElementLocator;
import ru.peterservice.qa.selenium.pagefactory.locators.SwitchableLocator;
import ru.peterservice.qa.selenium.pagefactory.proxybuilders.ProxyBuilder;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

public class DecoratorHelper {
    public static int timeOutInSeconds = 30;

    public static Object createProxy(Field field, ElementLocator locator,
            InterceptorFactory interceptorFactory, ProxyBuilder proxyBuilder,
            Switchable switcher) {
        return createPorxy(field.getType(), locator, interceptorFactory, proxyBuilder, switcher);
    }

    public static Object createPorxy(Class<?> classToProxy,
            ElementLocator locator, InterceptorFactory interceptorFactory,
            ProxyBuilder proxyBuilder, Switchable switcher) {

        // Из всех плоских локаторов делаем switchable.
        SwitchableLocator switchableLocator = new SwitchableElementLocator(switcher, locator);
        Interceptor interceptor = interceptorFactory.createInterceptor(switchableLocator);
        proxyBuilder.setClassToProxy(classToProxy);
        proxyBuilder.setInterceptor(interceptor);
        return proxyBuilder.build();
    }

    public static <T extends HtmlElement> Object createProxyList(Field field,
            ElementLocator locator, InterceptorFactory interceptorFactory,
            Switchable switcher) {

        ProxyBuilder proxyBuilder = new ProxyBuilder();
        SwitchableLocator switchableLocator = new SwitchableElementLocator(switcher, locator);
        return createProxy(field, switchableLocator, interceptorFactory, proxyBuilder, switcher);

    }

    public static boolean isFrameLocator(ElementLocator locator) {
        return locator.findElement().getTagName().startsWith("iframe");
    }

    public static Switchable createSwitcher(ElementLocator locator,
            WebDriver driver, int timeOutInSeconds) {
        if (isFrameLocator(locator)) {
            return new FrameLocator(locator, driver, timeOutInSeconds);
        }
        return null;

    }

    public static SwitchableLocator createSwitchableLocator(
            Switchable switcher, ElementLocator locator) {
        return new SwitchableElementLocator(switcher, locator);
    }

    public static void switchBack(Switchable switcher) {
        if (switcher != null) {
            switcher.switchBack();
        }
    }

    public static void initPage(WebDriver driver, int timeOutInSeconds,
            Object page) {

        // driver.switchTo().defaultContent();
        FieldDecorator decorator = new EnhancedElementDecorator(driver, timeOutInSeconds);
        PageFactory.initElements(decorator, page);
        // driver.switchTo().defaultContent();
    }

    public static void initPage(WebDriver driver, SearchContext searchContext,
            int timeOutInSeconds, Object page) {
        FieldDecorator decorator = new EnhancedElementDecorator(driver, searchContext, timeOutInSeconds);
        PageFactory.initElements(decorator, page);
    }

    public static void initPage(WebDriver driver, SearchContext searchContext,
            Object page) {
        FieldDecorator decorator = new EnhancedElementDecorator(driver, searchContext, timeOutInSeconds);
        PageFactory.initElements(decorator, page);
    }

    public static void initPage(WebDriver driver, Object page) {
        initPage(driver, DecoratorHelper.timeOutInSeconds, page);
    }

}
