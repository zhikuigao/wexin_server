package org.gzk.weixin.service.impl;

import java.net.URLEncoder;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.gzk.weixin.accessToken.AccessTokenCommon;
import org.gzk.weixin.accessToken.AcessToken;
import org.gzk.weixin.accessToken.ParaMap;

import com.jws.app.operater.model.UserInfo;
import com.jws.common.constant.Constants;
import com.jws.common.util.EHCacheConfig;
import com.jws.common.util.JiveGlobe;
/**
 * 配置微信服务类
 * 配置公账号菜单
 * 配置自动回复
 * 配置接受消息回复
 * @author Administrator
 *
 */

public class addChatMenu {
	private final Logger logger = Logger.getLogger(this.getClass());
	//创建公账号菜单
	private static String apiUrl = " https://api.weixin.qq.com/cgi-bin/menu/create";
	//接受普通消息
	private static String reqUser = "https://api.weixin.qq.com/cgi-bin/user/info";
	//获取网页授权token
	private static String codeUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
	//获取刷新token
	private static String refreshUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	//拉取用户消息
	private static String userUrl = "https://api.weixin.qq.com/sns/userinfo";
	//推送服务消息
	private static String pushMessage = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	// 先写死
	//http://120.24.7.124/weiixin/wecahtControl/interfaceEntry.do
	//String s = "{\"problemId\":\"2\",\"openid\":\"ceshi\",\"byOpenid\":\"ceshiBy\"}";
	private static String configureMenu = "{\"button\":[{	\"type\":\"view\",\"name\":\"创伙伴·帮忙\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx64719d9b69c5714d&redirect_uri=http://wx.fsdigital.cn/weixin/wecahtUserControl/interfaceEntry.do&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect\"}]}";
	private addChatMenu() {
	}

	private static class JiveGlobeHolder {
		private static addChatMenu instance = new addChatMenu();
	}

	public static addChatMenu getInstance() {
		return JiveGlobeHolder.instance;
	}
	public void addMenu() {
		String token = "";
		try {
			if (JiveGlobe.isEmpty(token)) {
				AccessTokenCommon.getInstance().refreshToken();
				token = EHCacheConfig.get("access_token").toString();
				if (JiveGlobe.isEmpty(token)) {
					logger.info("###配置menu获取不到token");
					return;
				}
			}
			ParaMap pm = ParaMap.create("access_token", token);
			String response = AccessTokenCommon.post(apiUrl, pm.getData(),
					configureMenu, null);
			JSONObject json = JSONObject.fromObject(response);
			if (!JiveGlobe.isEmpty(response)) {
				if ( !json.optString("errcode").equals("0")) {
					logger.info("###注入菜单失败"+json);
				}
			} else {
				logger.info("###注入菜单失败");
			}
		} catch (Exception e) {
			logger.info("###注入菜单异常:" + e);
		}
	}
	
	//
	public UserInfo pullUserinfo(String openid) {
		String token = "";
		try {
			token = EHCacheConfig.get("access_token").toString();
		} catch (Exception e) {
			logger.error("###配置menu获取token第一次失败:" + e);
		}
		try {
			if (JiveGlobe.isEmpty(token)) {
				AccessTokenCommon.getInstance().refreshToken();
				token = EHCacheConfig.get("access_token").toString();
				if (JiveGlobe.isEmpty(token)) {
					logger.info("###配置menu获取不到token");
					return null;
				}
			}
			ParaMap pm = ParaMap.create("access_token", token).put("lang","zh_CN").put("openid",openid);
			String response = AccessTokenCommon.get(reqUser, pm.getData(), null);
			JSONObject json = JSONObject.fromObject(response);
			if (!JiveGlobe.isEmpty(response)) {
				UserInfo record = new UserInfo();
				String subscribe = json.optString("subscribe");
				if(subscribe.equals("0")){
					return null;
				}
				String nickname = json.optString("nickname");
				int sex = json.optInt("sex");
				String country = json.optString("country");
				String headimgurl = json.optString("headimgurl");
				//存储用户信息
				record.setCountry(country);
				record.setSex(sex);
				record.setPhoto(headimgurl);
				record.setNickname(nickname);
				record.setOpenId(openid);
			}
		} catch (Exception e) {
			logger.error("###拉取用户信息失败:" + e);
			return null;
		}
		logger.info("###拉取用户信息失败");
		return null;
	}
	///code openid
	public String pullToken(String code){
		String response ="";
		 try {
			 String grant_type  = "authorization_code";
			 String appid  = Constants.appid;
			 String secret  = Constants.secret;
			 ParaMap pm = ParaMap.create("grant_type", grant_type).put("appid", appid).put("secret", secret).put("code", code);
			 response = AccessTokenCommon.get(codeUrl, pm.getData(), null);
			// logger.info(response);
		} catch (Exception e) {
			logger.error("###网页access_token获取erroe："+e);
		}
		return response;
	}
	
