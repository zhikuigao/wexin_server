package com.jws.app.operater.data;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.ProblemShareRecord;

public interface ProblemShareRecordMapper {
 
    int insert(ProblemShareRecord record);
    ProblemShareRecord queryProblemShare(@Param("openid") String openid,@Param("probelemid") String probelemid);
    List<ProblemShareRecord> queryProblemShareDetail(@Param("openid") String openid,@Param("probelemid") Integer probelemid);
    List<ProblemShareRecord> queryProblemShareFindOne(@Param("openid") String openid);
    List<ProblemShareRecord> queryProblemShareFindList(@Param("probelemid") String probelemid);
    
    
}