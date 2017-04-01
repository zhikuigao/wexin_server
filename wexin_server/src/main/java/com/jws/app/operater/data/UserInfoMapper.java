package com.jws.app.operater.data;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.Feedback;
import com.jws.app.operater.model.UserInfo;

public interface UserInfoMapper {


    int deleteByPrimaryKey(Integer id);
    int insert(UserInfo record);
    int updateByPrimaryKey(UserInfo record);
    String queryUserPhone(@Param("openid") String openId);
    UserInfo queryPersonInfo(@Param("openid") String openId);
    List<UserInfo> queryUserInfo(@Param("page") Integer page,@Param("pageNum") Integer pageNum);
    List<UserInfo> queryUserIstruePhone(@Param("phone") String phone);
    List<UserInfo> queryUserInfoList(@Param("page") Integer page,@Param("pageNum") Integer pageNum);
    int queryUserInfoCount();
    int queryUserInfoListCount();
    int insertFeek(Feedback record);
}