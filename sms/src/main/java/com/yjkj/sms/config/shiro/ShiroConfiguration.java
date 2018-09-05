package com.yjkj.sms.config.shiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yjkj.sms.config.shiro.dto.SysMenu;
import com.yjkj.sms.config.shiro.dto.SysRole;
import com.yjkj.sms.mapper.SysMenuMapper;
import com.yjkj.sms.mapper.YjShiroMapper;

/**
 * Shiro 配置
 *
 */
@Configuration
public class ShiroConfiguration {

	@Bean
	public EhCacheManager getEhCacheManager() {
		EhCacheManager em = new EhCacheManager();
		em.setCacheManagerConfigFile("classpath:conf/ehcache-shiro.xml");
		return em;
	}

	@Bean(name = "yjShiroRealm")
	public YJShiroRealm myShiroRealm(EhCacheManager cacheManager) {
		YJShiroRealm realm = new YJShiroRealm();
		HashedCredentialsMatcher hm = new HashedCredentialsMatcher("md5");
		realm.setCacheManager(cacheManager);
		hm.setHashIterations(2);
		realm.setCredentialsMatcher(hm);
		return realm;
	}

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(YJShiroRealm yjShiroRealm) {
		DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
		Authenticator authc = dwsm.getAuthenticator();
		if (authc instanceof ModularRealmAuthenticator) {
			ModularRealmAuthenticator mauthc = (ModularRealmAuthenticator) authc;
			List<AuthenticationListener> listeners = new ArrayList<>();
			listeners.add(new YJAuthcListener());
			mauthc.setAuthenticationListeners(listeners);
		}
		dwsm.setRealm(yjShiroRealm);
		dwsm.setCacheManager(yjShiroRealm.getCacheManager());
		return dwsm;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(
			DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(securityManager);
		return aasa;
	}

	/**
	 * ShiroFilter<br/>
	 *
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager, YjShiroMapper shiroMapper , SysMenuMapper sysMenuMapper) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setLoginUrl("/login");
		shiroFilterFactoryBean.setSuccessUrl("/index");
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(getFilterChainDefinitionMap(shiroMapper,sysMenuMapper));

		Map<String, Filter> filterMap = new LinkedHashMap<>();
		filterMap.put("permsOr", new PermsOrFilter());
		shiroFilterFactoryBean.setFilters(filterMap);
		return shiroFilterFactoryBean;
	}
	
	private static Map<String, String> getFilterChainDefinitionMap(YjShiroMapper shiroMapper,SysMenuMapper sysMenuMapper) {
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/logon", "anon");
		filterChainDefinitionMap.put("/idx/index", "anon");
		filterChainDefinitionMap.put("/yjkj/sms/inner/**", "anon");
		
		Map<String,Object> param = new HashMap<>();
		param.put("enable", 1);
		param.put("leaf", 1);
		List<SysMenu> leafMenus = sysMenuMapper.selectMenu(param);
		List<SysRole> roles = shiroMapper.selectAllRoles();

		for (SysMenu menu : leafMenus) {
			String href = menu.getHref();
			if (StringUtils.isBlank(href))
				continue;
			if (href.indexOf("http://") >= 0 || href.indexOf("https://") >= 0)
				continue;
			int idx = href.indexOf("/", 2);

			href = idx < 0 ? href + "/**" : href.substring(0, href.indexOf("/", 2)) + "/**";

			StringBuilder p = new StringBuilder();
			for (SysRole role : roles) {
				if (menu.getMenuType() == null || role.getMenuTypes().indexOf(menu.getMenuType()) >=0 ) {
					p.append(role.getRoleId() + ",");
				}
			}
			if (p.length() > 0) {
				p.deleteCharAt(p.length() - 1);
				filterChainDefinitionMap.put(href, "permsOr[ADMIN," + p.toString() + "]");
			}else{
				filterChainDefinitionMap.put(href, "permsOr[ADMIN]");
			}
		}
		filterChainDefinitionMap.put("/**", "authc");
		
		return filterChainDefinitionMap;
	}
}
