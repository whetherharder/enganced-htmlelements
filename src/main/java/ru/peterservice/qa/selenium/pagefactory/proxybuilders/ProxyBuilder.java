package ru.peterservice.qa.selenium.pagefactory.proxybuilders;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class ProxyBuilder {


    protected Class clazzToProxy;
    private MethodInterceptor interceptor;


    public ProxyBuilder() {
        // TODO Auto-generated constructor stub
    }


    public void setClassToProxy(Class clazz) {
        this.clazzToProxy = clazz;
    }

    public Enhancer getEnhancer() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.clazzToProxy);
        enhancer.setCallback(getInterceptor());
        return enhancer;
    }



    public Object build(Object... args) {

        if (args.length > 0) {
            Class[] argTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            return getEnhancer().create(argTypes, args);
        }
        else {
            return getEnhancer().create();
        }
    }




    /**
     * @return the interceptor
     */
    public MethodInterceptor getInterceptor() {
        return interceptor;
    }

    /**
     * @param interceptor
     *            the interceptor to set
     */
    public void setInterceptor(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

}
