package yuntongxun.sms.sdk.service;

import net.sf.json.JSONObject;

public interface SmsCodeControlService {
			 public JSONObject requestSmsCode(JSONObject json);
			 public JSONObject testSmsCode(JSONObject json);
}		
	