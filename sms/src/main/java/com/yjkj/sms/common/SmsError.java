package com.yjkj.sms.common;

public enum SmsError{
	E_S000("系统异常，请联系管理员"),
	E_S001("错误请求"),
	E_S002("接收手机号格式异常，请检查！"),
	E_S003("手机号码[%s]格式错误，请检查!"),
	E_S004("手机号码不能为空"),
	E_S005("验证码不能为空！"),
	E_S006("短信内容不能为空！"),
	E_S007("业务类型不能为空！"),
	E_S008("短信模板不存在，请检查！"),
	E_S009("短信模板ID不能为空！"),
	E_S010("发送手机号数量不能超过[%s]，请检查!"),
	E_S011("父菜单【%s】是叶子菜单(含有url链接)，\n不允许添加子菜单，请检查!"),
	E_DAY_MAX_CNT("已达当日最大发送次数！")
	;

	private String err;
	private SmsError(String err) {
		this.err = err;
	}
	public String getError(Object... args) {
		return String.format(err, args);
	}
}
