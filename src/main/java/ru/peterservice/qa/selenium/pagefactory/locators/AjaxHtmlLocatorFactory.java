package ru.peterservice.qa.selenium.pagefactory.locators;

import java.lang.reflect.Field;

import org.openqa.selenium.SearchContext;

import ru.yandex.qatools.htmlelements.pagefactory.AjaxElementLocator;
import ru.yandex.qatools.htmlelements.pagefactory.CustomElementLocatorFactory;

import org.openqa.selenium.support.pagefactory.ElementLocator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementClassAnnotationsHandler;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementFieldAnnotationsHandler;

public class AjaxHtmlLocatorFactory extends CustomElementLocatorFactory {
    private int timeOutInSeconds = 5;
    private SearchContext searchContext;

    public AjaxHtmlLocatorFactory(SearchContext searchContext, int timeOutinSeconds) {
        this.searchContext = searchContext;
        this.timeOutInSeconds = timeOutinSeconds;
    }

    public AjaxHtmlLocatorFactory(SearchContext searchContext) {
        this.searchContext = searchContext;
    }


    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ElementLocator createLocator(Class clazz) {
        return new AjaxElementLocator(searchContext, timeOutInSeconds, new HtmlElementClassAnnotationsHandler(clazz));
    }


    @Override
    public ElementLocator createLocator(Field field) {
        return new AjaxElementLocator(searchContext, timeOutInSeconds, new HtmlElementFieldAnnotationsHandler(field));
    }

}
