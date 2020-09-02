package org.cwv.client.sdk;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.cwv.client.sdk.chain.NonceKeeper;
import org.cwv.client.sdk.chain.TxTracker;
import org.cwv.client.sdk.contract.abi.Function;
import org.cwv.client.sdk.https.*;
import org.cwv.client.sdk.model.*;
import org.cwv.client.sdk.model.Model.*;
import org.cwv.client.sdk.model.contract.AbsContractCallReq;
import org.cwv.client.sdk.model.contract.AbsContractCallResp;
import org.cwv.client.sdk.util.*;
import org.spongycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


/**
 * 入口函数
 */
@Slf4j
public final class HiChain {

    //凡是使用sdk都要预先做一次初始化
    public static void init(String address){
        
        //通过助记词获取用户地址私钥并保存
//        KeyPairs kp = WalletUtil.getKeyPair(mnemonicWords);
//        log.info("Loaded KeyPairs Info, Address:"+kp.getAddress()
//                    +"; pubKey:"+kp.getPubkey());
//        Config.setKeyPairs(kp);
        
        
        //初始化domain-pool,它必须先做。完成后再进行下面的操作。
        //DomainPool.getDynamicDomians();
        
        
        //初始化nonce
        if(address != null && address.length()>30){
            NonceKeeper.initNonce(address);
        }
        
        //其初始化工作
        if (Config.isMultiQueueExecUsed){
            MultiQueuedExecutor.init();
        }else{
            QueuedExecutor.init();
        }
        PureOkHttpExecutor.init();
        OKHttpExecutor.init();
        TxTracker.init();
        SingleThreadOKHttpExecutor.init();
        LocalCrypto.getInstance().sign("1111111111111111111111111111111111111111111111111111111111111111",new byte[]{1});
        log.info("初始化完成！");

    }

