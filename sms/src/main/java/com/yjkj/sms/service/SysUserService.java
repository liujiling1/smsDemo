package com.yjkj.sms.service;



import java.util.List;
import java.util.Map;

import com.yjkj.sms.config.shiro.dto.SysUser;


public interface SysUserService {
	List<Map<String,Object>> getUsers();
	
	Map<String,Object> getUserById(int id);
	
	SysUser getUserByUserName(String userName);
	
	int addUser(Map<String,Object> param);
	
	int updUser(Map<String,Object> param);
	
	int resetPwd(Map<String,Object> param);
	
/*    
    List<String> getRolesByUserName(String userName);
    List<String> getPermissionsByUserName(String userName);
    List<Map<String, Object>> getMenusByUserName(String userName);


    

    

    int modify(SysUser sysUser);

    List<Map<String,Object>> getUserAuths(int id);

    int saveUserAuths(int id, String selected);

    

    boolean isExist(Integer id, String userName);
    
    SysUser queryUserInfo(int id);
    
//    List<SysUserMenuConf> listUserMenu(Integer userId);
    
    void delUserMenu(Integer userId, Integer menuId);
    
    void addUserMenu(Integer userId, Integer menuId);
    
    List<Map<String,Object>> userStatInfo();
    
    Integer countUserNum();

    int enable(int id);

    int disable(int id);*/
}
