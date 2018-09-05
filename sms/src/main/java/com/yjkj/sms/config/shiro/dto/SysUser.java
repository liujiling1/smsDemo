package com.yjkj.sms.config.shiro.dto;

import java.io.Serializable;
import java.util.Date;

public class SysUser implements Serializable {
	private static final long serialVersionUID = -3190817938676344592L;
	private Integer id;
    private String userName;
    private String salt;
    private String password;
    private String roles;
    private String realName;
    private Integer enable;
    private Integer updaterId;
    private Date updateTime;
    private Integer createrId;
    private Date createTime;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Integer getUpdaterId() {
		return updaterId;
	}
	public void setUpdaterId(Integer updaterId) {
		this.updaterId = updaterId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getCreaterId() {
		return createrId;
	}
	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "SysUser [id=" + id + ", userName=" + userName + ", salt=" + salt + ", password=" + password + ", roles="
				+ roles + ", realName=" + realName + ", enable=" + enable + ", updaterId=" + updaterId + ", updateTime="
				+ updateTime + ", createrId=" + createrId + ", createTime=" + createTime + "]";
	}
	
}
