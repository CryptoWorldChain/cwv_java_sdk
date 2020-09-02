package org.cwv.client.sdk.contract;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.cwv.client.sdk.HiChain;
import org.cwv.client.sdk.chain.NonceKeeper;
import org.cwv.client.sdk.model.ChainCallBack;
import org.cwv.client.sdk.model.Model;
import org.cwv.client.sdk.util.LocalCrypto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PublishContract {

    public static String pub(String data, ChainCallBack... cbs){
        
        String fromAddr = "e330de8213a575eab5dd9d1e59c30b9efadf42c1";
        String fromPriKey = "ff628c601b414a40d04b9b7eac3cc085ecdf7ce5c0fa898051eff71d8728480f";

        int nonce = NonceKeeper.getNonce(fromAddr);
        //构造交易参数
        Model.SendTransaction.Builder st = Model.SendTransaction.newBuilder();
        st.setAddress(fromAddr);
        st.setPrivateKey(fromPriKey);
        st.setNonce(nonce);
        st.setExdata("");
        st.setTimestamp(System.currentTimeMillis());


        Model.SendPublicContractData.Builder  pcb = Model.SendPublicContractData.newBuilder();
        pcb.setData(data);
        pcb.setCode("");

        Model.SendTransactionData.Builder stb = Model.SendTransactionData.newBuilder();
        stb.setType(Model.SendDataType.PUBLICCONTRACT);
        stb.setPublicContractData(pcb);
        
        st.setData(stb);

        //发交易请求
        return HiChain.doTransaction(st.build(), LocalCrypto.getInstance(),cbs[0]);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        String dirPath = "C:\\Users\\xiaom\\Desktop\\contract\\solidity-windows\\build\\";
        File dir = new File(dirPath);
        for(File f :dir.listFiles()){
            if(f.getName().indexOf(".abi") >0){
                continue;
            }
            String ContractName = f.getName().replace(".bin","");
            BufferedReader br = new BufferedReader(new FileReader(f));

            String ct = br.readLine();
            if(ct == null || ct.trim().length()==0){
                continue;
            }
//            System.out.println(f.getName());

            String result = pub(ct);
//            System.out.println(result);

            JsonObject resultJson = new JsonParser().parse(result).getAsJsonObject();
            String contractAddress = resultJson.get("contractHash").getAsString();
            System.out.println("合约：："+ContractName+"----"+contractAddress);
            
            Thread.sleep(2000);
            
        }      
        
        
        
    }
    
}
