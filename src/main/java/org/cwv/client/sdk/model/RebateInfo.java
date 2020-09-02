package org.cwv.client.sdk.model;

import org.cwv.client.sdk.util.AccountUtil;
import org.cwv.client.sdk.util.RegexUtil;
import lombok.Data;

import java.math.BigInteger;
import java.util.regex.Pattern;

@Data
public class RebateInfo {

    private static Pattern RP_matchId =  Pattern.compile("(?<=\"key\": \"matchId\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_dividedRatio =  Pattern.compile("(?<=\"key\": \"dividedRatioThousandcent\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_totalAmount =  Pattern.compile("(?<=\"key\": \"totalAmount\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_backAmount =  Pattern.compile("(?<=\"key\": \"backAmount\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_feeAmount =  Pattern.compile("(?<=\"key\": \"feeAmount\",\"value\": \")[0-9a-f]*");


    private String matchId;  //撮合id
    private String dividedRatioThousandcent;  //分润比例，以千分之几计
    private String totalAmount;  //分润总额度
    private String backAmount;  //分润返利，返给经销商的金额
    private String feeAmount;  //分润返利，返给平台的金额
    
    
    public static RebateInfo parse(String result){
        RebateInfo ri = new RebateInfo();
        ri.matchId = RegexUtil.getValue(RP_matchId,result);
        if(ri.matchId == null){return null;}
        String hex_dividedRatioThousandcent = RegexUtil.getValue(RP_dividedRatio,result);
        String hex_totalAmount = RegexUtil.getValue(RP_totalAmount,result);
        String hex_backAmount = RegexUtil.getValue(RP_backAmount,result);
        String hex_feeAmount = RegexUtil.getValue(RP_feeAmount,result);

        ri.dividedRatioThousandcent = new BigInteger(hex_dividedRatioThousandcent,16).toString(10);
        ri.totalAmount = AccountUtil.div18(new BigInteger(hex_totalAmount,16).toString(10));
        ri.backAmount = AccountUtil.div18(new BigInteger(hex_backAmount,16).toString(10));
        ri.feeAmount = AccountUtil.div18(new BigInteger(hex_feeAmount,16).toString(10));
        return ri;
    }
    
    
}
