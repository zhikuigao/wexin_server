package yuntongxun.sms.sdk.control;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yuntongxun.sms.sdk.service.SmsCodeControlService;
import com.jws.app.operater.model.InterfaceLog;
import com.jws.app.operater.service.IInterfaceLogService;
import com.jws.app.operater.service.ProblemServiceControl;
import com.jws.app.operater.service.UserInfoServerControl;
import com.jws.common.constant.BusiConstants;
import com.jws.common.constant.ConstantsCode;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.ResultPackaging;

@Controller("entryControllerSms")
@RequestMapping("/entrysms")
public class EntryControllerSms {
	private final Logger logger = Logger.getLogger(this.getClass());
	// 日志
	@Resource(name = "interfaceLogService")
	private IInterfaceLogService interfaceLogService;
	@Resource(name="userInfoServerControlImpl")
	private UserInfoServerControl userInfoServerControl;
	@Resource(name="problemServiceControlImpl")
	private ProblemServiceControl problemServiceControl;
	@Resource(name="smsCodeControlServiceImpl")
	private SmsCodeControlService smsCodeControlService;  
	/**
	 * 统一入口
	 * 
	 * @param paramObject
	 *            json格式
	 * @return
	 */
	@RequestMapping(value = "/interfaceEntry.do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject interfaceEntry(@RequestBody HashMap<String,String> paramObject, HttpServletRequest req, HttpServletResponse res) {
		// 插入调用日志
		InterfaceLog log = new InterfaceLog();
		log.setInputobject(paramObject.toString());
		
		try {
			log = interfaceLogService.insert(log);
		} catch (Exception e) {
			logger.error("记录接口入参日志信息异常:"+e.getMessage());
		}
		// 返回对象
		JSONObject returnObject = new JSONObject();
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
		if(json.get("busiCode").equals(BusiConstants.SMS_REQUESTSMSCODE)) {
			  returnObject = smsCodeControlService.requestSmsCode(json);
		}else if(json.get("busiCode").equals(BusiConstants.SMS_TESTSMSCODE)){
			returnObject = smsCodeControlService.testSmsCode(json);
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

}
