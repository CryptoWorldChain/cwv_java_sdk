package org.cwv.client.sdk.util;

import com.develop.mnemonic.KeyPairUtils;
import com.develop.mnemonic.MnemonicUtils;
import org.brewchain.core.crypto.cwv.keystore.KeyStoreFile;
import org.brewchain.core.crypto.cwv.keystore.KeyStoreHelper;
import org.brewchain.core.crypto.model.KeyPairs;


public final class WalletUtil {

    public static void main(String[] args) throws Exception {
//        System.out.println(getMnemonic());
//        String mnemonic = getMnemonic();
//        String mnemonic = "fragile since guard embody forget casual peanut flavor path typical collect blur";
//        KeyPairs kp = getKeyPair(mnemonic);
//        System.out.println(kp);
        
        
//        String ks = genKeyStoreFromMnemonic(mnemonic,"a1234567");
//        System.out.println(ks);

        //生成助记词
        String words = WalletUtil.getMnemonic();
        System.out.println(words);
        
        //助记词生成公私钥地址对
        KeyPairs kp = WalletUtil.getKeyPair(words);
        System.out.println("地址:"+kp.getAddress());
        System.out.println("公钥:"+kp.getPubkey());
        System.out.println("私钥:"+kp.getPrikey());
        
        //助记词生成keystore文件内容
        String ksJson = WalletUtil.genKeyStoreFromMnemonic(words,"password");
        System.out.println("keyStoreJson:"+ksJson);
        
        //从keystore恢复公私钥
        KeyPairs kp2 = WalletUtil.restoreFromKeyStore(ksJson,"password");
        System.out.println("地址:"+kp2.getAddress());
        System.out.println("公钥:"+kp2.getPubkey());
        System.out.println("私钥:"+kp2.getPrikey());
        
        
    }

    /**
     * 生成助记词
     * @return e.g.: "gas twist liar foster crunch arrow brush market author knee fit frown"
     */
    public static String getMnemonic(){
        String words = MnemonicUtils.generateMnemonic();
//        List<String> words = Arrays.asList("apart genre supply plate doctor coach stay anger chimney stable member marble".split(" "));
        return words;
    }

    /**
     * 助记词生成keystore文件内容
     */
    public static String genKeyStoreFromMnemonic(String mnemonic,String password){
        byte[] bb = KeyPairUtils.generatePrivateKey(mnemonic, KeyPairUtils.CoinTypes.CWV);
        KeyPairs kp = CryptoUtil.privatekeyToAccountKey(bb);

        KeyStoreHelper ksh = new KeyStoreHelper(CryptoUtil.crypto);

        KeyStoreFile keyStoreFile = ksh.generate(kp,password);
        String str = ksh.parseToJsonStr(keyStoreFile);

        return str;
    }


    /**
     * 从keystore恢复公私钥
     */
    public static KeyPairs restoreFromKeyStore(String ksJson,String pwd){
        KeyStoreHelper ksh = new KeyStoreHelper(CryptoUtil.crypto);
        return CryptoUtil.privatekeyToAccountKey(ksh.getKeyStore(ksJson,pwd).getPrikey());
    }

    /**
     * 从助记词生成公私钥
     */
    public static KeyPairs getKeyPair(String mnemonic){
        byte[] bb = KeyPairUtils.generatePrivateKey(mnemonic, KeyPairUtils.CoinTypes.CWV);
        KeyPairs kp = CryptoUtil.privatekeyToAccountKey(bb);
        return kp;
    }

    /**
     * 从私钥生成公私钥
     * @param pk 私钥
     * @return
     */
    public static KeyPairs getKeyPairFromPk(String pk){
        KeyPairs kp = CryptoUtil.privatekeyToAccountKey(pk);
        return kp;
    }
 

    /**
     * 
     */




}
