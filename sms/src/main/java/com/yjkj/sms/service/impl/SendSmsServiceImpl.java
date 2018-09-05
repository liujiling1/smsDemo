package com.yjkj.sms.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yjkj.sms.component.SmsService;
import com.yjkj.sms.component.ValidCodeService;
import com.yjkj.sms.entity.SmsReource;
import com.yjkj.sms.service.SendSmsService;
import com.yjkj.sms.util.SmsUtil;
import com.yjkj.util.common.HttpUtilAdd;

@Service
public class SendSmsServiceImpl implements SendSmsService{

	@Autowired
	ValidCodeService validCodeService;

	@Autowired
	SmsReource smsReource;

	@Autowired
	SmsService smsService;

	public String sendSms(Map<String, String> param) throws Exception {
		String phone = param.get("phone");
		String tempId = param.get("tempId");
		String arg;
		Object[] args=null;
		if(StringUtils.isNotEmpty(arg = param.get("args"))) {
			args = arg.split(",");
		}

		return smsService.sendSmsByTempId(phone, smsReource.getSmsTemp(tempId), null, args);
	}

	public String sendVcode(Map<String, String> param) throws Exception {
		String phone = param.get("phone");
		String tempId = param.get("tempId");
		
		String arg;
		Object[] args=null;
		if(StringUtils.isNotEmpty(arg = param.get("args"))) {
			args = arg.split(",");
		}

		String code = SmsUtil.genVCode();
		validCodeService.cachVcode(tempId, phone, code);

		return smsService.sendSmsByTempId(phone, smsReource.getSmsTemp(tempId), code, args );
	}

	@Override
	public boolean chkVcode(Map<String, String> param) throws Exception {
		String tempId = param.get("tempId");
		String phone = param.get("phone");
		String code = param.get("code");
		return validCodeService.chkVcode(tempId, phone, code);
	}

	@Override
	public int getSmsBalance() throws IOException {
		
		HashMap<String, String> params = new HashMap<>();
		params.put("Action", smsReource.getBalanceAction());
		params.put("UserName", smsReource.getUserName());
		params.put("Password", smsReource.getPassword());
		
		String s = HttpUtilAdd.postSSLByJson(smsReource.getUrl(), params);
		
		JsonObject jsonObj = new Gson().fromJson(s, JsonObject.class);
		
		return jsonObj.get("Balance").getAsInt();
	}

}
