package com.yjkj.sms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yjkj.sms.entity.SmsTempEntity;
import com.yjkj.sms.mapper.SmsTempMapper;
import com.yjkj.sms.service.SmsTempService;

@Service
public class SmsTempServiceImpl implements SmsTempService{

	@Autowired
	SmsTempMapper smsTempMapper;

	@Override
	public List<SmsTempEntity> qrySmsTempList(Map<String, Object> param) {

		return smsTempMapper.selectTempList(param);
	}

	@Override
	public SmsTempEntity qrySmsTemp(String tempId) {
		return smsTempMapper.selectById(tempId);
	}

	@Override
	public int addSmsTemp(Map<String, String> param) {
		return smsTempMapper.insertTemp(param);
	}

	@Override
	public int updSmsTemp(Map<String, String> param) {
		return smsTempMapper.updateTemp(param);
	}

	@Override
	public int delSmsTemp(String tempId) {
		return smsTempMapper.deleteTemp(tempId);
	}
}
