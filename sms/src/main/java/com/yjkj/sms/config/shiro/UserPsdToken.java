package com.yjkj.sms.config.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

import com.yjkj.sms.config.shiro.dto.SysUser;

public class UserPsdToken extends UsernamePasswordToken {

	private static final long serialVersionUID = -7907937079841501222L;

	private SysUser sysUser;

	public UserPsdToken(final String username, final String password) {
		super(username, password);
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

}
