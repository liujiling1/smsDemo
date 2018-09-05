package com.yjkj.sms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ip获取工具类
 *
 */
public class IPUtil {

	private static Log logger = LogFactory.getLog(IPUtil.class);

	public static final String IS_IPV4_ADDR = "^([1-9]?\\d|1\\d\\d|2[0-4]\\d|25[0-5]).([1-9]?\\d|1\\d\\d|2[0-4]\\d|25[0-5]).([1-9]?\\d|1\\d\\d|2[0-4]\\d|25[0-5]).([1-9]?\\d|1\\d\\d|2[0-4]\\d|25[0-5])$";

	/**
	 * 获取客户端ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getUsrIPAddr(HttpServletRequest request) {
		String ip = null;
		try {
			ip = getIpAddr(request);
			return isRealIPAddr(ip) ? ip : null;
		} catch (Exception e) {
			logger.error("get current request ip addr is error.");
		}
		return ip;
	}

	/**
	 * 真实IP地址验证， 防止IP攻击，错误的IP导致安全问题
	 * 
	 * @author:
	 * @date:
	 * @param ipAddr
	 * @return
	 * @throws Exception
	 */
	private static boolean isRealIPAddr(String ipAddr) throws Exception {
		boolean b1 = false;
		String spIP1 = "0.0.0.0";
		String spIp2 = "255.255.255.255";
		// 是否正确的ip地址
		boolean b = regexValid(ipAddr, IS_IPV4_ADDR);
		if (b) {
			if (!spIP1.equals(ipAddr) && !spIp2.equals(ipAddr)) {
				b1 = true;
			}
		}
		return b1;
	}

	/*
	 * 验证方法
	 * 
	 * @param input 要验证的（如：Email）
	 * 
	 * @param reg 规则 （如：上面的常量）
	 * 
	 * @return
	 */
	public static boolean regexValid(String input, String reg) throws Exception {
		if (input == null || "".equals(input) || reg == null || "".equals(reg)) {
			throw new Exception("参数不能为空!");
		}
		Pattern regex = Pattern.compile(reg);
		Matcher matcher = regex.matcher(input);
		return matcher.matches();
	}

	/**
	 * 获取客户端真实ip地址
	 *
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) throws Exception {
		boolean b = false;
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				ip = ip.substring(0, index);
				b = isRealIPAddr(ip);
				if (!b) {
					ip = "";
				}
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
			if (StringUtils.isNotBlank(ip)) {
				b = isRealIPAddr(ip);
				if (!b) {
					ip = "";
				}
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			if (StringUtils.isNotBlank(ip)) {
				b = isRealIPAddr(ip);
				if (!b) {
					ip = "";
				}
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			if (StringUtils.isNotBlank(ip)) {
				b = isRealIPAddr(ip);
				if (!b) {
					ip = "";
				}
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		logger.debug("current request ip addr is " + ip);
		return ip;
	}

}
