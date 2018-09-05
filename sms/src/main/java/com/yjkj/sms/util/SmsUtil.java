package com.yjkj.sms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.yjkj.sms.common.SmsConstant;
import com.yjkj.sms.common.SmsError;
import com.yjkj.util.common.StringTool;

public class SmsUtil {
	private static Pattern p = null;
	private static Matcher m = null;
	
	/**生成短信验证码*/
	public static String genVCode() {
		return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
	}
	
	/**
	 * 校验手机号码合法性
	 */
	public static void chkphone(String phone) throws SmsException{
		if(StringTool.isEmpty(phone)){
			throw new SmsException(SmsError.E_S004);
		}
		
		String[] phoneNumList = phone.split(",");
		if(phoneNumList==null || phoneNumList.length == 0) {
			throw new SmsException(SmsError.E_S002);
		}
		int len= phoneNumList.length;
		if(len>SmsConstant.SMS_MAX) {
			throw new SmsException(SmsError.E_S010, SmsConstant.SMS_MAX);
		}
		
		for(int i=0;i<len;i++) {
			if(!isPhone(phoneNumList[i])){
				throw new SmsException(SmsError.E_S003, phoneNumList[i]);
			}
		}
	}
	
	/**
	 * 校验码合法性
	 * @param phoneNum
	 * @throws MessageSendException
	 */
	public static void chkVerifyCode(String verifyCode) throws SmsException{
		if(StringTool.isEmpty(verifyCode)){
			throw new SmsException(SmsError.E_S005);
		}
	}
	
	/**
	 * 验证单个手机号码
	 * @param mobiles
	 */
	public static boolean isPhone(String mobiles) {
		try {
			p = p==null?Pattern.compile(SmsConstant.REGEX_PHONE):p;
			m = m==null?p.matcher(mobiles):m.reset(mobiles);
			
			return m.matches();
		} catch (Exception e) {
			return false;
		}
	}
}