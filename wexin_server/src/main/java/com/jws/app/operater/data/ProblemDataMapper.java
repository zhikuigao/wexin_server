package com.jws.app.operater.data;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.ProblemData;

public interface ProblemDataMapper {

    int insert(ProblemData record);
    List<ProblemData> queryProblemlist(@Param("page") Integer page,@Param("pageNum") Integer pageNum);
    int updateProStatus(@Param("statusProblem") Integer statusProblem,@Param("id") Integer id);
    List<ProblemData> queryProblemInfo(@Param("openid") String openid);
    ProblemData queryProblemDesc(@Param("id") String id);
    int queryPrimarkeyId(@Param("openid") String openid,@Param("money") String money,@Param("content") String content,@Param("sf") String sf);    
    List<ProblemData> queryBalanceDisplay(@Param("openid") String openid);
    List<ProblemData> queryBalanceExtract(@Param("openid") String openid);
    int queryCountShare(@Param("probelemId") Integer probelemId);
    String querySf2(@Param("openid") String id);
    List<ProblemData>   queryOverdueProblem();
    int queryProblemlistCount();
    List<ProblemData> queryProblemAll(@Param("page") Integer page,@Param("pageNum") Integer pageNum);
    int queryProblemAllCount();
    int updateByPrimaryKey(ProblemData record);
}