package rft.fuc;


import org.cwv.client.sdk.util.RegexUtil;
import lombok.Data;

import java.util.regex.Pattern;
@Data
public class FeeInfo {

    private static Pattern RP_sellMin           = Pattern.compile("(?<=\"key\": \"sellMin\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_sellMax           = Pattern.compile("(?<=\"key\": \"sellMax\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_sellFeeRatio      = Pattern.compile("(?<=\"key\": \"sellFeeRatio\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_buyMin            = Pattern.compile("(?<=\"key\": \"buyMin\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_buyMax            = Pattern.compile("(?<=\"key\": \"buyMax\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_buyFeeRatio       = Pattern.compile("(?<=\"key\": \"buyFeeRatio\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_upperAddr         = Pattern.compile("(?<=\"key\": \"upperAddr\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_myAddr            = Pattern.compile("(?<=\"key\": \"myAddr\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_upperCode         = Pattern.compile("(?<=\"key\": \"upperCode\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_forbidden         = Pattern.compile("(?<=\"key\": \"forbidden\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_subSellMin        = Pattern.compile("(?<=\"key\": \"subSellMin\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_subSellMax        = Pattern.compile("(?<=\"key\": \"subSellMax\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_subSellFeeRatio   = Pattern.compile("(?<=\"key\": \"subSellFeeRatio\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_subBuyMin         = Pattern.compile("(?<=\"key\": \"subBuyMin\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_subBuyMax         = Pattern.compile("(?<=\"key\": \"subBuyMax\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_subBuyFeeRatio    = Pattern.compile("(?<=\"key\": \"subBuyFeeRatio\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_subAddress        = Pattern.compile("(?<=\"key\": \"subAddress\",\"value\": \")[0-9a-f]*");


    private String sellMin;
    private String sellMax;
    private String sellFeeRatio;
    private String buyMin;
    private String buyMax;
    private String buyFeeRatio;
    private String upperAddr;
    private String myAddr;
    private String upperCode;
    private String forbidden;
    private String subSellMin;
    private String subSellMax;
    private String subSellFeeRatio;
    private String subBuyMin;
    private String subBuyMax;
    private String subBuyFeeRatio;
    private String subAddress;



    public static FeeInfo parse(String result){

        String hex_sellMin = RegexUtil.getValue(RP_sellMin,result);



return null;
    }

}
