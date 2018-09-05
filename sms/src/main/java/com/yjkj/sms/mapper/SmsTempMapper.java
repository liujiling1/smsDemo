package com.yjkj.sms.mapper;

import java.util.List;
import java.util.Map;

import com.yjkj.sms.entity.SmsTempEntity;

public interface SmsTempMapper {
	
	SmsTempEntity selectById(String tempId);

	List<SmsTempEntity> selectTempList(Map<String, Object> param);
	
	int insertTemp(Map<String, String> param);
	
	int updateTemp(Map<String, String> param);
	
	int deleteTemp(String tempId);
}
