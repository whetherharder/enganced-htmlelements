package ru.peterservice.qa.selenium.pagefactory.locators;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.FluentWait;

import ru.peterservice.qa.selenium.pagefactory.conditions.ExpectedCondition;
import ru.peterservice.qa.selenium.pagefactory.conditions.ExpectedConditions;
import ru.peterservice.qa.selenium.pagefactory.conditions.ExpectedElementCondition;
import ru.peterservice.qa.selenium.pagefactory.conditions.ExpectedElementConditions;
import ru.yandex.qatools.htmlelements.pagefactory.AnnotationsHandler;

/**
 * Класс реализует поиск по селектору в течении заданного таймаута
 * {@link RefreshableElementLocator#searchContext} контекст в котором
 * производится поиск {@link RefreshableElementLocator#timeOutInSeconds} таймаут
 *
 *
 *
 *
 */
public class RefreshableElementLocator implements ElementLocator {
    private final FluentWait<SearchContext> wait;
    private final SearchContext searchContext;
    private final int timeOutInSeconds;
    private final By by;
    private ExpectedElementCondition<Boolean> elementIsPresent = ExpectedElementConditions.isPresent();
    private ExpectedElementCondition<Boolean> elementIsVisible = ExpectedElementConditions.isVisible();
    private static Logger log = Logger.getLogger("locator");

    public RefreshableElementLocator(SearchContext searchContext,
            int timeOutInSeconds, AnnotationsHandler annotationsHandler) {
        this.searchContext = searchContext;
        this.timeOutInSeconds = timeOutInSeconds;
        this.by = annotationsHandler.buildBy();
        wait = new FluentWait<SearchContext>(this.searchContext).withTimeout(this.timeOutInSeconds, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class, NoSuchElementException.class);

    }

    public RefreshableElementLocator(SearchContext searchContext,
            int timeOutInSeconds, By by) {
        this.searchContext = searchContext;
        this.timeOutInSeconds = timeOutInSeconds;
        this.by = by;
        this.wait = new FluentWait<SearchContext>(this.searchContext).withTimeout(this.timeOutInSeconds, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class, NoSuchElementException.class);
    }

    @Override
    public WebElement findElement() {
        String message = "locator used= " + this.by;
        try {
            log.fine(this.by + " is being searched");
            ExpectedCondition<WebElement> contextCondition1 = ExpectedConditions.anyElementLocated(by, elementIsPresent);
            WebElement result = wait.until(contextCondition1);
            log.fine(this.by + " was found");
            return result;

        } catch (StaleElementReferenceException e) {
            log.log(Level.SEVERE, message + " Exception: ", e);
            throw e;
        }

        catch (NoSuchElementException e) {
            log.log(Level.SEVERE, message + " Exception: ", e);
            throw e;

        } catch (Exception e) {
            log.log(Level.SEVERE, message + " Exception: ", e);
            throw e;
        }
    }

    @Override
    public List<WebElement> findElements() {
        ExpectedCondition<List<WebElement>> condition1 = ExpectedConditions.eachElementLocated(by, elementIsPresent);
        return wait.until(condition1);
    }

}
