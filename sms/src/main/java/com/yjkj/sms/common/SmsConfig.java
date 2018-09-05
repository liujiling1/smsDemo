package com.yjkj.sms.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.yjkj.sms.entity.SmsReource;

@Configuration
public class SmsConfig {

    @Bean
    public SmsReource SmsReource() {
        return new SmsReource();
    }

}
