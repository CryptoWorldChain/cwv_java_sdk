package org.cwv.client.sdk;

public class HiChainTest {

  public void config(){
      /**主链地址*/
      Config.host = "";
      /**交易hash查询间隔，毫秒数*/
      Config.HashTrackerIntervalTime = 300;
      /**交易hash查询重试次数*/
      Config.TxTrackRetryTimes = 10;
      /**是否使用多队列交易执行器，默认不启用*/
      Config.isMultiQueueExecUsed = false;
      /**交易并行队列数目，isMultiQueueExecUsed=true时才有用*/
      Config.MultiExecQueueNum = 20;
      /**是否开启串行化交易发送，即单个账户并发发起交易，默认不启用*/
      Config.isSerialSendTx = false;
      
      HiChain.init("address");
      
  }
  
    
    
}