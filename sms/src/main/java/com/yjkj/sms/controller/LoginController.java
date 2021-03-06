package com.yjkj.sms.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.yjkj.sms.config.shiro.UserPsdToken;
import com.yjkj.sms.config.shiro.dto.SysUser;
import com.yjkj.sms.service.SysMenuService;

@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	SysMenuService sysMenuService;

	@RequestMapping("/")
	public String index(Model model, HttpServletRequest request) {
		logger.info("默认页");
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, Map<String, Object> model) {
		logger.info("登录");
		return "login";
	}

	@RequestMapping(value = "/logon", method = RequestMethod.POST)
	public String logon(HttpServletRequest request) {
		logger.info("登录验证");
		String loginName = request.getParameter("username");
		String loginPassword = request.getParameter("password");

		try {
			SecurityUtils.getSubject().login(new UserPsdToken(loginName, loginPassword));
		} catch (IncorrectCredentialsException ex) {
			return "redirect:/login?flag=1";
		} catch (DisabledAccountException ex) {
			return "redirect:/login?flag=2";
		}

		logger.info("登录成功");

		return "redirect:/index";
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		logger.info("登出");
		SecurityUtils.getSubject().getSession().removeAttribute("user");
		SecurityUtils.getSubject().logout();

		return "login";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> model) {
		model.put("menus", new Gson().toJson(sysMenuService.qryMenus()));
		model.put("userName",((SysUser)SecurityUtils.getSubject().getSession().getAttribute("user")).getUserName());
		return "index";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String unAuthenticate(Map<String, Object> model) {
		return "403";
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main(Map<String, Object> model) {
		// model.put("menus", new Gson().toJson(sysMenuService.qryMenus()));

		return "main";
	}

	@RequestMapping("/idx/index")
	public String idx(Map<String, Object> model) {
		return "main";
	}

}
