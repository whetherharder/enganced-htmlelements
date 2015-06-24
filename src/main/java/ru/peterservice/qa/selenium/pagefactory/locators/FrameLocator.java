package ru.peterservice.qa.selenium.pagefactory.locators;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FrameLocator implements SwitchableLocator {
    private WebDriver driver;
    private final ElementLocator contextLocator;
    private boolean wasSwitched = false;
    private int timeOutInSeconds;
    private final static Logger log = Logger.getLogger("locator");
    private WebDriverWait wait;


    public FrameLocator(ElementLocator contextLocator,
            WebDriver driver,int timeOutInSeconds) {
        this.driver = driver;
        this.contextLocator = contextLocator;
        this.timeOutInSeconds = timeOutInSeconds;
        wait = new WebDriverWait(driver, this.timeOutInSeconds);
        wait.ignoring(StaleElementReferenceException.class, NoSuchElementException.class);

    }

    @Override
    public WebElement findElement() {
        log.fine("frame locator is to switch frame");
        switchTo();

        By bodyBy = new By.ByTagName("body");
        RefreshableElementLocator bodyLocator = new RefreshableElementLocator(driver, this.timeOutInSeconds, bodyBy);
        log.fine("frame locator returns new frame body ");

        WebElement result = bodyLocator.findElement();
        log.fine("frame");
        log.fine(this.driver.getPageSource());
        return result;
    }

    @Override
    public List<WebElement> findElements() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void switchTo() {

        log.fine("frame locator is to switch");
        if (!this.wasSwitched()) {
            log.fine("frame locator switches frame");

            // Перед переключением на новый фрейм запоминаем старый


            try {

                wait.until(new FrameIsAvailiable());

                log.finer("curent frame");
                log.finer(this.driver.getPageSource());
            }catch(StaleElementReferenceException e){
                log.severe("frame context " + this.contextLocator + "was not fond");
                log.severe("driver source " + driver.getPageSource());
            }

        }

    }

    protected class FrameIsAvailiable implements ExpectedCondition<WebDriver>{
        FluentWait<WebDriver> waitContent;
        FluentWait<WebDriver> waitCurrentFrame;


        public FrameIsAvailiable() {
            this.waitCurrentFrame = new FluentWait<WebDriver>(driver).pollingEvery(2, TimeUnit.MILLISECONDS).withTimeout(timeOutInSeconds, TimeUnit.SECONDS);
            this.waitContent = new FluentWait<WebDriver>(driver).withTimeout(timeOutInSeconds, TimeUnit.SECONDS).ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
        }

        public boolean isOnTopWindow() {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            boolean result = (boolean) executor.executeScript("return window.self === window.top");
            return result;
        }

        public WebElement getCurrentFrame() {
            if (isOnTopWindow()) {
                return null;
            }
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            WebElement curentFrame = (WebElement) executor.executeScript("return window.frameElement");
            return curentFrame;
        }

        public boolean framesHaveSameId(WebElement currentFrame,
                WebElement newFrame) {
            if (currentFrame != null) {
                String currentFrameId = currentFrame.getAttribute("id");
                String newFrameId = newFrame.getAttribute("id");
                return currentFrameId.equals(newFrameId);
            }
            return false;
        }

        @Override
        public WebDriver apply(WebDriver arg0) {

            WebElement context = FrameLocator.this.contextLocator.findElement();
            WebElement currentFrame = getCurrentFrame();

            if (currentFrame != null) {
                waitCurrentFrame.until(ExpectedConditions.stalenessOf(currentFrame));
            }
            String frameId = context.getAttribute("id");
            // Переключаемся на новый контекст
            WebDriver switchedDriver = driver.switchTo().frame(frameId);

            // Ждем пока в новом фрейме появятся элементы

            waitContent.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("body")));


            FrameLocator.this.wasSwitched = true;
            return switchedDriver;
        }

    }




    @Override
    public boolean wasSwitched() {
        return this.wasSwitched;
    }

    @SuppressWarnings("unused")
    @Override
    public void switchBack() {
        if (wasSwitched) {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            String currentFrame = (String) executor.executeScript("return window.frameElement.id");
            this.driver.switchTo().parentFrame();
            WebElement parentFrame = (WebElement) executor.executeScript("return window.frameElement");
            if (parentFrame != null) {
                FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).pollingEvery(2, TimeUnit.MILLISECONDS).withTimeout(timeOutInSeconds, TimeUnit.SECONDS);
                wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.id(currentFrame))));
            }
            this.wasSwitched = false;
        }

    };

}





