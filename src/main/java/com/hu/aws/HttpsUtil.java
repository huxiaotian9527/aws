package com.hu.aws;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @Author hutiantian
 * @Date 2018/12/24 13:37:15
 */
public class HttpsUtil {

    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String UTF8 = "utf-8";

    private static HttpsURLConnection getHttpsURLConnection(String uri, String method) throws IOException {
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager()}, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SSLSocketFactory ssf = ctx.getSocketFactory();
        URL url = new URL(uri);
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
        httpsConn.setSSLSocketFactory(ssf);
        httpsConn.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        httpsConn.setRequestMethod(method);
        httpsConn.setDoInput(true);
        httpsConn.setDoOutput(true);
        return httpsConn;
    }

    /**
     * 默认utf-8编码
     */
    public static String doPost(String url,String jsonData, Map<String,String> header) throws Exception{
        return doPost(url,UTF8,jsonData,header);
    }

    /**
     * https发送post请求
     * @param url 请求路径
     * @param encoding 编码
     * @param jsonData body的数据
     * @param header 头部数据
     * @return response的body
     */
    public static String doPost(String url,String encoding,String jsonData, Map<String,String> header) throws Exception{
        HttpsURLConnection httpsConn = getHttpsURLConnection(url, POST);
        //设置报文头
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpsConn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        OutputStream os = httpsConn.getOutputStream();
        os.write(jsonData.getBytes(encoding));
        os.flush();
        os.close();
        InputStream is;
        //返回结果不是200取错误流
        if(httpsConn.getResponseCode()==httpsConn.HTTP_OK||
                httpsConn.getResponseCode()==httpsConn.HTTP_CREATED||httpsConn.getResponseCode()==httpsConn.HTTP_ACCEPTED ){
            is = httpsConn.getInputStream();
        }else {
            is = httpsConn.getErrorStream();
        }
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        while ((len = is.read(bytes)) != -1) {
            bao.write(bytes, 0, len);
        }
        is.close();
        return new String(bao.toByteArray(), encoding);
    }

    /**
     * get请求
     */
    public static String doGet(String url,Map<String,String> header) throws Exception{
        HttpsURLConnection httpsConn = getHttpsURLConnection(url, GET);
        //设置报文头
        for(Map.Entry<String,String> entry:header.entrySet()){
            httpsConn.setRequestProperty(entry.getKey(),entry.getValue());
        }
        httpsConn.setConnectTimeout(10000);
        httpsConn.setReadTimeout(10000);
        InputStream is = httpsConn.getInputStream();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        while ((len = is.read(bytes)) != -1) {
            bao.write(bytes, 0, len);
        }
        is.close();
        return new String(bao.toByteArray(),UTF8);
    }

    private static final class DefaultTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        public X509Certificate[] getAcceptedIssuers() {return null; }

    }
}
