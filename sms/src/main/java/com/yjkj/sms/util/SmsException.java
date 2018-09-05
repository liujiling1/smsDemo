package com.yjkj.sms.util;

import com.yjkj.sms.common.SmsError;

public class SmsException extends Exception {

	private static final long serialVersionUID = -1095903810226070091L;
	private String errCode;

	public SmsException(SmsError errCode, Throwable e, Object... args) {
		super(errCode.getError(args), e);
		this.errCode = errCode.name();
	}

	public SmsException(SmsError errCode, Object... args) {
		super(errCode.getError(args));
		this.errCode = errCode.name();
	}

	public String getErrCode() {
		return errCode;
	}

}
