package com.yjkj.sms.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.yjkj.sms.entity.SmsTempEntity;

@Service
public interface SmsTempService {

	/**
	 * 查询短信模板列表
	 */
	public List<SmsTempEntity> qrySmsTempList(Map<String, Object> param);
	
	
	/**
	 * 查询短信模板
	 */
	public SmsTempEntity qrySmsTemp(String tempId);
	
	/**
	 * 新增短信模板
	 */
	public int addSmsTemp(Map<String, String> param);
	
	/**
	 * 修改短信模板
	 */
	public int updSmsTemp(Map<String, String> param);
	
	/**
	 * 删除短信模板
	 */
	public int delSmsTemp(String tempId);

}
