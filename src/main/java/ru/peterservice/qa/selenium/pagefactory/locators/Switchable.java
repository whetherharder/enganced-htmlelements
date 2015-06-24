package ru.peterservice.qa.selenium.pagefactory.locators;

public interface Switchable {
    public void switchTo();

    public boolean wasSwitched();

    public void switchBack();
}
