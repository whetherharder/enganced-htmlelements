package ru.peterservice.qa.selenium.pagefactory.locators;

import java.lang.reflect.Field;

import ru.yandex.qatools.htmlelements.pagefactory.CustomElementLocatorFactory;

public class SwitchableLocatorFactory extends CustomElementLocatorFactory {

    private Switchable switcher;
    private CustomElementLocatorFactory contextLocatorFactory;

    public SwitchableLocatorFactory(Switchable switcher,
            CustomElementLocatorFactory locatorFactory) {
        this.switcher = switcher;
        this.contextLocatorFactory = locatorFactory;
    }


    @Override
    public SwitchableElementLocator createLocator(Class clazz) {
        return new SwitchableElementLocator(this.switcher, contextLocatorFactory.createLocator(clazz));
    }


    @Override
    public SwitchableElementLocator createLocator(Field field) {
        return new SwitchableElementLocator(this.switcher, contextLocatorFactory.createLocator(field));
    }
}
