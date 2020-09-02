package cwv;

import com.google.protobuf.ByteString;
import org.cwv.client.sdk.Config;
import org.cwv.client.sdk.HiChain;
import org.cwv.client.sdk.model.TransferInfo;
import org.cwv.client.sdk.util.CryptoUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Hician {

    public static void main(String[] args) {

        Config.host = "http://114.115.205.57:8000";
        
        TransferInfo ti = new TransferInfo();
        ti.setToAddr("0e821881557ec50c86d7ac4ba3550fe1327d3681");
        ti.setAmount("0");
//        ti.setToken("AAA");
//        ti.setTokenAmount("2000");
//        ti.setSymbol("TTT");
//        List<String> tokens = new ArrayList<>();
//        tokens.add("a1");
//        tokens.add("a2");
//        ti.setCryptoToken(tokens);
        
        List<TransferInfo> outs = new ArrayList();
        outs.add(ti);
//        outs.add(ti);
        String result = HiChain.transferTo("f084bdda314a69e08af5a3a7ccee3c4ee339e602",
                "7b7eea34454afd9607339fdc236c6dfe8707ae40f140f7c7ff5c58836636541f","exdata",outs);

        System.out.println("----"+result);
        String jsonResult = HiChain.getUserAccountInfo("0e821881557ec50c86d7ac4ba3550fe1327d3681");

        System.out.println(jsonResult);
        
    }
    
    @Test
    public void byteStringTest(){

        byte[] datas = new byte[]{};
        String exdata = "5199dc1ac7ea693d2bf087e4548aa940a0ffdfdc";
        try {
            datas = exdata.getBytes("utf-8");

            
            String hexString = CryptoUtil.bytesToHexStr(datas);
            
            ByteString bs = ByteString.copyFrom(CryptoUtil.hexStrToBytes(exdata));

            String s = new String(bs.toByteArray(),"utf-8");
            System.out.println( new String(CryptoUtil.hexStrToBytes(bs.toStringUtf8())));
//            System.out.println(bs.toStringUtf8());

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
}
