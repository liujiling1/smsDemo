package com.yjkj.sms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yjkj.sms.util.ResponseMsg.ResponseStatusEnum;

public class DecryptUtil {

	private static final String DEFAULT_KEY = "y60408133381405j";

	public static final String AUTHC_KEY_FLAG = "1";

	public static final String SESSION_NAME = "TOKEN";

	/***
	 * aes ecb 块加密
	 * 
	 * @param strContent
	 * @return
	 * @throws Exception
	 */
	public static String getAESEncodeBase64(String strContent) throws Exception {
		return getAESEncodeBase64(DEFAULT_KEY, strContent.getBytes("utf-8"));
	}

	/***
	 * aes ecb 块加密
	 * 
	 * @param strKey
	 * @param strContent
	 * @return
	 * @throws Exception
	 */
	public static String getAESEncodeBase64(String strKey, String strContent) throws Exception {
		return getAESEncodeBase64(strKey, strContent.getBytes("utf-8"));
	}

	/***
	 * aes ecb 块加密
	 * 
	 * @param strKey
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String getAESEncodeBase64(String strKey, byte[] content) throws Exception {
		SecretKeySpec key = new SecretKeySpec(strKey.getBytes("utf-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedData = cipher.doFinal(content);
		return new String(Base64.encode(encryptedData), "UTF-8");
	}

	/***
	 * aes ecb base 解密
	 * 
	 * @param enContent
	 * @return
	 * @throws Exception
	 */
	public static String getAESDecodeBase64(String enContent) throws Exception {
		byte[] bytes = Base64.decode(enContent);
		return getAESDecodeECB(DEFAULT_KEY, bytes);
	}

	/***
	 * aes ecb base 解密
	 * 
	 * @param key
	 * @param enContent
	 * @return
	 * @throws Exception
	 */
	public static String getAESDecodeBase64(String key, String enContent) throws Exception {
		byte[] bytes = Base64.decode(enContent);
		return getAESDecodeECB(key, bytes);
	}

	/***
	 * ase ecb 解密
	 * 
	 * @param strkey
	 * @param enContent
	 * @return
	 * @throws Exception
	 */
	public static String getAESDecodeECB(String strkey, byte[] enContent) throws Exception {
		SecretKeySpec key = new SecretKeySpec(strkey.getBytes("utf-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decrypted = cipher.doFinal(enContent);
		return new String(decrypted, "UTF-8");
	}

	/**
	 * 获取key
	 * 
	 * @param sourceKey
	 * @return
	 */
	public static String getKey(String sourceKey) {
		return DEFAULT_KEY.substring(0, 8) + sourceKey.substring(0, 8);
	}

	public static String getReqContent(InputStream in) throws IOException {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(in));) {
			StringBuilder sb = new StringBuilder();
			String s1 = "";
			while ((s1 = br.readLine()) != null) {
				sb.append(s1);
			}
			return URLDecoder.decode(sb.toString().substring(sb.toString().indexOf("=") + 1), "utf-8");
		}
	}

	/**
	 * 获取加密key
	 * 
	 * @param request
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String getDeKey(HttpServletRequest request, InputStream in) throws Exception {
		Boolean authc = (Boolean) request.getAttribute("authc");
		if (authc == null) {
			String c = getReqContent(in);
			return getKey(c, request, false);
		}
		if (authc) {
			HttpSession session = request.getSession();
			return DecryptUtil.getKey(DecryptUtil.getBase64(session.getId()));
		} else {
			return DEFAULT_KEY;
		}
	}

	public static Boolean isDefKey(String key) throws Exception {
		return DEFAULT_KEY.equals(key);
	}

	public static String getKey(String c, HttpServletRequest request, Boolean en) throws Exception {
		if (StringUtils.isBlank(c))
			return DEFAULT_KEY;

		boolean authc = DecryptUtil.AUTHC_KEY_FLAG.equals(c.substring(0, 1));
		request.setAttribute("authc", authc);
		if (authc) {
			HttpSession session = request.getSession();
			String sessionId = DecryptUtil.getBase64(session.getId());
			Cookie[] cookies = request.getCookies();
			String token = "";
			for (Cookie cookie : cookies) {
				if (SESSION_NAME.equals(cookie.getName())) {
					token = cookie.getValue();
				}
			}
			if (!token.equals(sessionId)) {
				request.setAttribute("authc", false);
				return en ? DecryptUtil.getKey(token) : DecryptUtil.DEFAULT_KEY;
			}
			return DecryptUtil.getKey(sessionId);
		} else {
			return DecryptUtil.DEFAULT_KEY;
		}
	}

	/***
	 * aes ecb base 解密
	 * 
	 * @param key
	 * @param enContent
	 * @return
	 * @throws Exception
	 */
	public static String getBase64(String value) throws Exception {
		return new String(Base64.encode(value.getBytes()), "utf-8");
	}

	public static String getFilterResponse(ResponseStatusEnum status,Boolean secret){
		Gson gson = new GsonBuilder().create();
		String strResult = gson.toJson(ResponseMsg.getInstance(status, null));
		try {
			return secret ? "{\"data\" : \"0" + getAESEncodeBase64(strResult) + "\"}" : strResult;
		} catch (Exception e) {
		}
		return "";
	}

	public static void main(String[] args) throws Exception {
		System.err.println(getAESDecodeBase64(getKey("NzI1NmEyYzktMWRlNC00MWUwLWJjYzMtZjg0OWYzYTc2YzJk"), "8VE0uLJnOZL6yUFSdAcF3tjOoApz/E1Mg9iptZpc7d+JRiWrCtInU9L+rQgjkZdB33SdMY23c/OVLEf9sPnBugXHmH1kmv/RK+TrXXwoRlqzmfrutZS+EmEdSW1ebnoLsu+I1oVbChICVN2q0If9VvXI7YLJMfX0jYqbPHq1k6J382mtfc+y5UiD8gLmgO0VuWkC5sg11tB6+r+584jKcRXn+AJNXKZimMiMh6A3cZYWv7HRF1dbQcc9JNfVn2iUrARLNZi83qTJ2GsHW+okog=="));
		
		System.out.println(URLEncoder.encode(getAESEncodeBase64("{\"rechargeId\":75}"), "utf-8"));

		System.out.println(URLEncoder.encode(getAESEncodeBase64(DEFAULT_KEY,
				"{\"username\":\"15004034979\",\"password\" : \"123123\",\"busiType\" : 2,\"pushALi\":\"B1ywLeMbs6ijR8eG8kf1r9f4kIr\"}"),	"utf-8"));

		System.out.println(URLEncoder
				.encode(getAESEncodeBase64(DEFAULT_KEY, "{\"username\":\"15312043942\",\"validType\":4}"), "utf-8"));

		System.out.println(URLEncoder.encode(
				getAESEncodeBase64(getKey("NzI1NmEyYzktMWRlNC00MWUwLWJjYzMtZjg0OWYzYTc2YzJk"),
						"{\"bankCardId\":56,\"busiType\":2,\"amount\":0.01}"),
				"utf-8"));

		System.out.println(URLEncoder.encode(
				getAESEncodeBase64(DEFAULT_KEY,
						"{\"busiType\":\"2\",\"authType\":\"1\",\"realName\":\"邹政勇\",\"cardNo\":\"6230520390014456274\",\"userCardNo\":\"362202198608224250\",\"phone\":\"15312043942\",\"validCode\":\"831590\"}"),
				"utf-8"));

		System.out.println(
				URLEncoder.encode(getAESEncodeBase64(DEFAULT_KEY, "{\"busiType\":1,\"status\":1,\"id\":25}"), "utf-8"));

	}
}