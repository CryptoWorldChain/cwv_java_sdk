package org.cwv.client.sdk.model;

public interface ChainCallBack<T>{

    void call(T body);
    
    void error(String body,Exception e);

}