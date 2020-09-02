package org.cwv.client.sdk.model;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import org.cwv.client.sdk.model.Match.OpInfo;
import org.cwv.client.sdk.util.AccountUtil;
import org.cwv.client.sdk.util.CryptoUtil;
import org.cwv.client.sdk.util.RegexUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.regex.Pattern;
@Data
@Slf4j
public class OrderInfo {

    private static Pattern RP_sellerAddr          = Pattern.compile("(?<=\"key\": \"sellerAddr\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_buyerAddr           = Pattern.compile("(?<=\"key\": \"buyerAddr\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_direct              = Pattern.compile("(?<=\"key\": \"direct\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_orderid             = Pattern.compile("(?<=\"key\": \"orderid\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_orderAmount         = Pattern.compile("(?<=\"key\": \"orderAmount\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_dealAmount          = Pattern.compile("(?<=\"key\": \"dealAmount\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_feeAmount           = Pattern.compile("(?<=\"key\": \"feeAmount\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_feeRadio            = Pattern.compile("(?<=\"key\": \"feeRadio\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_exchangeRadio       = Pattern.compile("(?<=\"key\": \"exchangeRadio\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_matchStatus         = Pattern.compile("(?<=\"key\": \"matchStatus\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_matchblockNumber    = Pattern.compile("(?<=\"key\": \"matchblockNumber\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_matchTimestamp      = Pattern.compile("(?<=\"key\": \"matchTimestamp\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_matchDatas          = Pattern.compile("(?<=\"key\": \"matchDatas\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_payStatus           = Pattern.compile("(?<=\"key\": \"payStatus\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_payblockNumber      = Pattern.compile("(?<=\"key\": \"payblockNumber\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_payTimestamp        = Pattern.compile("(?<=\"key\": \"payTimestamp\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_payDatas            = Pattern.compile("(?<=\"key\": \"payDatas\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_completeStatus      = Pattern.compile("(?<=\"key\": \"completeStatus\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_completeblockNumber = Pattern.compile("(?<=\"key\": \"completeblockNumber\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_completeTimestamp   = Pattern.compile("(?<=\"key\": \"completeTimestamp\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_completeDatas       = Pattern.compile("(?<=\"key\": \"completeDatas\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_cancelStatus        = Pattern.compile("(?<=\"key\": \"cancelStatus\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_cancelblockNumber   = Pattern.compile("(?<=\"key\": \"cancelblockNumber\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_cancelTimestamp     = Pattern.compile("(?<=\"key\": \"cancelTimestamp\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_cancelDatas         = Pattern.compile("(?<=\"key\": \"cancelDatas\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_lockId              = Pattern.compile("(?<=\"key\": \"lockId\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_status              = Pattern.compile("(?<=\"key\": \"status\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_payType             = Pattern.compile("(?<=\"key\": \"payType\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_buyerPayDatas       = Pattern.compile("(?<=\"key\": \"buyerPayDatas\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_sellerPayDatas      = Pattern.compile("(?<=\"key\": \"sellerPayDatas\",\"value\": \")[0-9a-f]*");
    private static Pattern RP_orderType           = Pattern.compile("(?<=\"key\": \"orderType\",\"value\": \")[0-9a-f]*");
    
    
    private String sellerAddr;//卖方钱包地址
    private String buyerAddr;//买方钱包地址
    private String direct;//方向，01：xxx_token；02；token_xxx
    private String orderid;//商户订单id
    private String orderAmount;//订单金额
    private String dealAmount;//实际成交金额
    private String feeAmount;//手续费
    private String feeRadio;//手续费费率
    private String exchangeRadio;//买卖币种汇率，如1000 SC兑换200USDT，此值则为200
    private int matchStatus;//撮合状态
    private String matchblockNumber;//撮合块num
    private String matchTimestamp;//撮合时间戳
    private OpInfo matchDatas;//撮合操作信息。
    private int payStatus;//支付状态
    private String payblockNumber;//支付块num
    private String payTimestamp;//支付时间戳
    private OpInfo payDatas;//支付操作信息
    private int completeStatus;//完成状态
    private String completeblockNumber;//完成块num
    private String completeTimestamp;//完成时间戳
    private OpInfo completeDatas;//完成操作信息
    private int cancelStatus;//取消状态
    private String cancelblockNumber;//取消块num
    private String cancelTimestamp;//取消时间戳
    private OpInfo cancelDatas;//取消操作信息
    private String lockId;//撮合id
    private int status;//订单状态；1 - 已撮合；2 - 已支付；3 - 已放行；4 - 已取消
    private int payType;//支付类型；0微信，1支付宝，2银行卡，3 USDT,4 CUSDT
    private Object buyerPayDatas;//买方支付信息
    private Object sellerPayDatas;//卖方支付信息
    private int orderType;//订单类型

    private String buyerPayDatasJson;// 支付数据json
    private String sellerPayDatasJson;//支付数据json


    public static OrderInfo parse(String result){
        String hex_sellerAddr = RegexUtil.getValue(RP_sellerAddr,result);
        if(hex_sellerAddr == null || hex_sellerAddr.length()==0){
            return null;
        }
        OrderInfo oi = new OrderInfo();
        oi.sellerAddr = hex_sellerAddr;
        oi.buyerAddr = RegexUtil.getValue(RP_buyerAddr,result);
        oi.sellerAddr = oi.sellerAddr.substring(24);
        oi.buyerAddr = oi.buyerAddr.substring(24);
        
        oi.direct = RegexUtil.getValue(RP_direct,result);
        try {
            oi.orderid = RegexUtil.getValue(RP_orderid,result);
            oi.orderid = new String(CryptoUtil.hexStrToBytes(oi.orderid),"UTF-8");
        } catch (Exception e) {
            log.error("orderid 转换异常",e);
        }
        String hex_orderAmount = RegexUtil.getValue(RP_orderAmount,result);
        oi.orderAmount = AccountUtil.div18(new BigInteger(hex_orderAmount,16).toString());
        String hex_dealAmount = RegexUtil.getValue(RP_dealAmount,result);
        oi.dealAmount = AccountUtil.div18(new BigInteger(hex_dealAmount,16).toString());
        String hex_feeAmount = RegexUtil.getValue(RP_feeAmount,result);
        oi.feeAmount = AccountUtil.div18(new BigInteger(hex_feeAmount,16).toString());
        oi.feeRadio = RegexUtil.getValue(RP_feeRadio,result);
        oi.exchangeRadio = RegexUtil.getValue(RP_exchangeRadio,result);
        oi.matchStatus = new BigInteger(RegexUtil.getValue(RP_matchStatus,result),16).intValue();
        oi.matchblockNumber = RegexUtil.getValue(RP_matchblockNumber,result);
        oi.matchTimestamp = RegexUtil.getValue(RP_matchTimestamp,result);
        String hex_matchDatas = RegexUtil.getValue(RP_matchDatas,result);
        try {
            oi.matchDatas = OpInfo.parseFrom(CryptoUtil.hexStrToBytes(
                    hex_matchDatas ==null?"":hex_matchDatas));
        } catch (Exception e) {
            log.error("matchDatas 转换错误,输入:"+hex_matchDatas,e);
        }
        oi.payStatus = new BigInteger(RegexUtil.getValue(RP_payStatus,result),16).intValue();
        oi.payblockNumber = RegexUtil.getValue(RP_payblockNumber,result);
        oi.payTimestamp = RegexUtil.getValue(RP_payTimestamp,result);
        String hex_payDatas = RegexUtil.getValue(RP_payDatas,result);
        try {
            oi.payDatas = OpInfo.parseFrom(CryptoUtil.hexStrToBytes(
                    hex_payDatas ==null?"":hex_payDatas));
        } catch (Exception e) {
            log.error("payDatas 转换错误,输入:"+hex_payDatas,e);
        }
        oi.completeStatus = new BigInteger(RegexUtil.getValue(RP_completeStatus,result),16).intValue();
        oi.completeblockNumber = RegexUtil.getValue(RP_completeblockNumber,result);
        oi.completeTimestamp = RegexUtil.getValue(RP_completeTimestamp,result);
        String hex_completeDatas = RegexUtil.getValue(RP_completeDatas,result);
        try {
            oi.completeDatas = OpInfo.parseFrom(CryptoUtil.hexStrToBytes(
                    hex_completeDatas ==null?"":hex_completeDatas));
        } catch (Exception e) {
            log.error("completeDatas 转换错误,输入:"+hex_completeDatas,e);
        }
        oi.cancelStatus = new BigInteger(RegexUtil.getValue(RP_cancelStatus,result),16).intValue();
        oi.cancelblockNumber = RegexUtil.getValue(RP_cancelblockNumber,result);
        oi.cancelTimestamp = RegexUtil.getValue(RP_cancelTimestamp,result);
        String hex_cancelDatas = RegexUtil.getValue(RP_cancelDatas,result);
        try {
            oi.cancelDatas = OpInfo.parseFrom(CryptoUtil.hexStrToBytes(
                    hex_cancelDatas ==null?"":hex_cancelDatas));
        } catch (Exception e) {
            log.error("cancelDatas 转换错误,输入:"+hex_cancelDatas,e);
        }
        oi.lockId = RegexUtil.getValue(RP_lockId,result);
        oi.status = new BigInteger(RegexUtil.getValue(RP_status,result)).intValue();
        oi.payType = new BigInteger(RegexUtil.getValue(RP_payType,result)).intValue();
        
        String hex_buyerPayDatas = RegexUtil.getValue(RP_buyerPayDatas,result);
        oi.buyerPayDatas = getPayInfo(oi.payType,hex_buyerPayDatas);
        oi.buyerPayDatasJson = new Gson().toJson(oi.buyerPayDatas);
        
        String hex_sellerPayDatas = RegexUtil.getValue(RP_sellerPayDatas,result);
        oi.sellerPayDatas = getPayInfo(oi.payType,hex_sellerPayDatas);
        oi.sellerPayDatasJson = new Gson().toJson(oi.sellerPayDatas);

        String _orderType = RegexUtil.getValue(RP_orderType,result);
        oi.orderType = new BigInteger(_orderType==null?"0":_orderType).intValue();

        return oi;
    }

    private static Object getPayInfo(int payType,String data){
        if(data == null||data.length() == 0){
            return null;
        }
        try {
            Object b = new Object();
            switch (payType){
                case 0:
                    b =  User.WechatPayInfo.parseFrom(CryptoUtil.hexStrToBytes(data));
                    break;
                case 1:
                    b =  User.AliPayInfo.parseFrom(CryptoUtil.hexStrToBytes(data));
                    break;
                case 2:
                    b =  User.BankInfo.parseFrom(CryptoUtil.hexStrToBytes(data));
                    break;
                case 3:
                    b =  User.USDTPayInfo.parseFrom(CryptoUtil.hexStrToBytes(data));
                    break;
            }
            if(payType == 3){

                String addr = ((User.USDTPayInfo) b).getAddr();
                if(addr == null || "".equalsIgnoreCase(addr)){
                    b = User.USDTPayRecord.parseFrom(CryptoUtil.hexStrToBytes(data));
                }
            }
            return b;
        } catch (Exception e) {
            log.error("支付信息反序列化错误",e);
        }
        return null;
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        String result = "{\"retCode\": 1,\"items\": [{\"key\": \"lockid\",\"subItems\": [{\"key\": \"sellerAddr\"," +
                "\"value\": \"00000000000000000000000000c88e26b85223ee81436d91054778d7a9bc6d8e\"},{\"key\": " +
                "\"buyerAddr\",\"value\": \"0000000000000000000000004baf940a7a24962bf6288f914fa6b0954a62f9a8\"}," +
                "{\"key\": \"direct\",\"value\": \"01\"},{\"key\": \"orderid\",\"value\": " +
                "\"37316234623337372d656165622d346336342d386665372d306531306336613535316638\"},{\"key\": " +
                "\"orderAmount\",\"value\": \"056bc75e2d63100000\"},{\"key\": \"dealAmount\",\"value\": " +
                "\"055005f0c614480000\"},{\"key\": \"feeAmount\",\"value\": \"1bc16d674ec80000\"},{\"key\": " +
                "\"feeRadio\",\"value\": \"20\"},{\"key\": \"exchangeRadio\",\"value\": \"1000\"},{\"key\": " +
                "\"matchStatus\",\"value\": \"01\"},{\"key\": \"matchblockNumber\",\"value\": \"110041\"},{\"key\": " +
                "\"matchTimestamp\",\"value\": \"1568899525918\"},{\"key\": \"matchDatas\"},{\"key\": \"payStatus\"," +
                "\"value\": \"02\"},{\"key\": \"payblockNumber\",\"value\": \"110096\"},{\"key\": \"payTimestamp\"," +
                "\"value\": \"1568899582208\"},{\"key\": \"payDatas\"},{\"key\": \"completeStatus\",\"value\": " +
                "\"00\"},{\"key\": \"completeblockNumber\",\"value\": \"0\"},{\"key\": \"completeTimestamp\"," +
                "\"value\": \"0\"},{\"key\": \"completeDatas\"},{\"key\": \"cancelStatus\",\"value\": \"00\"}," +
                "{\"key\": \"cancelblockNumber\",\"value\": \"0\"},{\"key\": \"cancelTimestamp\",\"value\": \"0\"}," +
                "{\"key\": \"cancelDatas\"},{\"key\": \"lockId\",\"value\": " +
                "\"ac05fec15805eb6a1e632a07cea6475974fc0b12bddc170a2f3baea99ba2c81b\"},{\"key\": \"status\"," +
                "\"value\": \"2\"},{\"key\": \"payType\",\"value\": \"1\"},{\"key\": \"buyerPayDatas\"},{\"key\": " +
                "\"sellerPayDatas\",\"value\": " +
                "\"10011a0ce782b9e8a7a3e782b9e8a7a3220fe5a49ae4b985e883bde588b0e591a23001\"},{\"key\": \"orderType\"," +
                "\"value\": \"01\"}]}]}";

        OrderInfo op = parse(result);
        System.out.println(op);
        
    }

}