    /**
     * 异步调用合约方法。
     * @param address    钱包地址
     * @param privateKey 钱包私钥
     * @param exdata     额外信息，暂时不用，传""
     * @param ccr        合约方法调用请求实现类
     * @param txNotifier 回调实现类
     * @param iCryptos   自定义加密组件
     * @param <T>        指定回调参数类型
     */
    public static <T extends AbsContractCallResp> void asyncCallContractMethod(
            String address, String privateKey, String exdata,
            AbsContractCallReq ccr, TxNotifier<T> txNotifier, ICrypto... iCryptos) {

        log.debug("HiChain 异步调用开始...");
        ChainCallBack ccb = new ChainCallBack<String>() {
            @Override
            public void call(String body) {//返回txhash
                TxResult tr = new Gson().fromJson(body,TxResult.class);
                if(tr.getRetCode() == 1){
                    log.debug("获取到交易hash:{}",tr.getHash());
                    //交易提交成功
                    txNotifier.sendHash(tr.getHash());//通知调用方
                    //添加交易任务追踪
                    TxTracker.add(tr.getHash(), new TxCallback() {
                        @Override
                        public void call(String txhash, String body) {
                            TxContent txContent = new Gson().fromJson(body,TxContent.class);
                            if(txContent.getRetCode() == 1){
                                if(txContent.getTransaction().getStatus() != null){
                                    if("D".equals(txContent.getTransaction().getStatus().getStatus())){
                                        String result = txContent.getTransaction().getStatus().getResult();
                                        try {
                                            T t = (T)ccr.buildResp();
                                            t.setAcceptTimestamp(txContent.getTransaction().getAccepttimestamp());
                                            t.setBlockTimestamp(txContent.getTransaction().getStatus().getTimestamp());
                                            txNotifier.sendResult((T)t.decode(txhash,result));
                                            return;
                                        } catch (Exception e) {
                                            log.error("交易结果转换异常，hash:{};req:{};resp:{}",txhash,ccr,body);
                                            T t = (T)ccr.buildResp();
                                            t.setCode(-1);
                                            t.setMsg("交易结果转换异常");
                                            t.setResult(body);
                                            t.setTxHash(txhash);
                                            t.setAcceptTimestamp(txContent.getTransaction().getAccepttimestamp());
                                            t.setBlockTimestamp(txContent.getTransaction().getStatus().getTimestamp());
                                            txNotifier.error(t);
                                            return;
                                        }
                                    }else{//nonce异常处理
                                        //fixme 这个if应该不会进入
                                        if (body.indexOf("sender nonce is large than transaction nonce")>-1){
                                            new Thread(()->{
                                                NonceKeeper.refreshNonce(address);
                                            },"Nonce_Refresh_Thread").start();
                                        }
                                        log.error("交易失败，hash:{};req:{};resp:{}",txhash,ccr,body);
                                        T t = (T)ccr.buildResp();
                                        t.setCode(-1);
                                        t.setMsg("交易失败");
                                        t.setResult(body);
                                        t.setTxHash(txhash);
                                        t.setAcceptTimestamp(txContent.getTransaction().getAccepttimestamp());
                                        t.setBlockTimestamp(txContent.getTransaction().getStatus().getTimestamp());
                                        txNotifier.error(t);
                                    }
                                }else{
                                    log.error("交易未执行，hash:{};req:{};resp:{}",txhash,ccr,body);
                                    T t = (T)ccr.buildResp();
                                    t.setCode(-1);
                                    t.setMsg("交易未执行");
                                    t.setResult(body);
                                    t.setTxHash(txhash);
                                    t.setAcceptTimestamp(txContent.getTransaction().getAccepttimestamp());
                                    txNotifier.error(t);
                                }
                            }else{
                                log.error("交易失败，hash:{};req:{};resp:{}",txhash,ccr,body);
                                T t = (T)ccr.buildResp();
                                t.setCode(-1);
                                t.setMsg("交易失败");
                                t.setResult(body);
                                t.setTxHash(txhash);
                                txNotifier.error(t);
                            }
                        }
                        @Override
                        public void exceptionCall(String txhash, String message, String body) {
                            log.error("交易结果查询异常，req:{};resp:{}",ccr,body);
                            T t = (T)ccr.buildResp();
                            t.setCode(-1);
                            t.setMsg("交易结果查询异常");
                            t.setResult(body);
                            t.setTxHash(txhash);
                            txNotifier.error(t);
                        }
                    });
                }else{
                    log.error("交易提交异常，req:{};resp:{}",ccr,body);
                    T t = (T)ccr.buildResp();
                    t.setCode(-1);
                    t.setMsg("交易提交异常");
                    t.setResult(body);
                    txNotifier.error(t);
                }
            }

            @Override
            public void error(String body, Exception e) {
                if(e !=null) log.error("交易请求失败，req:{};resp:{}",ccr,body,e);
                else log.error("交易请求失败，req:{};resp:{}",ccr,body);
                T t = (T)ccr.buildResp();
                t.setCode(-1);
                t.setMsg("交易请求失败");
                t.setResult(body);
                txNotifier.error(t);
            }
        };

        //设置exData
//        if((ccr instanceof BuyOrderReq) &&ccr.getExdata() != null){
//            exdata = ccr.getExdata();
//        }

        contractCall(address, privateKey,
                ccr.getContractAddress(), ccr.toBytes(), exdata, ccb);

    }

