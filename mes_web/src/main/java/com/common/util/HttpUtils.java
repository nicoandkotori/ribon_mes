package com.common.util;

import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

/**
 * httpClient操作
 * Created by sunyin on 2018/2/24.
 */
public class HttpUtils {
    private static CloseableHttpClient httpClientBuilder=null;
    private static Logger logger = Logger.getLogger(HttpUtils.class);

    /**
     *    http 和 https
     * @param useProxy 是否使用代理
     * @param needCert 是否需要证书
     * @return
     */
    private static CloseableHttpClient createSSLClientDefault(boolean useProxy,boolean needCert) {
        SSLConnectionSocketFactory sslsf = null;
        try {
            if(needCert){
                InputStream instream = new FileInputStream(new File("D:/javatest/cert/server.p12"));
                InputStream instream1 = new FileInputStream(new File("D:/javatest/cert/server.jks"));
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                KeyStore trustStore = KeyStore.getInstance("JKS");
                try {
                    //设置客户端证书
                    keyStore.load(instream, "123456".toCharArray());
                    //设置服务器证书
                    trustStore.load(instream1, "123456".toCharArray());
                } catch (Exception e) {
                    logger.error("导入证书错误" + e);
                } finally {
                    if (instream != null) {
                        instream.close();
                    }
                    if (instream1 != null) {
                        instream1.close();
                    }
                }
                SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore).loadKeyMaterial(keyStore, "123456".toCharArray()).build();
                sslsf = new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            }else{
                SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                        return true;
                    }
                }).build();
                sslsf = new SSLConnectionSocketFactory(sslContext,new String[]{"TLSv1.2"}, null,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            }
            if(useProxy){
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                AuthScope authScope = new AuthScope(PropertiesUtil.properties.getProperty("proxy_host"), Integer.parseInt(PropertiesUtil.properties.getProperty("proxy_port")));
                Credentials credentials = new UsernamePasswordCredentials(PropertiesUtil.properties.getProperty("proxy_user"), PropertiesUtil.properties.getProperty("proxy_password"));
                credsProvider.setCredentials(authScope, credentials);
                httpClientBuilder= HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCredentialsProvider(credsProvider).build();
            }else{
                httpClientBuilder= HttpClients.custom().setSSLSocketFactory(sslsf).build();
            }
            return httpClientBuilder;
        } catch (Exception e) {
            logger.error("创建https导入证书错误"+e);
        }
        return HttpClients.createDefault();
    }

    /**
     *
     * @param url 请求地址
     * @param map 请求参数
     * @param res 返回结果
     * @param timeOut 超时时间(min)
     * @param useProxy 是否使用代理
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String,String> map, String res, int timeOut, boolean useProxy, boolean needCert) throws Exception {
        RequestConfig config = null;
        CloseableHttpResponse response=null;
        if(httpClientBuilder==null){
            httpClientBuilder= HttpUtils.createSSLClientDefault(useProxy,needCert);
        }

        URIBuilder uriBuilder=new URIBuilder(url);
        if(map != null){
            for (Entry<String, String> entry : map.entrySet()) {
                uriBuilder = uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        URI uri=uriBuilder.build();
        HttpGet httpGet=new HttpGet(uri);
        try {
            if(useProxy){
                HttpHost proxy = new HttpHost(PropertiesUtil.properties.getProperty("proxy_host"), Integer.parseInt(PropertiesUtil.properties.getProperty("proxy_port")),"http");
                config = RequestConfig.custom().setProxy(proxy).setConnectTimeout(timeOut * 1000 * 60).build();
            }else{
                config = RequestConfig.custom().setConnectTimeout(timeOut * 1000 * 60).build();
            }

            httpGet.setConfig(config);
            logger.info("执行get请求" + httpGet.getRequestLine());
            response = httpClientBuilder.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                logger.info("响应状态:"+ response.getStatusLine());
                String rStr= EntityUtils.toString(entity,"UTF-8");
                logger.info("响应内容:" + rStr);
                if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){
                    res=rStr;
                }
                EntityUtils.consume(entity);
            }
        }catch (Exception e) {
            logger.info("http请求错误"+e);
            throw e;
        }finally{
            if(httpGet!=null){
                httpGet.releaseConnection();
            }
            if(response!=null){
                response.close();
            }
        }
        return res;
    }

    private static String entityToString(HttpEntity entity) throws IOException {
        String result = null;
        if (entity != null) {
            long lenth = entity.getContentLength();
            if (lenth != -1 && lenth < 2048) {
                result = EntityUtils.toString(entity, "UTF-8");
            } else {
                InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
                CharArrayBuffer buffer = new CharArrayBuffer(2048);
                char[] tmp = new char[1024];
                int l;
                while ((l = reader1.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                result = buffer.toString();
            }
        }
        return result;
    }
    /**
     * post请求，参数为json字符串
     *
     * @param url        请求地址
     * @param jsonString json字符串
     * @return 响应
     */
    public static String postJson(String url, String jsonString, int timeOut, boolean useProxy, boolean needCert) throws IOException {
        RequestConfig config = null;
        CloseableHttpResponse response = null;
        if (httpClientBuilder == null) {
            httpClientBuilder = HttpUtils.createSSLClientDefault(useProxy, needCert);
        }

        HttpPost httpPost = new HttpPost(url);
        if (useProxy) {
            HttpHost proxy = new HttpHost(PropertiesUtil.properties.getProperty("proxy_host"), Integer.parseInt(PropertiesUtil.properties.getProperty("proxy_port")), "http");
            config = RequestConfig.custom().setProxy(proxy).setConnectTimeout(timeOut * 1000 * 60).build();
        } else {
            config = RequestConfig.custom().setConnectTimeout(timeOut * 1000 * 60).build();
        }
        httpPost.setConfig(config);
        httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
        // 设置类型
        httpPost.setEntity(new ByteArrayEntity(jsonString.getBytes("UTF-8")));
        String res = null;
        try {
            response = httpClientBuilder.execute(httpPost);
            if (302 == response.getStatusLine().getStatusCode()) {
                logger.info(response.getLastHeader("Location").getValue());
                postJson(response.getLastHeader("Location").getValue(), jsonString, timeOut, useProxy, needCert);
            }
            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity, "UTF-8");
            logger.info(res);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            logger.info("请求异常" + e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return res;
    }

    /**
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param res 返回结果
     * @param timeOut 超时时间(min)
     * @param useProxy 是否使用代理
     * @param needCert 是否使用证书
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "deprecation", "unused" })
    public static String post(String url, List<NameValuePair> params, String res, int timeOut, boolean useProxy, boolean needCert) throws Exception {
        RequestConfig config = null;
        CloseableHttpResponse response = null;
        if (httpClientBuilder == null) {
            httpClientBuilder = HttpUtils.createSSLClientDefault(useProxy, needCert);
        }

        HttpPost httpPost = new HttpPost(url);
        if (useProxy) {
            HttpHost proxy = new HttpHost(PropertiesUtil.properties.getProperty("proxy_host"), Integer.parseInt(PropertiesUtil.properties.getProperty("proxy_port")), "http");
            config = RequestConfig.custom().setProxy(proxy).setConnectTimeout(timeOut * 1000 * 60).build();
        } else {
            config = RequestConfig.custom().setConnectTimeout(timeOut * 1000 * 60).build();
        }
        httpPost.setConfig(config);
        // 设置类型
        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        try {
            response = httpClientBuilder.execute(httpPost);
            if (302 == response.getStatusLine().getStatusCode()) {
                logger.info(response.getLastHeader("Location").getValue());
                post(response.getLastHeader("Location").getValue(), params, res, timeOut, useProxy, needCert);
            }
            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity, "UTF-8");
            logger.info(res);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            logger.info("请求异常" + e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return res;
    }



    // http  post请求，  参数是 map
    public static String doPost(String url, Map<String, Object> paramsMap) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        //配置连接超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)
                .build();
        HttpPost httpPost = new HttpPost(url);
        //设置超时时间
        httpPost.setConfig(requestConfig);

        //装配post请求参数
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (String key : paramsMap.keySet()) {
            list.add(new BasicNameValuePair(key, String.valueOf(paramsMap.get(key))));
        }

        try {
            //将参数进行编码为合适的格式,如将键值对编码为param1=value1&param2=value2
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "utf-8");
            httpPost.setEntity(urlEncodedFormEntity);

            //执行 post请求
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
            String strRequest = "";
            if (null != closeableHttpResponse && !"".equals(closeableHttpResponse)) {
                System.out.println(closeableHttpResponse.getStatusLine().getStatusCode());
                if (closeableHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity httpEntity = closeableHttpResponse.getEntity();
                    strRequest = EntityUtils.toString(httpEntity);
                } else {
                    strRequest = "Error Response" + closeableHttpResponse.getStatusLine().getStatusCode();
                }
            }
            return strRequest;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return "协议异常";
        } catch (ParseException e) {
            e.printStackTrace();
            return "解析异常";
        } catch (IOException e) {
            e.printStackTrace();
            return "传输异常";
        } finally {
            try {
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
