package com.yjkj.sms.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	
	private final static int CONNECTION_TIMEOUT = 2 * 1000; // timeout in millis

	public static List<NameValuePair>  parsePostParamsInList(String postQuery){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(postQuery!=null&&!"".equals(postQuery.trim())){

			String[] keyValues = postQuery.split("&");
			
			for(String keyvalue : keyValues){
				String[] temp = keyvalue.split("=");
				String key = temp[0];
				String value = null;
				if(temp.length > 1){
					value = temp[1];
				}
				params.add(new BasicNameValuePair(key, value));
			}
			return params;
		}
		return null;
	}
	
	public static Map<String,String>  parsePostParamsImMap(String postQuery){
		Map<String,String> map = new HashMap<String,String>();
		if(postQuery!=null&&!"".equals(postQuery.trim())){
			
			String[] keyValues = postQuery.split("&");
			
			for(String keyvalue : keyValues){
				String[] temp = keyvalue.split("=");
				String key = temp[0];
				String value = temp[1];
				map.put(key, value);
			}
			return map;
		}
		return null;
	}
	
	
	public static String doPost(List<NameValuePair> paramList, String url, String charset) throws IOException{
		
		if(StringTool.isEmpty(charset)) {
			charset = "UTF-8";
		}
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		/*if(null != charset && "" != charset){
			httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset);
		}*/
		
		RequestConfig requestConfig = RequestConfig.custom()
		    .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
		    .setConnectTimeout(CONNECTION_TIMEOUT)
		    .setSocketTimeout(CONNECTION_TIMEOUT)
		    .build();
		HttpPost httpPost = new HttpPost(url); 
		httpPost.setConfig(requestConfig);
      
        CloseableHttpResponse response  = null;
        
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(paramList, charset));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
        	throw new IOException("httpclient other exception:" + e.getMessage());
        }finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}
