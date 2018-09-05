package com.yjkj.sms.config.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

public class YJAuthcListener implements AuthenticationListener{

	@Override
	public void onSuccess(AuthenticationToken authenticationToken, AuthenticationInfo info) {
		UserPsdToken token = (UserPsdToken) authenticationToken;
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute("user", token.getSysUser());
	}

	@Override
	public void onFailure(AuthenticationToken token, AuthenticationException ae) {
	}

	@Override
	public void onLogout(PrincipalCollection principals) {
	}

}
