package com.yjkj.sms.component;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.yjkj.sms.common.SmsError;
import com.yjkj.sms.util.ResponseMsg;
import com.yjkj.sms.util.ResponseMsg.ResponseStatusEnum;
import com.yjkj.sms.util.SmsException;
import com.yjkj.sms.common.SmsError;
import com.yjkj.sms.util.SmsException;

/**
 * @Description: 全局异常处理
 * @ClassName:GlobalExceptionHandler
 * @author:Admin
 * @date:2017/7/6 16:18
 */
@ControllerAdvice
public class SmsExceptionAdvice extends ResponseEntityExceptionHandler {

	Logger logger = LoggerFactory.getLogger(SmsExceptionAdvice.class);
	
	public final static String EX_HEAD_NAME = "yj_head_ex";
	public final static String EX_HEAD_CODE = "yj_head_code";
	public final static String EX_HEAD_DATA = "yj_head_data";

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map<String, Object> jsonErrorHandler(HttpServletRequest req,HttpServletResponse res, Exception e) throws Exception {

		Map<String, Object> map = new HashMap<>();
		res.setHeader(EX_HEAD_NAME, "true");

		String errCode;
		String error;
		if (e instanceof SmsException) {
			SmsException ex = (SmsException) e;
			errCode = ex.getErrCode();
			error = ex.getMessage();
			
		} else {
			logger.error("未捕获异常>>>",e);
			errCode = SmsError.E_S000.name();
			error = SmsError.E_S000.getError();
		}
		res.setHeader(EX_HEAD_CODE, errCode);
		res.setHeader(EX_HEAD_DATA, error);
		map.put("errCode", errCode);
		map.put("error", error);
		return ResponseMsg.getInstance(ResponseStatusEnum.SYS_INSIDEERROR, map);
	}

}
