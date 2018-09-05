package com.yjkj.sms.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTool {

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 */
	public static boolean isPhoneNumber4Username(String mobiles) {
		try {
			Pattern p = Pattern.compile("^((13[0-9])|17[0-9]|(166)|(19[8-9])|(14[5,7,9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			return m.matches();
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否是正整数
	 * @param str
	 * @return
	 */
	public static boolean isPositiveInteger(String str) {
		try {
			Pattern p = Pattern.compile("^[1-9]*[1-9][0-9]*$");
			Matcher m = p.matcher(str);
			return m.matches();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public String replaceBlank(String str) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(str);
		String after = m.replaceAll("");
		return after;
	}

	/**
	 * 获取当前时间
	 * @param format
	 * @return
	 */
	public static String getNowTime(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	public static boolean isEmpty(String str) {
		return (str == null) || str.trim().equals("");
	}
	
	
	public static String UUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	
	/**
	 * 拼串
	 */
	public static String join(String... sources) {
		StringBuilder sb = new StringBuilder();
		for (String source : sources) {
			sb.append(source);
		}
		return sb.length() > 0 ? sb.toString() : "";
	}


	/**
	 * 按照需求截取字符串的某几位 从begin位开始取，取bit位
	 * 如果长度不够就返回字符串本身
	 */
	public static String splitByLength(String source, int begin, int bit) {
		if (source == null || "".equals(source)) {
			return "";
		}

		int length = source.length();
		if (length <= bit) {
			return source;
		}else if(length <= (begin + bit)) {
			return source.substring(0, bit);
		}

		return source.substring(begin, begin + bit);
	}
}
