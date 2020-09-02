package org.cwv.client.sdk.model;

import org.cwv.client.sdk.util.AccountUtil;
import org.cwv.client.sdk.util.RegexUtil;
import lombok.Data;

import java.math.BigInteger;
import java.util.regex.Pattern;

@Data
public class BrokerOnlineInfo {
//    private static Pattern RP_onlineAddr =  Pattern.compile("(?<=\"key\": \"onlineAddr\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_active =  Pattern.compile("(?<=\"key\": \"active\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_totalBuy =  Pattern.compile("(?<=\"key\": \"totalBuy\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_totalSell =  Pattern.compile("(?<=\"key\": \"totalSell\",\"value\": \")[0-9a-f]*");
//    private static Pattern RP_originBuy =  Pattern.compile("(?<=\"key\": \"originBuy\",\"value\": \")[0-9a-f]*");
//    private static Pattern RP_originSell =  Pattern.compile("(?<=\"key\": \"originSell\",\"value\": \")[0-9a-f]*");


//    private String onlineAddr;  //经销商地址
    private String active;      //上线块高
    private String totalBuy;    //剩余总购买量
    private String totalSell;   //剩余总出售量
//    private String originBuy;   //上线时填写总购买量
//    private String originSell;  //上线时填写总出售量

    public static BrokerOnlineInfo parse(String result){
        BrokerOnlineInfo ri = new BrokerOnlineInfo();
        
        String hex_active = nullFilter(RegexUtil.getValue(RP_active,result));
        if(hex_active == null){return null;}
        String hex_totalBuy = nullFilter(RegexUtil.getValue(RP_totalBuy,result));
        String hex_totalSell = nullFilter(RegexUtil.getValue(RP_totalSell,result));
//        String hex_originBuy = nullFilter(RegexUtil.getValue(RP_originBuy,result));
//        String hex_originSell = nullFilter(RegexUtil.getValue(RP_originSell,result));

//        ri.onlineAddr = ri.onlineAddr.substring(24);
        ri.active = new BigInteger(hex_active,10).toString(10);
        ri.totalBuy = AccountUtil.div18(new BigInteger(hex_totalBuy,16).toString(10));
        ri.totalSell = AccountUtil.div18(new BigInteger(hex_totalSell,16).toString(10));
//        ri.originBuy = AccountUtil.div18(new BigInteger(hex_originBuy,16).toString(10));
//        ri.originSell = AccountUtil.div18(new BigInteger(hex_originSell,16).toString(10));
        return ri;
    }

    private static String nullFilter(String str){
        if(str == null|| str.trim().length() == 0){
            return "0";
        }else 
            return str;
    }

    public static void main(String[] args) {

        String str = "{\"retCode\": 1,\"items\": [{\"key\": \"online\",\"subItems\": [{\"key\": \"onlineAddr\"}," +
                "{\"key\": \"datas\"},{\"key\": \"datas\"},{\"key\": \"active\",\"value\": \"0\"},{\"key\": " +
                "\"index\",\"value\": \"0\"},{\"key\": \"totalBuy\"},{\"key\": \"totalSell\"},{\"key\": " +
                "\"originBuy\"},{\"key\": \"originSell\"}]}]}";

        System.out.println(BrokerOnlineInfo.parse(str));
    }

}
