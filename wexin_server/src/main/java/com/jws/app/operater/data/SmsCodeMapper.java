package com.jws.app.operater.data;



import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.SmsCode;

public interface SmsCodeMapper {
    
    int insert(SmsCode record);
    SmsCode testSmsCode(@Param("openid")String openid,@Param("phone")String phone);
   
}