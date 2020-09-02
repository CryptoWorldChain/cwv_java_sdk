package org.cwv.client.sdk.chain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.cwv.client.sdk.Config;
import org.cwv.client.sdk.https.OKHttpExecutor;
import org.cwv.client.sdk.https.RequestBuilder;
import org.cwv.client.sdk.model.ChainCallBack;
import org.cwv.client.sdk.model.ChainRequest;
import org.cwv.client.sdk.model.TxCallback;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 追踪合约执行情况。并通知。
 */
@Slf4j
public final class TxTracker {

    public static void main(String[] args) throws InterruptedException {
        TxTracker.add("7d203c900db8565c956cf453eee5f2e41958925e23a7f66c743809525ab0630f",
                new TxCallback() {
                    @Override
                    public void exceptionCall(String txhash, String message, String body) {
                        System.out.println(txhash +" is error!");
                    }

                    @Override
                    public void call(String txhash,String body) {
                        System.out.println(txhash +" is done!");
                    }
                });
        TxTracker.add("8d203c900db8565c956cf453eee5f2e41958925e23a7f66c743809525ab0630f",
                new TxCallback() {
                    @Override
                    public void exceptionCall(String txhash, String message, String body) {
                        System.out.println(txhash +" is error!");
                    }

                    @Override
                    public void call(String txhash,String body) {
                        System.out.println(txhash +" is done!");
                    }
                });
        TxTracker.add("9d203c900db8565c956cf453eee5f2e41958925e23a7f66c743809525ab0630f",
                new TxCallback() {
                    @Override
                    public void exceptionCall(String txhash, String message, String body) {
                        System.out.println(txhash +" is error!");
                    }

                    @Override
                    public void call(String txhash,String body) {
                        System.out.println(txhash +" is done!");
                    }
                });
        
        Thread.sleep(50000);
        TxTracker.add("9d203c900db8565c956cf453eee5f2e41958925e23a7f66c743809525ab0630f",
                new TxCallback() {
                    @Override
                    public void exceptionCall(String txhash, String message, String body) {
                        System.out.println(txhash +" is error!");
                    }

                    @Override
                    public void call(String txhash,String body) {
                        System.out.println(txhash +" is done!");
                    }
                });
    }

    //任务队列
    private static LinkedBlockingDeque<TxTask> queue = new LinkedBlockingDeque<>();

