package org.cwv.client.sdk.https;

import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class NativeHttpsHandler {

    /**
     * 发送https请求共用体  
     */
    public static JSONObject sendPost(String url,String method, String parame, Map<String,Object> pmap) throws IOException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException{
        // 请求结果  
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        URL realUrl;
        HttpsURLConnection conn;
        //查询地址  
        String queryString = url;
        //请求参数获取  
        String postpar = "";
        //字符串请求参数  
        if(parame!=null){
            postpar = parame;
        }
        // map格式的请求参数  
        if(pmap!=null){
            StringBuffer mstr = new StringBuffer();
            for(String str:pmap.keySet()){
                String val = (String) pmap.get(str);
                try {
                    val= URLEncoder.encode(val,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                mstr.append(str+"="+val+"&");
            }
            // 最终参数  
            postpar = mstr.toString();
            int lasts=postpar.lastIndexOf("&");
            postpar=postpar.substring(0, lasts);
        }
        if(method.toUpperCase().equals("GET")){
            queryString+="?"+postpar;
        }
        SSLSocketFactory  ssf= OfficialX509TrustManager.getSSFactory();
        try {
            realUrl= new URL(queryString);
            conn = (HttpsURLConnection)realUrl.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if(method.toUpperCase().equals("POST")){
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                out = new PrintWriter(conn.getOutputStream());
                out.write(postpar);
                out.flush();
            }else{
                conn.connect();
            }
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println("result---"+result);
            json = JSONObject.parseObject(result);
        }finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return json;
    }

    
    public static String post(){
        return "";
    }




    private static class OfficialX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            
            for(X509Certificate xc : x509Certificates){
                System.out.println(xc);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public static SSLSocketFactory getSSFactory() throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
            TrustManager[] tm = { new OfficialX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            return  ssf;
        }
    }
    
}
