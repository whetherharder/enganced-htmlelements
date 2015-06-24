package ru.peterservice.qa.selenium.pagefactory;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import ru.peterservice.qa.selenium.pagefactory.interceptors.HtmlElementInterceptorFactory;
import ru.peterservice.qa.selenium.pagefactory.interceptors.InterceptorFactory;
import ru.peterservice.qa.selenium.pagefactory.locators.Switchable;
import ru.peterservice.qa.selenium.pagefactory.proxybuilders.ProxyBuilder;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

public class ContainerFieldDecorator extends EnhancedElementDecorator {
    private Class<HtmlElement> actualClass = null;
    private Switchable switcher = null;
    private final static Logger log = Logger.getLogger("decorator");

    public ContainerFieldDecorator(WebDriver driver,
            SearchContext searchContext, int timeOutInSeconds,
            Class<HtmlElement> actualClass) {
        super(driver, searchContext, timeOutInSeconds);
        this.actualClass = actualClass;

    }

    public void setSwitcher(Switchable switcher) {
        this.switcher = switcher;
    }


    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (isDecoratableField(field) && field.getType().isAssignableFrom(actualClass)) {
            log.fine("container " + actualClass + " is being decorated");
            log.fine("field " + field + " decoration started");

            ElementLocator locator = this.locatorFactory.createLocator(actualClass);
            InterceptorFactory interceptorFactory = new HtmlElementInterceptorFactory();
            ProxyBuilder proxyBuilder = new ProxyBuilder();


            // Создаем прокси-объект для содержимого контейнера
            Object proxyBlock = DecoratorHelper.createPorxy(actualClass, locator, interceptorFactory, proxyBuilder, this.switcher);
            HtmlElement contentElement = (HtmlElement) proxyBlock;
            SearchContext contentContext = contentElement.getWrappedElement();

            EnhancedElementDecorator decorator = new EnhancedElementDecorator(this.driver, contentContext, this.timeOutInSeconds);
            log.fine("container " + actualClass + " content decoration started ");
            PageFactory.initElements(decorator, proxyBlock);
            log.fine("container " + actualClass + " content decoration finished ");
            DecoratorHelper.switchBack(switcher);
            log.fine("field " + field + " decoration finished");
            return proxyBlock;
        }
        return null;
    }

}
