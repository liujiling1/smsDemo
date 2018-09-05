package com.yjkj.sms.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	@Autowired
	IpInterceptor ipInterceptor;
	
	@Autowired
	TotalCntInterceptor totalCntInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> patterns = new ArrayList<>();
		patterns.add("/yjkj/sms/inner/send*");
		
		registry.addInterceptor(ipInterceptor).addPathPatterns(patterns);
		registry.addInterceptor(totalCntInterceptor).addPathPatterns(patterns);
	}
}
