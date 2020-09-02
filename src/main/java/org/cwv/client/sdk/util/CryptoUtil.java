package org.cwv.client.sdk.util;


import org.brewchain.core.crypto.JavaEncImpl;
import org.brewchain.core.crypto.model.KeyPairs;

public class CryptoUtil {


    public static JavaEncImpl crypto;

    static {
        crypto = new JavaEncImpl();
    }

    public static byte[] hexStrToBytes(String hexStr){
        return crypto.hexDec(hexStr);
    }

    public static String bytesToHexStr(byte[] bytes){
        return crypto.hexEnc(bytes);
    }

    public static byte[] sha3(byte[] contentBytes){
        return crypto.sha3Encode(contentBytes);
    }

    public static KeyPairs privatekeyToAccountKey(byte[] pkBytes){
        return crypto.priKeyToKey(crypto.hexEnc(pkBytes));
    }
    public static KeyPairs privatekeyToAccountKey(String pk){
        return crypto.priKeyToKey(pk);
    }

    public static byte[] sign(String privateKey,byte[] contentBytes){
        return crypto.ecSign(privateKey,contentBytes);
    }
    public static String signHex(String privateKey,byte[] contentBytes){
        return bytesToHexStr(crypto.ecSign(privateKey,contentBytes));
    }

    public static boolean verifySign(String pubKey,byte[] cont,String sign){
        return crypto.ecVerify(pubKey,cont,hexStrToBytes(sign));
    }

    public static String privateKeyToAddress(byte[] pkBytes){
        return crypto.priKeyToKey(crypto.hexEnc(pkBytes)).getAddress();
    }

    public static String privateKeyToPublicKey(byte[] pkBytes){
        return crypto.priKeyToKey(crypto.hexEnc(pkBytes)).getPubkey();
    }

    public static KeyPairs getRandomKP(){
        return crypto.getRandomKP();
    }




}