    /**
     * 同步调用方法
     */
    public static <T extends AbsContractCallResp> T syncCallContractMethod(
            String address, String privateKey, String exdata,
            AbsContractCallReq ccr, ICrypto... iCryptos) {

        CountDownLatch cdl = new CountDownLatch(1);
        T resp = (T) ccr.buildResp();

        ChainCallBack ccb = new ChainCallBack<String>() {
            @Override
            public void call(String body) {//返回txhash
                TxResult tr = new Gson().fromJson(body,TxResult.class);
                if(tr.getRetCode() == 1){
                    //交易提交成功
                    resp.setTxHash(tr.getHash());

                    //添加交易任务追踪
                    TxTracker.add(tr.getHash(), new TxCallback() {
                        @Override
                        public void call(String txhash, String body) {
                            TxContent txContent = new Gson().fromJson(body,TxContent.class);
                            if(txContent.getRetCode() == 1){
                                if(txContent.getTransaction().getStatus() != null){
                                    if("D".equals(txContent.getTransaction().getStatus().getStatus())){
                                        String result = txContent.getTransaction().getStatus().getResult();
                                        try {
                                            resp.setAcceptTimestamp(txContent.getTransaction().getAccepttimestamp());
                                            resp.setBlockTimestamp(txContent.getTransaction().getStatus().getTimestamp());
                                            resp.decode(txhash,result);
                                            cdl.countDown();
                                            return;
                                        } catch (Exception e) {
                                            log.error("交易结果转换异常，hash:{};req:{};resp:{}",txhash,ccr,body);
                                            resp.setCode(-1);
                                            resp.setMsg("交易结果转换异常");
                                            resp.setResult(body);
                                            resp.setTxHash(txhash);
                                            resp.setAcceptTimestamp(txContent.getTransaction().getAccepttimestamp());
                                            resp.setBlockTimestamp(txContent.getTransaction().getStatus().getTimestamp());
                                            cdl.countDown();
                                            return;
                                        }
                                    }else{//nonce异常处理
                                        //fixme 这个if应该不会进入
                                        if (body.indexOf("sender nonce is large than transaction nonce")>-1){
                                            new Thread(()->{
                                                NonceKeeper.refreshNonce(address);
                                            },"Nonce_Refresh_Thread").start();
                                        }
                                        log.error("交易执行失败，hash:{};req:{};resp:{}",txhash,ccr,body);
                                        resp.setCode(-1);
                                        resp.setMsg("交易执行失败");
                                        resp.setResult(body);
                                        resp.setTxHash(txhash);
                                        resp.setAcceptTimestamp(txContent.getTransaction().getAccepttimestamp());
                                        resp.setBlockTimestamp(txContent.getTransaction().getStatus().getTimestamp());
                                        cdl.countDown();
                                    }
                                }else{
                                    log.error("交易未执行，hash:{};req:{};resp:{}",txhash,ccr,body);
                                    resp.setCode(-1);
                                    resp.setMsg("交易未执行");
                                    resp.setResult(body);
                                    resp.setTxHash(txhash);
                                    resp.setAcceptTimestamp(txContent.getTransaction().getAccepttimestamp());
                                    cdl.countDown();
                                }
                            }else{
                                log.error("交易执行异常，hash:{};req:{};resp:{}",txhash,ccr,body);
                                resp.setCode(-1);
                                resp.setMsg("交易执行异常");
                                resp.setResult(body);
                                resp.setTxHash(txhash);
                                cdl.countDown();
                            }
                        }
                        @Override
                        public void exceptionCall(String txhash, String message, String body) {
                            log.error("交易结果查询异常，req:{};resp:{}",ccr,body);
                            resp.setCode(-1);
                            resp.setMsg("交易结果查询异常");
                            resp.setResult(body);
                            resp.setTxHash(txhash);
                            cdl.countDown();
                        }
                    });
                }else{
                    log.error("交易提交异常，req:{};resp:{}",ccr,body);
                    resp.setCode(-1);
                    resp.setMsg("交易提交异常");
                    resp.setResult(body);
                    cdl.countDown();
                }
            }

            @Override
            public void error(String body, Exception e) {
                if(e !=null) log.error("交易请求失败，req:{};resp:{}",ccr,body,e);
                else log.error("交易请求失败，req:{};resp:{}",ccr,body);
                resp.setCode(-1);
                resp.setMsg("交易请求失败");
                resp.setResult(body);
                cdl.countDown();
            }
        };

        //设置exData
//        if((ccr instanceof BuyOrderReq) &&ccr.getExdata() != null){
//            exdata = ccr.getExdata();
//        }

        contractCall(address, privateKey,
                ccr.getContractAddress(), ccr.toBytes(), exdata, ccb);

        //工作线程等待执行结果
        try {
            cdl.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("交易结果获取线程超时或被终止",e);
        }
        return resp;
    }

    
    /**
     * 向主链发交易，包括各种转账，货币管理，联合账户写操作，合约操作等
     */
    public static String doTransaction(SendTransaction st,ICrypto iCrypto,ChainCallBack cbs){
        
//        int nonce = st.getNonce();//取出交易的nonce，不是获取新nonce
        
        //todo 添加异步结果获取
//        String tx = TransactionBuilder.build(st,iCrypto);

//        ChainRequest req = RequestBuilder.buildTransactionReq(tx);
        
        //fixme 异步方式需要优化。
        //todo 可以按照地址实现多个QueuedExecutor并行
//        if(cbs != null && cbs.length>0){//防手贱传null进来
//            req.setCallback(cbs);
//            QueuedExecutor.doExecute(nonce,req);
            if(Config.isMultiQueueExecUsed){
                MultiQueuedExecutor.doExecute(st,iCrypto,cbs);
            }else if(Config.isSerialSendTx){
                QueuedExecutor.doExecute(st,iCrypto,cbs);
            }else{
                SendTransaction stt = st.toBuilder().setNonce( NonceKeeper.getNonce(st.getAddress())).build();
                String tx = TransactionBuilder.build(stt,iCrypto);
                ChainRequest req = RequestBuilder.buildTransactionReq(tx);
                if(cbs != null){
                    req.setCallback(cbs);
                }
                return doExecute(req);
            }
            return "";
//        }
//        return doExecute(req);
    }

