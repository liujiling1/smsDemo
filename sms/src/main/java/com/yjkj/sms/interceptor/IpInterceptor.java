package com.yjkj.sms.interceptor;

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
}
