package com.yjkj.sms.component;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yjkj.sms.common.SmsConstant;
import com.yjkj.sms.common.SmsConstant.Success;
import com.yjkj.sms.entity.SmsLogEntity;
import com.yjkj.sms.mapper.SmsLogMapper;

/**
 * 日志切面
 */
@Aspect
@Component
public class LogAspect {
	private static final ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
	
	@Autowired
	SmsLogMapper smsLogMapper;

	@Pointcut("execution(public * com.yjkj.sms.controller.SendSmsController.send*(..))")
	public void logPointcut() {
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Before("logPointcut()")
	public void before(JoinPoint joinPoint) throws Throwable {
		Map<String,String> params = (Map) joinPoint.getArgs()[0];
		
		SmsLogEntity LogEntity = new SmsLogEntity();
		LogEntity.setSendMethod(joinPoint.getSignature().getName());
		LogEntity.setTempId(params.get("tempId"));
		LogEntity.setParams(params.get("args"));
		LogEntity.setSendFrom(params.get("sendFrom"));
		LogEntity.setPhone(params.get("phone"));
		
		smsLogMapper.insertSmsLog(LogEntity);
		threadLocal.set(LogEntity.getId());
	}

	@AfterReturning(returning = "ret", pointcut = "logPointcut()")
	public void afterReturning(Object ret){
		
		SmsLogEntity LogEntity = new SmsLogEntity();
		LogEntity.setResult(ret+"");
		LogEntity.setId(threadLocal.get());
		
		if(ret != null) {
			JsonObject jsonObj = new Gson().fromJson(ret.toString(), JsonObject.class);
			if(SmsConstant.YzsCode.S0.code() == jsonObj.get("Status").getAsInt()) {
				LogEntity.setSuccess(Success.SUCCESS.code());
			}
		}
		smsLogMapper.updateSmsLog(LogEntity);
	}

	@AfterThrowing(throwing = "ex", pointcut = "logPointcut()")
	public void afterThrowing(Exception ex) {
		SmsLogEntity LogEntity = new SmsLogEntity();
		LogEntity.setResult(ex.getMessage());
		LogEntity.setId(threadLocal.get());
		LogEntity.setSuccess(Success.FAIL.code());
		smsLogMapper.updateSmsLog(LogEntity);
	}
	
}