    /**
     * 创建token
     * @param address
     * @param privateKey
     * @param token 代币名称（AAA）
     * @param amount 真实金额 * 10^精度（18）
     * @param exdata 扩展信息（备注）
     * @param cbs 可不传
     * @return
     */
    protected static String tokenCreate(String address,String privateKey, String token,String amount,String exdata, ChainCallBack cbs){
        //构造交易参数
        if(address == null || "".equals(address)) {
            throw new IllegalArgumentException("param [fromAddr] is null");
        }
        if(privateKey == null || "".equals(privateKey)) {
            throw new IllegalArgumentException("param [fromPriKey] isbe null");
        }
        if(token == null || "".equals(token)) {
            throw new IllegalArgumentException("param [codeData] isbe null");
        }
        if(amount == null || "".equals(amount)) {
            throw new IllegalArgumentException("param [amount] isbe null");
        }

        SendTransaction.Builder st = SendTransaction.newBuilder();
        st.setAddress(address);
        st.setPrivateKey(privateKey);
        st.setNonce(-1);
        st.setExdata(exdata);
        st.setTimestamp(System.currentTimeMillis());
        
        SendTransactionData.Builder std = SendTransactionData.newBuilder();
        std.setType(SendDataType.OWNERTOKEN);
        std.setOwnerTokenData(SendOwnerTokenData.newBuilder().setAmount(amount).setToken(token));

        st.setData(std);
        return doTransaction(st.build(),LocalCrypto.getInstance(),cbs);
    }

    
    /**
     * 单对单token转账
     * @param fromAddr  付款人
     * @param fromPriKey  付款人私钥
     * @param exData  额外信息，暂时没用
     * @param toAddr  收款人地址
     * @param tokenType  token类型，“AAA”
     * @param amount  金额
     * @return  交易hash
     */
    @Deprecated
    public static String transferTo(String fromAddr,String fromPriKey,String exData,
                                    String toAddr,String tokenType,String amount,ChainCallBack cbs,ICrypto... iCryptos){
//        int nonce = getNonce(fromAddr);
        //构造交易参数
        SendTransaction.Builder st = SendTransaction.newBuilder();
        st.setAddress(fromAddr);
        st.setPrivateKey(fromPriKey);
        st.setNonce(-1);
        st.setExdata(exData);
        st.setTimestamp(System.currentTimeMillis());

        SendTransactionOutput.Builder sto = SendTransactionOutput.newBuilder();
        sto.setAddress(toAddr);
        sto.setAmount("0");
        sto.setToken(tokenType);
        sto.setTokenAmount(AccountUtil.multy18(amount));
        st.addOutputs(sto);

        //发交易请求
        return doTransaction(st.build(),iCryptos[0],cbs);
    }

    /**
     * 普通转账，token转账
     * @param fromAddr
     * @param fromPriKey
     * @param exData
     * @param outs 接收者信息
     * @param cbs 可不传
     * @return
     */
    public static String transferTo(String fromAddr,String fromPriKey,String exData,
                                    List<TransferInfo> outs, ChainCallBack cbs) {
        if(outs == null || outs.size()==0) throw new RuntimeException("param [outs] isbe null");
        //构造交易参数
        SendTransaction.Builder st = SendTransaction.newBuilder();
        st.setAddress(fromAddr);
        st.setPrivateKey(fromPriKey);
        st.setNonce(-1);
        st.setExdata(exData);
        st.setTimestamp(System.currentTimeMillis());

        for (int i = 0; i < outs.size(); i++) {
            TransferInfo ti = outs.get(i);
            SendTransactionOutput.Builder sto = SendTransactionOutput.newBuilder();
            sto.setAddress(ti.getToAddr());
            sto.setAmount("0");
            if(ti.getAmount() !=null){
                sto.setAmount(AccountUtil.multy18(ti.getAmount()));
            }
            if(ti.getToken() !=null){
                if(ti.getTokenAmount() == null){
                    throw new RuntimeException("when token is not null,TokenAmount isbe null");
                }
                sto.setToken(ti.getToken());
                sto.setTokenAmount(AccountUtil.multy18(ti.getTokenAmount()));
            }
            
            if(ti.getSymbol()!=null){
                if(ti.getCryptoToken() == null || ti.getCryptoToken().size()==0){
                    throw new RuntimeException("when Symbol is not null,CryptoToken list isbe null");
                }
                sto.setSymbol(ti.getSymbol());
                sto.addAllCryptoToken(ti.getCryptoToken());
            }
            
            
            st.addOutputs(sto);
        }
        //发交易请求
        return doTransaction(st.build(),LocalCrypto.getInstance(),cbs);
    }

