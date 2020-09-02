package org.cwv.client.sdk.model;
 
public class ChainRequest {
    
    private String domain;
    
    private String url;
    
    private String body;
    
    private ChainCallBack callback;
    
    public ChainRequest(String url, String body){
        this.url = url;
        this.body = body;
    }
    public String getUrl(){
        return url;
    }
    public String getBody(){
        return body;
    }

    public ChainRequest setCallback(ChainCallBack callback){
        this.callback = callback;
        return this;
    }

    public void call(String body){
        this.callback.call(body);
    }    
    public void error(String body,Exception e){
        this.callback.error(body,e);
    }
    public boolean isCallBackNull(){
        return callback == null;
    }
    
    

}
