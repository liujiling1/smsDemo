package com.yjkj.sms.controller;	

import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.yjkj.sms.service.SendSmsService;

@RestController
@RequestMapping("/yjkj/sms/inner")
public class SendSmsController {
	Logger logger = LoggerFactory.getLogger(SendSmsController.class);
   
	@Autowired
    private SendSmsService sendSmsService;
   
    @RequestMapping("/sendSms")
    public String sendSms(@RequestParam Map<String, String> param) throws Exception{
       return sendSmsService.sendSms(param);
    }
    
    @RequestMapping("/sendVcode")
    public String sendVcode(@RequestParam Map<String, String> param) throws Exception{
       return sendSmsService.sendVcode(param);
    }
    
    @RequestMapping("/chkVcode")
    public boolean chkVcode(@RequestParam Map<String, String> param) throws Exception{
       return sendSmsService.chkVcode(param);
    }
    
	/**
	 * 获取短信余额
	 * @throws IOException 
	 */
    @RequestMapping("/getSmsBalance")
	public Integer getSmsBalance() throws IOException {
		return sendSmsService.getSmsBalance();
	}
}
