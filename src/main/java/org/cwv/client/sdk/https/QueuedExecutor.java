package org.cwv.client.sdk.https;

import org.cwv.client.sdk.chain.NonceKeeper;
import org.cwv.client.sdk.model.ChainCallBack;
import org.cwv.client.sdk.model.ChainRequest;
import org.cwv.client.sdk.model.ICrypto;
import org.cwv.client.sdk.model.Model;
import org.cwv.client.sdk.util.TransactionBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 队列化请求器
 */
@Slf4j
public final class QueuedExecutor {
    
    private static BlockingDeque<Task> queue = new LinkedBlockingDeque<>();
    
    static {
        new Thread(()->{
            Random random = new Random();
            Thread.currentThread().setName("QueuedExecutor_Dispatch_Thread");
            log.info(Thread.currentThread().getName()+" is starting!");
                while (true){
                    ChainRequest req = null;
                    try {
                        Task t = queue.takeFirst();
                        log.debug("任务执行中...");
                        String address = t.getSt().getAddress();
                        //获取nonce
                        int nonce = NonceKeeper.getNonce(address);
                        //测试使用，概率性使用错误的nonce
//                        if(random.nextInt(10)<8){
//                            log.info("错误的nonce");
//                            nonce = 1;
//                        }
                        Model.SendTransaction st = t.getSt().toBuilder().setNonce(nonce).build();
//                        t.setSt(st);
                        //构建tx,加密
                        log.debug("任务准备签名");
                        String tx = TransactionBuilder.build(st,t.getICrypto());
                        //构建ChainRequest
                        req = RequestBuilder.buildTransactionReq(tx);
                        req.setCallback(t.getCbs());
                        //同步发送请求
                        log.debug("任务签名完成，网络发送中...");
                        String result = SingleThreadOKHttpExecutor.execute(req);
                        log.debug("网络请求完成");
                        log.info("请求nonce:"+nonce);
                        //获取结果，解析
                        if(result == null){//请求失败
                            req.error("未知异常",null);
                            continue;
                        }
                        if (result.indexOf("sender nonce is large than transaction nonce")>-1){
                            //如果是nonce问题，刷新nonce,重发一遍
                            log.info("请求nonce:{},请求异常，刷新nonce，重发请求。",nonce);
                            NonceKeeper.refreshNonce(address);
                            //放回队头，重试。
                            queue.putFirst(t);
                            continue;
                        }
                        //请求成功返回
                        log.debug("任务回调中...");
                        req.call(result);
                        log.debug("任务回调完成");
                    } catch (Exception e) {
                        log.error("任务执行异常",e);
                        if(req != null){
                            req.error("任务执行异常",e); 
                        }
                    }
                }
        }).start();
    }
    
//    public static void doExecute(int nonce,ChainRequest cr){
//        if(cr.isCallBackNull()){
//            throw new UnsupportedOperationException("队列化请求必须设置回调");
//        }
//        Task task = new Task(nonce,cr);
//        queue.offer(task,5, TimeUnit.SECONDS);//添加任务尝试等待5秒
//    }


    public static void init(){
        log.info("初始化...");
    }
    
    public static void doExecute(Model.SendTransaction st, ICrypto iCrypto, ChainCallBack cbs){
        if(cbs == null){
            throw new UnsupportedOperationException("队列化请求必须设置回调");
        }
        Task task = new Task(st,iCrypto,cbs);
        try {
            log.debug("任务提交中...");
            queue.putLast(task);
            log.debug("任务提交成功");
        } catch (InterruptedException e) {
            log.error("任务提交失败",e);
        }
    }
    
    
    @Data
    @AllArgsConstructor
    public static class Task{
        private Model.SendTransaction st;
        private ICrypto iCrypto;
        private ChainCallBack cbs;
    }
    
    
}
