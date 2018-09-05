package com.yjkj.sms.config.shiro.dto;

import java.io.Serializable;
import java.util.Date;

public class SysMenu implements Serializable {
	private static final long serialVersionUID = -4511401785902615683L;
	private Integer id;
    private String menuName;
    private String menuType;
    
    private String href;
	private String code;
	private String sort;
	private String pid;
	private String leaf;
	private String enable;
	private String updaterId;
	private Date updateTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getLeaf() {
		return leaf;
	}
	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getUpdaterId() {
		return updaterId;
	}
	public void setUpdaterId(String updaterId) {
		this.updaterId = updaterId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "SysMenu [id=" + id + ", menuName=" + menuName + ", menuType=" + menuType + ", href=" + href + ", code="
				+ code + ", sort=" + sort + ", pid=" + pid + ", leaf=" + leaf + ", enable=" + enable + ", updaterId="
				+ updaterId + ", updateTime=" + updateTime + "]";
	}
	
}