package com.yjkj.sms.config.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 重写权限过滤，满足其一就通过
 */
public class PermsOrFilter extends PermissionsAuthorizationFilter{
	Logger logger = LoggerFactory.getLogger(PermsOrFilter.class);


	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse servletResponse, Object mappedValue)
			throws IOException {
		HttpServletRequest rq = (HttpServletRequest) request;
		logger.debug("requestUrl>>>"+rq.getRequestURL());
		Subject subject = getSubject(request, servletResponse);
		String[] permsArray = (String[]) mappedValue;
		if (permsArray == null || permsArray.length == 0) {
			return true;
		}
		boolean isPermit = false;
		for(String role : permsArray) {
			if (subject.hasRole(role)) {
				isPermit= true;
				break;
			}
		}
		return isPermit;
	}
	
}
