package org.cwv.client.sdk.cryptotest;


import org.cwv.client.sdk.util.CryptoUtil;

public class test {

    public static void main(String[] args) {


//        String ss = HiChain.sha3("000000000000000000000000554f885c019d950e32743af6735c3f8bdf74d3390000000000000000000000000000000000000000000000000000000000000004");
//        //主链lib
//        System.out.println(ss);

//        byte[] bb = CryptoUtil.sha3("aaaaa".getBytes());
//        String str = CryptoUtil.bytesToHexStr(bb);
//        System.out.println(str);

        String prikey = "8d3025a26237eddfa09b4ff83a904f573d0b43c0c0f92ef714e4dafa1e51e8ba";
        String pukey="6fd95189facf96b2b0b9739e7d156154035b3fddd466eeea08543687167936f3a7bc0f43512c0f39baba11d95a7c86e7877e838939d45d447583867abc511ae8";
        byte[] bb = CryptoUtil.sign(prikey,
                "aaaaa".getBytes());
        String str = CryptoUtil.bytesToHexStr(bb);
        System.out.println(str);
        
        boolean b= CryptoUtil.verifySign(pukey,"aaaaa".getBytes(),"6fd95189facf96b2b0b9739e7d156154035b3fddd466eeea08543687167936f3a7bc0f43512c0f39baba11d95a7c86e7877e838939d45d447583867abc511ae82f281d64789a749dee559d43f643fbed0831699e3ecce83c61bd49a3211b2f7e061db0b7e0af470ef2cd87d467c4ec0bd8b7f282dcb8486d21956fe240e7070aa4dcaaa74495d9afd6f90eb1ae262c82ce5883ef");
        System.out.println(b);
        
//        
    }
    
}