    /**
     * 转账
     * @param fromAddr 发起人地址
     * @param fromPriKey 发起人私钥
     * @param exData 附加信息
     * @param toAddr 收款人地址
     * @param tokenType token名称
     * @param amount 转账金额
     * @param iCryptos 签名工具类
     * @return
     */
    @Deprecated
    public static String transferTo(String fromAddr,String fromPriKey,String exData,
                                    String toAddr,String tokenType,String amount,ICrypto... iCryptos){
        final String[] result = {"{\"code\":-1,\"msg\":\"未知异常\",\"result\":\"\"}"};
        CountDownLatch cdl = new CountDownLatch(1);
        ChainCallBack<String> cbs = new ChainCallBack<String>() {
            @Override
            public void call(String body) {
                TxResult tr = new Gson().fromJson(body,TxResult.class);
                if(tr.getRetCode() == 1){
                    //交易提交成功
//                            txNotifier.sendHash(tr.getHash());//通知调用方
                    log.info("转账交易，from:{},to:{},amount:{},hash:{}",fromAddr,toAddr,amount,tr.getHash());
                    //添加交易任务追踪
                    TxTracker.add(tr.getHash(), new TxCallback() {
                        @Override
                        public void call(String txhash, String body) {
                            TxContent txContent = new Gson().fromJson(body,TxContent.class);
                            if(txContent.getRetCode() == 1){
                                if(txContent.getTransaction().getStatus() != null){
                                    if("D".equals(txContent.getTransaction().getStatus().getStatus())){
                                        log.error("转账成功，from:{},to:{},amount:{},hash{},result:{}",fromAddr,toAddr,amount,txhash,body);
                                        result[0] = "{\"code\":1,\"msg\":\"转账成功\",\"result\":\"转账成功\"}";
                                        cdl.countDown();
                                    }else{
                                        log.error("转账交易失败，from:{},to:{},amount:{},hash{},result:{}",fromAddr,toAddr,amount,txhash,body);
                                        result[0] = "{\"code\":-1,\"msg\":\"转账交易失败\",\"result\":\"\"}";
                                        cdl.countDown();
                                    }
                                }else{
                                    log.error("转账交易未执行，from:{},to:{},amount:{},hash{},result:{}",fromAddr,toAddr,amount,txhash,body);
                                    result[0] = "{\"code\":-1,\"msg\":\"转账交易未执行\",\"result\":\"\"}";
                                    cdl.countDown();
                                }
                            }else{
                                log.error("转账交易失败，from:{},to:{},amount:{},hash{},result:{}",fromAddr,toAddr,amount,txhash,body);
                                result[0] = "{\"code\":-1,\"msg\":\"转账交易失败\",\"result\":\"\"}";
                                cdl.countDown();
                            }
                        }
                        @Override
                        public void exceptionCall(String txhash, String message, String body) {
                            log.error("转账交易结果查询异常，from:{},to:{},amount:{},hash{},result:{}",fromAddr,toAddr,amount,txhash,body);
                            result[0] = "{\"code\":-1,\"msg\":\"转账交易结果查询异常\",\"result\":\"\"}";
                            cdl.countDown();
                        }
                    });
                }else{
                    log.error("转账交易异常，from:{},to:{},amount:{},result:{}",fromAddr,toAddr,amount,body);
                    result[0] = "{\"code\":-1,\"msg\":\"转账交易异常\",\"result\":\"\"}";
                    cdl.countDown();
                }
            }

            @Override
            public void error(String body, Exception e) {
                log.error("转账失败，from:{},to:{},amount:{},result:{}",fromAddr,toAddr,amount,body);
                result[0] = "{\"code\":-1,\"msg\":\"转账失败\",\"result\":\""+e.getMessage()+"\"}";
                cdl.countDown();
            }
        };
        if(iCryptos == null || iCryptos.length == 0){
            iCryptos = new ICrypto[]{LocalCrypto.getInstance()};
        }
        transferTo(fromAddr,fromPriKey,exData,
                toAddr,tokenType,amount,cbs,iCryptos);
        //工作线程等待执行结果
        try {
            cdl.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("交易结果获取线程超时或被终止",e);
        }
        return result[0];
    }

