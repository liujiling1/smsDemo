package com.yjkj.sms.util;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.yjkj.sms.util.ResponseMsg.ResponseStatusEnum;
import net.sf.json.JSONObject;  
/** 
 * @Description httpclient处理http请求和https请求 
 */  
public class HttpUtilAdd {
	
	// timeout in millis
	private final static int CONNECTION_TIMEOUT = 15 * 1000; 
	
	//charset
	private final static String CONNECTION_CHARSET = "utf-8"; 
	
	/** 
     * @Description 处理http请求的post方法 
     * @param url 
     *            :url 
     * @param params 
     *            :参数 
     * @return 返回的字符串 
     */  
    public static String post(String url, HashMap<String, String> params) throws IOException{  
        CloseableHttpClient httpClient = null;  
        HttpPost httpPost = null;  
        try {  
            httpClient = HttpClients.createDefault();  
            RequestConfig requestConfig = RequestConfig.custom()  
            		.setConnectionRequestTimeout(CONNECTION_TIMEOUT)
                    .setSocketTimeout(CONNECTION_TIMEOUT)
                    .setConnectTimeout(CONNECTION_TIMEOUT).build();  
            httpPost = new HttpPost(url);  
            httpPost.setConfig(requestConfig);  
            List<NameValuePair> ps = new ArrayList<NameValuePair>();  
            for (String pKey : params.keySet()) {  
                ps.add(new BasicNameValuePair(pKey, params.get(pKey)));  
            }  
            httpPost.setEntity(new UrlEncodedFormEntity(ps, CONNECTION_CHARSET));  
            CloseableHttpResponse response = httpClient.execute(httpPost);  
            HttpEntity httpEntity = response.getEntity();  
  
            return EntityUtils.toString(httpEntity, CONNECTION_CHARSET);  
  
        } catch (ClientProtocolException e) {  
        	throw new IOException("httpclient ClientProtocolException exception:" + e.getMessage());
        } catch (IOException e) {  
        	throw new IOException("httpclient IOException exception:" + e.getMessage());
        } finally {  
            try {  
                if (httpPost != null) {  
                    httpPost.releaseConnection();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
            	throw new IOException("httpclient IOException exception:" + e.getMessage());
            }  
        }  
    }  
    
    /** 
     * @Description 处理http请求的post方法 
     * @param url 
     *            :url 
     * @param params 
     *            :参数 
     * @return 返回的字符串 
     */  
    public static String post(String url, String params){  
        CloseableHttpClient httpClient = null;  
        HttpPost httpPost = null;  
        try {  
            httpClient = HttpClients.createDefault();  
            RequestConfig requestConfig = RequestConfig.custom()  
            		.setConnectionRequestTimeout(CONNECTION_TIMEOUT)
                    .setSocketTimeout(CONNECTION_TIMEOUT)
                    .setConnectTimeout(CONNECTION_TIMEOUT).build();  
            httpPost = new HttpPost(url);  
            httpPost.setConfig(requestConfig);  
           
            httpPost.setEntity(new StringEntity(params));  
            CloseableHttpResponse response = httpClient.execute(httpPost);  
            HttpEntity httpEntity = response.getEntity();  
  
            return EntityUtils.toString(httpEntity, CONNECTION_CHARSET);  
  
        } catch (ClientProtocolException e) {  
        	throw new ThrowAbleException(ResponseStatusEnum.SYS_INSIDEERROR,e);
        } catch (IOException e) {  
        	throw new ThrowAbleException(ResponseStatusEnum.SYS_INSIDEERROR,e);
        } finally {  
            try {  
                if (httpPost != null) {  
                    httpPost.releaseConnection();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
            	throw new ThrowAbleException(ResponseStatusEnum.SYS_INSIDEERROR,e);
            }  
        }  
    }  
  
    /** 
     * @Description 处理http请求的get方法 
     * @param url 
     *            :url 
     * @param params 
     *            :参数 
     * @return 返回的字符串 
     */  
    public static String get(String url, HashMap<String, String> params) throws IOException{  
        CloseableHttpClient httpClient = null;  
        HttpGet httpGet = null;  
  
        try {  
            httpClient = HttpClients.createDefault();  
            RequestConfig requestConfig = RequestConfig.custom()  
                    .setSocketTimeout(CONNECTION_TIMEOUT)
                    .setConnectTimeout(CONNECTION_TIMEOUT).build();  
            String ps = "";  
            for (String pKey : params.keySet()) {  
                if (!"".equals(ps)) {  
                    ps = ps + "&";  
                }  
                // 处理特殊字符，%替换成%25，空格替换为%20，#替换为%23  
                String pValue = params.get(pKey).replace("%", "%25")  
                        .replace(" ", "%20").replace("#", "%23");  
                ps += pKey + "=" + pValue;  
            }  
            if (!"".equals(ps)) {  
                url = url + "?" + ps;  
            }  
            httpGet = new HttpGet(url);  
            httpGet.setConfig(requestConfig);  
            CloseableHttpResponse response = httpClient.execute(httpGet);  
            HttpEntity httpEntity = response.getEntity();  
            return EntityUtils.toString(httpEntity, CONNECTION_CHARSET);  
        } catch (ClientProtocolException e) {  
        	throw new IOException("httpclient ClientProtocolException exception:" + e.getMessage());
        } catch (IOException e) {  
        	throw new IOException("httpclient IOException exception:" + e.getMessage());
        } catch (Exception e) {  
        	throw new IOException("httpclient other exception:" + e.getMessage());
        } finally {  
            try {  
                if (httpGet != null) {  
                    httpGet.releaseConnection();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
            	throw new IOException("httpclient IOException exception:" + e.getMessage());
            }  
        }  
    }  
  
    /** 
     * @Description 处理https请求的post方法 
     * @param url 
     *            :url 
     * @param params 
     *            :参数 
     * @return 返回的字符串 
     */  
    public static String postSSL(String url, HashMap<String, String> params) throws IOException{  
        CloseableHttpClient httpClient = null;  
        HttpPost httpPost = null;  
        try {  
            httpClient = (CloseableHttpClient) wrapClient();  
            RequestConfig requestConfig = RequestConfig.custom()  
                    .setSocketTimeout(CONNECTION_TIMEOUT)
                    .setConnectTimeout(CONNECTION_TIMEOUT).build();  
            httpPost = new HttpPost(url);  
            httpPost.setConfig(requestConfig);  
            List<NameValuePair> ps = new ArrayList<NameValuePair>();  
            for (String pKey : params.keySet()) {  
                ps.add(new BasicNameValuePair(pKey, params.get(pKey)));  
            }  
            httpPost.setEntity(new UrlEncodedFormEntity(ps, CONNECTION_CHARSET));  
            CloseableHttpResponse response = httpClient.execute(httpPost);  
            HttpEntity httpEntity = response.getEntity();  
  
            return EntityUtils.toString(httpEntity, CONNECTION_CHARSET);  
  
        } catch (ClientProtocolException e) {  
        	throw new IOException("httpclient ClientProtocolException exception:" + e.getMessage());
        } catch (IOException e) {  
        	throw new IOException("httpclient IOException exception:" + e.getMessage());
        } finally {  
            try {  
                if (httpPost != null) {  
                    httpPost.releaseConnection();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
            	throw new IOException("httpclient IOException exception:" + e.getMessage());
            }  
        }  
    }  
    
    
    /** 
     * @Description 处理https请求的post方法 
     * @param url 
     *            :url 
     * @param params 
     *            :参数 
     * @return 返回的字符串 
     */  
    public static String postSSLByJson(String url, HashMap<String, String> params) throws IOException{  
        CloseableHttpClient httpClient = null;  
        HttpPost httpPost = null;  
        try {  
            httpClient = (CloseableHttpClient) wrapClient();  
            RequestConfig requestConfig = RequestConfig.custom()  
                    .setSocketTimeout(CONNECTION_TIMEOUT)
                    .setConnectTimeout(CONNECTION_TIMEOUT).build();  
            httpPost = new HttpPost(url);  
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setConfig(requestConfig);  

            
            
            JSONObject jsonParam = new JSONObject();  
            for (String pKey : params.keySet()) {  
            	jsonParam.put(pKey, params.get(pKey));
            }  
            StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");
            entity.setContentEncoding("UTF-8");    
            
            httpPost.setEntity(entity);
            
            CloseableHttpResponse response = httpClient.execute(httpPost);  
            HttpEntity httpEntity = response.getEntity();  
  
            return EntityUtils.toString(httpEntity, CONNECTION_CHARSET);  
  
        } catch (ClientProtocolException e) {  
        	throw new IOException("httpclient ClientProtocolException exception:" + e.getMessage());
        } catch (IOException e) {  
        	throw new IOException("httpclient IOException exception:" + e.getMessage());
        } finally {  
            try {  
                if (httpPost != null) {  
                    httpPost.releaseConnection();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
            	throw new IOException("httpclient IOException exception:" + e.getMessage());
            }  
        }  
    }  
  
    /** 
     * @Description 处理https请求的get方法 
     * @param url 
     *            :url 
     * @param params 
     *            :参数 
     * @return 返回的字符串 
     */  
    public static String getSSL(String url, HashMap<String, String> params) throws IOException{  
        CloseableHttpClient httpClient = null;  
        HttpGet httpGet = null;  
  
        try {  
            httpClient = (CloseableHttpClient) wrapClient();  
            RequestConfig requestConfig = RequestConfig.custom()  
                    .setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build();  
            String ps = "";  
            for (String pKey : params.keySet()) {  
                if (!"".equals(ps)) {  
                    ps = ps + "&";  
                }  
                // 处理特殊字符，%替换成%25，空格替换为%20，#替换为%23  
                String pValue = params.get(pKey).replace("%", "%25")  
                        .replace(" ", "%20").replace("#", "%23");  
                ps += pKey + "=" + pValue;  
            }  
            if (!"".equals(ps)) {  
                url = url + "?" + ps;  
            }  
            httpGet = new HttpGet(url);  
            httpGet.setConfig(requestConfig);  
            CloseableHttpResponse response = httpClient.execute(httpGet);  
            HttpEntity httpEntity = response.getEntity();  
            return EntityUtils.toString(httpEntity, CONNECTION_CHARSET);  
        } catch (ClientProtocolException e) {  
        	throw new IOException("httpclient ClientProtocolException exception:" + e.getMessage());
        } catch (IOException e) {  
        	throw new IOException("httpclient IOException exception:" + e.getMessage());
        } catch (Exception e) {  
        	throw new IOException("httpclient other exception:" + e.getMessage());
        } finally {  
            try {  
                if (httpGet != null) {  
                    httpGet.releaseConnection();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
            	throw new IOException("httpclient IOException exception:" + e.getMessage());
            }  
        }  
    }  
  
    /** 
     * @Description 处理http请求的post方法（含有大数据的参数） 
     * @param url 
     *            :url 
     * @param params 
     *            :参数 
     * @return 返回的字符串 
     */  
    /*public static String postMultipart(String url, Map<String, String> params) {  
        CloseableHttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = "";  
        String fileName = "";  
        try {  
            httpClient = HttpClients.createDefault();  
            RequestConfig requestConfig = RequestConfig.custom()  
                    .setSocketTimeout(4000).setConnectTimeout(4000).build();  
            httpPost = new HttpPost(url);  
            httpPost.setConfig(requestConfig);  
  
            List<NameValuePair> ps = new ArrayList<NameValuePair>();  
            for (String pKey : params.keySet()) {  
                ps.add(new BasicNameValuePair(pKey, params.get(pKey)));  
            }  
  
            HashMap<String, File> files = new HashMap<String, File>();  
            fileName = ProArgs.WorkPath + params.get("FileName") + ".txt";  
            String content = params.get("Report");  
            FileIOMethod.SaveTextFile(fileName, content, "utf-8");  
            File file = new File(fileName);  
            files.put("Report", file);  
            HttpEntity entity = makeMultipartEntity(ps, files);  
            httpPost.setEntity(entity);  
            CloseableHttpResponse response = httpClient.execute(httpPost);  
            HttpEntity httpEntity = response.getEntity();  
            result = EntityUtils.toString(httpEntity, "utf-8");  
        } catch (ClientProtocolException e) {  
            result = "";  
        } catch (IOException e) {  
            result = "";  
        } finally {  
            FileIOMethod.DelFile(fileName);  
            try {  
                if (httpPost != null) {  
                    httpPost.releaseConnection();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
                result = "";  
            }  
        }  
        return result;  
    }  */
  
    /** 
     * @Description 处理https请求的post方法（含有大数据的参数） 
     * @param url 
     *            :url 
     * @param params 
     *            :参数 
     * @return 返回的字符串 
     */  
    /*public static String postMultipartSSL(String url,HashMap<String, String> params) {  
        CloseableHttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = "";  
        String fileName = "";  
        try {  
            httpClient = (CloseableHttpClient) wrapClient();  
            RequestConfig requestConfig = RequestConfig.custom()  
                    .setSocketTimeout(4000).setConnectTimeout(4000).build();  
            httpPost = new HttpPost(url);  
            httpPost.setConfig(requestConfig);  
  
            List<NameValuePair> ps = new ArrayList<NameValuePair>();  
            for (String pKey : params.keySet()) {  
                ps.add(new BasicNameValuePair(pKey, params.get(pKey)));  
            }  
  
            HashMap<String, File> files = new HashMap<String, File>();  
            fileName = ProArgs.WorkPath + params.get("FileName") + ".txt";  
            String content = params.get("Report");  
            FileIOMethod.SaveTextFile(fileName, content, "utf-8");  
            File file = new File(fileName);  
            files.put("Report", file);  
            HttpEntity entity = makeMultipartEntity(ps, files);  
            httpPost.setEntity(entity);  
            CloseableHttpResponse response = httpClient.execute(httpPost);  
            HttpEntity httpEntity = response.getEntity();  
            result = EntityUtils.toString(httpEntity, "utf-8");  
  
        } catch (ClientProtocolException e) {  
            result = "";  
        } catch (IOException e) {  
            result = "";  
        } finally {  
            FileIOMethod.DelFile(fileName);  
            try {  
                if (httpPost != null) {  
                    httpPost.releaseConnection();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
                result = "";  
            }  
        }  
        return result;  
    }  */
  
    /** 
     * @Description 创建一个不进行正式验证的请求客户端对象 不用导入SSL证书 
     * @return HttpClient 
     */  
    public static HttpClient wrapClient() {  
        try {  
            SSLContext ctx = SSLContext.getInstance("TLS");  
            X509TrustManager tm = new X509TrustManager() {  
                public X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }  
  
                public void checkClientTrusted(X509Certificate[] arg0,  
                        String arg1) throws CertificateException {  
                }  
  
                public void checkServerTrusted(X509Certificate[] arg0,  
                        String arg1) throws CertificateException {  
                }  
            };  
            ctx.init(null, new TrustManager[] { tm }, null);  
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(  
                    ctx, NoopHostnameVerifier.INSTANCE);  
            CloseableHttpClient httpclient = HttpClients.custom()  
                    .setSSLSocketFactory(ssf).build();  
            return httpclient;  
        } catch (Exception e) {  
            return HttpClients.createDefault();  
        }  
    }  
  
    /** 
     * @Description 创建一个HTTPEntity对象 
     * @param params 
     *            :字符串的入参 
     * @param files 
     *            :大数据的入参 
     * @return HttpClient 
     */  
    public static HttpEntity makeMultipartEntity(List<NameValuePair> params,  
            Map<String, File> files) {  
  
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();  
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); // 如果有SocketTimeoutException等情况，可修改这个枚举  
        if (params != null && params.size() > 0) {  
            for (NameValuePair p : params) {  
                builder.addTextBody(p.getName(), p.getValue(),  
                        ContentType.TEXT_PLAIN.withCharset("UTF-8"));  
            }  
        }  
  
        if (files != null && files.size() > 0) {  
            Set<Entry<String, File>> entries = files.entrySet();  
            for (Entry<String, File> entry : entries) {  
                builder.addPart(entry.getKey(), new FileBody(entry.getValue()));  
            }  
        }  
  
        return builder.build();  
  
    }  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
  
        String url = "";  
        HashMap<String, String> params = new HashMap<String, String>();  
        params.put("username", "123");  
        params.put("password", "123");  
        String a;
		try {
			a = postSSL(url, params);
			System.out.println("值为：" + a);  
		} catch (IOException e) {
			e.printStackTrace();
		}  
    }  
}
