package org.cwv.client.sdk.mnemeric;

import org.cwv.client.sdk.util.CryptoUtil;
import org.cwv.client.sdk.util.WalletUtil;
import org.brewchain.core.crypto.model.KeyPairs;
import org.junit.Test;

public class WalletTest {
    
    @Test
    public void genKP(){
        //生成助记词
        String words = WalletUtil.getMnemonic();
//        String words = "runway slow charge satoshi interest member spread country frame impose crush reward";
//        System.out.println("助记词："+words);
        
        //从助记词生成钱包
        KeyPairs kp  =WalletUtil.getKeyPair(words);
        System.out.println("钱包地址："+kp.getAddress());
        System.out.println("钱包公钥："+kp.getPubkey());
        System.out.println("钱包私钥："+kp.getPrikey());

        //从助记词生成keyStore文件，需要密码
        String keyStoreJson = WalletUtil.genKeyStoreFromMnemonic(words,"a123456");
        System.out.println("keyStore文件json："+keyStoreJson);

        //从keyStore文件恢复钱包，需要密码
        kp =  WalletUtil.restoreFromKeyStore(keyStoreJson,"a123456");
        System.out.println("钱包地址："+kp.getAddress());
        System.out.println("钱包公钥："+kp.getPubkey());
        System.out.println("钱包私钥："+kp.getPrikey());
        
        kp = WalletUtil.getKeyPairFromPk(kp.getPrikey());
        System.out.println("钱包地址："+kp.getAddress());
        System.out.println("钱包公钥："+kp.getPubkey());
        System.out.println("钱包私钥："+kp.getPrikey());
        
    }
    
    @Test
    public void get(){
        for(int i=0;i<3;i++){
            String words = WalletUtil.getMnemonic();
            //从助记词生成钱包
            KeyPairs kp  =WalletUtil.getKeyPair(words);
            System.out.println("钱包地址："+kp.getAddress());
            System.out.println("钱包公钥："+kp.getPubkey());
            System.out.println("钱包私钥："+kp.getPrikey());
        }
    }
    
    @Test
    public void randomTest(){
        KeyPairs kp  = CryptoUtil.getRandomKP();
        System.out.println("钱包地址："+kp.getAddress());
        System.out.println("钱包公钥："+kp.getPubkey());
        System.out.println("钱包私钥："+kp.getPrikey());
        
        String sign = CryptoUtil.signHex(kp.getPrikey(),"aaaa".getBytes());
        System.out.println("sign:"+sign);
        boolean b = CryptoUtil.verifySign(kp.getPubkey(),"aaaa".getBytes(),sign);
        System.out.println(b);


        kp  = WalletUtil.getKeyPairFromPk(kp.getPrikey());
//        kp  = WalletUtil.getKeyPairFromPk("d44e1e6d18f82d7d858283b4a939e0477d2d17f7279b4615aef166c6e64bdeb3");
        System.out.println("钱包地址："+kp.getAddress());
        System.out.println("钱包公钥："+kp.getPubkey());
        System.out.println("钱包私钥："+kp.getPrikey());

        sign = CryptoUtil.signHex(kp.getPrikey(),"aaaa".getBytes());
        b = CryptoUtil.verifySign(kp.getPubkey(),"aaaa".getBytes(),sign);
        System.out.println(b);
        
    }
    
    
}
