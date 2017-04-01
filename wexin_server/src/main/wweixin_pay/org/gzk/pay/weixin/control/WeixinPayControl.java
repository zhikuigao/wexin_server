package org.gzk.pay.weixin.control;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.rwl.pay.weixin.manager.WeixinJsbridgePojo;
import org.rwl.pay.weixin.utils.MD5Util;
import org.rwl.pay.weixin.utils.TenpayUtil;
import org.rwl.pay.weixin.utils.WXUtil;
import org.rwl.pay.weixin.utils.XMLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jws.app.operater.model.InterfaceLog;
import com.jws.app.operater.service.IInterfaceLogService;
import com.jws.common.constant.Constants;
import com.jws.common.constant.ConstantsCode;
import com.jws.common.util.EHCacheConfig;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.ResultPackaging;

@Controller("entryControllerPay")
public class WeixinPayControl {
	private final Logger logger = Logger.getLogger(this.getClass());
	// 日志
	@Resource(name = "interfaceLogService")
	private IInterfaceLogService interfaceLogService;
	/**
	 * 统一入口
	 * @param paramObject
	 *            json格式
	 * @return 
	 * @return
	 */
	@RequestMapping(value = "weixinpay/interface.do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String interfaceEntry(@RequestBody HashMap<String,String> paramObject, HttpServletRequest req, HttpServletResponse res) {
		
//		if(JiveGlobe.isEmpty(paramObject.get("ticket"))){
//			logger.info("####"+"tick不存在");
//			return "";
//		}else{
//			String ticket =paramObject.get("ticket").toString();
//			String openid = paramObject.get("openid").toString();
//			try {
//				String ticketEh = EHCacheConfig.get(openid).toString();
//				if(JiveGlobe.isEmpty(ticketEh) || !ticket.equalsIgnoreCase(ticketEh)){
//					logger.info("####ticket校验失败");
//					return "";
//				}
//			} catch (Exception e) {
//				logger.info("####缓存中无法获取ticket");
//				return "";
//			}
//		}
		//组装信息 被J8微信搞怕了
		if(JiveGlobe.isEmpty(paramObject.get("money")) || JiveGlobe.isEmpty(paramObject.get("openid")) ){
			logger.info("####获取支付参数失败");
			return "";
		}
		String noncestr = WXUtil.getNonceStr();
		double totalprice = Double.parseDouble(paramObject.get("money").toString());
		String openid = paramObject.get("openid").toString();
		WeixinJsbridgePojo _config = new WeixinJsbridgePojo();
		_config.openid = openid;
		String subject = "测试支付";
		_config.appid = "wx64719d9b69c5714d";
		_config.clientaddr = req.getRemoteAddr();
		_config.clientsendurl = "http://wx.fsdigital.cn/weixin/weixinpay/resultClient.jsp";
		_config.mchid = "1374938902";
		_config.notifyurl = "http://wx.fsdigital.cn/weixin/weixinpay/weixinjsbridge_notify.do";
		_config.setAPIKEY("F164CCB267B15E0C531ED6BE288E11B2");
		_config.useragent = req.getHeader("user-agent");
		/** 总金额(分为单位) */
		int total = (int) (totalprice * 100);
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		/** 公众号APPID */
		parameters.put("appid", _config.appid);
		/** 商户号 */
		parameters.put("mch_id", _config.mchid);
		/** 随机字符串 */
		parameters.put("nonce_str", noncestr);
		/** 商品名称 */
		parameters.put("body", subject);

		/** 当前时间 yyyyMMddHHmmss */
		String currTime = TenpayUtil.getCurrTime();
		/** 8位日期 */
		String strTime = currTime.substring(8, currTime.length());
		/** 四位随机数 */
		String strRandom = TenpayUtil.buildRandom(4) + "";
		/** 订单号 */
		parameters.put("out_trade_no", ""+System.currentTimeMillis());

		/** 订单金额以分为单位，只能为整数 */
		parameters.put("total_fee", total);
		/** 客户端本地ip */
		parameters.put("spbill_create_ip", _config.clientaddr);
		/** 支付回调地址 */
		parameters.put("notify_url", _config.notifyurl);
		/** 支付方式为JSAPI支付 */
		parameters.put("trade_type", "JSAPI");
		/** 用户微信的openid，当trade_type为JSAPI的时候，该属性字段必须设置 */
		parameters.put("openid", _config.openid);

		/** MD5进行签名，必须为UTF-8编码，注意上面几个参数名称的大小写 */
		String sign = createSign("UTF-8", parameters);
		parameters.put("sign", sign);

		/** 生成xml结构的数据，用于统一下单接口的请求 */
		String requestXML = getRequestXml(parameters);
		//logger.info("统一下单信息：" + requestXML);
		/** 开始请求统一下单接口，获取预支付prepay_id */
		HttpClient client = new HttpClient();
		PostMethod myPost = new PostMethod(_config.uniurl);
		client.getParams().setSoTimeout(300 * 1000);
		String result = null;
		try {
			myPost.setRequestEntity(new StringRequestEntity(requestXML, "text/xml", "utf-8"));
			int statusCode = client.executeMethod(myPost);
			if (statusCode == HttpStatus.SC_OK) {
				BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
				byte[] bytes = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int count = 0;
				while ((count = bis.read(bytes)) != -1) {
					bos.write(bytes, 0, count);
				}
				byte[] strByte = bos.toByteArray();
				result = new String(strByte, 0, strByte.length, "utf-8");
				bos.close();
				bis.close();
			}
		} catch (Exception e) {
			logger.error("###请求微信支付"+e);
		}
		/** 需要释放掉、关闭连接 */
		myPost.releaseConnection();
		client.getHttpConnectionManager().closeIdleConnections(0);

		//logger.info("请求统一支付接口的返回结果:"+result);
		//logger.info(result);
		try {
			/** 解析微信返回的信息，以Map形式存储便于取值 */
			Map<String, String> map = XMLUtil.doXMLParse(result);
			SortedMap<Object, Object> params = new TreeMap<Object, Object>();
			params.put("appId", _config.appid);
			params.put("timeStamp", "\"" + new Date().getTime() + "\"");
			params.put("nonceStr", WXUtil.getNonceStr());
			/**
			 * 获取预支付单号prepay_id后，需要将它参与签名。
			 * 微信支付最新接口中，要求package的值的固定格式为prepay_id=...
			 */
			params.put("package", "prepay_id=" + map.get("prepay_id"));

			/** 微信支付新版本签名算法使用MD5，不是SHA1 */
			params.put("signType", "MD5");
			/**
			 * 获取预支付prepay_id之后，需要再次进行签名，参与签名的参数有：appId、timeStamp、nonceStr、
			 * package、signType. 主意上面参数名称的大小写.
			 * 该签名用于前端js中WeixinJSBridge.invoke中的paySign的参数值
			 */
			String paySign = createSign("UTF-8", params);
			params.put("paySign", paySign);

			/** 预支付单号，前端ajax回调获取。由于js中package为关键字，所以，这里使用packageValue作为key。 */
			params.put("packageValue", "prepay_id=" + map.get("prepay_id"));

			/** 付款成功后，微信会同步请求我们自定义的成功通知页面，通知用户支付成功 */
			String clientsendurl = _config.clientsendurl;
			clientsendurl += "?prepay_id=" + map.get("prepay_id");
			clientsendurl += "&totalprice=" + totalprice;
			params.put("sendUrl", clientsendurl);
			/** 获取用户的微信客户端版本号，用于前端支付之前进行版本判断，微信版本低于5.0无法使用微信支付 */
			String userAgent = _config.useragent;
			char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger") + 15);
			params.put("agent", new String(new char[] { agent }));
			params.put("nonceNum", noncestr);
			String json = JSONArray.fromObject(params).toString();
			//logger.info("组装支付返回信息:"+json);
			return json;

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * sign签名
	 * 
	 * 作者: zhoubang 日期：2015年6月10日 上午9:31:24
	 * 
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<Object, Object>> es = parameters.entrySet();
		Iterator<Entry<Object, Object>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> entry = it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			/** 如果参数为key或者sign，则不参与加密签名 */
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		/** 支付密钥必须参与加密，放在字符串最后面 */
		sb.append("key=" + WeixinJsbridgePojo.getAPIKEY());
		/** 记得最后一定要转换为大写 */
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}
	/**
	 * 将请求参数转换为xml格式的string
	 * 
	 * 作者: zhoubang 日期：2015年6月10日 上午9:25:51
	 * 
	 * @param parameters
	 * @return
	 */
	public static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set<Entry<Object, Object>> es = parameters.entrySet();
		Iterator<Entry<Object, Object>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> entry = it.next();
			String k = (String) entry.getKey();
			String v = entry.getValue() + "";
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}
}
