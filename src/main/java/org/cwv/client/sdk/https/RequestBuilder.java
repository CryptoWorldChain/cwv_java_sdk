package org.cwv.client.sdk.https;


import org.cwv.client.sdk.Config;
import org.cwv.client.sdk.model.ChainRequest;

public final class RequestBuilder {

    public static ChainRequest buildTestReq(){
        return new ChainRequest(
                Config.host+"/helloworld"
                ,"{\"name\":\"张三\",\"age\":\"18\"}");
    }
    
    public static ChainRequest buildGetStorageReq(String body){
        return new ChainRequest(Config.host+"/fbs/act/pbqcs.do"
        ,body);
    }
    
    public static ChainRequest buildGetUserInfoReq(String addr){
        return new ChainRequest(
                Config.host+"/fbs/act/pbgac.do"
                ,"{\"address\": \""+addr+"\"}");
    }

    public static ChainRequest buildGetBlockByHeightReq(long height){
        return new ChainRequest(
                Config.host+"/fbs/bct/pbgbn.do"
                ,"{\"height\": "+height+",\"type\": 0}");
    }

    public static ChainRequest buildGetLastedBlock(){
        return new ChainRequest(
                Config.host+"/fbs/bct/pbglb.do"
                ,"{}");
    }

    /**
     * 获取节点地址请求 todo
     */
    public static ChainRequest buildGetDynamicDomainsReq(){
        return new ChainRequest("http://localhost:9000"+"/getDomains","");
    }

    public static ChainRequest builtGetTxInfoReq(String hash){
        return new ChainRequest(Config.host+"/fbs/tct/pbgth.do",
                "{\"hash\": \""+hash+"\"}");
    }
    
    

    
    public static ChainRequest buildTransactionReq(String tx){
        return new ChainRequest(
                Config.host+"/fbs/tct/pbmtx.do"
                ,"{\"tx\":\""+tx+"\"}");
    }
    
    
    private static String getDomain(){
        return DomainPool.randomGet();
    }




    public static ChainRequest chainRequest_1(){
        return new ChainRequest("","chainRequest_1");
    }
    public static ChainRequest chainRequest_2(){
        return new ChainRequest("","chainRequest_2");
    }
    public static ChainRequest chainRequest_3(){
        return new ChainRequest("","chainRequest_3");
    }
    public static ChainRequest chainRequest_4(){
        return new ChainRequest("","chainRequest_4");
    }
    public static ChainRequest chainRequest_5(){
        return new ChainRequest("","chainRequest_5");
    }


}