    /**
     * 转账
     * @param fromAddr 发起人地址
     * @param fromPriKey 发起人私钥
     * @param exData 附加信息
     * @param iCryptos 签名工具类
     * @return
     */
    public static String transferTo(String fromAddr,String fromPriKey,String exData,
                                    List<TransferInfo> outs,ICrypto... iCryptos){
        final String[] result = {"{\"code\":-1,\"msg\":\"未知异常\",\"result\":\"\"}"};
        CountDownLatch cdl = new CountDownLatch(1);
        ChainCallBack<String> cbs = new ChainCallBack<String>() {
            @Override
            public void call(String body) {
                TxResult tr = new Gson().fromJson(body,TxResult.class);
                if(tr.getRetCode() == 1){
                    //交易提交成功
//                            txNotifier.sendHash(tr.getHash());//通知调用方
                    log.info("转账交易，from:{},outs:{},hash:{}",fromAddr,outs,tr.getHash());
                    //添加交易任务追踪
                    TxTracker.add(tr.getHash(), new TxCallback() {
                        @Override
                        public void call(String txhash, String body) {
                            TxContent txContent = new Gson().fromJson(body,TxContent.class);
                            if(txContent.getRetCode() == 1){
                                if(txContent.getTransaction().getStatus() != null){
                                    if("D".equals(txContent.getTransaction().getStatus().getStatus())){
                                        log.error("转账成功，from:{},outs:{},hash:{},result:{}",fromAddr,outs,txhash,body);
                                        result[0] = "{\"code\":1,\"msg\":\"转账成功\",\"result\":\"转账成功\"}";
                                        cdl.countDown();
                                    }else{
                                        log.error("转账交易失败，from:{},outs:{},hash{},result:{}",fromAddr,outs,txhash,body);
                                        result[0] = "{\"code\":-1,\"msg\":\"转账交易失败\",\"result\":\"\"}";
                                        cdl.countDown();
                                    }
                                }else{
                                    log.error("转账交易未执行，from:{},outs:{},hash{},result:{}",fromAddr,outs,txhash,body);
                                    result[0] = "{\"code\":-1,\"msg\":\"转账交易未执行\",\"result\":\"\"}";
                                    cdl.countDown();
                                }
                            }else{
                                log.error("转账交易失败，from:{},outs:{},hash{},result:{}",fromAddr,outs,txhash,body);
                                result[0] = "{\"code\":-1,\"msg\":\"转账交易失败\",\"result\":\"\"}";
                                cdl.countDown();
                            }
                        }
                        @Override
                        public void exceptionCall(String txhash, String message, String body) {
                            log.error("转账交易结果查询异常，from:{},outs:{},hash{},result:{}",fromAddr,outs,txhash,body);
                            result[0] = "{\"code\":-1,\"msg\":\"转账交易结果查询异常\",\"result\":\"\"}";
                            cdl.countDown();
                        }
                    });
                }else{
                    log.error("转账交易异常，from:{},outs:{},result:{}",fromAddr,outs,body);
                    result[0] = "{\"code\":-1,\"msg\":\"转账交易异常\",\"result\":\"\"}";
                    cdl.countDown();
                }
            }

            @Override
            public void error(String body, Exception e) {
                log.error("转账失败，from:{},outs:{},result:{}",fromAddr,outs,body);
                result[0] = "{\"code\":-1,\"msg\":\"转账失败\",\"result\":\""+e.getMessage()+"\"}";
                cdl.countDown();
            }
        };
        if(iCryptos == null || iCryptos.length == 0){
            iCryptos = new ICrypto[]{LocalCrypto.getInstance()};
        }
        transferTo(fromAddr,fromPriKey,exData,
                outs,cbs);
        //工作线程等待执行结果
        try {
            cdl.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("交易结果获取线程超时或被终止",e);
        }
        return result[0];
    }
    
