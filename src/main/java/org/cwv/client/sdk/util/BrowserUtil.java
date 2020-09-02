package org.cwv.client.sdk.util;

import org.cwv.client.sdk.https.SSLSocketClient;
import org.cwv.client.sdk.model.ChainCallBack;
import org.cwv.client.sdk.model.ChainException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 调用区块链浏览器接口的工具方法
 */@Slf4j
public class BrowserUtil {
    
    public static final String browser_host = "http://47.103.27.196:38002/fbs/cwvrest/";

    private static  final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .writeTimeout(30,TimeUnit.SECONDS)
//            .dispatcher(new Dispatcher().)
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),(X509TrustManager)SSLSocketClient.getTrustManager()[0])
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .retryOnConnectionFailure(false).build();
    
    public static String get(String tableName,String query){

        String result = "";
        Request request = new Request.Builder()
                .url(browser_host+tableName+"?"+query)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("url:"+request.url()+";response code:"+response.code());
                throw new RuntimeException("Unexpected code " + response);
            }
            result = response.body().string();
        } catch (Exception e) {
            log.error("请求失败，url:"+request.url(),e);
            throw new RuntimeException(e);
        }
        return result;
        
    }
    
    public static void asyncGet(String tableName, String query, ChainCallBack cb){
        if(cb == null){throw new RuntimeException("ChainRequest.CallBack must not be null!");}

        Request request = new Request.Builder()
                .url(browser_host+tableName+"?"+query)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                log.error("请求失败，url:"+request.url(),e);
                cb.error(request.url().toString(),e);
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        cb.error(responseBody.string(),new ChainException("响应错误"));
                    }
                    String result = responseBody.string();
                    cb.call(result);
                }catch (Exception e){
                    log.error("请求失败，url:"+request.url(),e);
                    cb.error(response.body().string(),e);
                }
            }
        });
    }

    public static void main(String[] args) {
        
//        String s=  get("mainblock","query={\"height\":{\"$lt\":300}}");
//        System.out.println(s);

        asyncGet("mainblock", "query={\"height\":{\"$lt\":300}}", new ChainCallBack<String>() {
            @Override
            public void call(String body) {
                System.out.println(body);
            }

            @Override
            public void error(String body, Exception e) {
                System.err.println(body);
            }
        });

    }
    
}
