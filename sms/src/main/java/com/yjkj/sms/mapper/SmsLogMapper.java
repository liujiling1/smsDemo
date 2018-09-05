package com.yjkj.sms.mapper;

import java.util.List;

import com.yjkj.sms.entity.SmsLogEntity;

public interface SmsLogMapper {
	SmsLogEntity selectByPrimaryKey(Integer id);
	
	List<SmsLogEntity> selectBySmsLogEntity(SmsLogEntity recode);
	 
	int insertSmsLog(SmsLogEntity recode);
	
	int updateSmsLog(SmsLogEntity recode);

}
