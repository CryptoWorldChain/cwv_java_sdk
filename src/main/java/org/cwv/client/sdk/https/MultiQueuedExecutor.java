package org.cwv.client.sdk.https;

import org.cwv.client.sdk.Config;
import org.cwv.client.sdk.chain.NonceKeeper;
import org.cwv.client.sdk.https.QueuedExecutor.Task;
import org.cwv.client.sdk.model.ChainCallBack;
import org.cwv.client.sdk.model.ChainRequest;
import org.cwv.client.sdk.model.ICrypto;
import org.cwv.client.sdk.model.Model;
import org.cwv.client.sdk.util.LocalCrypto;
import org.cwv.client.sdk.util.TransactionBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 多核队列化请求器
 */
@Slf4j
public final class MultiQueuedExecutor {

    private static Object checkAndBindLock = new Object();
    //地址绑定核心
    private static ConcurrentHashMap<String,ExecutorCore> coreMap =
            new ConcurrentHashMap<>();
    
    private static LinkedList<ExecutorCore> cores = new LinkedList<>();
    

    static{
        for (int i = 0; i < Config.MultiExecQueueNum; i++) {
            ExecutorCore core = new ExecutorCore();
            cores.addLast(core);
            new Thread(core,"TxExecutor_"+i).start();
        }
    }
    
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
            String addr = st.getAddress();
            ExecutorCore core = coreMap.get(addr);
            if(core != null){
                core.enqueue(task);
            }else{
                log.debug("加入新地址:{},绑定核心中...",addr);
                bindCore(addr);
                log.debug("新地址:{},绑定成功",addr);
                doExecute(st,iCrypto,cbs);//添加任务到队列
            }
            log.debug("任务提交成功");
        } catch (Exception e) {
            log.error("任务提交失败",e);
        }
    }
    
    
    private static void bindCore(String addr){
        synchronized (checkAndBindLock){
            ExecutorCore core = coreMap.get(addr);
            if(core == null){
                //找一个核心
                ExecutorCore lastCore = cores.removeLast();
                //绑定
                coreMap.put(addr,lastCore);
                cores.addFirst(lastCore);
            }
        }
    }


    public static class ExecutorCore implements Runnable{
        private BlockingDeque<Task> queue = new LinkedBlockingDeque<>();

        public void enqueue(Task task){
            try {
                log.debug("任务入队中...");
                queue.putLast(task);
                log.debug("任务入队成功");
            } catch (Exception e) {
                log.error("任务入队提交失败",e);
            }
        }
        
        public void run(){

            while (true){
                ChainRequest req = null;
                try {
                    Task t = queue.takeFirst();
                    String address = t.getSt().getAddress();
                    log.debug("地址:{}，任务执行中...",address);

//                    for (int i = 0; i < 10; i++) {
//                        log.info("地址:{},任务执行中...",address);
//                        Thread.sleep(700);
//                    }
//                    log.debug("地址:{}，任务执行中完成",address);
//                    if(1==1){continue;}
                    
                    //获取nonce
                    int nonce = NonceKeeper.getNonce(address);
                    //测试使用，概率性使用错误的nonce
//                        if(random.nextInt(10)<8){
//                            log.info("错误的nonce");
//                            nonce = 1;
//                        }
                    Model.SendTransaction st = t.getSt().toBuilder().setNonce(nonce).build();
                    //构建tx,加密
                    log.debug("任务准备签名");
                    String tx = TransactionBuilder.build(st,t.getICrypto());
                    //构建ChainRequest
                    req = RequestBuilder.buildTransactionReq(tx);
                    req.setCallback(t.getCbs());
                    //同步发送请求
                    log.debug("任务签名完成，网络发送中...");
                    String result = OKHttpExecutor.execute(req);
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
        }
    }
    
    
    
    
    
    
   

    public static void main(String[] args) throws InterruptedException {

        MultiQueuedExecutor.init();

        log.info("任务提交...");

        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(()->{
                for (int j = 0; j < 20; j++) {
                    Model.SendTransaction st = Model.SendTransaction.newBuilder()
                            .setAddress("addr_"+ finalI +"_" +j).build();

                    MultiQueuedExecutor.doExecute(st, LocalCrypto.getInstance(), new ChainCallBack<String>() {
                        @Override
                        public void call(String body) {
                            log.info("执行成功，addr:{}",st.getAddress());
                        }

                        @Override
                        public void error(String body, Exception e) {
                            log.info("执行失败，addr:{}",st.getAddress());
                        }
                    });
                }
            }).start();
        }
        
        log.info("任务提交完成");


    }
    
    
}
