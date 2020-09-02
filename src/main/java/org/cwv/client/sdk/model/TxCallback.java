package org.cwv.client.sdk.model;

public interface TxCallback {

    /**
     * 交易结果通知
     * @param txhash  交易hash
     * @param body  交易查询结果，json String
     */
    void call(String txhash,String body);

    /**
     * 异常通知
     * @param txhash  交易hash
     * @param message  错误提示信息
     * @param body  交易查询结果，json String
     */
    void exceptionCall(String txhash,String message,String body);
    
    
}
