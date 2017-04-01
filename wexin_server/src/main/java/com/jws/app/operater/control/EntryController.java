package com.jws.app.operater.control;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jws.app.operater.model.InterfaceLog;
import com.jws.app.operater.service.IInterfaceLogService;
import com.jws.app.operater.service.PersionPhoteAuthServer;
import com.jws.app.operater.service.ProblemServiceControl;
import com.jws.app.operater.service.UserInfoServerControl;
import com.jws.common.constant.BusiConstants;
import com.jws.common.constant.ConstantsCode;
import com.jws.common.util.EHCacheConfig;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.ResultPackaging;

@Controller("entryController")
@RequestMapping("/entry")
public class EntryController {
	private final Logger logger = Logger.getLogger(this.getClass());
	// 日志updatePersonInfo
	@Resource(name = "interfaceLogService")
	private IInterfaceLogService interfaceLogService;
	@Resource(name="userInfoServerControlImpl")
	private UserInfoServerControl userInfoServerControl;
	@Resource(name="problemServiceControlImpl")
	private ProblemServiceControl problemServiceControl;
	@Resource(name="persionPhoteAuthServerImpl")
	private PersionPhoteAuthServer persionPhoteAuthServerImpl;
	/**
	 * 统一入口
	 * @param paramObject
	 *            json格式
	 * @return
	 */
	@RequestMapping(value = "/interfaceEntry.do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject interfaceEntry(@RequestBody HashMap<String,String> paramObject, HttpServletRequest req, HttpServletResponse res) {
//	public JSONObject interfaceEntry(String paramObject) {
		// 插入调用日志
		InterfaceLog log = new InterfaceLog();
		//log.setInputobject(paramObject.toString());
		try {
		//	log = interfaceLogService.insert(log);
		} catch (Exception e) {
			logger.error("记录接口入参日志信息异常:"+e.getMessage());
		}
		// 返回对象 
		JSONObject returnObject = new JSONObject();
		//HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		String language =  request.getHeader("language"); //从header里面获取
//		if (StringUtils.isEmpty(language) || StringUtils.isEmpty(language.trim())) {
//			language = "EN";//获取不到，默认英文
//		}
		// 入参转成json对象
		if(JiveGlobe.isEmpty(paramObject)){
			logger.error("####入参为空:");
			returnObject = ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.LOGIN_CODE_FAIL, null);
			return returnObject;
		}
		JSONObject json = null;
		try {
			json = JSONObject.fromObject(paramObject);
		} catch (Exception e) {
			logger.error("#################### json对象转化失败！" + e.getMessage()
					+ "####################");
			returnObject = ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_TURN_JSON, null);
			// --更新日志记录
			if (null != log) {
				log.setReturnobject(returnObject.toString());
				interfaceLogService.updateByPrimaryKey(log);
			}
			return returnObject;
		}
		/**
		 * tick流程
		 
		if(!json.has("ticket") || JiveGlobe.isEmpty(json.optString("ticket"))){
			returnObject = ResultPackaging.dealJsonObject(ConstantsCode.CODE_SEND_EMAIL, ConstantsCode.CODE_SEND_EMAIL, null);
			return returnObject;
		}else{
			String ticket = json.optString("ticket");
			String openid = json.optString("openid");
			try {
				String ticketEh = EHCacheConfig.get(openid).toString();
				if(JiveGlobe.isEmpty(ticketEh) || !ticket.equalsIgnoreCase(ticketEh)){
					returnObject = ResultPackaging.dealJsonObject(ConstantsCode.CODE_SETTING_PWD, ConstantsCode.CODE_SETTING_PWD, null);
					return returnObject;
				}
			} catch (Exception e) {
				returnObject = ResultPackaging.dealJsonObject(ConstantsCode.CODE_OLDPWD_ERROR, ConstantsCode.CODE_OLDPWD_ERROR, null);
				return returnObject;
			}
		}
	*/
		//json.put("language", "Zh");
		// 2.根据业务code跳转到对应的业务方法中
		if (null == json || !json.has("busiCode")|| null == json.get("busiCode")) {
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
			// --更新日志记录
			if (null != log) {
				log.setReturnobject(returnObject.toString());
				interfaceLogService.updateByPrimaryKey(log);
			}	
			//直接返回错误结果
			return returnObject;
		}		
		if(json.get("busiCode").equals(BusiConstants.USER_UPDATEPERSIONINFO)) {
			  returnObject = userInfoServerControl.updatePersonInfo(json);
		}else if(json.get("busiCode").equals(BusiConstants.PROBLEM_QUERYPRODATA)){
			returnObject = problemServiceControl.queryProData();
		}else if(json.get("busiCode").equals(BusiConstants.PROBLEM_ADDPROBLEM)){
			returnObject = problemServiceControl.addProblem(json);
		}else if(json.get("busiCode").equals(BusiConstants.	PROBLEM_QUERYPROBLEMLIST)){
			returnObject = problemServiceControl.queryProblemlist(json);
		}else if(json.get("busiCode").equals(BusiConstants.	PROBLEM_SOLVPROBLEMRECORD)){
			returnObject = problemServiceControl.solvProblemRecord(json);
		}else if(json.get("busiCode").equals(BusiConstants.	PROBLEM_UPDATEPROSTATUS)){
			returnObject = problemServiceControl.updateProStatus(json);
		}else if(json.get("busiCode").equals(BusiConstants.	PROBLEM_UPDATESOLVPROSTATUS)){
			returnObject = problemServiceControl.updateSolvProStatus(json);
		}else if(json.get("busiCode").equals(BusiConstants.	USER_QUERYPERSONINFO)){
			returnObject = userInfoServerControl.queryPersonInfo(json);
		}else if(json.get("busiCode").equals(BusiConstants.	USER_QUERYPROBLEMINFO)){
			returnObject = userInfoServerControl.queryProblemInfo(json);
		}else if(json.get("busiCode").equals(BusiConstants.	USER_ADDSHARERECORD)){
			returnObject = problemServiceControl.addShareRecord(json);
		}else if(json.get("busiCode").equals(BusiConstants.	PROBLEM_GENERATESHAREIMAGE)){
			returnObject = problemServiceControl.generateShareImage(json);
		}else if(json.get("busiCode").equals(BusiConstants.	PROBLEM_QUERYPROBLEMDESC)){
			returnObject = problemServiceControl.queryProblemDesc(json);
		}else if(json.get("busiCode").equals(BusiConstants.	PROBLEM_QUERYPROBLEMKILLRECORD)){
			returnObject = problemServiceControl.queryProblemKillRecord(json);
		}else if(json.get("busiCode").equals(BusiConstants.	PROBLEM_PROBLEMADOPT)){
			returnObject = problemServiceControl.problemAdopt(json);
		}else if(json.get("busiCode").equals(BusiConstants.	PROBLEM_PROBLEMAPPLYPAY)){
			returnObject = problemServiceControl.problemApplypay(json);
		}else if(json.get("busiCode").equals(BusiConstants.	PROBLEM_VERIFICATIONPROBLEM)){
			returnObject = problemServiceControl.verificationProblem(json);
		}else if(json.get("busiCode").equals(BusiConstants.	PROBLEM_QUERYRESPONDENTMONEY)){
			returnObject = problemServiceControl.queryRespondentMoney(json);
		}else if(json.get("busiCode").equals(BusiConstants.	suggestCollect)){
			returnObject = userInfoServerControl.suggestCollect(json);
		}else {	
			returnObject = ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_ERROR_BUSICODE, null);//未找到对应的busiCode
		}
		// --更新日志记录
		if (null != returnObject && null != log) {
			log.setReturnobject(returnObject.toString());
			try {
				interfaceLogService.updateByPrimaryKey(log);
			} catch (Exception e) {
				logger.error("更新接口返回日志信息异常:"+e.getMessage());
			}
		}
		return returnObject;
	}
	
	  /**
	   * 流文件处理模块
	   * @param args
	   */
	@RequestMapping(value = "/interfaceEntryForStream.do")
	@ResponseBody
	public JSONObject interfaceEntryForStream(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		String language = "EN";
//		String language =  request.getHeader("language"); //从header里面获取
//		if (StringUtils.isEmpty(language) || StringUtils.isEmpty(language.trim())) {
//			language = "EN";//获取不到，默认英文
//		}
		//获取busiCode
//		Object busiCode = request.getHeader("busiCode");
//		if (null == busiCode || StringUtils.isEmpty(busiCode.toString())) {
//			
//		}
//		//根据busiCode跳转到不同的业务处理逻辑中
//		if (StringUtils.equals(busiCode.toString(), "")) {
//			//创建文章	
//			//return articleServiceController.saveIdea(request);
//			return null;
//		}else{
//			return  ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_ERROR_BUSICODE, null);
//		}
		return persionPhoteAuthServerImpl.uploadPhoto(request);
	}

	public static void main(String[] args) {
		String s1 = "1234";
		String s2 = "4322";
		System.out.println(s1+","+s2);
	}
}
