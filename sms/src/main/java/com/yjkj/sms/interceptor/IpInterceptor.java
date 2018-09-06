package com.yjkj.sms.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.yjkj.sms.util.IPUtil;

import net.sf.json.JSONObject;

@Component
@PropertySource("classpath:sms.properties")
public class IpInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(IpInterceptor.class);
	
	@Value( "${auth.white.ips}" ) 
	private String ips;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("=====IppreHandle=====" + request.getRequestURI());
		if(StringUtils.isBlank(ips)){
    		return true;
    	}
    	String ip = IPUtil.getUsrIPAddr(request);
		if(ip == null){
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}
		String[] ipArray = ips.split(",");
		for (String str : ipArray) {
			if(str.equals(ip)){
				return true;
			}
		}
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
		return false;
	}

	/** 
	 * 以JSON格式输出 
	 * @param response 
	 */  
	protected void responseOutWithJson(HttpServletResponse response,  
	        Object responseObject) {  
	    //将实体对象转换为JSON Object转换  
	    JSONObject responseJSONObject = JSONObject.fromObject(responseObject);  
	    response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try {  
	        out = response.getWriter();  
	        out.append(responseJSONObject.toString());  
	        //logger.debug("返回是\n");  
	        //logger.debug(responseJSONObject.toString());  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }  
	    }  
	} 
}
