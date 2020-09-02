package org.brewchain.core.crypto;

import org.brewchain.core.crypto.cwv.ECKey;
import org.brewchain.core.crypto.cwv.HashUtil;
import org.brewchain.core.crypto.cwv.jce.SpongyCastleProvider;
import org.brewchain.core.crypto.cwv.util.BytesHelper;
import org.brewchain.core.crypto.cwv.util.EndianHelper;
import org.brewchain.core.crypto.model.KeyPairs;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.jce.spec.ECPrivateKeySpec;
import org.spongycastle.jce.spec.ECPublicKeySpec;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.Arrays;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class JavaEncImpl{
    
    static{
        Security.addProvider(SpongyCastleProvider.getInstance());
    }

    private static ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");

    /**
     * 签名
     * @param priKeyStr
     * @param vcontentHash  二进制
     * @return
     */
    public byte[] ecSign(String priKeyStr,byte[] vcontentHash) {

        try {
            String javaKey = hexEnc(EndianHelper.revert(Hex.decode(priKeyStr)));
            byte[] contentHash = EndianHelper.revert(sha256Encode(vcontentHash));

            BigInteger priS = new BigInteger(javaKey, 16);
            KeySpec privKeySpec = new ECPrivateKeySpec(priS, ecSpec);
            KeyFactory kf = KeyFactory.getInstance("ECDSA", "SC");
            BCECPrivateKey prikey = (BCECPrivateKey)kf.generatePrivate(privKeySpec);

            String prikey_restore = hexEnc(EndianHelper.revert(prikey.getS().toByteArray())).substring(0, 64);
            ECParameterSpec param = prikey.getParameters(); //new ECParameterSpec();

            ECPoint Q = ecSpec.getG().multiply(prikey.getD());

            ECPublicKeySpec pubSpec = new ECPublicKeySpec(Q, ecSpec);
            BCECPublicKey pub = (BCECPublicKey)kf.generatePublic(pubSpec);
            //        val pubkey = hexEnc(EndianHelper.revert(pub.getW.getAffineX.toByteArray())).substring(0, 64) +
            //          hexEnc(EndianHelper.revert(pub.getW.getAffineY.toByteArray())).substring(0, 64);
            //         println("regen.pubkey="+pubkey)
            ECDSASigner ecdsaSigner = new ECDSASigner();
            ecdsaSigner.init(true, new ECPrivateKeyParameters(prikey.getD(),
                    new ECDomainParameters(param.getCurve(), param.getG(), param.getN())));

            BigInteger[] sig = ecdsaSigner.generateSignature(EndianHelper.revert(contentHash));

            byte[] s = EndianHelper.revert(sig[0].toByteArray());
            byte[] a = EndianHelper.revert(sig[1].toByteArray());
            //    val ds = EndianHelper.revert(hexDec(s))
            //    val da = EndianHelper.revert(hexDec(a))
            //    println("ds=" + hexEnc(s).substring(0,64));
            //    println("da=" + hexEnc(a).substring(0,64));
            byte[] rand20bytes = new byte[20];
            {
                byte[] hash = HashUtil.sha256(s);
                new Random(new BigInteger(hash).longValue()).nextBytes(rand20bytes);
            }
            byte[] rl = BytesHelper.merge(
                    hexDec(hexEnc(EndianHelper.revert(pub.getW().getAffineX().toByteArray())).substring(0, 64)),
                    hexDec(hexEnc(EndianHelper.revert(pub.getW().getAffineY().toByteArray())).substring(0, 64)), rand20bytes,
                    hexDec(hexEnc(s).substring(0, 64)),
                    hexDec(hexEnc(a).substring(0, 64)));

            return rl;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } 
    }

    /**
     * 验证签名
     * @param pubKey
     * @param vcontentHash  二进制
     * @param sign  十六进制转码，e.g.,hexDec(Str)
     * @return
     */
    public boolean ecVerify(String pubKey, byte[] vcontentHash, byte[] sign){
        try {
            String strsign = hexEnc(sign);

            byte[] contentHash = EndianHelper.revert(sha256Encode(vcontentHash));
            byte[] javaKey_x = EndianHelper.revert(Hex.decode(strsign.substring(0, 64)));
            byte[] javaKey_y = EndianHelper.revert(Hex.decode(strsign.substring(64, 128)));

            byte[] r_byte = new byte[32];
            byte[] s_byte = new byte[32];
            System.arraycopy(sign, 84, r_byte, 0, 32);
            System.arraycopy(sign, 116, s_byte, 0, 32);
            byte[] r = EndianHelper.revert(r_byte);
            byte[] s = EndianHelper.revert(s_byte);

//            KeyFactory kf = KeyFactory.getInstance("ECDSA", "SC");

            ECPoint ecpoint = ecSpec.getCurve().createPoint(new BigInteger(hexEnc(javaKey_x), 16), new BigInteger(hexEnc(javaKey_y), 16));

            ECPublicKeySpec pubkeySpec = new ECPublicKeySpec(ecpoint, new ECParameterSpec(ecSpec.getCurve(), ecSpec.getG(), ecSpec.getN()));
            ECDSASigner ecdsaSigner = new ECDSASigner();
            ecdsaSigner.init(false, new ECPublicKeyParameters(pubkeySpec.getQ(),
                    new ECDomainParameters(ecSpec.getCurve(), ecSpec.getG(), ecSpec.getN())));
            boolean vresult = ecdsaSigner.verifySignature(EndianHelper.revert(contentHash),
                    new BigInteger(hexEnc(r), 16),
                    new BigInteger(hexEnc(s), 16));
            return vresult;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } 
    }



    public KeyPairs priKeyToKey(String privKey) {

        try {
            String javaKey = hexEnc(EndianHelper.revert(Hex.decode(privKey)));

            BigInteger priS = new BigInteger(javaKey, 16);
            ECPrivateKeySpec privKeySpec = new ECPrivateKeySpec(priS, ecSpec);
            KeyFactory  kf = KeyFactory.getInstance("ECDSA", "SC");

            BCECPrivateKey prikey = (BCECPrivateKey)kf.generatePrivate(privKeySpec);
//        String prikey_restore = hexEnc(EndianHelper.revert(prikey.getS().toByteArray())).substring(0, 64);
//        ECParameterSpec param = prikey.getParameters(); //new ECParameterSpec();

            ECPoint Q = ecSpec.getG().multiply(prikey.getD());

            ECPublicKeySpec pubSpec = new ECPublicKeySpec(Q, ecSpec);
            BCECPublicKey pub = (BCECPublicKey)kf.generatePublic(pubSpec);

            String pubkey = hexEnc(EndianHelper.revert(pub.getW().getAffineX().toByteArray())).substring(0, 64) +
                    hexEnc(EndianHelper.revert(pub.getW().getAffineY().toByteArray())).substring(0, 64);
            String address = hexEnc(Arrays.copyOfRange(sha256Encode(Hex.decode(pubkey)), 0, 20));

            KeyPairs kp = new KeyPairs(
                    pubkey,
                    privKey, address,
                    nextUID(pubkey));
            return kp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } 
    }
    
    
    public KeyPairs getRandomKP(){
        SecureRandom ran = new SecureRandom();
        //ran.generateSeed(System.currentTimeMillis().asInstanceOf[Int])
        ECKey eckey = new ECKey(ran);
        String pubstr = Hex.toHexString(eckey.getPubKey());
        KeyPairs kp = new KeyPairs(
                hexEnc(eckey.getPubKey()),
                hexEnc(eckey.getPrivKeyBytes()),
                hexEnc(eckey.getAddress()),
                nextUID(pubstr));
        return kp;
    }
    
    
    public byte[]  sha3Encode(byte[] content){
        return HashUtil.sha3(content);
    }
    public byte[]  sha256Encode(byte[] content){
        return HashUtil.sha256(content);
    }
    
///////////////////////////

    public String hexEnc(byte[] data){
        return org.spongycastle.util.encoders.Hex.toHexString(data);
    }
    public byte[] hexDec(String str){
        return org.spongycastle.util.encoders.Hex.decode(str);
    }

    public String nextUID(String key){
        if(key == null){
            key = "BCC2018";
        }
        //    val id = UUIG.generate()
        SecureRandom ran = new SecureRandom(key.getBytes());
        //ran.generateSeed(System.currentTimeMillis().asInstanceOf[Int])
        ECKey eckey = new ECKey(ran);
        byte[] encby = HashUtil.ripemd160(eckey.getPubKey());
        //    println("hex=" + Hex.toHexString(encby))
        BigInteger i = new BigInteger(hexEnc(encby), 16);
        //    println("i=" + i)
        String id = hexToMapping(i);
        String mix = mixStr(id, key);
        return mix + SessionIDGenerator.genSum(mix);
    }

    private static char[] StrMapping = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789".toCharArray();
    private static int radix = StrMapping.length;
    private static BigInteger modx = new BigInteger(radix+"");
    
    public String hexToMapping(BigInteger lbi){
        BigInteger v = lbi;
        StringBuffer sb = new StringBuffer();
        while (v.bitCount() > 0) {
            //      println("v="+v.mod(modx))
            sb.append(StrMapping[v.mod(modx).intValue()]);
            v = v.divide( modx);
        }
        return sb.reverse().toString();
    }


    private static char[] StrMapping2 = "qwertyuiFGHJKLZXCopzxcvbnmQWERTY567238DasdfghjklVBNM014UIOPAS9".toCharArray();
    private static Map<Character,Integer> mapIdex = new HashMap<>();
    private static int radix2 = StrMapping2.length;
    static{
        for(int i=0;i<radix2;i++){
            mapIdex.put(StrMapping2[i],i);
        }
    }

    public String mixStr(String str, String key){
        int offset = Math.abs((key.hashCode() + 100) % radix);
        StringBuffer re = new StringBuffer();
        for(char ch:str.toCharArray()){
            Integer idx = mapIdex.get(ch);
            if(idx != null){
                re.append(StrMapping2[(idx + offset)% radix2]);
            }else{
                re.append(ch);
            }
        }
        return re.toString();
    }
    
    
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
/*//        byte[] bb = sha3Encode("aaaaa".getBytes());
//        String str = hexEnc(bb);
//        System.out.println(str);
        
        //-------upper is ok 


        String prikey = "8d3025a26237eddfa09b4ff83a904f573d0b43c0c0f92ef714e4dafa1e51e8ba";
        String pukey="6fd95189facf96b2b0b9739e7d156154035b3fddd466eeea08543687167936f3a7bc0f43512c0f39baba11d95a7c86e7877e838939d45d447583867abc511ae8";
       
//        byte[] bb = ecSign(prikey,"aaaaa".getBytes());
//        String str = hexEnc(bb);
//        System.out.println(str);

//        boolean b = ecVerify(pukey,"aaaaa".getBytes(),hexDec("6fd95189facf96b2b0b9739e7d156154035b3fddd466eeea08543687167936f3a7bc0f43512c0f39baba11d95a7c86e7877e838939d45d447583867abc511ae88c49158275be874cd288f47b0a96360fa21aed182503cf5b2ff7bc82bd813df2730a27e2e6fb51baaff2710a6555719e5ed3a7eb012b0be4572f1360689ce141579d3e46cbbae3ba6db97afbf649c2d82bbc5044"));
//        System.out.println(b);


//        String s = mixStr("bhj(tiy","001");
////        System.out.println(s);

        KeyPairs kp = priKeyToKey(prikey);
        System.out.println(kp);


        KeyStoreHelper ksh = new KeyStoreHelper(new JavaEncImpl());

        KeyStoreFile keyStoreFile = ksh.generate(kp,"a1234567");
        String str = ksh.parseToJsonStr(keyStoreFile);
        System.out.println("keyStore json---"+str);
        
        kp = ksh.getKeyStore(str,"a1234567");
        System.out.println("local convert --"+kp);


        str = "{\"ksType\":\"aes\",\"params\":{\"dklen\":256,\"c\":128,\"l\":241,\"salt\":\"3d305fe32e1e8e49\"},\"pwd\":\"202098edd0eb4b92e7f3070ee9a3a9d886ed2d91b8a8121b3856c509ff0dcd9e\",\"cipher\":\"cbc\",\"cipherParams\":{\"iv\":\"74ac162fd63024ffe2d4b3ce84f4f58d\"},\"cipherText\":\"4c42d38e06a4b628b1cf42291e68be2b121789f9e857359d3eb6cca20052ae9bc3590a485f7a7785f7802fb885d282d90e4b2faeb020685633a85ad2a5120333971e1ef3b8c8ddc1088113a10d8617b103af5bc37c2f39a5a4bc06dc74f24a92fa28ed9da2840f80ad64174cf4432f2d209fe379b3a7cc080191d041ca47c3cdbd7a59da24dc1294d1084c2da9bb29fa5fc4036b1ff5cd40c0cdf65d3d017594df73519beda13f1fd1be98bd23ff9a5555f68ac83a863f73bfde6a640faf1c7fdb18bcaffa70f192ba31434c98c58fa1307f62bbbffaa617bc0f98ad8f33eb1bf62339be49337c48ba91b02e85a8bad9625f0b375ee3f30ab4aa7d465663e9e7\"}";
        kp = ksh.getKeyStore(str,"a1234567");
        System.out.println("cross convert--"+kp);*/
    }
}