    static {

        /**
            * ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1);
        schedule.scheduleWithFixedDelay(() -> {
            Thread.currentThread().setName("Transaction_Tracker_TimerTask");
            try {
                log.debug(Thread.currentThread().getName()+" running");
                TxTask tt = queue.takeFirst();
                ChainRequest cr = builtReq(tt)
                        .setCallback(new ChainCallBack<String>() {
                    @Override
                    public void call(String body) {
                        
                        JSONObject bodyJ = JSON.parseObject(body);//可能失败
                        int retCode = bodyJ.getIntValue("retCode");
                        if(retCode != 1){//交易异常
//                            log.info("Task:"+tt.txHash+", info is not correct,please check it!");
//                            tt.tc.exceptionCall(tt.txHash,"交易结果异常",body);
                            log.info("Transaction:"+tt.txHash+",result is not found, Tracker will track it again!");
                            ++tt.retry;
                            try {
                                queue.putLast(tt);
                            } catch (InterruptedException e) {
                                //should not be here generally
                                log.error("TransactionTask enqueue fail!",e);
                                tt.tc.exceptionCall(tt.txHash,"交易任务排队失败",body);
                            }
                            return;
                        }

                        if(body !=null && body.indexOf("\"status\":") != -1){//交易完成
                            tt.tc.call(tt.txHash,body);
                            return;
                        }else{
                            try {
                                //做一些判断
                                if(tt.retry >= Config.TxTrackRetryTimes){
                                    log.info("Task:"+tt.txHash+", retries too many times,please check!");
                                    tt.tc.exceptionCall(tt.txHash,"查询重试次数过多",body);
                                    return;
                                }
                                ++tt.retry;
                                queue.putLast(tt);
                            } catch (InterruptedException e) {
                                //should not be here generally
                                log.error("TransactionTask enqueue fail!",e);
                                tt.tc.exceptionCall(tt.txHash,"交易任务排队失败",body);
                            }
                        }
                    }

                    @Override
                    public void error(String body,Exception e) {
                        log.error("http错误",body,e);
                        try {
                            ++tt.retry;
                            queue.putLast(tt);
                        } catch (InterruptedException ew) {
                            log.error("TransactionTask enqueue fail!",ew);
                        }
                    }
                });
                
                //do async request;
                OKHttpExecutor.asyncExecute(cr);
                
            } catch (Exception e) {
                log.error("DomainPool refresh task error",e);
            }

        }, 5, Config.HashTrackerIntervalTime, TimeUnit.MILLISECONDS);
        */
        
        new Thread(()->{
            Thread.currentThread().setName("Transaction_Tracker_TimerTask");
            try {
                while (true){
                    TxTask tt = queue.takeFirst();
                    if(tt.retry == 1){
                        //第一次查询，延长休眠，因为交易很可能没执行
                        Thread.sleep(Config.HashTrackerIntervalTime+400);
                    }
                    log.debug("find hash tracker task,hash:{}",tt.txHash);
                    //构造查询请求
                    ChainRequest cr = builtReq(tt)
                            .setCallback(new ChainCallBack<String>() {
                                @Override
                                public void call(String body) {

                                    JSONObject bodyJ = JSON.parseObject(body);//可能失败
                                    int retCode = bodyJ.getIntValue("retCode");
                                    if(retCode != 1){//交易异常
//                            log.info("Task:"+tt.txHash+", info is not correct,please check it!");
//                            tt.tc.exceptionCall(tt.txHash,"交易结果异常",body);
                                        log.info("Transaction:"+tt.txHash+",result is not found, Tracker will track it again!");
                                        ++tt.retry;
                                        try {
                                            Thread.sleep(Config.HashTrackerIntervalTime);
                                            queue.putLast(tt);
                                        } catch (InterruptedException e) {
                                            //should not be here generally
                                            log.error("TransactionTask enqueue fail!",e);
                                            tt.tc.exceptionCall(tt.txHash,"交易任务排队失败",body);
                                        }
                                        return;
                                    }

                                    if(body !=null && body.indexOf("\"status\":") != -1){//交易完成
                                        tt.tc.call(tt.txHash,body);
                                        return;
                                    }else{
                                        try {
                                            //做一些判断
                                            if(tt.retry >= Config.TxTrackRetryTimes){
                                                log.info("Task:"+tt.txHash+", retries too many times,please check!");
                                                tt.tc.exceptionCall(tt.txHash,"查询重试次数过多",body);
                                                return;
                                            }
                                            ++tt.retry;
                                            Thread.sleep(Config.HashTrackerIntervalTime);
                                            queue.putLast(tt);
                                            log.debug("交易结果未获取到，重新入队");
                                        } catch (InterruptedException e) {
                                            //should not be here generally
                                            log.error("TransactionTask enqueue fail!",e);
                                            tt.tc.exceptionCall(tt.txHash,"交易任务排队失败",body);
                                        }
                                    }
                                }

                                @Override
                                public void error(String body,Exception e) {
                                    log.error("http错误",body,e);
                                    try {
                                        ++tt.retry;
                                        Thread.sleep(Config.HashTrackerIntervalTime);
                                        queue.putLast(tt);
                                    } catch (InterruptedException ew) {
                                        log.error("TransactionTask enqueue fail!",ew);
                                    }
                                }
                            });

                    //do async request;
                    OKHttpExecutor.asyncExecute(cr);
                }
            } catch (Exception e) {
                log.error("DomainPool refresh task error",e);
            }
        }).start();
        
        
    }

    public static void init(){
        log.info("初始化...");
    }
    
    /**
     * 提交交易结果确认任务
     * @param txHash 交易哈希
     * @param tc 交易类型回调通知实现类
     */
    public static void add(String txHash, TxCallback tc) {
        TxTask tt = new TxTask(txHash, tc);
        queue.addLast(tt);//操作失败直接抛异常
    }
    
    public static ChainRequest builtReq(TxTask tt){
        return RequestBuilder.builtGetTxInfoReq(tt.getTxHash());
    }
    
    
    
    public static class TxTask{
        private String txHash;
        private TxCallback tc;
        private volatile int status = 1;
        private volatile int retry = 1;

        public TxTask(String txHash, TxCallback tc) {
            this.txHash = txHash;
            this.tc = tc;
        }

        public String getTxHash() {
            return txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }

        public TxCallback getCcb() {
            return tc;
        }

        public void setCcb(TxCallback tc) {
            this.tc = tc;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
    
    
}
