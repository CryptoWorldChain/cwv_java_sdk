package org.cwv.client.sdk.model;

import lombok.Data;

@Data
/**
 * 经销商上线时提交的收款信息
 */
public class OnlinePayInfoData {
    
    private byte payType;//支付方式
    private int payId;//支付id
    private String amount;//支付金额
    
}
