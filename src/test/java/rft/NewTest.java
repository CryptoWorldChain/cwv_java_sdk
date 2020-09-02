//package rft;
//
//import com.google.gson.Gson;
//import org.cwv.client.sdk.Config;
//import org.cwv.client.sdk.HiChain;
//import org.cwv.client.sdk.contract.Contracts;
//import org.cwv.client.sdk.contract.abi.Function;
//import org.cwv.client.sdk.model.*;
//import org.cwv.client.sdk.model.contract.AbsContractCallReq;
//import org.cwv.client.sdk.model.contract.AbsContractCallResp;
//import org.cwv.client.sdk.model.contract.info.broker.BrokerInfoReq;
//import org.cwv.client.sdk.model.contract.match.broker.BrokerOnlineReq;
//import org.cwv.client.sdk.util.AccountUtil;
//import org.brewchain.core.crypto.cwv.util.BytesHelper;
//import org.junit.Test;
//import rft.fuc.GetFeeReq;
//import rft.fuc.GetFeeResp;
//
//import java.math.BigInteger;
//import java.security.SecureRandom;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class NewTest {
//
//    //配置信息
//    static String Broker_address = "30aa55ebf6c7335d3c1d01fbadafc653f58714b5";
//    static String Broker_privateKey = "bd3f3ed1c2701ee15a7982101da3063161a1cf8ed7e69e36fdebfa83d8eb9106";
//
//    static String merchant_address = "e6cfe3c9a59d6ffebf17d18dcddaf1aa7cedee90";
//    static String merchant_privateKey = "3c543c94c8be79c373ee272fb74eaf179b451b7d7d4b56642dc335a9b1cb18ec";
//
//    //本地
////    static String admin_address = "e330de8213a575eab5dd9d1e59c30b9efadf42c1";
////    static String admin_privateKey = "ff628c601b414a40d04b9b7eac3cc085ecdf7ce5c0fa898051eff71d8728480f";
//
//    //http://47.102.115.64:38000  服务器
//    static String admin_address = "59514f8d87c964520fcaf515d300e3f704bf6fcb";
//    static String admin_privateKey = "865d9b25aa0296d4c8cc0780eaa4b03cf8ecf43f5a1c3624b18f75d96c681839";
//
//
//
//
//    //经销商信息添加修改
//    @Test
//    public void brokerInfoAddTest(){
//        BrokerInfoReq bir = new BrokerInfoReq();
////        BrokerInfoResp bs = bir.buildResp();
////        System.out.println("+++++"+bs);
////        System.out.println("+++++"+bs.decode("aaaaaaaaaa","0000000000000000000000000000000000000000000000000000000000000001"));
////        System.out.println("+++++"+bs);
////        System.out.println("+++++"+new Gson().toJson(bs));
//
//        User.UserInfo.Builder ub = User.UserInfo.newBuilder();
//        ub.setAlias("王二麻子bbb");
//        ub.setCompanyName("公司名称公司名称名称bbb");
//        ub.setPhone("13788888888bbb");
//        ub.setQq("23456776543bbb");
//        ub.setArea("上海SHDJDBbbb");
//        ub.setEmail("214353523@qq.comabb");
//        bir.setUinfo(ub.build());
//
//        for (int i = 0; i < 10; i++) {
//            sendCall(Broker_address,Broker_privateKey,bir);
//        }
//        
//    }
//
////    @Test
////    public void getUserInfo(){
////        User.UserInfo u = org.cwv.client.sdk.HiChain.getBrokerInfo(Broker_address);
////        System.out.println("经销商信息："+u);
////    }
//    
//    @Test
//    public void pb2json(){
//        BrokerInfoReq bir = new BrokerInfoReq();
//        User.UserInfo.Builder ub = User.UserInfo.newBuilder();
//        ub.setAlias("王二麻子bbb");
//        ub.setCompanyName("公司名称公司名称名称bbb");
//        ub.setPhone("13788888888bbb");
//        ub.setQq("23456776543bbb");
//        ub.setArea("上海SHDJDBbbb");
//        ub.setEmail("214353523@qq.comabb");
//        bir.setUinfo(ub.build());
//
//        User.UserInfo uu = ub.build();
//        System.out.println(uu.getAlias());
//        
//        uu = uu.toBuilder().setAlias("王三棍").build();
//        System.out.println(uu.getAlias());
//
////        Gson gson = new Gson();
////        String fullJSON = gson.toJson(bir);
////
////        System.out.println(fullJSON);
//
//    }
//
//
//    public static void main(String[] args) {
////        byte[] cc = new byte[]{};
////
////        cc = BytesHelper.appendByte(cc,(byte)32);
////        cc = BytesHelper.appendByte(cc,(byte)12);
////
////        byte[] bb = BytesHelper.bigIntegerToBytes(new BigInteger(AccountUtil.multy18("100")),30);
////         cc = BytesHelper.appendBytes(cc,bb);
////
////        System.out.println(BytesHelper.toHexString(cc));
//        
//        String s = "200c000000000000000000000000000000000000000000056bc75e2d63100000";
//        byte[] d= BytesHelper.hexStringToBytes(s);
//        System.out.println(d);
//
//    }
//    
//    
//    @Test
//    public void onlineTest(){
//        BrokerOnlineReq vr =new BrokerOnlineReq();
//
//        List<OnlinePayInfoData> list = new ArrayList<>();
//        for(int i=0;i<10;i++){
//            OnlinePayInfoData od = new OnlinePayInfoData();
//            od.setPayType((byte)i);
//            od.setPayId((byte)i+10);
//            od.setAmount((1000+i)+"");
//            list.add(od); 
//        }
//        vr.setList(list);
//        System.out.println(list);
//
//        byte[] bb = vr.payInfoDataToBytes();
//        list = vr.bytesToPayInfoDataToBytes(bb);
//        System.out.println(list);
//
//    }
//    
//    @Test
//    public void balance(){
//        Config.host = "http://47.103.149.80:8001";
//        String s = HiChain.getBalance("e9d3ce255e9295215b66709d1ea1a3e4c892b386",0);
//        System.out.println("可用："+s);
//
//        s = HiChain.getBalance("b5bec3bf53a78a1d9ef03436b479c6b8328814de",1);
//        System.out.println("锁定："+s);
//    }
//    
//    @Test
//    public void getBrokerOnlineAllInfo(){
//        Config.host = "http://otc.mars99999.cloud";
//        BrokerOnlineInfo bi = HiChain.getBrokerOnlineAllInfo("e9d3ce255e9295215b66709d1ea1a3e4c892b386");
//        System.out.println(bi);
//    }
//    
//    @Test
//    public void base58(){
//        String s = HiChain.base58Encode(Broker_address);
//        System.out.println(s);
//    }
//    
//    
//    @Test
//    public void getFeeInfo() throws ParseException {
////        Config.host = "http://192.168.1.129:8000";
//        Config.changeContractAddress("Distributors","ed4c4da03bfe48619299ce9e1ed6dd4e50f3055c");
//        
//        GetFeeReq gr = new GetFeeReq();
//        gr.setInvCode("Mf5qpVG");
//        GetFeeResp resp = HiChain.syncCallContractMethod(admin_address,admin_privateKey,"",gr);
//        System.out.println(resp.getTxHash());
//        System.out.println(resp);
//        System.out.println();
//
//        
//        gr.setInvCode("MrCSJft");
//        resp = HiChain.syncCallContractMethod(admin_address,admin_privateKey,"",gr);
//        System.out.println(resp.getTxHash());
//        System.out.println(resp);
//        System.out.println();
//        
//    }
//    
//    
//    @Test
//    public void getInvCode(){
//        String addr = "440027b5373dd7e01a5b88f97b7ccc3a706365cc";
//        String str = HiChain.sha3(addr);
//        String b58 = HiChain.base58Encode(str);
//        String code = b58.substring(b58.length()-6);
//        System.out.println(code);
//    }
//    
//    @Test
//    public void realAmount(){
//        String s = "21e19e0c9bab2400000";
//        BigInteger b = new BigInteger(s,16);
//        String a = AccountUtil.div18(b.toString());
//        System.out.println(a);
//    }
//    
//    
//    public void config(){
//
//        Config.changeContractAddress(Contracts.AAA,"f32457e6e9090d7c4bd96870e29232ec3242a2af");
//        Config.changeContractAddress(Contracts.SOTC,"7c9f85ecd522f06dd73b8c944cbfb9f570cedeea");
//        Config.changeContractAddress(Contracts.SOTCAppeal,"ccccdfc8eb538113d66345ff2244deb5ec07fde7");
//        Config.changeContractAddress(Contracts.Broker,"1f1c71e3a904455151ef238321540c2d2b1dfef5");
//        Config.changeContractAddress(Contracts.Merchant,"aac93a45183695fef621851d84e7766c76289195");
//        Config.changeContractAddress(Contracts.Distributors,"68c3de8c08947e39484ac943267df22312663b27");
//
//    }
//    
//    
//    public void sendCall(String addr, String pk, AbsContractCallReq ccr){
//
//        AbsContractCallResp rel = HiChain.syncCallContractMethod(addr, pk, "",
//                ccr
//                /*, new TxNotifier<AbsContractCallResp>() {
//                    private String txHash;
//                    @Override
//                    public void sendHash(String hash) {
//                        txHash = hash;
//                        System.out.println("调用方获取到了txHash:"+txHash);
//                        //do some business
//                    }
//
//                    @Override
//                    public void sendResult(AbsContractCallResp result) {
//                        System.out.println("调用方获取到了交易结果:"+result);
//                        System.out.println(new Gson().toJson(result));
//                        //do some business
//                    }
//
//                    @Override
//                    public void error(AbsContractCallResp cr) {
//                        System.out.println(cr);
//                    }
//
//                }*/
//                );
//
////        sleep(20);
//
//        System.out.println("调用方获取到了交易结果:"+rel);
//        System.out.println(new Gson().toJson(rel));
//    }
//
//    public void decode(ContractCallRequest ccr,String callData,String result) throws Exception {
//        ccr.fromBytes(callData);
//        System.out.println(ccr);
//        ContractCallResponse bif = ccr.buildResp().decode(result);
//        System.out.println(bif);
//    }
//
//    public static void sleep(int n){
//        try {
//            Thread.sleep(n*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//    public static String getRandom4(){
//        return getRandom(4);
//    }
//    public static String getRandom(int n){
//        StringBuffer sb = new StringBuffer();
//        SecureRandom generater = new SecureRandom();
//        for(int i=0;i<n;i++){
//            sb.append(generater.nextInt(10));
//        }
//        return sb.toString();
//    } 
//}
