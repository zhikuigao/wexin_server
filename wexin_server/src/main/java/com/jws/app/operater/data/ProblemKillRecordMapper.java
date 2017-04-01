package com.jws.app.operater.data;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.ProblemKillRecord;

public interface ProblemKillRecordMapper {
    
    int insert(ProblemKillRecord record);
    int updateSolvProStatus(@Param("statusProblem") Integer statusProblem,@Param("id") Integer id);
    List<ProblemKillRecord> queryProblemKill(@Param("openid") String openid);
    List<ProblemKillRecord> queryProblemKillRecord(@Param("id") String id);
    ProblemKillRecord queryProblemKillRecordFindOne(@Param("id") String id);
    int queryProblemKillCount(@Param("statusProblem") String statusProblem,@Param("openid") String openid);
    
}