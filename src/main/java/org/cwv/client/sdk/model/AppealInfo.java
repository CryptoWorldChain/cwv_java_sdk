package org.cwv.client.sdk.model;

import org.cwv.client.sdk.model.Match.AppealRecord;
import org.cwv.client.sdk.util.CryptoUtil;
import org.cwv.client.sdk.util.RegexUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Data
@Slf4j
public class AppealInfo {
    private static Pattern p =  Pattern.compile("\"subItems\": \\[\\{[\\S\\s]*?\\}\\]");

    private static Pattern RP_status      = Pattern.compile("(?<=\"key\": \"status\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_content     = Pattern.compile("(?<=\"key\": \"content\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_payId       = Pattern.compile("(?<=\"key\": \"payId\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_blockNumber = Pattern.compile("(?<=\"key\": \"blockNumber\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_timestamp   = Pattern.compile("(?<=\"key\": \"timestamp\",\"value\": \")[0-9a-f]*");
    
    private int status;//申诉状态
    private AppealRecord content;//申诉内容
    private String matchId;//撮合id
    private long blockNumber;//块高
    private String timestamp;//申诉时间戳
    
    
    public static List<AppealInfo> parseList(String result){
        List<AppealInfo> list = new ArrayList<>();
        List<String> res = RegexUtil.getMatchList(p,result);
        for (String sub: res){
            AppealInfo ai = parse(sub);
            if(ai !=null) list.add(ai);
        }
        return list;
    }
    
    public static AppealInfo parse(String subResult){
        String hex_status = RegexUtil.getValue(RP_status,subResult);
        if(hex_status == null || hex_status.length() == 0) return null;
        AppealInfo ai = new AppealInfo();
        ai.status = new BigInteger(hex_status).intValue();
        String hex_content = RegexUtil.getValue(RP_content,subResult);
        try {
            ai.content = AppealRecord.parseFrom(CryptoUtil.hexStrToBytes(hex_content));
        } catch (Exception e) {
            log.error("AppealInfo content 转化错误，hex_content："+hex_content,e);
        }
        ai.matchId = RegexUtil.getValue(RP_payId,subResult);
        ai.blockNumber = Long.parseLong(RegexUtil.getValue(RP_blockNumber,subResult));
        ai.timestamp = RegexUtil.getValue(RP_timestamp,subResult);
        return ai;
    }

    public static void main(String[] args) {
        String str = "{\"retCode\": 1,\"items\": [{\"key\": \"appeals\",\"subItems\": [{\"key\": \"item\"," +
                "\"subItems\": [{\"key\": \"status\",\"value\": \"1\"},{\"key\": \"content\",\"value\": " +
                "\"080112160a06616161616161120ce794b3e8af89e794b3e8af89\"},{\"key\": \"payId\",\"value\": " +
                "\"8de9c9a051cfed17b6a3c90aa8bca1d044f41571887a213095bf7a59fb534594\"},{\"key\": \"blockNumber\"," +
                "\"value\": \"72920\"},{\"key\": \"timestamp\",\"value\": \"1568615546035\"}]},{\"key\": \"item\"," +
                "\"subItems\": [{\"key\": \"status\",\"value\": \"1\"},{\"key\": \"content\",\"value\": " +
                "\"080112160a06616161616161120ce794b3e8bea9e794b3e8bea9\"},{\"key\": \"payId\",\"value\": " +
                "\"8de9c9a051cfed17b6a3c90aa8bca1d044f41571887a213095bf7a59fb534594\"},{\"key\": \"blockNumber\"," +
                "\"value\": \"72940\"},{\"key\": \"timestamp\",\"value\": \"1568615566292\"}]}]}]}";

        List l = AppealInfo.parseList(str);
        System.out.println(l);
        System.out.println(l.size());
        
    }

}
