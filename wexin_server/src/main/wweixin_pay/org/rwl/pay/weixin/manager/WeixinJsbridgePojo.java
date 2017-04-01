package org.rwl.pay.weixin.manager;

public class WeixinJsbridgePojo {
	public String appid = ""; //微信APPID
	public String mchid = ""; //商户ID
	public String openid = ""; //微信客户的OPENID
	public String clientaddr = ""; //客户端本地ID, request.getRemoteAddr
	public String notifyurl = "http://wx.fsdigital.cn/weixin/weixinpay/weixinjsbridge_notify.do";//服务器通知的成功和失败地址,二级域名
	public String clientsendurl = "http://wx.fsdigital.cn/weixinpay/weixinpay/resultClient.jsp"; //客户端返回的通知结果
	public String uniurl = "https://api.mch.weixin.qq.com/pay/unifiedorder"; //获取预支付单号的URL
	public String useragent = ""; //request.getHeader("user-agent"),微信客户端的版本号

	private static String APIKEY = ""; //支付密匙
	/**
	 * 获取支付密匙
	 * @return
	 */
	public static String getAPIKEY() {
		return APIKEY;
	}
	public static void setAPIKEY(String _apikey) {
		APIKEY = _apikey;
	}
	
	@Override
	public String toString() {
		return appid + " " + mchid + " " + openid + " " + APIKEY + " " + clientaddr + " " + notifyurl + " " + clientsendurl + " ";
	}
}
