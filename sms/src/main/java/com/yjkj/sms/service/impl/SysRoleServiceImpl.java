package com.yjkj.sms.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.yjkj.sms.config.shiro.dto.SysRole;
import com.yjkj.sms.mapper.YjShiroMapper;
import com.yjkj.sms.service.SysRoleService;
import com.yjkj.sms.util.CommUtil;


@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    YjShiroMapper shiroMapper;

	@Override
	public List<SysRole> qryRoleList() {
		return shiroMapper.selectAllRoles();
	}

	@Override
	public SysRole qryRoleById(int id) {
		return shiroMapper.selectRoleById(id);
	}

	@Override
	public int addRole(Map<String,Object> param) {
		return shiroMapper.insertRole(param);
	}

	@Override
	public int updRole(Map<String,Object> param) {
		param.put("updaterId", CommUtil.getLoginUserId());
		return shiroMapper.updateRole(param);
	}
}
