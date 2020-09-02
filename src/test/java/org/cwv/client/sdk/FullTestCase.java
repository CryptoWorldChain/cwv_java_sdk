package org.cwv.client.sdk;

import java.math.BigInteger;
import java.security.SecureRandom;

public class FullTestCase {
    //本地
//    static String admin_address = "e330de8213a575eab5dd9d1e59c30b9efadf42c1";
//    static String admin_privateKey = "ff628c601b414a40d04b9b7eac3cc085ecdf7ce5c0fa898051eff71d8728480f";

    //http://47.102.115.64:38000  服务器
    static String admin_address = "59514f8d87c964520fcaf515d300e3f704bf6fcb";
    static String admin_privateKey = "865d9b25aa0296d4c8cc0780eaa4b03cf8ecf43f5a1c3624b18f75d96c681839";

    public static void main(String[] args) {

        System.out.println(new BigInteger("000000000000000000a",16));
    }
    
    public static void sleep(int n){
        try {
            Thread.sleep(n*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static String getRandom4(){
        return getRandom(4);
    }
    public static String getRandom(int n){
        StringBuffer sb = new StringBuffer();
        SecureRandom generater = new SecureRandom();
        for(int i=0;i<n;i++){
            sb.append(generater.nextInt(10));
        }
        return sb.toString();
    } 
}
