package com.jws.app.operater.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.jws.app.operater.data.ProbelmPayRecordMapper;
import com.jws.app.operater.data.ProbelmPayWewiChatRecordMapper;
import com.jws.app.operater.data.ProblemDataMapper;
import com.jws.app.operater.data.ProblemKillRecordMapper;
import com.jws.app.operater.data.ProblemShareRecordMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.Feedback;
import com.jws.app.operater.model.ProbelmPayRecord;
import com.jws.app.operater.model.ProbelmPayWewiChatRecord;
import com.jws.app.operater.model.ProblemData;
import com.jws.app.operater.model.ProblemKillRecord;
import com.jws.app.operater.model.ProblemShareRecord;
import com.jws.app.operater.model.UserInfo;
import com.jws.app.operater.service.UserInfoServerControl;
import com.jws.common.constant.Constants;
import com.jws.common.constant.ConstantsCode;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.JsonUtil;
import com.jws.common.util.ResultPackaging;

@Service("userInfoServerControlImpl")
public class UserInfoServerControlImpl implements UserInfoServerControl {
	@Resource
	private UserInfoMapper userInfoMapper;
	@Resource
	private ProblemDataMapper problemDataMapper;
	@Resource
	private ProblemKillRecordMapper problemKillRecordMapper;
	@Resource
	private ProblemShareRecordMapper problemShareRecordMapper;
	@Resource
	private ProbelmPayWewiChatRecordMapper probelmPayWewiChatRecordMapper;
	@Resource
	private ProbelmPayRecordMapper probelmPayRecordMapper;
	private final Logger logger = Logger.getLogger(this.getClass());
	@Override
	public JSONObject updatePersonInfo(JSONObject json) {
		JSONObject returnObject= new JSONObject();
		//验证参数
		if (null==json || !json.has("openid")  || JiveGlobe.isEmpty(json.optString("openid"))) {
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
			return returnObject;			
		}	
		try {
			String openid = json.optString("openid");
			String nickname = json.optString("nickname");
			String mobphone = json.optString("mobphone");
			String company = json.optString("company");
			String post = json.optString("post");
			String introductionSelf = json.optString("introductionSelf"); 
			String photo = json.optString("photo");
			String sex = json.optString("sex");
			String country = json.optString("country");
			String weixinId = json.optString("wechat");
			UserInfo ui = new UserInfo();
			if(!JiveGlobe.isEmpty(nickname)){	
				ui.setNickname(URLEncoder.encode(nickname, "utf-8"));
			}
			if(!JiveGlobe.isEmpty(mobphone)){
				ui.setMobPhone(mobphone);
			}
			if(!JiveGlobe.isEmpty(post)){
				ui.setPost(post);
			}
			if(!JiveGlobe.isEmpty(introductionSelf)){
				ui.setIntroductionself(introductionSelf);
			}
			if(!JiveGlobe.isEmpty(photo)){
				ui.setPhoto(photo);
			}
			ui.setOpenId(openid);
			if(!JiveGlobe.isEmpty(sex)){
				ui.setSex(Integer.valueOf(sex));
			}
			if(!JiveGlobe.isEmpty(country)){
				ui.setCountry(country);
			}
			if(!JiveGlobe.isEmpty(company)){
				ui.setCompany(company);
			}
			if(!JiveGlobe.isEmpty(weixinId)){
				ui.setSpareField4(weixinId);
			}
			this.userInfoMapper.updateByPrimaryKey(ui);
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, null);
		} catch (Exception e) {
			logger.error("###更新用户失败："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}
	
	
	@Override
	public JSONObject queryPersonInfo(JSONObject json) {
		JSONObject returnObject= new JSONObject();
		//验证参数
		if (null==json || JiveGlobe.isEmpty("openid")  ) {
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
			return returnObject;			
		}	
		try {
		//	int amountMoney = 0;
		//	int amountMoneyDisplay = 0;
			double amountMoneyExtract = 0;
			String openid = json.optString("openid");
			UserInfo ui = userInfoMapper.queryPersonInfo(openid);
			//查询余额
			if(!JiveGlobe.isEmpty(ui)){
			if(!JiveGlobe.isEmpty(ui.getSpareField3())){
				ui.setSpareField3(Constants.linuximageSuo+ui.getSpareField3());
			}
			ui.setNickname(java.net.URLDecoder.decode(ui.getNickname(),"utf-8"));
//				List<ProbelmPayWewiChatRecord> recordList = probelmPayWewiChatRecordMapper.queryWeiChatBalance(openid);
//				for(int i=0;i< recordList.size();i++){
//					ProbelmPayWewiChatRecord pd = recordList.get(i);
//					amountMoney = amountMoney+pd.getAmountMoney();
//				}
//				//可显示余额
//				List<ProblemData> recordList2 = problemDataMapper.queryBalanceDisplay(openid);
//				for(int i=0;i< recordList2.size();i++){
//					ProblemData pd =  recordList2.get(i);
//					amountMoneyDisplay = amountMoneyDisplay+ pd.getAmountMoney();
//				}
				//可提现余额
				//List<ProblemData> recordList3 = problemDataMapper.queryBalanceExtract(openid);
				List<ProbelmPayRecord> payRecord = probelmPayRecordMapper.queryPayInfoPersion(openid);
				for(int i=0;i< payRecord.size();i++){
					ProbelmPayRecord pd = payRecord.get(i);
					amountMoneyExtract = pd.getAmountMoney()+ amountMoneyExtract;
				}
			}
//			if(amountMoneyExtract>0){
//				amountMoneyExtract = amountMoneyExtract;
//			}
//			if(amountMoneyDisplay>0){
//				amountMoneyDisplay = amountMoneyDisplay+(amountMoney/100);
//			}
		
				//ui.setSpareField1(""+amountMoneyDisplay);
			if(!JiveGlobe.isEmpty(ui)){
				ui.setSpareField2(""+amountMoneyExtract);
			}
			if(JiveGlobe.isEmpty(ui)){
				return ResultPackaging.dealJsonObject(ConstantsCode.CODE_PASE_VERSION, ConstantsCode.CODE_PASE_VERSION, null);
			}
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, ui);
		} catch (Exception e) {
			System.out.println(e);
			logger.error("###获取用户信息失败："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}
	
	@Override
	public JSONObject queryProblemInfo(JSONObject json) {
		JSONObject returnObject= new JSONObject();
	//	JSONObject jsondata= new JSONObject();
		//验证参数
		if (null==json || JiveGlobe.isEmpty("openid") ) {
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
			return returnObject;			
		}	
		try {
			String openid = json.optString("openid");
			//这个查的只是我的提问
			List<ProblemData> pd = problemDataMapper.queryProblemInfo(openid);
			//我回答的
			List<ProblemKillRecord> pk = problemKillRecordMapper.queryProblemKill(openid);
			//我的帮忙
			List<ProblemShareRecord> ps = problemShareRecordMapper.queryProblemShareFindOne(openid);
//			jsondata.put("problemData", pd);
//			jsondata.put("problemKillData", pk);
//			jsondata.put("problemShareData", ps);
			returnObject = JsonUtil.addJsonObject("problemData", pd, returnObject);
			returnObject = JsonUtil.addJsonObject("problemKillData", pk,returnObject);
			returnObject = JsonUtil.addJsonObject("problemShareData", ps,returnObject);
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, returnObject);
		} catch (Exception e) {
			logger.error("###获取问题信息："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}


	@Override
	public JSONObject suggestCollect(JSONObject json) {
		JSONObject returnObject= new JSONObject();
	//	JSONObject jsondata= new JSONObject();
		//验证参数
		if (null==json || JiveGlobe.isEmpty("openid")  || JiveGlobe.isEmpty("problemId")  || JiveGlobe.isEmpty("content") )  {
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
			return returnObject;			
		}	
		try {
			Feedback fa =new Feedback();
			fa.setContent(json.optString("content"));
			fa.setOpenid(json.optString("openid"));
			fa.setProblemid(json.optInt("problemId"));
			userInfoMapper.insertFeek(fa);
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, returnObject);
		} catch (Exception e) {
			logger.error("###添加反饋："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}
}
