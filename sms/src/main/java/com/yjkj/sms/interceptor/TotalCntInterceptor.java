package com.yjkj.sms.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.yjkj.sms.common.SmsError;

import net.sf.json.JSONObject;

/**
 * 验证当日短信发送最大次数
 *
 */
@Component
@PropertySource("classpath:sms.properties")
public class TotalCntInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(TotalCntInterceptor.class);
	private static final String SMS_DAY_CNT = "sms_day_cnt_";
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Value( "${auth.dayMaxCnt}" ) 
	private String dayMaxCnt;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("=====TotalCntInterceptor=====" + request.getRequestURI());
		
		long dayCnt = redisTemplate.opsForValue().increment(SMS_DAY_CNT + DateFormatUtils.format(new Date(), "yyyyMMdd"), 1);
		if (dayCnt > Long.parseLong(dayMaxCnt)){
			Map<String, String> res = new HashMap<String, String>();
			res.put("msg", SmsError.E_DAY_MAX_CNT.getError());
			
//			responseOutWithJson(response,res);
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}
		return true;
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
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }  
	    }  
	} 
}
