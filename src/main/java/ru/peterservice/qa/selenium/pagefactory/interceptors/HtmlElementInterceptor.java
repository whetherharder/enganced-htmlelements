package ru.peterservice.qa.selenium.pagefactory.interceptors;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

import org.openqa.selenium.WebElement;

import ru.peterservice.qa.selenium.pagefactory.locators.SwitchableLocator;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

public class HtmlElementInterceptor extends Interceptor {

    public HtmlElementInterceptor(SwitchableLocator locator) {
        this.locator = locator;

    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects,
            MethodProxy methodProxy) throws Throwable {

        if (method.getName().equals("getName") || method.getName().equals("hashCode") || method.getName().equals("toString") || method.getName().equals("finalize")) {
            return methodProxy.invokeSuper(o, objects);
        }
        // TODO Auto-generated method stub
        log.fine("method " + method + " was intercepted");
        if (!method.getName().equals("setWrappedElement")) {
            super.getInvocationParameters(o, method, objects, methodProxy);
            this.beforeInvoke();
            this.result = methodProxy.invokeSuper(o, objects);
            this.afterInvoke();
            return result;
        } else {
            return methodProxy.invokeSuper(o, objects);
        }

    }

    @Override
    protected void beforeInvoke() {
        if (!this.targetMethod.getName().equals("setWrappedElement")) {

            HtmlElement element = (HtmlElement) proxyObject;
            WebElement wrappedElement = locator.findElement();
            element.setWrappedElement(wrappedElement);
        }
    }

    @Override
    protected void afterInvoke() {
        if (!this.targetMethod.getName().equals("getWrappedElement")) {
            if (locator.wasSwitched()) {
                locator.switchBack();
            }
        }
    }



    @Override
    protected Object getResult() throws Throwable {
        if (this.targetMethod.getName().equals("setWrappedElement")) {
            return this.methodProxy.invokeSuper(proxyObject, methodArgs);
        }
        super.result = methodProxy.invokeSuper(proxyObject, methodArgs);
        return this.result;

    }
}
