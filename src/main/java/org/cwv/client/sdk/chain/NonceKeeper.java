package org.cwv.client.sdk.chain;

import com.alibaba.fastjson.JSON;
import org.cwv.client.sdk.https.OKHttpExecutor;
import org.cwv.client.sdk.https.RequestBuilder;
import org.cwv.client.sdk.model.ChainRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * nonce维护器。sdk使用前时初始化。获取nonce，串行加一。
 */
@Slf4j
public final class NonceKeeper {
    
//    private static AtomicInteger nonce = new AtomicInteger(0);
    
    private static ConcurrentHashMap<String,AtomicInteger> nonces = new ConcurrentHashMap<>();
    
    
    public static int getNonce(String addr){
        if(nonces.get(addr) == null){
            log.error("地址："+addr+" 的nonce不在本地缓存，重新拉取账户nonce");
            initNonce(addr);
        }
        return nonces.get(addr).getAndIncrement();
//        return nonce.getAndIncrement();
    }
    
    
    public static void refreshNonce(String address){
        log.info("address:"+address+" nonce is refreshing");
        try {
            ChainRequest cr = RequestBuilder.buildGetUserInfoReq(address);
            String result = OKHttpExecutor.execute(cr);
            int i = JSON.parseObject(result).getJSONObject("account").getJSONObject("value")
                    .getIntValue("nonce");
            AtomicInteger nonce = new AtomicInteger(i);
            nonces.put(address,nonce);//替换为新nonce
            log.info("address:"+address+" nonce now is:{}",i);
        } catch (Exception e){
            log.error("账户："+address+" nonce刷新失败");
        }
    }

    public static void initNonce(String address){
        int i = -1;
        nonces.put(address, new AtomicInteger(i));
        try {
            ChainRequest cr = RequestBuilder.buildGetUserInfoReq(address);
            String result = OKHttpExecutor.execute(cr);
            i = JSON.parseObject(result).getJSONObject("account").getJSONObject("value")
                    .getIntValue("nonce");

            //nonce.compareAndSet(0,i);

        } catch (Exception e){
            log.error("账户："+address+" 不存在，nonce默认为0");
            i=0;
        }finally {
            nonces.get(address).compareAndSet(-1,i);
        }
    }
    
    

    public static void main(String[] args) throws InterruptedException {

//        System.out.println("nonce is "+NonceKeeper.getNonce("8e37db7d580b84fc096ea04a1c047b48b07a8958"));
//        System.out.println("nonce is "+NonceKeeper.getNonce("8e37db7d580b84fc096ea04a1c047b48b07a8958"));

        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                int n = NonceKeeper.getNonce("d8cfb85da53678f348e30c1fc4f792a418a1f443");
                System.out.println(n);

            }).start();
        }

        Thread.sleep(3000);
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                int n = NonceKeeper.getNonce("d8cfb85da53678f348e30c1fc4f792a418a1f443");
                System.out.println(n);

            }).start();
        }
        
    }
}
