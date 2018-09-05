package com.yjkj.sms.controller;	

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.yjkj.sms.entity.SmsLogEntity;
import com.yjkj.sms.service.SmsLogService;
import com.yjkj.util.result.ResponseMsg;

@Controller
@RequestMapping("/sms")
public class SmsLogController {
	
	@Autowired
	SmsLogService smsLogService;
    
    @RequestMapping(value = "/logList", method = RequestMethod.GET)
	public String logList(HttpServletRequest request, Map<String, Object> model) {
    	return "smslog/loglist";
	}
    

    @RequestMapping(value = "/qryLogList")
    @ResponseBody
	public Map<String, Object> qrytempList(SmsLogEntity smsLogEntity,HttpServletRequest request) {
    	PageInfo<SmsLogEntity> pageInfo = smsLogService.querySmsLogListPage(smsLogEntity,request);
		return ResponseMsg.getInstance(pageInfo.getTotal(),pageInfo.getList());
	}
}
