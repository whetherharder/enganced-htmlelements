package ru.peterservice.qa.selenium.pagefactory.proxybuilders;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import net.sf.cglib.proxy.Enhancer;

public class WebelementArgProxyBuilder extends ProxyBuilder {
    private ElementLocator locator;

    public WebelementArgProxyBuilder() {
        // TODO Auto-generated constructor stub
    }

    public WebelementArgProxyBuilder(ElementLocator locator) {
        this.locator = locator;
    }

    @Override
    public Object build(Object... args) {
        Enhancer e = getEnhancer();
        Object[] constructorArgs = { this.locator.findElement() };
        Class[] argTypes = { WebElement.class };


        return e.create(argTypes, constructorArgs);

    }

}
