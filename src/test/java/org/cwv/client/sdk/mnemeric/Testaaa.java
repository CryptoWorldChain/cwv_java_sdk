package org.cwv.client.sdk.mnemeric;

import com.develop.mnemonic.KeyPairUtils;
import com.develop.mnemonic.MnemonicUtils;
import com.develop.mnemonic.utils.Numeric;

public class Testaaa {

    public static void main(String[] args) {

        // 默认生成12个单词的助记词
//        String mnemonic = MnemonicUtils.generateMnemonic();
        String mnemonic = "gas twist liar foster crunch arrow brush market author knee fit frown";
        System.out.println("mnemonic = " + mnemonic);

        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
        System.out.println("seed = " + Numeric.toHexStringNoPrefix(seed));

        byte[] privateKeyBytes = KeyPairUtils.generatePrivateKey(seed, KeyPairUtils.CoinTypes.CWV);
        System.out.println("privateKeyBytes:"+ Numeric.toHexStringNoPrefix(privateKeyBytes));

        byte[] bb = KeyPairUtils.generatePrivateKey(mnemonic, KeyPairUtils.CoinTypes.CWV);
        System.out.println("privateKeyBytes:"+ Numeric.toHexStringNoPrefix(bb));




    }
}
