package com.yjkj.sms.util;


import org.apache.shiro.SecurityUtils;

import com.yjkj.sms.config.shiro.dto.SysUser;


public class CommUtil {
    public static String getLoginUserName(){
        return (String) SecurityUtils.getSubject().getPrincipal();
    }

    public static int getLoginUserId(){
        return  getLoginUser().getId();
    }

    public static SysUser getLoginUser(){
        return ((SysUser) SecurityUtils.getSubject().getSession().getAttribute("user"));
    }
}
