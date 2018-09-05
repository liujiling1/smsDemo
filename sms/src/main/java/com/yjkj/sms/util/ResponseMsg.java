package com.yjkj.sms.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 返回给请求的数据json 格式
 * @ClassName:ResponseMsg
 * @author:Admin
 * @date:2017/3/16 16:53
 */
public class ResponseMsg<K, V> extends HashMap<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2579625741110172625L;

	public enum ResponseStatusEnum implements ResponseStatus{
		SYS_SUCCESS(1200, "请求成功!"),
		SYS_ERROR(1400, "错误请求"),
		SYS_ILLEGAL(1401, "身份验证错误!"),
		SYS_ILLEGALAU(1402, "没有权限访问"),
		SYS_INSIDEERROR(9999, "网络异常，请稍后重试!"),
		SYS_EXCEPTION(1416, "请求范围不符合要求!"),
		
		V_ARG(9400,"参数校验错误"),
		V_WARN(9500,"数据权限错误"),
		
		FILE_UPLOAD_ERROR(3100,"文件上传错误"),
		FILE_UPLOAD_MAX_ERROR(3101,"文件过大"),
		
		VADLID_CODE_ERROR(8101,"过期或无效验证码"),
		ALI_IM_EXCEPTION(1404, "阿里百川即时通讯异常!"),
		
		USER_UNABLE(1403, "用户被禁用"),
		USER_PASSWORD_ERROR(8102,"密码错误"),
		USER_REGIST_REPEAT(8103, "注册用户已存在"),
		USER_UNREGIST_CHECKED(8105, "无注册验证码通过用户"),
		USER_PAY_PASSWORD_ERROR(8106,"支付密码错误"),
		USER_SMS_ERROR(8107,"短信发送错误"),
		USER_NO_EXIST(8108, "用户不存在"),
		USER_BANK_ERP_AUTH_ERROR(8109,"企业信息未认证"),
		USER_BANK_BING_ERROR(8110,"银行卡认证失败"),
		USER_BANK_BING(8111,"请绑定银行卡"),
		USER_BANK_BING_REPEAT(8112,"银行卡已被绑定"),
		USER_AUTH_PERSON_REPEAT(8113,"身份证已被绑定"),
		USER_REGIST_ERP(8114, "企业已被注册"),
		USER_WALLET_AMOUNT_ERROR(8115,"账户余额不足"),
		USER_NO_AUTH_ERROR(8120,"用户未认证"),
		
		BANK_RECHARGE_FAILURE(2100,"充值失败"),
		BANK_CASH_FAILURE(2101,"提现异常"),
		BANK_CHECK_FAILURE(2102,"校验异常"),
		
		WARES_DEL_W_R_INFO(4101, "该货源已有预定单，不能删除货源！"),
		
		ORDER_R_EXSITE_INFO(3101, "您已有预定单，不能继续运货！"),
		ORDER_DIS_R_INFO(3102, "不能放弃预订单！"),
		ORDER_UP_R_INFO(3103, "不能修改报价！"),
		ORDER_UP_W_R_INFO(3104, "不能修改重量！"),
		
		SHIP_DEL_W_R_INFO(4101, "该船期已有预定单，不能关闭船期！"),
		SHIP_PERIOD_EXIST(4102, "该船只已经有未结束的船期！");

		private final int statusCode;

		private final String reasonPhrase;

		private ResponseStatusEnum(int statusCode, String reasonPhrase) {
			this.statusCode = statusCode;
			this.reasonPhrase = reasonPhrase;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public String getReasonPhrase() {
			return reasonPhrase;
		}

		public static ResponseStatusEnum findStatus(int status) {
			ResponseStatusEnum[] ss = ResponseStatusEnum.values();
			for (ResponseStatusEnum responseStatusEnum : ss) {
				if (status == responseStatusEnum.getStatusCode()) {
					return responseStatusEnum;
				}
			}
			return null;
		}
	}

	public static ResponseMsg<String, Object> getInstance(ResponseStatus status) {
		return getInstance(status, Collections.EMPTY_LIST);
	}

	public static ResponseMsg<String, Object> getInstance(Object object) {
		return getInstance(ResponseStatusEnum.SYS_SUCCESS, object);
	}

	public static ResponseMsg<String, Object> getInstance(ResponseStatus status, Object object) {
		return getInstance(status, object, null);
	}

	public static ResponseMsg<String, Object> getInstance(Long totalCount, Object object) {
		return getInstance(ResponseStatusEnum.SYS_SUCCESS, object, totalCount);
	}

	public static ResponseMsg<String, Object> getInstance(ResponseStatus status, Object object, Long totalCount) {
		ResponseMsg<String, Object> msg = new ResponseMsg<>();
		msg.put("code", status.getStatusCode());
		msg.put("success", status.getStatusCode() == ResponseStatusEnum.SYS_SUCCESS.getStatusCode());
		msg.put("msg", status.getReasonPhrase());
		if (totalCount != null) {
			msg.put("totalCount", totalCount);
			msg.put("recordsTotal", totalCount);
			msg.put("recordsFiltered", totalCount);
		}
		if (null == object) {
			msg.put("data", Collections.EMPTY_LIST);// 返回空数组
		} else {
			if (object instanceof List) {
				msg.put("data", object);
			} else {
				List<Object> data = new ArrayList<>();
				data.add(object);
				msg.put("data", data);
			}
		}
		return msg;
	}
}
