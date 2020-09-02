package org.cwv.client.sdk.okhttp3;

import org.cwv.client.sdk.https.OKHttpExecutor;
import org.cwv.client.sdk.https.RequestBuilder;
import org.cwv.client.sdk.model.ChainRequest;
import org.junit.Test;

public class OKHttpExecutorTest {

    @Test
    public void execute() {

        ChainRequest cr = RequestBuilder.buildTestReq();

        String result = OKHttpExecutor.execute(cr);

        System.out.println(result);
    }

    @Test
    public void asyncExecute() throws InterruptedException {
//        ChainRequest cr = RequestBuilder.buildTestReq().setCallback((body) -> {
//            System.out.println("this is callback!");
//            System.out.println("body--"+body);
//        });
//        
//        OKHttpExecutor.asyncExecute(cr);
//        int i=0;
//        while (i<10){
//            Thread.sleep(1000);
//            i++;
//        }
//        
        
    }
    
    
    
}