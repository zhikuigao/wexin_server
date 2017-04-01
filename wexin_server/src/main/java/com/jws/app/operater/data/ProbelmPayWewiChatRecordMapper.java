package com.jws.app.operater.data;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.ProbelmPayWewiChatRecord;

public interface ProbelmPayWewiChatRecordMapper {
   

    int insert(ProbelmPayWewiChatRecord record);
    List<ProbelmPayWewiChatRecord>  queryWeiChatBalance(@Param("openid") String openid);
   
}