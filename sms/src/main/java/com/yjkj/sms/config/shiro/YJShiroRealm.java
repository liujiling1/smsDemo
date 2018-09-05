package com.yjkj.sms.config.shiro;


import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yjkj.sms.config.shiro.dto.SysUser;
import com.yjkj.sms.mapper.YjShiroMapper;
import com.yjkj.sms.util.ThrowAbleException;


/**
 * MyShiroRealm
 *
 * @create    2016年1月13日
 */
public class YJShiroRealm extends AuthorizingRealm{

    private static final Logger logger = LoggerFactory.getLogger(YJShiroRealm.class);

    @Resource
    private YjShiroMapper shiroMapper;

//    protected final Map<String, SimpleAuthorizationInfo> roles = new ConcurrentHashMap<String, SimpleAuthorizationInfo>();


    /**
     * 权限认证，为当前登录的Subject授予角色和权限
     * @see 经测试：本例中该方法的调用时机为需授权资源被访问时
     * @see 经测试：并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache
     * @see 经测试：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo =  new SimpleAuthorizationInfo();
//        authorizationInfo.addStringPermissions(shiroMapper.selectRolesByUserName(userName));
        authorizationInfo.addRoles(shiroMapper.selectRolesByUserName(userName));
//        roles.put(userName, authorizationInfo);

        return authorizationInfo;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException, ThrowAbleException {

    	UserPsdToken authenticationToken = (UserPsdToken) token;
    	
        String userName = (String) authenticationToken.getPrincipal();
        SysUser user = shiroMapper.getUserByUserName(userName);
        if(user == null){
            throw new IncorrectCredentialsException();
        }

        try {
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(),
                    ByteSource.Util.bytes(UserPwdGenerator.genSalt(user.getSalt())), this.getName());
            authenticationToken.setSysUser(user);
            return authenticationInfo;
        }catch (Throwable e){
            logger.error(e.getMessage());
            throw new AuthenticationException();
        }

    }
}
