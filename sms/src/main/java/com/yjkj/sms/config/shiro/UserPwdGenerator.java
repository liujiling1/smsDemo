package com.yjkj.sms.config.shiro;

import org.apache.commons.codec.digest.DigestUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * Created by Administrator on 2017/3/24.
 */
public class UserPwdGenerator {
    public static String genSalt(String salt){
        return DigestUtils.md5Hex(StringUtils.reverse(salt.substring(salt.length()-8) + salt.substring(0,8)));
    }

    public static String genPwd(String pwd, String salt){
        return new SimpleHash("MD5",pwd, genSalt(salt),2).toHex();
    }
    
}
