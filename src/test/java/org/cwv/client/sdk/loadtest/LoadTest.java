package org.cwv.client.sdk.loadtest;

import org.cwv.client.sdk.util.CryptoUtil;
import org.cwv.client.sdk.util.FileAppender;
import org.cwv.client.sdk.util.FileUtil;
import org.brewchain.core.crypto.model.KeyPairs;

import java.util.List;

public class LoadTest {


    /**
     * 生成 1w 地址和私钥
     */
    public static void generatKP() {
        String dir_path= System.getProperty("user.dir")+"/testfile";
        System.out.println(dir_path);
        
        String kpFile = dir_path+"/account.txt";

        FileAppender fa = FileAppender.getAppender(kpFile,false);
        for (int i = 0; i < 10000; i++) {
            KeyPairs kp = CryptoUtil.getRandomKP();
            String line = kp.getAddress()+"  "+kp.getPrikey();
            fa.appendLine(line);
        }
        
        fa.release();

    }

    public static void main(String[] args) {
        String kpFile = System.getProperty("user.dir")+"/testfile"+"/account.txt";
        List<String> list = FileUtil.getContent(kpFile);
        for (int i = 0; i < 100; i++) {
            System.out.println(list.get(i));
        }
    }
    
}
