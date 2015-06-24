package ru.peterservice.qa.selenium.pagefactory.interceptors;

import java.lang.reflect.Method;

import ru.peterservice.qa.selenium.pagefactory.locators.SwitchableLocator;
import ru.peterservice.qa.selenium.pagefactory.proxybuilders.ProxyBuilder;
import ru.peterservice.qa.selenium.pagefactory.proxybuilders.WebelementArgProxyBuilder;
import net.sf.cglib.proxy.MethodProxy;

public class TypifiedElementInterceptor extends Interceptor {
    ProxyBuilder proxyBuilder = new WebelementArgProxyBuilder();

    public TypifiedElementInterceptor(SwitchableLocator locator) {
        this.locator = locator;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects,
            MethodProxy methodProxy) throws Throwable {
        if (method.getName().equals("getName") || method.getName().equals("hashCode") || method.getName().equals("toString")) {
            log.fine("method + " + method.getName() + " was intercepted");
            return methodProxy.invokeSuper(o, objects);
        }
        this.getInvocationParameters(o, method, objects, methodProxy);

        Object result = methodProxy.invokeSuper(proxyObject, methodArgs);
        afterInvoke();
        return result;

    }

    @Override
    protected void beforeInvoke() {
        proxyBuilder.setClassToProxy(proxyObject.getClass().getSuperclass());
    }

    @Override
    protected Object getResult() throws Throwable {
        return null;
    }

    @Override
    protected void afterInvoke() {
        if (locator.wasSwitched()) {
            locator.switchBack();
        }
    }




}