    /**
     * 查询用户信息，包含账户余额
     * @param address
     * @param cbs
     * @return
     */
    public static String getUserAccountInfo(String address,ChainCallBack... cbs){
        ChainRequest cr = RequestBuilder.buildGetUserInfoReq(address);
        if(cbs != null && cbs.length > 0){
            cr.setCallback(cbs[0]);
        }
        return doExecute(cr);
    }

    /**
     * 获取账户token余额
     * @param address
     * @param cbs
     * @return
     */
    public static AccountTokenBalance getAccountInfo(String address,ChainCallBack<AccountTokenBalance>... cbs){
        ChainRequest cr = RequestBuilder.buildGetUserInfoReq(address);
        if(cbs != null && cbs.length > 0){
            cr.setCallback(new ChainCallBack<String>() {
                @Override
                public void call(String body) {
                    AccountInfo ac = new Gson().fromJson(body,AccountInfo.class);
                    AccountTokenBalance atb = new AccountTokenBalance();
                    if(ac.getRetCode()==1){
                        List<AccountInfo.AccountBean.ValueBean.TokensBean> list = ac.getAccount().getValue().getTokens();
                        if(list!=null && list.size()>0){
                            for (int i = 0; i < list.size(); i++) {
                                AccountInfo.AccountBean.ValueBean.TokensBean tb =
                                        list.get(i);
                                atb.put(tb.getToken(),tb.getBalance());
                            }
                        }
                    }
                    atb.offChainFormat();
                    cbs[0].call(atb);
                }
                @Override
                public void error(String body, Exception e) {
                    cbs[0].error(body,e);
                }
            });
            doExecute(cr);
            return null;
        }else{
            String result = doExecute(cr);
            AccountInfo ac = new Gson().fromJson(result,AccountInfo.class);
            AccountTokenBalance atb = new AccountTokenBalance();
            if(ac.getRetCode()==1){
                List<AccountInfo.AccountBean.ValueBean.TokensBean> list = ac.getAccount().getValue().getTokens();
                if(list!=null && list.size()>0){
                    for (int i = 0; i < list.size(); i++) {
                        AccountInfo.AccountBean.ValueBean.TokensBean tb =
                                list.get(i);
                        atb.put(tb.getToken(),tb.getBalance());
                    }
                }
            }
            atb.offChainFormat();
            return atb;
        }
    }


    //获取交易结果查询对象
    public static TxContent getTxContent(String tx,ChainCallBack<TxContent>... cbs){
        System.out.println("交易hash:"+tx);
        TxResult tr = new Gson().fromJson(tx,TxResult.class);

        if(tr.getRetCode() ==1 ){
            ChainRequest cr = RequestBuilder.builtGetTxInfoReq(tr.getHash());
            if(cbs != null && cbs.length > 0){
                //cr.setCallback(cbs[0]);
                cr.setCallback(new ChainCallBack<String>() {
                    @Override
                    public void call(String body) {
                        cbs[0].call(new Gson().fromJson(body,TxContent.class));
                    }
                    @Override
                    public void error(String body, Exception e) {
                        cbs[0].error(body,e);
                    }
                });

                doExecute(cr);
                return null;
            }else{
                String re = doExecute(cr);
                return new Gson().fromJson(re,TxContent.class);
            }
        }else {
            return null;
        }
    }

    /**
     * hexString 转换为UTF-8格式的String .
     * 读取交易的exdata使用
     */
    public static String hexStringToUTF8(String hexString) throws UnsupportedEncodingException {
        return new String(CryptoUtil.hexStrToBytes(hexString),"utf-8");
    }
    
    /**
     * 获取块时间
     * @param height
     * @return
     */
    public static long getBlockTimeByHeight(long height){
        ChainRequest cr = RequestBuilder.buildGetBlockByHeightReq(height);
        try {
            String result = doExecute(cr);

            BlockInfo bi = new Gson().fromJson(result,BlockInfo.class);
            if(bi!=null && bi.getRetCode() == 1&&
                bi.getBlock() !=null &&bi.getBlock().size()>0 && 
                bi.getBlock().get(0) !=null){
                BlockInfo.BlockBean bb = bi.getBlock().get(0);
                return bb.getHeader().getTimestamp();
            }
        } catch (Exception e) {
            log.error("获取块时间异常",height,e);
        }
        return 0;
    }

