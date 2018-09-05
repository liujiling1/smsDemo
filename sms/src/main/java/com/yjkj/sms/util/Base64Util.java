package com.yjkj.sms.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	/**
	 * encode 使用Base64加密 return
	 */
	public static String encodeStr(String plainText,String charset) {
		byte[] b;
		if(StringTool.isEmpty(charset)) {
			b = plainText.getBytes();
		}else {
			try {
				b = plainText.getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				b = plainText.getBytes();
			}
		}
		Base64 base64 = new Base64();
		b = base64.encode(b);
		String s = new String(b);
		return s;
	}

	/**
	 * decode 使用Base64加密 return
	 */
	public static String decodeStr(String encodeStr) {
		byte[] b = encodeStr.getBytes();
		Base64 base64 = new Base64();
		b = base64.decode(b);
		String s = new String(b);
		return s;
	}

}
