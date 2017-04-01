package com.jws.app.operater.data;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.ProbelmPayRecord;

public interface ProbelmPayRecordMapper {
   
    int insert(ProbelmPayRecord record);
    List<ProbelmPayRecord> queryPayInfo();
    List<ProbelmPayRecord> queryPayInfoPersion(@Param("openid") String openid);
    int updatePayInfoPersion(@Param("id") Integer id);
    ProbelmPayRecord queryRespondentMoney(@Param("openid") String openid,@Param("probelemId") Integer probelemId);
    int queryRespondentMoneyCount(@Param("probelemId") Integer probelemId);
}