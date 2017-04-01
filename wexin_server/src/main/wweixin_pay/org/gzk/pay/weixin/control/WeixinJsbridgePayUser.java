package org.gzk.pay.weixin.control;


import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.rwl.pay.weixin.utils.MD5Util;
import org.rwl.pay.weixin.utils.WXUtil;
import org.rwl.pay.weixin.utils.XMLUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jws.app.operater.data.IndusReportMapper;
import com.jws.app.operater.data.ProbelmPayRecordMapper;
import com.jws.app.operater.data.ProblemDataMapper;
import com.jws.app.operater.data.ProblemKillRecordMapper;
import com.jws.app.operater.data.ProblemShareRecordMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.ProbelmPayRecord;
import com.jws.app.operater.model.ProblemData;
import com.jws.app.operater.model.ProblemKillRecord;
import com.jws.app.operater.model.ProblemShareRecord;
import com.jws.common.constant.Constants;
import com.jws.common.util.JiveGlobe;


public class WeixinJsbridgePayUser {
	
	public  WeixinJsbridgePayUser(){
		 ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		if(probelmPayRecordMapper==null){
            BeanFactory factory = (BeanFactory) appContext;
            probelmPayRecordMapper = (ProbelmPayRecordMapper) factory.getBean ("probelmPayRecordMapper");
   	}
		if(problemDataMapper==null){
            BeanFactory factory = (BeanFactory) appContext;
            problemDataMapper = (ProblemDataMapper) factory.getBean ("problemDataMapper");
   	}
		if(indusReportMapper==null){
            BeanFactory factory = (BeanFactory) appContext;
            indusReportMapper = (IndusReportMapper) factory.getBean ("indusReportMapper");
   	}
		
		if(userInfoMapper==null){
            BeanFactory factory = (BeanFactory) appContext;
            userInfoMapper = (UserInfoMapper) factory.getBean ("userInfoMapper");
   	}
		
		if(problemKillRecordMapper==null){
            BeanFactory factory = (BeanFactory) appContext;
            problemKillRecordMapper = (ProblemKillRecordMapper) factory.getBean ("problemKillRecordMapper");
   	}
		if(problemShareRecordMapper==null){
            BeanFactory factory = (BeanFactory) appContext;
            problemShareRecordMapper = (ProblemShareRecordMapper) factory.getBean ("problemShareRecordMapper");
   	}
	}
	@Resource
	private static ProbelmPayRecordMapper probelmPayRecordMapper;
	@Resource
	private static ProblemDataMapper problemDataMapper;
	@Resource
	private static IndusReportMapper indusReportMapper;
	@Resource
	private static UserInfoMapper userInfoMapper;
	@Resource 
	private static ProblemKillRecordMapper problemKillRecordMapper;
	@Resource
	private static ProblemShareRecordMapper problemShareRecordMapper;
	//微信发红包
	private final Logger logger = Logger.getLogger(this.getClass());
     private static String payUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
     private static String mchid = "1374938902";
     private static String appid = Constants.appid;
     private static String act_name="创伙伴支付";
     
     
    public  void requestPayInfo(){
    			try {
    				//拿到支付信息
    	        	List<ProbelmPayRecord> recordList = probelmPayRecordMapper.queryPayInfo();
    	        //	logger.info("产生支付记录："+recordList.size());
    	        	//检查支付信息
    	        	for(int i=0;i<recordList.size();i++){
    	        		ProbelmPayRecord record = recordList.get(i);
    	        	//	logger.info("产生支付信息："+record.getProbelemid());
    	        	//	if(WeixinJsbridgePayUser.isValidDate(record.getCreateTime())){
    	        			this.Gopay(record.getAmountMoney(),record.getOpenid(),record.getId());
    	        	//	}
    	        	}
				} catch (Exception e) {
					logger.error("###拉取支付信息"+e);
				}
    	
    }
    
