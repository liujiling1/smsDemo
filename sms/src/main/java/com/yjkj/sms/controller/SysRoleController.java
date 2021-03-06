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
import com.yjkj.sms.config.shiro.dto.SysRole;
import com.yjkj.sms.service.SysRoleService;
import com.yjkj.sms.util.ResponseMsg;

@Controller
@RequestMapping("/role")
public class SysRoleController{

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("/roleList")
    public String roleList(Map<String,Object> model){
        return "role/roleList";
    }
    
    @RequestMapping("/selectRole")
    public String selectRole(Map<String,Object> model){
        return "role/selectRole";
    }
    
    @RequestMapping(value = "/modifyRole/{id}/{todo}", method = RequestMethod.GET)
	public String modifyRole(@PathVariable Integer id , @PathVariable String todo , Map<String, Object> model) {
		model.put("todo", todo);
		
		if(Todo.ADD.code().equals(todo)) {
			model.put("roleInfo",new SysRole());
		}else if(Todo.UPD.code().equals(todo)) {
			model.put("roleInfo",sysRoleService.qryRoleById(id));
		}
		return "role/modifyRole";
	}
    
    @RequestMapping("/qryRoleList")
    @ResponseBody
    public Map<String,Object> qryRoleList(Map<String,Object> model){
        return ResponseMsg.getInstance(sysRoleService.qryRoleList());
    }
    
    @RequestMapping(value = "/saveRoleInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> saveRoleInfo(@RequestParam Map<String, Object> param){
        String todo = param.get("todo")+"";
    	int result = 0;
    	if(Todo.ADD.code().equals(todo)) {
    		result = sysRoleService.addRole(param);
    		
    	}else if(Todo.UPD.code().equals(todo)){
    		result = sysRoleService.updRole(param);
    		
    	}
    	
    	return ResponseMsg.getInstance(result);
    }
}
