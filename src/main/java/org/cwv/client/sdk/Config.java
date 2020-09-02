package org.cwv.client.sdk;

import org.brewchain.core.crypto.model.KeyPairs;

import java.util.Random;

public final class Config {
    private Config(){}
    
    private static KeyPairs kp = null;
    
//    开发环境
    /**主链地址*/
    public static volatile String host = "http://114.115.205.57:8000";

    //测试1
//    public static volatile String host = "http://47.103.149.80:8001";
//    public static volatile String BlockBrowserHost = "http://47.103.94.48:8808";
    
    //生产
//    public static volatile String host = "http://otc.emars99999.com";
//    public static volatile String BlockBrowserHost = "http://mobile.emars99999.com";

    //生产2
//    public static volatile String host = "http://otc.mars99999.cloud";
//    public static volatile String BlockBrowserHost = "http://mobile.mars99999.cloud";

    //是否是生产环境
    public static boolean isProd = false;
    
    /**交易hash查询间隔，毫秒数*/
    public static int HashTrackerIntervalTime = 300;
    /**交易hash查询重试次数*/
    public static int TxTrackRetryTimes = 10;
    
    /**是否使用多队列交易执行器，默认不启用*/
    public static boolean isMultiQueueExecUsed = false;
    /**交易并行队列数目，isMultiQueueExecUsed=true时才有用*/
    public static int MultiExecQueueNum = 20;
    /**是否开启串行化交易发送，即单个账户并发发起交易，默认不启用*/
    public static boolean isSerialSendTx = false;


    public static String[] MainHosts = {
            "http://localhost:8000",
            "http://localhost:8000",
            "http://localhost:8000",
            "http://localhost:8000",
            "http://localhost:8000"
    };
    
    static  Random  rand= new Random();
    public static String getMainHost(){
        int size = MainHosts.length;
        int n = rand.nextInt(size);
        return MainHosts[n];
    }
    
    protected static void setKeyPairs(KeyPairs kp){
        
        Config.kp = kp;
    }
    
    public static String getAddress(){
        return kp.getAddress();
    }
    public static String getPrivateKey(){
        return kp.getPrikey();
    }
    public static String getPublicKey(){
        return kp.getPubkey();
    }

    public static void changeHost(String newHost){
        Config.host = newHost;
    }
    
}
