package com.yjkj.sms.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.yjkj.sms.common.SmsError;
import com.yjkj.sms.mapper.SmsTempMapper;
import com.yjkj.sms.util.SmsException;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

public class SmsReource {
	
	@Autowired
	SmsTempMapper smsTempMapper;
	
	private String type;
	private String time;
	private String url;
	private String action;
	private String userid;
	private String account;
	private String userName;
	private String password;
	private String balanceAction;
	private HashMap<String, String> params;

	public String getType() {
		return type;
	}

	public String getTime() {
		return time;
	}

	public String getUrl() {
		return url;
	}

	public String getAction() {
		return action;
	}

	public String getUserid() {
		return userid;
	}

	public String getAccount() {
		return account;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
	
	public String getBalanceAction() {
		return balanceAction;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> getParams() {
		return (HashMap<String, String>)params.clone();
	}

	public SmsReource() {

		try (InputStream in = SmsReource.class.getClassLoader().getResourceAsStream("sms.properties")) {
			Properties prop = new Properties();
			prop.load(new InputStreamReader(in, "UTF-8"));

			String pre = "sms.";
			type = prop.getProperty("sms.type");
			time = prop.getProperty(pre + type + ".time");
			url = prop.getProperty(pre + type + ".url");
			action = prop.getProperty(pre + type + ".action");
			userid = prop.getProperty(pre + type + ".userid");
			account = prop.getProperty(pre + type + ".account");
			userName = prop.getProperty(pre + type + ".userName");
			password = prop.getProperty(pre + type + ".password");
			balanceAction = prop.getProperty(pre + type + ".balanceAction");
			
			setParams();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setParams() {
		params = new HashMap<String, String>();
		params.put("type", type);
		params.put("time", time);
		params.put("url", url);
		params.put("action", action);
		params.put("userid", userid);
		params.put("account", account);
		params.put("userName", userName);
		params.put("password", password);
	}

	/**
	 * 获取短信模板by id
	 * @param tempId 模板id ，如：S0001
	 */
	public SmsTemp getSmsTemp(String tempId) throws SmsException{
		return new SmsTemp(tempId);
	}
	public final class SmsTemp{
		private String temp;
		public SmsTemp(String tempId) throws SmsException{
			SmsTempEntity tempEntity = smsTempMapper.selectById(tempId);
			if(tempEntity == null) {
				throw new SmsException(SmsError.E_S008);
			}
			this.temp = tempEntity.getTemp();
		}
		public String getTemp() {
			return this.temp;
		}
		public String getSmsContent(Object... args) {
			return String.format(this.temp.replaceAll("%C", ""), args);
		}
		public String getSmsContent(String temp, Object... args) {
			return String.format(temp, args);
		}
	}
}
