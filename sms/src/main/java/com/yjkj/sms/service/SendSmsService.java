package com.yjkj.sms.service;

import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public interface SendSmsService {


	/**
	 * 发送短信
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String sendSms(Map<String, String> param) throws Exception ;

	/**
	 * 发送短信验证码
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String sendVcode(Map<String, String> param) throws Exception;
	
	/**
	 * 验证短信验证码
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean chkVcode(Map<String, String> param) throws Exception;
	
	/**
	 * 获取短信余额
	 * @throws IOException 
	 */
	public int getSmsBalance() throws IOException;
}
