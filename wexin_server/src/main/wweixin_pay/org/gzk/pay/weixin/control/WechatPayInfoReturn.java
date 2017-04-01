package org.gzk.pay.weixin.control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.rwl.pay.weixin.manager.WeixinJsbridgeManager;
import org.rwl.pay.weixin.servlet.WeixinJsbridgeNotifyServlet;
import org.rwl.pay.weixin.utils.XMLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jws.app.operater.data.IndusReportMapper;
import com.jws.app.operater.data.ProbelmPayWewiChatRecordMapper;
import com.jws.app.operater.model.ProbelmPayWewiChatRecord;
import com.jws.common.util.JiveGlobe;


@Controller("viewControlPay")
public class WechatPayInfoReturn {
	@Resource
	private ProbelmPayWewiChatRecordMapper probelmPayWewiChatRecordMapper;
	@Resource
	private IndusReportMapper indusReportMapper;
	private static Logger log = Logger.getLogger(WeixinJsbridgeNotifyServlet.class);

	/**
	 * 统一入口
	 * 
	 * @param paramObject
	 *            json格式
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "weixinpay/weixinjsbridge_notify.do" ,method = RequestMethod.POST)
	public void interfaceEntry(HttpServletRequest req,  HttpServletResponse resp) throws IOException{
		Map<Object, Object> map = new HashMap<>();
		try {
		log.info("微信支付成功调用回调URL");
		InputStream inStream = req.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		log.info("~~~~~~~~~~~~~~~~付款成功~~~~~~~~~");
		outSteam.close();
		inStream.close();

		/** 支付成功后，微信回调返回的信息 */
		String result = new String(outSteam.toByteArray(), "utf-8");
		//log.info("微信返回的订单支付信息:" + result);
		map = XMLUtil.doXMLParse(result);
		} catch (Exception e) {
			log.error("回写支付信息error+",e);
		}

		// 用于验签
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		for (Object keyValue : map.keySet()) {
			/** 输出返回的订单支付信息 */
		//	log.info(keyValue + "=" + map.get(keyValue));
			if (!"sign".equals(keyValue)) {
				parameters.put(keyValue, map.get(keyValue));
			}
		}
		if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
			WeixinJsbridgeManager.getInstance();
			// 先进行校验，是否是微信服务器返回的信息
			String checkSign = WeixinJsbridgeManager.createSign("UTF-8", parameters);
			//log.info("对服务器返回的结果进行签名：" + checkSign);
			//log.info("服务器返回的结果签名：" + map.get("sign"));
			if (checkSign.equals(map.get("sign"))) {// 如果签名和服务器返回的签名一致，说明数据没有被篡改过
				//log.info("签名校验成功，信息合法，未被篡改过");
				/** 告诉微信服务器，我收到信息了，不要再调用回调方法了 */
				/**
				 * 如果不返回SUCCESS的信息给微信服务器，则微信服务器会在一定时间内，多次调用该回调方法，如果最终还未收到回馈，
				 * 微信默认该订单支付失败
				 */
				/** 微信默认会调用8次该回调地址 */
				resp.getWriter().write(setXML("SUCCESS", ""));
		//		log.info("-------------" + setXML("SUCCESS", ""));
				//增加支付信息
				String nonce_str = map.get("nonce_str").toString();
				String openid = map.get("openid").toString();
				//返回的支付订单号
				String transaction_id = map.get("transaction_id").toString();
				//商户订单号
				String out_trade_no = map.get("out_trade_no").toString();
				//支付金额
				String  cash_fee = map.get("cash_fee").toString();
			
				if(!JiveGlobe.isEmpty(openid) && !JiveGlobe.isEmpty(cash_fee) && !JiveGlobe.isEmpty(transaction_id) && !JiveGlobe.isEmpty(out_trade_no)){
					//probelmPayWewiChatRecordMapper
					try {
						ProbelmPayWewiChatRecord record = new ProbelmPayWewiChatRecord();
						record.setCreateTime(indusReportMapper.selectCurrTime());
						record.setPaymentId(nonce_str);
						record.setAmountMoney(Integer.valueOf(cash_fee));
						record.setPaymentStatus(1);
						record.setSpareField1(transaction_id);
						record.setSpareField2(out_trade_no);
						record.setSpareField3(openid);
						probelmPayWewiChatRecordMapper.insert(record);
					} catch (Exception e) {
						log.error("存储订单信息失败:"+e);
					}
				
				}
				// log.info("我去掉了发送SUCCESS给微信服务器");

			}
		}
	}
	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
	}
	
}
