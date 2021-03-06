package com.yjkj.sms.component;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.yjkj.sms.common.SmsError;
import com.yjkj.sms.util.DecryptUtil;
import com.yjkj.sms.util.SmsException;
import com.yjkj.sms.util.SmsException;

@Component
public class ValidCodeService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private static final long timeout = 60 * 5;
	private static final String SKey = "yjdxyz98685818kj";

	/**
	 * 缓存短信验证码
	 * 
	 * @param tempId
	 *            模板id
	 * @param phone
	 *            手机号
	 * @param code
	 *            验证码
	 */
	public void cachVcode(String tempId, String phone, String code) throws SmsException , Exception {
		if (StringUtils.isEmpty(tempId)) {
			throw new SmsException(SmsError.E_S009);
		}
		if (StringUtils.isEmpty(phone)) {
			throw new SmsException(SmsError.E_S004);
		}
		if (StringUtils.isEmpty(code)) {
			throw new SmsException(SmsError.E_S005);
		}

		redisTemplate.opsForValue().set(tempId + phone, DecryptUtil.getAESEncodeBase64(SKey, code), timeout,
				TimeUnit.SECONDS);
	}

	/**
	 * 验证码是否已存在
	 * 
	 * @param tempId
	 *            模板id
	 * @param phone
	 *            手机号
	 */
	public boolean existVcode(String tempId, String phone) throws Exception {
		if (StringUtils.isEmpty(tempId)) {
			throw new SmsException(SmsError.E_S009);
		}
		if (StringUtils.isEmpty(phone)) {
			throw new SmsException(SmsError.E_S004);
		}
		return redisTemplate.hasKey(tempId + phone);
	}

	/**
	 * 验证短信验证码
	 * 
	 * @param tempId
	 *            模板id
	 * @param phone
	 *            手机号
	 * @param code
	 *            验证码
	 */
	public boolean chkVcode(String tempId, String phone, String code) throws SmsException {
		if (StringUtils.isEmpty(tempId)) {
			throw new SmsException(SmsError.E_S009);
		}
		if (StringUtils.isEmpty(phone)) {
			throw new SmsException(SmsError.E_S004);
		}
		if (StringUtils.isEmpty(code)) {
			throw new SmsException(SmsError.E_S005);
		}
		try {
			return DecryptUtil.getAESEncodeBase64(SKey, code).equals(redisTemplate.opsForValue().get(tempId + phone) + "");
		} catch (Exception e) {

		}
		return false;
	}
	
}
