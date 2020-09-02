package org.cwv.client.sdk.model;

/**
 * 合约调用响应
 */
public interface ContractCallResponse {

    /**
     * 解码合约调用返值，获取结果。
     */
    ContractCallResponse decode(String result);
    
    default ContractCallResponse decode(String txHash,String result) throws Exception {
        return decode(result);
    }
    
}
