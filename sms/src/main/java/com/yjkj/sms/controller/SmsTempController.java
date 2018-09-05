package com.yjkj.sms.controller;	

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yjkj.sms.common.SmsConstant.Todo;
import com.yjkj.sms.entity.SmsTempEntity;
import com.yjkj.sms.service.SmsTempService;
import com.yjkj.sms.util.ResponseMsg;

@Controller
@RequestMapping("/sms")
public class SmsTempController {
	
	@Autowired
	SmsTempService smsTempService;
    
    @RequestMapping(value = "/tempList", method = RequestMethod.GET)
	public String tempList(HttpServletRequest request, Map<String, Object> model) {
		return "smstemp/templist";
	}
    
    @RequestMapping(value = "/modifyTemp/{tempId}", method = RequestMethod.GET)
	public String modifyTemp(@PathVariable String tempId , Map<String, Object> model) {
		model.put("template", smsTempService.qrySmsTemp(tempId));
		model.put("todo", 2);
		return "smstemp/modifyTemp";
	}
    
    @RequestMapping(value = "/addTemp", method = RequestMethod.GET)
   	public String addTemp( Map<String, Object> model) {
    	model.put("template", new SmsTempEntity());
    	model.put("todo", 0);
   		return "smstemp/modifyTemp";
   	}
    
    @RequestMapping(value = "/qryTempList", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
	public Map<String, Object> qrytempList(@RequestParam Map<String, Object> param) {
		return ResponseMsg.getInstance(smsTempService.qrySmsTempList(param));
	}
    
    @RequestMapping(value = "/saveTemplate", produces = { "application/json;charset=UTF-8" },method = RequestMethod.POST)
    @ResponseBody
	public Map<String, Object> saveTemplate(@RequestParam Map<String, String> param) {
    	
    	String todo = param.get("todo");
    	int result = 0;
    	if(Todo.ADD.code().equals(todo)) {
    		result = smsTempService.addSmsTemp(param);
    	}else if(Todo.UPD.code().equals(todo)){
    		result = smsTempService.updSmsTemp(param);
    	}
    	
    	return ResponseMsg.getInstance(result);
	}
    
    @RequestMapping(value = "/delTemplate/{tempId}", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
	public Map<String, Object> delTemp(@PathVariable String tempId) {
		return ResponseMsg.getInstance(smsTempService.delSmsTemp(tempId));
	}
}