    public void requestPay(){
    	try {
    		List<ProblemData>  pdList = problemDataMapper.queryOverdueProblem();
    		for(int i=0;i<pdList.size();i++){
    			ProbelmPayRecord record = new ProbelmPayRecord();
    			ProblemData pd = pdList.get(i);
    			//int status = pd.getStatusProblem();
    				problemDataMapper.updateProStatus(5, pd.getId());
    				List<ProblemKillRecord> killList = problemKillRecordMapper.queryProblemKillRecord(pd.getId()+"");
    				 List<ProblemShareRecord> shareList = problemShareRecordMapper.queryProblemShareFindList(pd.getId()+""); 
    				//发送下次到提现
    				 if(!JiveGlobe.isEmpty(killList) || !JiveGlobe.isEmpty(shareList)){
    						record.setAmountMoney(pd.getAmountMoney()*0.95);
    				 }else{
    						record.setAmountMoney(pd.getAmountMoney());
    				 }
        			record.setProbelemid(Integer.valueOf(pd.getId()));
        			record.setCreateTime(indusReportMapper.selectCurrTime());
        			record.setOpenid(pd.getOpenid());
        			record.setPaymentType(1);
        			record.setPaymentStatus(2);
        			record.setPaymentId(pd.getSpareField2());
        			probelmPayRecordMapper.insert(record);
    			//关闭状态
//    			if(!JiveGlobe.isEmpty(pd)){
//    				String phone = pd.getMobilePhone();
//    				if(!JiveGlobe.isEmpty(phone)){
//    					SDKTestSendTemplateSMS.getInstance().pushSmsNotice(phone, "118372");
//    				}
//    			}
    		}
		} catch (Exception e) {
			logger.error("###监测过期问题error"+e);
		}
    	
    }
     
    
    
     
     public void Gopay(double money,String openid,int problemId){
    	 try {
    		 	SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
    		 	String nonce_str =  WXUtil.getNonceStr();
    		 	int payMoney =  (int) (money * 100);
    		 	parameters.put("mch_appid", appid);
    		 	parameters.put("mchid", mchid);
    		 	parameters.put("nonce_str", nonce_str);
    		 	parameters.put("partner_trade_no", JiveGlobe.getFromRom26());
    		 	parameters.put("openid", openid);
    		 	parameters.put("check_name",  "NO_CHECK");
    		 	parameters.put("re_user_name", "");
    		 	parameters.put("amount", payMoney);
    		 	parameters.put("desc", act_name);
    		 	parameters.put("spbill_create_ip", InetAddress.getLocalHost().getHostAddress());
    		 	/**组装请求信息*/
    			String sign = createSign("UTF-8", parameters);
    			parameters.put("sign", sign);
    	 		String requestXML = getRequestXml(parameters);
    	 		/**#################################*/
    	 		/**#################################*/
    	 		/**#################################*/
    	 		/**#################################*/
    	 		//logger.info("###企业付款请求信息"+requestXML);
    	 		KeyStore keyStore  = KeyStore.getInstance("PKCS12");
    	 		FileInputStream instream = new FileInputStream(new File("/usr/weixin/apiclient_cert.p12"));
    	 	//	FileInputStream instream = new FileInputStream(new File("D:/apiclient_cert.p12"));
    	 		  try {
    	 	            keyStore.load(instream, "1374938902".toCharArray());
    	 	        } finally {
    	 	            instream.close();
    	 	        }
    	 		 // Trust own CA and all self-signed certs
    	 	    SSLContext sslcontext = SSLContexts.custom() .loadKeyMaterial(keyStore, "1374938902".toCharArray()).build();
    	 	        // Allow TLSv1 protocol only
    	 	    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,new String[] { "TLSv1" },null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    	 	    CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf) .build();
    	 	    //请求支付
    	 	   HttpPost httpost = new HttpPost(payUrl);
    	 	   httpost.addHeader("Connection", "keep-alive");
    	        httpost.addHeader("Accept", "*/*");
    	        httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    	        httpost.addHeader("Host", "api.mch.weixin.qq.com");
    	        httpost.addHeader("X-Requested-With", "XMLHttpRequest");
    	        httpost.addHeader("Cache-Control", "max-age=0");
    	        httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
    	        httpost.setEntity(new StringEntity(requestXML, "utf-8"));
    	        CloseableHttpResponse response = httpclient.execute(httpost);
    	        HttpEntity entity = response.getEntity();
    		//	String result = null;
    			String jsonStr = EntityUtils .toString(response.getEntity(), "UTF-8");
    			EntityUtils.consume(entity);
    			//logger.info("请求统一红包的返回结果:"+jsonStr);
    			//解析微信返回的信息
    			Map<String, String> map = XMLUtil.doXMLParse(jsonStr);
    			//SortedMap<Object, Object> map = new TreeMap<Object, Object>();
    			String return_code  = map.get("return_code").toString();
    			String return_msg = map.get("return_msg").toString();
    			if(!JiveGlobe.isEmpty(map) && return_code.equalsIgnoreCase("SUCCESS") && JiveGlobe.isEmpty(return_msg) ){
    				//更改数据库操作
    				//logger.info("###支付ID"+problemId);
    				probelmPayRecordMapper.updatePayInfoPersion(problemId);
//    				String phone = userInfoMapper.queryUserPhone(openid);
//    				if(JiveGlobe.isEmpty(phone)){
//    					if(type==2){
//    						SDKTestSendTemplateSMS.getInstance().pushSmsNotice(phone, "118370");
//        				}else if(type==3){
//        					SDKTestSendTemplateSMS.getInstance().pushSmsNotice(phone, "118368");
//        				}
//    				}
    			}else{
    				//付款失败,涉及到回滚操作
    				logger.info("#####付款失败#####");
    				logger.info("###失败信息"+map.toString());
    			}
		} catch (Exception e) {
			logger.info("###支付出错问题ID"+problemId);
			logger.error("###发放红包error"+e);
		}
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
 		sb.append("key=" + "F164CCB267B15E0C531ED6BE288E11B2");
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

	//检查日期
	public static boolean isValidDate(Date date){
		boolean isTrue = false;
		if(!JiveGlobe.isEmpty(date)){
	            Calendar cal = Calendar.getInstance();  
	            //当前日期
	            int day=  cal.get(Calendar.DAY_OF_MONTH);
	            cal.setTime(date);
	            int dbDay=cal.get(Calendar.DAY_OF_MONTH);
	            if(day!=dbDay){
	            	return true;
	            }
		}
		return isTrue;
	}
}
