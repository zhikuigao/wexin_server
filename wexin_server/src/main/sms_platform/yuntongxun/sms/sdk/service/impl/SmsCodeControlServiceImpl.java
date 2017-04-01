package yuntongxun.sms.sdk.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.cloopen.rest.sdk.SDKTestSendTemplateSMS;
import com.jws.app.operater.data.IndusReportMapper;
import com.jws.app.operater.data.SmsCodeMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.SmsCode;
import com.jws.app.operater.model.UserInfo;
import com.jws.common.constant.Constants;
import com.jws.common.constant.ConstantsCode;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.ResultPackaging;

import net.sf.json.JSONObject;
import yuntongxun.sms.sdk.service.SmsCodeControlService;
@Service("smsCodeControlServiceImpl")
public class SmsCodeControlServiceImpl implements SmsCodeControlService {
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private IndusReportMapper indusReportMapper;
	@Resource
	private SmsCodeMapper smsCodeMapper;
	@Resource
	private UserInfoMapper userInfoMapper;
	private  static String timeout = Constants.timeoutSms;
	private static Map<String, SmsCode>  smsMap = new HashMap<String, SmsCode>();
	@Override
	public JSONObject requestSmsCode(JSONObject josn) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==josn || !josn.has("phone") ||!josn.has("openid") || JiveGlobe.isEmpty(josn.opt("phone")) || JiveGlobe.isEmpty(josn.opt("openid"))) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
	
			String code = JiveGlobe.getFromRomSix();
			String phone = josn.optString("phone");
			String openid = josn.optString("openid");
			SmsCode record = new SmsCode();
			record.setCreateTime(indusReportMapper.selectCurrTime());
			record.setOpenid(openid);
			record.setPhone(phone);
			record.setCode(code);
			 List<UserInfo> phoneList =userInfoMapper.queryUserIstruePhone(phone); 
			 if(!JiveGlobe.isEmpty(phoneList)){
					return  ResultPackaging.dealJsonObject(ConstantsCode.CODE_USER_INFO, ConstantsCode.CODE_USER_INFO, null);
			 }
			
			if(!JiveGlobe.isEmpty(smsMap) ){
				SmsCode sm = smsMap.get(openid);
				if(!JiveGlobe.isEmpty(sm)){
					logger.info("回调");
					smsMap.remove(openid);
					return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, null);
				}
			}
			smsMap.put(openid, record);
			logger.info("发送短信");
			SDKTestSendTemplateSMS.getInstance().pushSms(code, phone);
			this.smsCodeMapper.insert(record);
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, null);
		} catch (Exception e) {
			logger.error("###获取验证码失败："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}

	@Override
	public JSONObject testSmsCode(JSONObject json) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==json || !json.has("phone") || !json.has("code")||!json.has("openid") || JiveGlobe.isEmpty(json.opt("phone")) || JiveGlobe.isEmpty(json.opt("openid"))) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
			String phone = json.optString("phone");
			String openid = json.optString("openid");
			String code = json.optString("code");
			if(JiveGlobe.isEmpty(timeout)){
				timeout = "30";
			}
			int time = Integer.valueOf(timeout);
			SmsCode record= smsCodeMapper.testSmsCode(openid, phone);
			if(!JiveGlobe.isEmpty(record)){
				String strCord = record.getCode();
			//	System.out.println(strCord);
			//	System.out.println(strCord);
				if(strCord.equals(code) && !JiveGlobe.isEmpty(record.getCreateTime())){
					//当前时间
					String dateNew = JiveGlobe.getDateToString( indusReportMapper.selectCurrTime());
					//数据库时间
					String date= JiveGlobe.getDateToString(record.getCreateTime());
					//若当前时间比数据库时间大XXX分钟就过期
					if(Long.parseLong(dateNew)-Long.parseLong(date)>time*100){
						return ResultPackaging.dealJsonObject(ConstantsCode.CODE_USER_NAME_LENGTH, ConstantsCode.CODE_USER_NAME_LENGTH, null);
					}else{
						//更新用户信息
						UserInfo ui =new UserInfo();
						ui.setOpenId(openid);
						ui.setMobPhone(phone);
						userInfoMapper.updateByPrimaryKey(ui);
						return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, null);
					}
				}
			}
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.CODE_ACCOUNT_FORMAT, ConstantsCode.CODE_ACCOUNT_FORMAT, null);
		} catch (Exception e) {
		//	System.out.println(e);
			logger.error("###校验验证码error："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}

}
