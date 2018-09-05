package com.yjkj.sms.service;

import java.util.List;
import java.util.Map;
import com.yjkj.sms.config.shiro.dto.SysRole;

public interface SysRoleService {
	
	List<SysRole> qryRoleList();
	
	SysRole qryRoleById(int id);
	
	int addRole(Map<String, Object> param);
	
	int updRole(Map<String, Object> param);
	
}
