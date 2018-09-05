package com.yjkj.sms.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.yjkj.sms.config.shiro.dto.SysMenu;
import com.yjkj.sms.util.SmsException;


@Service
public interface SysMenuService {

	/**
	 * 查询菜单列表
	 */
	public List<SysMenu> qryMenus();

    SysMenu getMenuById(int id);
    
    int addMenu(Map<String,Object> param) throws SmsException;

    int updMenu(Map<String,Object> param);

    int removeMenu(int id);

}
