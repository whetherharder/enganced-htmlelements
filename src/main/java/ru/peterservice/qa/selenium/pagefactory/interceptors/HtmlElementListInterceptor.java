package ru.peterservice.qa.selenium.pagefactory.interceptors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import ru.peterservice.qa.selenium.pagefactory.EnhancedElementDecorator;
import ru.peterservice.qa.selenium.pagefactory.locators.SwitchableLocator;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementFactory;

public class HtmlElementListInterceptor<T extends HtmlElement> extends Interceptor {
    private SwitchableLocator locator;
    private Class<T> elementClass;
    private WebDriver driver;
    private int timeOutInSeconds;

    public HtmlElementListInterceptor(SwitchableLocator locator,
            Class<T> elementClass, WebDriver driver, int timeOutInSeconds) {
        this.locator = locator;
        this.elementClass = elementClass;
        this.driver = driver;
        this.timeOutInSeconds = timeOutInSeconds;

    }

    @Override
    protected void beforeInvoke() {

    }

    @Override
    protected Object getResult() throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects,
            MethodProxy methodProxy) throws Throwable {
        if (method.getName().equals("getName") || method.getName().equals("hashCode") || method.getName().equals("toString") || method.getName().equals("finalize")) {
            return methodProxy.invokeSuper(o, objects);
        }
        List<WebElement> wrappedElements = locator.findElements();

        List<T> proxyList = new ArrayList<T>(wrappedElements.size());
        for (WebElement wrappedElement : wrappedElements) {

            T proxyElement = HtmlElementFactory.createHtmlElementInstance(this.elementClass);
            proxyElement.setWrappedElement(wrappedElement);
            EnhancedElementDecorator decorator = new EnhancedElementDecorator(this.driver, wrappedElement, this.timeOutInSeconds);
            PageFactory.initElements(decorator, proxyElement);
            proxyList.add(proxyElement);
        }
        return methodProxy.invoke(proxyList, objects);
    }

}
