package com.yjkj.sms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yjkj.sms.common.SmsError;
import com.yjkj.sms.config.shiro.dto.SysMenu;
import com.yjkj.sms.mapper.SysMenuMapper;
import com.yjkj.sms.service.SysMenuService;
import com.yjkj.sms.util.CommUtil;
import com.yjkj.sms.util.SmsException;

@Service
public class SysMenuServiceImpl implements SysMenuService{

	@Autowired
	SysMenuMapper sysMenuMapper;

	public List<SysMenu> qryMenus(){
		Map<String,Object> param = new HashMap<>();
		param.put("enable", 1);
		return sysMenuMapper.selectMenu(param);
	}

	@Override
	public SysMenu getMenuById(int id) {
		return null;
	}

	@Override
	public int addMenu(Map<String,Object> param) throws SmsException {
		
//		获取父菜单-处理
		SysMenu parentMenu = sysMenuMapper.selectMenuById(Integer.valueOf(param.get("parentId")+""));
        if("1".equals(parentMenu.getLeaf())){
            throw new SmsException(SmsError.E_S011, parentMenu.getMenuName());
        }
        
        if(StringUtils.isEmpty(param.get("href")+"")) {
        	param.put("leaf", 0);
        }else {
        	param.put("leaf", 1);
        }
        param.put("updaterId", CommUtil.getLoginUserId());
		return sysMenuMapper.insertMenu(param);
	}

	@Override
	public int updMenu(Map<String,Object> param) {
		param.put("updaterId", CommUtil.getLoginUserId());
		if(StringUtils.isEmpty(param.get("href")+"")) {
        	param.put("leaf", 0);
        }else {
        	param.put("leaf", 1);
        }
		return sysMenuMapper.updateMenu(param);
	}

	@Override
	public int removeMenu(int id) {
		
		return sysMenuMapper.deleteMenu(id);
	}
}
