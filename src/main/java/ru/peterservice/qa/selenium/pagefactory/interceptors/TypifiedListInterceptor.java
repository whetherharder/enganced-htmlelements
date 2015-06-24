package ru.peterservice.qa.selenium.pagefactory.interceptors;

import ru.peterservice.qa.selenium.pagefactory.proxybuilders.ProxyBuilder;

public class TypifiedListInterceptor<B extends ProxyBuilder> extends
Interceptor {
    B proxyBuilder;

    public TypifiedListInterceptor(B proxyBuilder) {
        this.proxyBuilder = proxyBuilder;
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Object getResult() {
        // TODO Auto-generated method stub
        return null;
    }



}


