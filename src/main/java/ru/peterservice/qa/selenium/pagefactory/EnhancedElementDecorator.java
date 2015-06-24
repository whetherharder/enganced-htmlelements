package ru.peterservice.qa.selenium.pagefactory;


import static ru.yandex.qatools.htmlelements.utils.HtmlElementUtils.*;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import ru.peterservice.qa.selenium.pagefactory.interceptors.HtmlElementInterceptorFactory;
import ru.peterservice.qa.selenium.pagefactory.interceptors.HtmlElementListInterceptorFactory;
import ru.peterservice.qa.selenium.pagefactory.interceptors.InterceptorFactory;
import ru.peterservice.qa.selenium.pagefactory.interceptors.TypifiedElementInterceptorFactory;
import ru.peterservice.qa.selenium.pagefactory.locators.FrameLocator;
import ru.peterservice.qa.selenium.pagefactory.locators.RefreshableLocatorFactory;
import ru.peterservice.qa.selenium.pagefactory.locators.Switchable;
import ru.peterservice.qa.selenium.pagefactory.proxybuilders.ProxyBuilder;
import ru.peterservice.qa.selenium.pagefactory.proxybuilders.WebelementArgProxyBuilder;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class EnhancedElementDecorator implements FieldDecorator {
    protected final WebDriver driver;
    protected final RefreshableLocatorFactory locatorFactory;
    protected final int timeOutInSeconds;
    protected static Logger log = Logger.getLogger("decorator");










    public enum ElementType {
        WEBELEMENT, WEBELEMENTLIST, TYPIFIEDELEMENT, TYPIFIEDELEMENTLIST, HTMLELEMENT, HTMLTLELEMENTLIST, HTMLELEMENTCONTAINER, UNKNOWN
    }

    public EnhancedElementDecorator(WebDriver driver,
            SearchContext searchContext, int timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
        this.locatorFactory = new RefreshableLocatorFactory(searchContext, timeOutInSeconds);
        this.driver = driver;
    }

    public EnhancedElementDecorator(WebDriver driver, int timeOutInSeconds) {
        this(driver, driver, timeOutInSeconds);
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!isDecoratableField(field)) {
            return null;
        }
        String logMessage = field + " in class " + field.getType();

        log.fine(logMessage + " decoration statrted");




        ElementLocator locator;

        switch (getElementType(field)) {

        case HTMLELEMENTCONTAINER:
            Class<HtmlElement> containerClass = (Class<HtmlElement>) field.getType();
            // Локатор на контекст контейнера
            ElementLocator containerLocator = locatorFactory.createLocator(containerClass);

            Object proxyContainer;
            Switchable switcher = null;

            if(DecoratorHelper.isFrameLocator(containerLocator)){
                FrameLocator frameLocator = new FrameLocator(containerLocator, driver, timeOutInSeconds);
                DecoratorHelper.createProxy(field, frameLocator, new HtmlElementInterceptorFactory(), new ProxyBuilder(), null);
                // Создаем прокси обхект для контейнера
                proxyContainer = DecoratorHelper.createProxy(field, frameLocator, new HtmlElementInterceptorFactory(), new ProxyBuilder(), switcher);
                switcher = frameLocator;
            }
            else{
                proxyContainer = DecoratorHelper.createProxy(field, containerLocator, new HtmlElementInterceptorFactory(), new ProxyBuilder(), switcher);
            }



            //Инициализируем содержимое контейнера
            HtmlElement containerElement = (HtmlElement) proxyContainer;
            SearchContext containerContext = containerElement.getWrappedElement();

            // Вычисляем актуальный тип содержимого контейнера
            ParameterizedTypeImpl paramTypeImpl = (ParameterizedTypeImpl) field.getGenericType();
            Class<HtmlElement> actualClass =(Class<HtmlElement>)paramTypeImpl.getActualTypeArguments()[0];

            // Инициализируем содержимое контейнера специализированным
            // декоратором полей контейнера

            // Создаем декоратор полей контейнера от контекста контейнера
            ContainerFieldDecorator containerDecorator = new ContainerFieldDecorator(driver, containerContext, timeOutInSeconds, actualClass);

            // Предаем декоратору полей контейнера переключатель на фрейм
            // контейнера

            containerDecorator.setSwitcher(switcher);
            PageFactory.initElements(containerDecorator, proxyContainer);
            DecoratorHelper.switchBack(switcher);

            log.fine(logMessage + " decoration finished");
            return proxyContainer;

        case HTMLELEMENT:
            // Создаем локатор из из аннотации класса поля

            Class<HtmlElement> htmlElementClass = (Class<HtmlElement>) field.getType();
            locator = locatorFactory.createLocator(htmlElementClass);

            // Создаем прокси обхект для блока элементов
            Object proxyElement = DecoratorHelper.createProxy(field, locator, new HtmlElementInterceptorFactory(), new ProxyBuilder(), null);

            HtmlElement proxyBlock = (HtmlElement) proxyElement;

            // Получаем контекст блока
            SearchContext blockContext = proxyBlock.getWrappedElement();

            // Если блок является контейнером, то сначала инициализируем
            // содержимое контейнера


            // Рекурсивно иницилизируем полученный прокси-блок, как отдельный
            // PageObject
            PageFactory.initElements(new EnhancedElementDecorator(driver, blockContext, timeOutInSeconds), proxyElement);
            log.fine(logMessage + " decoration finished");
            return proxyElement;

        case HTMLTLELEMENTLIST:
            locator = locatorFactory.createLocator(field);
            Class<HtmlElement> htmlListElementClass = getGenericParameterClass(field);
            InterceptorFactory htmlListInterceptorFactory = new HtmlElementListInterceptorFactory(htmlListElementClass);
            log.fine(logMessage + " decoration finished");
            return DecoratorHelper.createProxyList(field, locator, htmlListInterceptorFactory, null);

        case TYPIFIEDELEMENT:
            locator = locatorFactory.createLocator(field);
            WebelementArgProxyBuilder builder =
                    new WebelementArgProxyBuilder(locator);
            builder.setClassToProxy(field.getType());
            log.fine(logMessage + " decoration finished");
            return DecoratorHelper.createProxy(field, locator, new TypifiedElementInterceptorFactory(), new WebelementArgProxyBuilder(locator), null);



        case TYPIFIEDELEMENTLIST:
            locator = locatorFactory.createLocator(field);
            return null;

        case WEBELEMENT:
            locator = locatorFactory.createLocator(field);
            return null;
        case WEBELEMENTLIST:
            locator = locatorFactory.createLocator(field);
            return null;
        }

        return null;
    }



    protected boolean isDecoratableField(Field field) {
        // TODO Protecting wrapped element from initialization basing on its
        // name is unsafe. Think of a better way.
        if (isWebElement(field) && !field.getName().equals("wrappedElement")) {
            return true;
        }

        return (isWebElementList(field)
                || isHtmlElement(field)
                || isHtmlElementList(field)
                || isTypifiedElement(field) || isTypifiedElementList(field));
    }

    private ElementType getElementType(Field field) {
        if (isTypifiedElement(field)) {
            return ElementType.TYPIFIEDELEMENT;
        }
        if (isTypifiedElementList(field)) {
            return ElementType.TYPIFIEDELEMENTLIST;
        }

        if (isHtmlElement(field.getType()) && field.getGenericType() instanceof ParameterizedTypeImpl) {
            return ElementType.HTMLELEMENTCONTAINER;
        }


        if (isHtmlElement(field.getType())) {
            return ElementType.HTMLELEMENT;
        }

        if (isHtmlElementList(field)) {
            return ElementType.HTMLTLELEMENTLIST;
        }

        if (isWebElement(field.getType())) {
            return ElementType.WEBELEMENT;
        }

        if (isWebElementList(field)) {
            return ElementType.WEBELEMENTLIST;
        }

        return ElementType.UNKNOWN;

    }




}
