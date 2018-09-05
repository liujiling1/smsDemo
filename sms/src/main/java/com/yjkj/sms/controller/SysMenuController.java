package com.yjkj.sms.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yjkj.sms.common.SmsConstant.Todo;
import com.yjkj.sms.service.SysMenuService;
import com.yjkj.sms.util.ResponseMsg;
import com.yjkj.sms.util.SmsException;

@Controller
@RequestMapping("/menu")
public class SysMenuController{

    @Autowired
    private SysMenuService sysMenuService;

    @RequestMapping("/menuList")
    public String index(Map<String,Object> model){
        return "menu/menuList";
    }

    @RequestMapping(value="/qryMenuList",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> qryMenuList(Map<String,Object> model){
        return ResponseMsg.getInstance(sysMenuService.qryMenus());
    }

    @RequestMapping(value="/saveMenu",method = RequestMethod.POST)
    public String saveMenu(@RequestParam Map<String,Object> param) throws SmsException{
    	String todo = param.get("todo")+"";
        if(Todo.ADD.eq(todo)) {
        	sysMenuService.addMenu(param);
		}else if(Todo.UPD.eq(todo)) {
			sysMenuService.updMenu(param);
		}
        
        return "redirect:/menus/menuList";
    }

    @RequestMapping(value="/removeMenu/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> removeMenu(@PathVariable int id){
        return ResponseMsg.getInstance(sysMenuService.removeMenu(id));
    }
}
