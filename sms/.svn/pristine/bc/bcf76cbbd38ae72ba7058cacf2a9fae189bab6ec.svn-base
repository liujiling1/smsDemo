package com.yjkj.sms.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.yjkj.sms.entity.SmsLogEntity;

@Service
public interface SmsLogService {

	/**
	 * 查询短信日志
	 */
	public List<SmsLogEntity> querySmsLogList(SmsLogEntity recode,Integer page,Integer size);
	
	public PageInfo<SmsLogEntity> querySmsLogListPage(SmsLogEntity recode,HttpServletRequest request);

}