    /**
     * 发布合约
     * @param fromAddr 账户地址
     * @param fromPriKey 私钥
     * @param codeData 合约编译后的二进制码
     * @param exData 扩展信息
     * @return
     */
    public static String contractCreate(String fromAddr,String fromPriKey, String codeData, String exData) {
        if(fromAddr == null || "".equals(fromAddr)) {
            throw new IllegalArgumentException("param [fromAddr] isbe null");
        }
        if(fromPriKey == null || "".equals(fromPriKey)) {
            throw new IllegalArgumentException("param [fromPriKey] isbe null");
        }
        if(codeData == null || "".equals(codeData)) {
            throw new IllegalArgumentException("param [codeData] isbe null");
        }
        SendTransaction.Builder st = SendTransaction.newBuilder();
        st.setAddress(fromAddr)
                .setPrivateKey(fromPriKey)
                .setNonce(-1)
                .setData(SendTransactionData.newBuilder().setPublicContractData(SendPublicContractData.newBuilder().setData(codeData)))
                .setTimestamp(System.currentTimeMillis());
        if(exData !=null && "".equals(exData)) {
            st.setExdata(exData);
        }

        //发交易请求
        return doTransaction(st.build(), LocalCrypto.getInstance(),null);
    }

    /**
     * 执行合约
     * @param fromAddr 账户
     * @param fromPriKey 私钥
     * @param contractAddress 合约地址
     * @param codeData 合约方法及参数编译后的二进制码
     * @param exData 扩展信息
     * @return
     */
    public static String contractCall(String fromAddr,String fromPriKey, String contractAddress, String codeData, String exData, ChainCallBack ccb) {
        if(fromAddr == null || "".equals(fromAddr)) {
            throw new IllegalArgumentException("param [fromAddr] isbe null");
        }
        if(fromPriKey == null || "".equals(fromPriKey)) {
            throw new IllegalArgumentException("param [fromPriKey] isbe null");
        }
        if(codeData == null || "".equals(codeData)) {
            throw new IllegalArgumentException("param [codeData] isbe null");
        }
        SendTransaction.Builder st = SendTransaction.newBuilder();

        st.setAddress(fromAddr)
                .setPrivateKey(fromPriKey)
                .setNonce(-1)
                .setData(SendTransactionData.newBuilder().setCallContractData(SendCallContractData.newBuilder().setData(codeData).setContract(contractAddress)))
                .setTimestamp(System.currentTimeMillis());

        if(exData !=null && "".equals(exData)) {
            st.setExdata(exData);
        }

        //发交易请求
        return doTransaction(st.build(),LocalCrypto.getInstance(),ccb);
    }


    public static Pattern p= Pattern.compile("(?<=base_last_cny\":\")[0-9.]*");
    public static Pattern p2= Pattern.compile("(?<=last_usd\":\")[0-9.]*");

    public static Pattern p3= Pattern.compile("(?<=\"coinName\":\"USDT\",\"buy\":\")[0-9.]+");
    public static Pattern p4= Pattern.compile("(?<=\"coinName\":\"USDT\",\"buy\":\"[0-9.]{2,4}\",\"sell\":\")[0-9\\.]+");

    public static String getTokenEncodeAddr(String meth2) {
        String template = "0000000000000000000000000000000000000000";
        String code = new String(Hex.encode(meth2.getBytes()))+"20";
        return template.substring(0,template.length()-code.length())+code;
    }


    public static String doExecute(ChainRequest cr){
        if(!cr.isCallBackNull()) {
            OKHttpExecutor.asyncExecute(cr);
            return "";
        }
        else
            return OKHttpExecutor.execute(cr);
    }

    public static String sha3(String hexStr){
        return CryptoUtil.bytesToHexStr(CryptoUtil.sha3(CryptoUtil.hexStrToBytes(hexStr)));
    }


    public static String base58Encode(String hexStr){
        byte[] b = CryptoUtil.hexStrToBytes(hexStr);
        return Base58.encode(b);
    }

    /**
     * 签名交易
     * @param privateKey 私钥
     * @param txData  交易数据
     */
    public static byte[] signTx(String privateKey,byte[] txData){
        return CryptoUtil.sign(privateKey,txData);
    }

    public static Function getFunctionSignByAbi(String functionAbi){
        try {
           return Function.fromJson(functionAbi);
        } catch (ParseException e) {
            log.error("Function.fromJson error",e);
            return null;
        }
    }

    /**
     * 查询用户信息
     * @param address
     * @return
     */
    public static String getUserInfo(String address){
        ChainRequest cr = RequestBuilder.buildGetUserInfoReq(address);
        return doExecute(cr);
    }


}
