package com.jws.common.constant;



import java.io.File;
import java.util.Properties;
import com.jws.common.util.FileOperation;


/**
 * 系统常量
 * @author wujh	
 *
 */
public class Constants {
	private Constants() {
	}

	//读取数据库常量配置表
	public static Properties properties=FileOperation.readConfigProperties("dataConfig.properties");
	public static String imageUrl = properties.getProperty("FileSave.url") + File.separator + "weixin" +File.separator + "resources" + File.separator + "imageProblem" + File.separator;
	public static String imageUrlBack = properties.getProperty("FileSave.url") + File.separator + "weixin"+File.separator + "resources" + File.separator + "imageBackground" + File.separator;
	public static String imageSuo = properties.getProperty("FileSave.url") + File.separator + "weixin"+ File.separator + "resources" + File.separator + "imageSuo" + File.separator;
	public static String linuximageSuo = properties.getProperty("ipdress.url") + File.separator + "weixin" +File.separator + "resources" + File.separator + "imageProblem" + File.separator;
	//读取账号密码
	public static String account = properties.getProperty("account");
	public static String password = properties.getProperty("password");
	//分账
	public static String weixinPayNoHelp = properties.getProperty("weixinPayNoHelp");
	public static String weixinPayHelpMan = properties.getProperty("weixinPayHelpMan");
	public static String weixinPayKillProblemMan = properties.getProperty("weixinPayKillProblemMan");
	public static String time = properties.getProperty("time");
	
	
	
	//读取系统配置表
	public static Properties propertiesConfig=FileOperation.readConfigProperties("dataConfig.properties");
	public static String token  = propertiesConfig.getProperty("weixin.token");
	public static String isTrue = propertiesConfig.getProperty("develop.model");
	
	//读取微信配置表
	public static Properties weixinConfig=FileOperation.readConfigProperties("weiixnConfig.properties");
	public static String grant_type = weixinConfig.getProperty("weixin.grant_type");
	public static String appid = weixinConfig.getProperty("weixin.appid");
	public static String secret =weixinConfig.getProperty("weixin.secret");
	public static String tokenTimeout =weixinConfig.getProperty("token.timeout");
	public static String mchid =weixinConfig.getProperty("weixin.mchid");
	public static String apikey =weixinConfig.getProperty("weixin.apikey");
	
	
	/**读取消息*/
	public static String weixinConcernReply =weixinConfig.getProperty("weixin.add_friend_autoreply_info");
	public static String weixinMessageReply =weixinConfig.getProperty("weixin.message_default_autoreply_info");

	
	//读取任务分类配置表
	public static Properties problemConfig=FileOperation.readConfigProperties("problemConfig.properties");
	

   //读取sms配置表
	public static Properties smsConfig=FileOperation.readConfigProperties("smsConfig.properties");
	public static String serverIPSms = smsConfig.getProperty("sms.serverIP");
	public static String serverPortSms = smsConfig.getProperty("sms.serverPort");
	public static String accountSidSms = smsConfig.getProperty("sms.accountSid");
	public static String accountTokenSms = smsConfig.getProperty("sms.accountToken");
	public static String timeoutSms = smsConfig.getProperty("sms.timeout");
	public static String mobanSms = smsConfig.getProperty("sms.moban");
	public static String appidSms = smsConfig.getProperty("sms.appid");
	
	
	

}
