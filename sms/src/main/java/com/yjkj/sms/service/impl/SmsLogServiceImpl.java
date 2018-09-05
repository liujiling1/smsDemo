package com.yjkj.sms.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjkj.sms.entity.SmsLogEntity;
import com.yjkj.sms.mapper.SmsLogMapper;
import com.yjkj.sms.service.SmsLogService;

@Service
public class SmsLogServiceImpl implements SmsLogService{
	
	@Autowired
	SmsLogMapper smsLogMapper;
	
	@Override
	public List<SmsLogEntity> querySmsLogList(SmsLogEntity recode,Integer page,Integer size) {
		
		return smsLogMapper.selectBySmsLogEntity(recode);
	}

	@Override
	public PageInfo<SmsLogEntity> querySmsLogListPage(SmsLogEntity recode,HttpServletRequest request) {
		Integer currentPage = Integer.parseInt(request.getParameter("page"));
		Integer pageSize = Integer.parseInt(request.getParameter("size"));
		currentPage = (currentPage==null)? 1 :currentPage;
		pageSize = (pageSize==null)? 10 :pageSize;
		PageHelper.startPage(currentPage, pageSize);
		PageInfo<SmsLogEntity> pageInfo = new PageInfo<>(smsLogMapper.selectBySmsLogEntity(recode));
		
		return pageInfo;
	}
}
