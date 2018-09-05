package com.yjkj.sms.mapper;

import java.util.List;
import java.util.Map;

import com.yjkj.sms.config.shiro.dto.SysMenu;


public interface SysMenuMapper {
	
	List<SysMenu> selectMenu(Map<String,Object> param);
	
	int insertMenu(Map<String,Object> param);
	
	int updateMenu(Map<String,Object> param);
	
	SysMenu selectMenuById(int id);
	
	int deleteMenu(int id);
}
