package ru.peterservice.qa.selenium.pagefactory.locators;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementClassAnnotationsHandler;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementFieldAnnotationsHandler;
import ru.yandex.qatools.htmlelements.pagefactory.CustomElementLocatorFactory;

public class RefreshableLocatorFactory extends CustomElementLocatorFactory {
    private SearchContext context;
    private int timeOutInSeconds;
    private final static Logger log = Logger.getLogger("locatorfactory");

    public RefreshableLocatorFactory(SearchContext context, int timeOutInSeconds) {
        this.context = context;
        this.timeOutInSeconds = timeOutInSeconds;
    }



    @Override
    public ElementLocator createLocator(Class clazz) {
        // TODO Auto-generated method stub
        HtmlElementClassAnnotationsHandler handler = new HtmlElementClassAnnotationsHandler(clazz);
        By by = handler.buildBy();
        log.fine("selector " + by + " created");
        return new RefreshableElementLocator(context, this.timeOutInSeconds, handler);
    }

    @Override
    public ElementLocator createLocator(Field field) {
        // TODO Auto-generated method stub
        HtmlElementFieldAnnotationsHandler handler = new HtmlElementFieldAnnotationsHandler(field);
        By by = handler.buildBy();
        log.fine("selector " + by + " created");
        return new RefreshableElementLocator(context, this.timeOutInSeconds, handler);
    }

    public ElementLocator createLocator(By selector) {
        log.fine("locator " + selector + " used");
        return new RefreshableElementLocator(this.context, timeOutInSeconds, selector);
    }

}
