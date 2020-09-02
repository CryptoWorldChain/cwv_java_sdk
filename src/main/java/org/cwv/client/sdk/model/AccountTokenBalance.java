package org.cwv.client.sdk.model;

import org.cwv.client.sdk.util.AccountUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户token余额
 */
@Data
public class AccountTokenBalance {
    public AccountTokenBalance(){
        
    }

    /**
     * 格式化好的余额。除以10^18。
     * tokenName-->balance
     */
    private Map<String,String> tokenBalance = new HashMap<>();
    
    public void put(String tokenName,String balance){
        tokenBalance.put(tokenName,balance);
    }
    
    public String getTokenAmount(String tokenName){
        return tokenBalance.getOrDefault(tokenName,"0");
    }
    
    public void offChainFormat(){
        for (Map.Entry<String,String> entry:tokenBalance.entrySet()) {
            String newBalance = AccountUtil.div18(entry.getValue());
            tokenBalance.put(entry.getKey(),newBalance);
        }
    }
    
}
