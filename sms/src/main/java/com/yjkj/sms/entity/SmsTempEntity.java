package com.yjkj.sms.entity;

import java.util.Date;

public class SmsTempEntity {
	private String tempId;
	private String temp;
	private String type;
	private Date creatTime;
	public String getTempId() {
		return tempId;
	}
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	@Override
	public String toString() {
		return "SmsTempEntity [tempId=" + tempId + ", temp=" + temp + ", type=" + type + ", creatTime=" + creatTime
				+ "]";
	}

	
}
