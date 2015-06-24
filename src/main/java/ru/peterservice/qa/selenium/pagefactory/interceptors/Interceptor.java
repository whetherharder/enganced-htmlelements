package ru.peterservice.qa.selenium.pagefactory.interceptors;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import ru.peterservice.qa.selenium.pagefactory.locators.SwitchableLocator;

public abstract class Interceptor implements MethodInterceptor {
    protected final static Logger log = Logger.getLogger("interceptor");
    protected SwitchableLocator locator;

    protected Object proxyObject = null;
    protected Method targetMethod = null;
    protected Object[] methodArgs = null;
    protected MethodProxy methodProxy = null;
    protected Object result;




    @Override
    public Object intercept(Object o, Method method, Object[] objects,
            MethodProxy methodProxy) throws Throwable {
        getInvocationParameters(o, method, objects, methodProxy);
        beforeInvoke();
        getResult();
        afterInvoke();
        return this.result;

    }


    protected void beforeInvoke() {

    }

    protected void afterInvoke() {

    }

    protected abstract Object getResult() throws Throwable;

    protected void getInvocationParameters(Object o, Method method,
            Object[] objects, MethodProxy methodProxy) {
        this.proxyObject = o;
        this.targetMethod = method;
        this.methodArgs = objects;
        this.methodProxy = methodProxy;
    }



}










