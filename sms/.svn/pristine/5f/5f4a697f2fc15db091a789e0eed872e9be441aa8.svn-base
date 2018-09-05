package com.yjkj.sms.component;

import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.yjkj.sms.common.SmsError;
import com.yjkj.sms.entity.SmsReource;
import com.yjkj.sms.entity.SmsReource.SmsTemp;
import com.yjkj.sms.util.SmsException;
import com.yjkj.sms.util.SmsUtil;
import com.yjkj.util.common.Base64Util;
import com.yjkj.util.common.HttpUtilAdd;
import com.yjkj.util.common.StringTool;

@Component
public class SmsService{
	private static final Logger logger = LoggerFactory.getLogger(SmsService.class);
	private static final String C = "%C";
	@Autowired
	SmsReource smsReource;
	
	/**
	 * 发送短信（指定模板）
	 * @param phone 手机号
	 * @param smsTemp 短信模板
	 * @param code 短信验证码 没有则传空
	 * @param args 模板参数
	 */
	public String sendSmsByTempId(String phone, SmsTemp smsTemp,String code, Object... args) throws SmsException, IOException{
		if(StringUtils.isEmpty(code)) {
			return sendSMS(phone, smsTemp.getSmsContent(args));
		}else {
			return sendSMS(phone, smsTemp.getSmsContent(smsTemp.getTemp().replaceAll(C, code), args));
		}
	}
	
	private String sendSMS(String phone, String content) throws IOException, SmsException {
		logger.debug("phone={},content={}",phone,content);
		if(StringTool.isEmpty(content)) {
			throw new SmsException(SmsError.E_S006);
		}
		
		SmsUtil.chkphone(phone);
		
		HashMap<String, String> params = smsReource.getParams();
		params.put("message", Base64Util.encodeStr(content,"UTF-8"));
		params.put("mobile", phone);
		return HttpUtilAdd.postSSLByJson(smsReource.getUrl(), params);
	}
	
}