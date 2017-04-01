package com.cloopen.rest.sdk;

import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.jws.common.constant.Constants;


public class SDKTestSendTemplateSMS {
	private final Logger logger = Logger.getLogger(this.getClass());
	private final static String serverIPSms = Constants.serverIPSms;
	private final static String serverPortSms = Constants.serverPortSms;
	private final static String accountSidSms = Constants.accountSidSms;
	private final static String accountTokenSms = Constants.accountTokenSms;
	private final static String mobanSms = Constants.mobanSms;
	private final static String appidSms = Constants.appidSms;
	private final static String timeoutSms = Constants.timeoutSms;

	private SDKTestSendTemplateSMS() {
	}
	private static class JiveGlobeHolder {
		private static SDKTestSendTemplateSMS instance = new SDKTestSendTemplateSMS();
	}

	public static SDKTestSendTemplateSMS getInstance() {
		return JiveGlobeHolder.instance;
	}

	public static void main(String[] args) {
		HashMap<String, Object> result = null;
		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init(serverIPSms, serverPortSms);
		// 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
		restAPI.setAccount("8aaf07085635aae501564adc8d5f0d65", "08b848de88ee4c5b9d1ceeee725d8c9e");
		// 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在"控制台-应用"中看到开发者主账号ACCOUNT SID和
		// 主账号令牌AUTH TOKEN。
		restAPI.setAppId("8aaf07085635aae501564adc8db60d6b");
		// 初始化应用ID，如果是在沙盒环境开发，请配置"控制台-应用-测试DEMO"中的APPID。
		// 如切换到生产环境，请使用自己创建应用的APPID
		result = restAPI.sendTemplateSMS("18565741311", "118375", null);
		//System.out.println("SDKTestGetSubAccounts result=" + result);
		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				//System.out.println(key + " = " + object);
			}
		} else {
			// 异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") + " 错误信息= "
					+ result.get("statusMsg"));
		}
	}
	
	public String pushSms(String code,String phone){
		try {
			HashMap<String, Object> result = null;
			CCPRestSDK restAPI = new CCPRestSDK();
			restAPI.init(serverIPSms, serverPortSms);
			// 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
			restAPI.setAccount(accountSidSms, accountTokenSms);
			// 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在"控制台-应用"中看到开发者主账号ACCOUNT SID和
			// 主账号令牌AUTH TOKEN。
			restAPI.setAppId(appidSms);
			// 初始化应用ID，如果是在沙盒环境开发，请配置"控制台-应用-测试DEMO"中的APPID。
			// 如切换到生产环境，请使用自己创建应用的APPID
			result = restAPI.sendTemplateSMS(phone, mobanSms, new String[] {code, timeoutSms+"分钟" });
			String statusCode = result.get("statusCode").toString();
			if ("000000".equals(result.get("statusCode"))) {
				return statusCode;
			} else {
				// 异常返回输出错误码和错误信息
				logger.info("错误码=" + result.get("statusCode") + " 错误信息= "+ result.get("statusMsg"));
			}
		} catch (Exception e) {
			logger.equals("###云通讯获取验证码"+e);
			return null;
		}
		return null;
	}
	
	
	public String pushSmsNotice(String phone,String mobna){
		try {
			HashMap<String, Object> result = null;
			CCPRestSDK restAPI = new CCPRestSDK();
			restAPI.init(serverIPSms, serverPortSms);
			// 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
			restAPI.setAccount(accountSidSms, accountTokenSms);
			// 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在"控制台-应用"中看到开发者主账号ACCOUNT SID和
			// 主账号令牌AUTH TOKEN。
			restAPI.setAppId(appidSms);
			// 初始化应用ID，如果是在沙盒环境开发，请配置"控制台-应用-测试DEMO"中的APPID。
			// 如切换到生产环境，请使用自己创建应用的APPID
			result = restAPI.sendTemplateSMS(phone, mobna,null);
			String statusCode = result.get("statusCode").toString();
			if ("000000".equals(result.get("statusCode"))) {
				return statusCode;
			} else {
				// 异常返回输出错误码和错误信息
				logger.info("错误码=" + result.get("statusCode") + " 错误信息= "+ result.get("statusMsg"));
			}
		} catch (Exception e) {
			logger.equals("###云通讯获取验证码"+e);
			return null;
		}
		return null;
	}
	
	
	
}