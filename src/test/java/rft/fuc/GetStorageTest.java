package rft.fuc;

import org.cwv.client.sdk.Config;
import org.cwv.client.sdk.HiChain;
import org.cwv.client.sdk.https.RequestBuilder;
import org.cwv.client.sdk.model.ChainRequest;
import org.cwv.client.sdk.util.CryptoUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class GetStorageTest {
    
    @Before
    public void init(){
        //        Config.host = "http://192.168.1.129:8000";
//        Config.host = "http://47.103.149.80:8001";

        System.out.println("Config.host:::::::"+ Config.host);
    }
    
    @Test
    public void getUserFeeConfig() throws UnsupportedEncodingException {
        String rd = "Mf5qpVG";
        getFeeInfo(rd,2);

        rd = "MrCSJft";
        getFeeInfo(rd,2);
        
    }
    
    /**
     * type : 1按照地址查询；2按照邀请码查询
     */
    public void getFeeInfo(String rd,int type) throws UnsupportedEncodingException {

        String key = "";
        if(type ==1){
            key = HiChain.sha3("000000000000000000000000"+rd +
                    "000000000000000000000000000000000000000000000000000000000000000a");
        }else{//code
            String s ="000000000000000000000000000000000000000000000000000000000000000" + CryptoUtil.bytesToHexStr(rd.getBytes("utf-8"));
            
            s=s.substring(0,64);
            key = HiChain.sha3(s +
                    "0000000000000000000000000000000000000000000000000000000000000009");
        }
        
        String body = "{\"address\": \"ed4c4da03bfe48619299ce9e1ed6dd4e50f3055c\",\n" +
                "\"keys\": [{\"key\": \""+key+"\",\n" +
                "\"desc\": {\"name\": \"online\",\"type\": \"struct\",\"propType\": [{\"name\": \"sellMin\",\"type\":" +
                " \"direct\"},\n" +
                "{\"name\": \"sellMax\",\"type\": \"direct\"},{\"name\": \"sellFeeRatio\",\"type\": \"uint256\"}," +
                "{\"name\": \"buyMin\",\"type\": \"direct\"},\n" +
                "{\"name\": \"buyMax\",\"type\": \"direct\"},{\"name\": \"buyFeeRatio\",\"type\": \"uint256\"}," +
                "{\"name\": \"upperAddr\",\"type\": \"direct\"},\n" +
                "{\"name\": \"myAddr\",\"type\": \"direct\"},{\"name\": \"upperCode\",\"type\": \"bytes\"},{\"name\":" +
                " \"forbidden\",\"type\": \"direct\"},\n" +
                "{\"name\": \"subSellMin\",\"type\": \"direct\"},{\"name\": \"subSellMax\",\"type\": \"direct\"}," +
                "{\"name\": \"subSellFeeRatio\",\"type\": \"uint256\"},\n" +
                "{\"name\": \"subBuyMin\",\"type\": \"direct\"},{\"name\": \"subBuyMax\",\"type\": \"direct\"}," +
                "{\"name\": \"subBuyFeeRatio\",\"type\": \"uint256\"},\n" +
                "{\"name\": \"subAddress\",\"type\": \"array\",\"subValueType\": {\"name\": \"addr\",\"type\": " +
                "\"direct\"}}]}}]}";

        ChainRequest cr = RequestBuilder.buildGetStorageReq(body);

        String result = HiChain.doExecute(cr);
        System.out.println(result);
    }

    
}
