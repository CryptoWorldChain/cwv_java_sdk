package org.cwv.client.sdk.https;

import org.cwv.client.sdk.model.ChainException;
import org.cwv.client.sdk.model.ChainRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 单线程http执行器
 */
@Slf4j
public final class SingleThreadOKHttpExecutor {

    private static  final OkHttpClient client;

    static {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(1);
        dispatcher.setMaxRequests(1);

        client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .dispatcher(dispatcher)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),(X509TrustManager)SSLSocketClient.getTrustManager()[0])
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .retryOnConnectionFailure(false).build();

    }
    
    public static void init(){
        log.info("初始化...");
        execute(RequestBuilder.buildGetLastedBlock());
    }

    public static String execute(ChainRequest cr){
        if(cr == null){throw new RuntimeException("ChainRequest must not be null!");}

        String result = "";
        Request request = new Request.Builder()
                .url(cr.getUrl())
                .post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8")
                        ,cr.getBody()))
                .build();

        log.debug("\r\nrequest url -----"+
                cr.getUrl());
        log.debug("\r\nrequest body -----"+
                cr.getBody());

        try (Response response = client.newCall(request).execute()) {
            log.debug("请求返回，URL："+request.url()+",\tbody:"+request.body().toString());
            if (!response.isSuccessful()) {
                log.error("url:"+request.url()+";response code:"+response.code());
                return null;
            }
            result = response.body().string();
            log.debug("result---"+result);
        } catch (Exception e) {
            DomainPool.remove("");//todo
            log.error("请求失败，url:"+request.url(),e);
            return null;
        }
        return result;
    }


    public static void asyncExecute(ChainRequest cr){
        if(cr == null){throw new RuntimeException("ChainRequest must not be null!");}
        if(cr.isCallBackNull()){throw new RuntimeException("ChainRequest.CallBack must not be null!");}

        log.debug("\r\nrequest url -----"+
                cr.getUrl());
        log.debug("\r\nrequest body -----"+
                cr.getBody());

        Request request = new Request.Builder()
                .url(cr.getUrl())
                .post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8")
                        ,cr.getBody()))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                DomainPool.remove("");//todo
                log.error("请求失败，url:"+request.url(),e);
                cr.error(cr.getBody(),e);
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                log.debug("请求返回，URL："+call.request().url()+",\tbody:"+call.request().body().toString());
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        cr.error(responseBody.string(),new ChainException("响应错误"));
                    }
                    String result = responseBody.string();
                    cr.call(result);
                }catch (Exception e){
                    log.error("请求失败，url:"+request.url(),e);
                    cr.error(response.body().string(),e);
                }
            }
        });
    }
    
    
    
}
