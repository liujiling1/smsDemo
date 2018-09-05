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
import com.yjkj.sms.config.shiro.dto.SysUser;
import com.yjkj.sms.service.SysUserService;
import com.yjkj.sms.util.ResponseMsg;

@Controller
@RequestMapping("/user")
public class SysUserController{

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/userList")
    public String userList(Map<String,Object> model){
        return "user/userList";
    }
    
    @RequestMapping("/qryUserList")
    @ResponseBody
    public Map<String,Object> qryUserList(Map<String,Object> model){
        return ResponseMsg.getInstance(sysUserService.getUsers());
    }
    
    @RequestMapping(value = "/modifyUser/{id}/{todo}", method = RequestMethod.GET)
	public String modifyUser(@PathVariable Integer id , @PathVariable String todo , Map<String, Object> model) {
		model.put("todo", todo);
		
		if(Todo.ADD.code().equals(todo)) {
			model.put("userInfo",new SysUser());
		}else if(Todo.UPD.code().equals(todo) || "psd".equals(todo)) {
			model.put("userInfo",sysUserService.getUserById(id));
		}
		return "user/modifyUser";
	}
    
    @RequestMapping(value = "/saveUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> saveUserAuths(@RequestParam Map<String, Object> param){
        String todo = param.get("todo")+"";
    	int result = 0;
    	if(Todo.ADD.code().equals(todo)) {
    		result = sysUserService.addUser(param);
    		
    	}else if(Todo.UPD.code().equals(todo)){
    		result = sysUserService.updUser(param);
    		
    	}else if("psd".equals(todo)) {
    		sysUserService.resetPwd(param);
    	}
    	
    	return ResponseMsg.getInstance(result);
    }
}
