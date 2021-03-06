package com.yjkj.sms.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.stereotype.Service;

import com.yjkj.sms.config.shiro.UserPwdGenerator;
import com.yjkj.sms.config.shiro.dto.SysUser;
import com.yjkj.sms.mapper.YjShiroMapper;
import com.yjkj.sms.service.SysUserService;
import com.yjkj.sms.util.CommUtil;


@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    YjShiroMapper shiroMapper;

   /* @Resource	
    SysMenuMapper sysMenuMapper;

    @Resource
    SysUserAuthorityMapper sysUserAuthorityMapper;

    @Resource
    SysAuthorityMapper sysAuthorityMapper;

    @Resource
    SysUserMenuConfMapper sysUserMenuConfMapper;*/

/*    @Override
    public SysUser getUserByUserName(String userName) {
        return sysUserMapper.getUserByUserName(userName);
    }

    @Override
    public List<String> getRolesByUserName(String userName) {
        return sysUserMapper.getRolesByUserName(userName);
    }

    @Override
    public List<String> getPermissionsByUserName(String userName) {
        return sysUserMapper.getPermissionsByUserName(userName);
    }

    @Override
    public List<Map<String, Object>> getMenusByUserName(String userName) {
        Map<String, Object> params = new HashMap<String, Object>();
        List<String>permissions = sysUserMapper.getPermissionsByUserName(userName);
        if(permissions.contains("ADMIN")){
            return sysMenuMapper.getMenus();
        }
        params.put("authCodes",permissions);
        return sysUserMapper.getMenusByAuthCodes(params);
    }*/

    @Override
    public List<Map<String, Object>> getUsers() {
        return shiroMapper.getUsers();
    }

	@Override
	public SysUser getUserByUserName(String userName) {
		return shiroMapper.getUserByUserName(userName);
	}
	
	@Override
    public Map<String,Object> getUserById(int id) {
        return shiroMapper.getUserById(id);
    }

	@Override
	public int addUser(Map<String, Object> param) {
		int curId = CommUtil.getLoginUserId();
        param.put("salt", new SecureRandomNumberGenerator().nextBytes().toHex());
        param.put("password", UserPwdGenerator.genPwd(param.get("password")+"", param.get("salt")+""));
        param.put("enable", "1");
        param.put("updaterId", curId);
        param.put("createrId", curId);
        
		return shiroMapper.insertUser(param);
	}

	@Override
	public int updUser(Map<String, Object> param) {
		param.put("password", null);
		param.put("updaterId", CommUtil.getLoginUserId());
		return shiroMapper.updateUser(param);
	}
	
    @Override
    public int resetPwd(Map<String,Object> param) {
    	int id = Integer.parseInt(param.get("id")+"");
    	String password = (String) param.get("password");
    	
    	Map<String,Object> params = new HashMap<>();
    	params.put("password", UserPwdGenerator.genPwd(password, shiroMapper.getUserById(id).get("salt")+""));
    	params.put("id", id);
    	params.put("updaterId", CommUtil.getLoginUserId());
        
        return shiroMapper.updateUser(params);
    }

/*    @Override
    public int add(SysUser sysUser) {
        int currentUserId = WebAppContext.getLoginUserId();
        sysUser.setSalt(new SecureRandomNumberGenerator().nextBytes().toHex());
        sysUser.setPassword(UserPwdGenerator.genPwd(sysUser.getPassword(),sysUser.getSalt()));
        sysUser.setEnable(1);//表示启用
        sysUser.setCreaterId(currentUserId);
        sysUser.setUpdaterId(currentUserId);
        return sysUserMapper.insertSelective(sysUser);
    }

    

    @Override
    public int modify(SysUser sysUser) {
        int currentUserId = WebAppContext.getLoginUserId();
        sysUser.setUpdaterId(currentUserId);
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public int saveUserAuths(int id, String selected) {
        int ret = 0;

        sysUserAuthorityMapper.removeByUserId(id);

        int currentUserId = WebAppContext.getLoginUserId();
        if(StringUtils.isNotBlank(selected)){
            for(String authIdStr : selected.split(",")){
                ret++;
                Integer authId = Integer.parseInt(authIdStr);
                SysAuthority sa = sysAuthorityMapper.selectByPrimaryKey(authId);
                SysUserAuthority sua = new SysUserAuthority();
                sua.setUserId(id);
                sua.setAuthorityId(authId);
                sua.setAuthorityCode(sa.getAuthorityCode());
                sua.setUpdaterId(currentUserId);
                sysUserAuthorityMapper.insertSelective(sua);
            }
        }
        return ret;
    }

    @Override
    public SysUser queryUserInfo(int id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean isExist(Integer id, String userName) {
        SysUser sysUser = sysUserMapper.isUserNameExist(id,userName);
        return sysUser != null;
    }

    @Override
    public List<Map<String, Object>> getUserAuths(int id) {
        return sysUserMapper.getUserAuths(id);
    }

    @Override
    public List<SysUserMenuConf> listUserMenu(Integer userId){
    	return sysUserMenuConfMapper.selectByUserId(userId);
    }
    
    @Override
    public void delUserMenu(Integer userId,Integer menuId){
    	SysUserMenuConf sumc = new SysUserMenuConf();
    	sumc.setMenuId(menuId);
    	sumc.setSysUserId(userId);
    	sysUserMenuConfMapper.deleteByPrimaryKey(sumc);
    }
    
    @Override
    public void addUserMenu(Integer userId,Integer menuId){
    	SysUserMenuConf sumc = new SysUserMenuConf();
    	sumc.setMenuId(menuId);
    	sumc.setSysUserId(userId);
    	sysUserMenuConfMapper.insertSelective(sumc);
    }
    
    @Override
    public List<Map<String,Object>> userStatInfo(){
    	return sysUserMapper.userStatInfo();
    }
    
    @Override
    public Integer countUserNum(){
    	return sysUserMapper.countUserNum();
    }

    @Override
    public int enable(int id) {
        SysUser user = sysUserMapper.selectByPrimaryKey(id);
        user.setEnable(1);
        user.setUpdaterId(WebAppContext.getLoginUserId());
        return sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int disable(int id) {
        SysUser user = sysUserMapper.selectByPrimaryKey(id);
        user.setEnable(0);
        user.setUpdaterId(WebAppContext.getLoginUserId());
        return sysUserMapper.updateByPrimaryKeySelective(user);
    }*/
}
