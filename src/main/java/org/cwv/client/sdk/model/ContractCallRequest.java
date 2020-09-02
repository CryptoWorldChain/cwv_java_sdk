package org.cwv.client.sdk.model;


/**
 * 合约调用请求
 */
public interface ContractCallRequest {

	
    /**
     * 将调用编码的值解码为参数
     * @param hexBytesString 十六进制数组字符串
     */
    void fromBytes(String hexBytesString) throws Exception;

    /**
     * 将参数列表编码为合约调用code
     * @return
     */
    String toBytes();

    /**
     * 获取合约地址
     */
    String getContractAddress();

    /**
     * 构建对应的响应对象
     * @return
     */
    ContractCallResponse buildResp();
    
    String getExdata();
    
}
