package org.cwv.client.sdk.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**转账信息*/
@Data
public class TransferInfo {
    
    private String toAddr;//接受地址
    private String amount;//转账主币数量
    private String token;//转账erc20token名称
    private String tokenAmount;//转账erc20token数量
    private String symbol;//转账erc721 标签
    private List<String> cryptoToken = new ArrayList<>();//转账erc721列表

    public TransferInfo() {
    }
    
    public void addCryptoToken(String token){
        cryptoToken.add(token);
    }
    
    
    
}
