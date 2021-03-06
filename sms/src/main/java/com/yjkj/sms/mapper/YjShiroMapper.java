package com.yjkj.sms.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yjkj.sms.config.shiro.dto.SysRole;
import com.yjkj.sms.config.shiro.dto.SysUser;

@Mapper
public interface YjShiroMapper {
	
	List<Map<String,Object>> getUsers();
	
	Map<String,Object> getUserById(int id);
	
	SysUser getUserByUserName(String userName);
    
    List<String> selectRolesByUserName(String userName);
    
    List<String> getPermissionsByUserName(String userName);
    
    int insertUser(Map<String,Object> param);
    
    int updateUser(Map<String,Object> param);
    
    List<SysRole> selectAllRoles();
    
    SysRole selectRoleById(int id);
    
    int insertRole(Map<String,Object> param);
    
    int updateRole(Map<String,Object> param);
    
/*    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser getUserByUserName(String userName);
    List<String> getRolesByUserName(String userName);
    List<String> getPermissionsByUserName(String userName);

    List<Map<String,Object>> getMenusByAuthCodes(Map<String, Object> userName);

    

    Map<String,Object> getUserById(int id);

    List<Map<String,Object>> getUserAuths(int id);
    
    List<Map<String,Object>> userStatInfo();
    
    Integer countUserNum();

    SysUser isUserNameExist(@Param("id") Integer id, @Param("userName") String userName);
    
    */
}