	//利用刷新token，拿去openid
	public String pullTokenRefu(String refresh){
		
		 AcessToken at = new AcessToken();
		 try {
			 String grant_type  = Constants.grant_type;
			 String appid  = Constants.appid;
			 ParaMap pm = ParaMap.create("grant_type", grant_type).put("appid", appid).put("refresh_token", refresh);
			  String response = AccessTokenCommon.get(refreshUrl, pm.getData(), null);
			  JSONObject json = JSONObject.fromObject(response);
			 return json.toString();
		} catch (Exception e) {
			logger.error("###刷新code获取erroe："+e);
		}	
		 return null;
	}
	
	public UserInfo pullTokenUserInfo(String token,String opendi){
		UserInfo record = new UserInfo();
		 try {
			 ParaMap pm = ParaMap.create("access_token", token).put("openid", opendi).put("lang", "zh_CN");
			 String response = AccessTokenCommon.get(userUrl, pm.getData(), null);
			 JSONObject json = JSONObject.fromObject(response);
			// logger.info("###获取用户信息打印:"+json.toString());
			 if(json!=null && json.has("openid")){
				 String nickname = URLEncoder.encode(json.optString("nickname"), "utf-8");
				 int sex = json.optInt("sex");
				 String country = json.optString("country");
				 String headimgurl = json.optString("headimgurl");
				 record.setCountry(country);
				 record.setOpenId(opendi);
				 record.setNickname(nickname);
				 record.setSex(sex);
				 record.setPhoto(headimgurl);
			 }	
		} catch (Exception e) {
			logger.error("###网页拉取用户信息erroe："+e);
		}
		return record;
	}
	/**
	 * 发送通知消息
	 * value1 =业务内容
	 * value 2= keynote1
	 * value 3= keynote2
	 * value 4= keynote4
	 * value5 = remark
	 */
	
	public JSONObject  pushSystemMessage(String openid,String template_id,String value1,String value2,String value3,String value4,String value5){
		String token = "";
		String data = "{\"touser\":\""+openid+"\",\"template_id\":\""+template_id+"\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx64719d9b69c5714d&redirect_uri=http://wx.fsdigital.cn/weixin/wecahtUserControl/interfaceEntry.do&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect\",\"data\":{\"first\": {\"value\":\""+value1+"\",\"color\":\"#173177\"},\"keynote1\":{\"value\":\""+value2+"\",\"color\":\"#173177\",\"keynote2\": {\"value\":\""+value3+"\",\"color\":\"#173177\"},\"keynote3\": {\"value\":\""+value4+"\",\"color\":\"#173177\"},\"remark\":{\"value\":\""+value5+"\",\"color\":\"#173177\"}}}";
		try {
			if (JiveGlobe.isEmpty(token)) {
				AccessTokenCommon.getInstance().refreshToken();
				token = EHCacheConfig.get("access_token").toString();
				if (JiveGlobe.isEmpty(token)) {
					logger.info("###配置menu获取不到token");
					return null;
				}
			}
			logger.info("###推送服务内容"+data);
			ParaMap pm = ParaMap.create("access_token", token);
			String response = AccessTokenCommon.post(pushMessage, pm.getData(),data, null);
			JSONObject json = JSONObject.fromObject(response);
			return json;
		} catch (Exception e) {
			logger.info("###推送服务消息异常:" + e);
		}
		return null;
	}
	

}
