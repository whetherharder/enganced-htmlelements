package ru.peterservice.qa.selenium.pagefactory.locators;

import java.util.List;
import java.util.logging.Logger;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

public class SwitchableElementLocator implements SwitchableLocator {
    private final Switchable switcher;
    private final ElementLocator locator;
    private final static Logger log = Logger.getLogger("locators");


    public SwitchableElementLocator(Switchable switcher, ElementLocator locator) {
        this.switcher = switcher;
        this.locator = locator;
    }

    @Override
    public WebElement findElement() {
        log.fine("try to find element");
        switchTo();
        return locator.findElement();
    }

    @Override
    public List<WebElement> findElements() {
        switchTo();

        return locator.findElements();
    }

    @Override
    public void switchTo() {
        if (this.switcher != null) {
            log.fine("locator is to switch ");
            switcher.switchTo();
        }
        log.fine("locator was not switched. there is no switcher ");
    }

    @Override
    public boolean wasSwitched() {
        if (this.switcher != null) {
            return this.switcher.wasSwitched();
        }
        return false;
    }

    @Override
    public void switchBack() {
        this.switcher.switchBack();
    }

}
