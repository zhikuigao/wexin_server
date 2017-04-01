package com.jws.app.operater.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.gzk.weixin.service.impl.addChatMenu;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jws.app.operater.data.IndusReportMapper;
import com.jws.app.operater.data.ProblemShareRecordMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.ProblemShareRecord;
import com.jws.app.operater.model.UserInfo;
import com.jws.common.util.EHCacheConfig;
import com.jws.common.util.JiveGlobe;

public class WexinFilterProblemDetailImpl {
	@Resource
	private IndusReportMapper indusReportMapper;
	@Resource 
	private UserInfoMapper userInfoMapper;
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private ProblemShareRecordMapper problemShareRecordMapper;
	public WexinFilterProblemDetailImpl(){
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		if(userInfoMapper==null){
            BeanFactory factory = (BeanFactory) appContext;
            userInfoMapper = (UserInfoMapper) factory.getBean ("userInfoMapper");
   	}
		if(problemShareRecordMapper==null){
            BeanFactory factory = (BeanFactory) appContext;
            problemShareRecordMapper = (ProblemShareRecordMapper) factory.getBean ("problemShareRecordMapper");
   	}
		if(indusReportMapper==null){
            BeanFactory factory = (BeanFactory) appContext;
            indusReportMapper = (IndusReportMapper) factory.getBean ("indusReportMapper");
   	}
}
	
	public void saveShareUserinfo(JSONObject json,String state,String openid,String openidBy,String problemId){
		if(!JiveGlobe.isEmpty(json) && !JiveGlobe.isEmpty(state) &&json.has("access_token") && json.has("openid")){
			String access_token = json.optString("access_token");
			 openid = json.optString("openid");
			//String session = openid+JiveGlobe.getFromRom();
		//	redirectAttributes.addFlashAttribute("openid",openid);
			 UserInfo userExits = userInfoMapper.queryPersonInfo(openid);
			//查询用户是否在系统里面,若用户存在直接跳转首页
				//EHCacheConfig.put(openid, session);
				//&& !JiveGlobe.isEmpty(userExits.getMobPhone())
				//可以去拉取用户信息了
				UserInfo record = new UserInfo();
				if(JiveGlobe.isEmpty(userExits)){
					 record = addChatMenu.getInstance().pullTokenUserInfo(access_token, openid);
					record.setCreateTime(indusReportMapper.selectCurrTime());
					userInfoMapper.insert(record);
				}
//				record.setCompany("创伙伴");
//				record.setCountry("中国");
//				record.setCreateTime(indusReportMapper.selectCurrTime());
//				record.setNickname("创伙伴测试哥");
//				record.setOpenId("ceshi1232432432");
//				record.setPhoto("");
//				record.setPost("助理");
//				record.setSex(1);
//				record.setPhoto(Constants.imageUrlBack+"cshi.jpg");
				if(!JiveGlobe.isEmpty(record)){
					try {
						//这里处理问题分享的记录
						if(state.split(",").length>1){
							 problemId = state.split(",")[0].split(":")[1];
							 openidBy = state.split(",")[1].split(":")[1];
						    //    logger.info("###添加进数据库"+problemId);
								List<ProblemShareRecord> rocord = problemShareRecordMapper.queryProblemShareDetail(openid,Integer.valueOf(problemId));
								if(JiveGlobe.isEmpty(rocord)){
									ProblemShareRecord ps = new ProblemShareRecord();
									ps.setCreateTime(indusReportMapper.selectCurrTime());
									ps.setProbelemid(Integer.valueOf(problemId));
									ps.setOpenid(openid);
									ps.setOpenidBy(openidBy);
									problemShareRecordMapper.insert(ps);
								}
						}
					} catch (Exception e) {
						logger.error("####分享记录异常"+e);
					}
				}
			}else{
				try {
					logger.info("###session进入"+openidBy);
					List<ProblemShareRecord> rocord = problemShareRecordMapper.queryProblemShareDetail(openid,Integer.valueOf(problemId));
					if(JiveGlobe.isEmpty(rocord)){
						ProblemShareRecord ps = new ProblemShareRecord();
						ps.setCreateTime(indusReportMapper.selectCurrTime());
						ps.setProbelemid(Integer.valueOf(problemId));
						ps.setOpenid(openid);
						ps.setOpenidBy(openidBy);
						problemShareRecordMapper.insert(ps);
					}
				} catch (Exception e) {
					logger.error("####分享记录异常"+e);
				}
		
			}
		}
	
}
