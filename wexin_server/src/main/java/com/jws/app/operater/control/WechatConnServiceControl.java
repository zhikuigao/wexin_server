package com.jws.app.operater.control;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jws.app.operater.data.IndusReportMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.service.IInterfaceLogService;
import com.jws.common.constant.BusiConstants;
import com.jws.common.util.Xmlutil;

@Controller("viewControl")
public class WechatConnServiceControl {
	@Resource
	private IndusReportMapper  indusReportMapper;
	// 日志
	@Resource(name = "interfaceLogService")
	private IInterfaceLogService interfaceLogService;
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private UserInfoMapper userInfoMapper;
	/**
	 * 统一入口
	 * 
	 * @param paramObject
	 *            json格式
	 * @return
	 */
	@RequestMapping(value = "/wecahtControl/interfaceEntry.do" ,method = RequestMethod.POST)
	public void interfaceEntry(HttpServletRequest request,  HttpServletResponse response){
		String fromUserName = "";
		String msgType = "";
		String event="";
		String toUserName = "";
		Integer createTime = 0;
		try {
		  response.setContentType("text/xml");
		 request.setCharacterEncoding("UTF-8");  
	     response.setCharacterEncoding("UTF-8");  
		InputStream inputStream = request.getInputStream();  
	    String paramObject = IOUtils.toString(inputStream, "UTF-8");
		//logger.info("###接受微信消息"+paramObject);
		//paramObject = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName><![CDATA[FromUser]]></FromUserName><CreateTime>123456789</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[CLICK]]></Event><EventKey><![CDATA[EVENTKEY]]></EventKey></xml>";
			/**
			 * <xml>
				<ToUserName><![CDATA[toUser]]></ToUserName>
				<FromUserName><![CDATA[FromUser]]></FromUserName>
				<CreateTime>123456789</CreateTime>
				<MsgType><![CDATA[event]]></MsgType>
				<Event><![CDATA[CLICK]]></Event>
				<EventKey><![CDATA[EVENTKEY]]></EventKey>
				</xml>
			 */
		
			 Map<String, String> record = Xmlutil.getInstance().xmlToMaping(paramObject);
			 fromUserName = record.get("FromUserName");
			 msgType = record.get("MsgType");
			 toUserName = record.get("ToUserName");
			  createTime  =  Integer.parseInt(record.get("CreateTime"));
			 if(msgType.equals(BusiConstants.TYPE_EVENT)){
				 	 //事件消息
				  parseInEvent(record,createTime, response);
			}else if(msgType.equals(BusiConstants.TYPE_TXT)){
					//普通消息
				 parseInTextMsg(record,createTime, response);
			}
		} catch (Exception e) {
			logger.error("###记录调用日志失败："+e);
		}
	
	}
	
	private  void parseInTextMsg(Map<String, String> record ,Integer createTime,HttpServletResponse response) {
		//InTextMsg msg = new InTextMsg(toUserName, fromUserName, createTime, msgType);
		//msg.setContent(replyPutong);
	//	msg.setMsgId(root.elementText("MsgId"));
		try {
		
			String  replyMessage="<xml>\n" +
					"<ToUserName><![CDATA["+record.get("FromUserName")+"]]></ToUserName>\n" +
					"<FromUserName><![CDATA["+record.get("ToUserName")+"]]></FromUserName>\n" +
					"<CreateTime>"+createTime+"</CreateTime>\n" +
					"<MsgType><![CDATA[text]]></MsgType>\n" +
					"<Content><![CDATA[欢迎关注创伙伴！！！]]></Content>\n" +
					"</xml>";
				//	logger.info("###回复消息"+replyMessage);
			  		 // 响应消息  
			        PrintWriter out = response.getWriter();  
			        out.print(replyMessage);  
			        out.close(); 
		} catch (Exception e) {
			logger.error("###回复消息出现问题"+e);
		}
		
	}
	
	// 解析各种事件
		private  void parseInEvent(Map<String, String> record ,Integer createTime,HttpServletResponse response) {
			String event = record.get("Event");
			try {
				// 关注/取消关注事件（包括二维码扫描关注，二维码扫描关注事件与扫描带参数二维码事件是两回事）
				if (("subscribe".equals(event) ) ) {
				//	return new InFollowEvent(toUserName, fromUserName, createTime, msgType, event);	
					  		String   replyMessage="<xml>\n" +
							"<ToUserName><![CDATA["+record.get("FromUserName")+"]]></ToUserName>\n" +
							"<FromUserName><![CDATA["+record.get("ToUserName")+"]]></FromUserName>\n" +
							"<CreateTime>"+createTime+"</CreateTime>\n" +
							"<MsgType><![CDATA[text]]></MsgType>\n" +
							"<Content><![CDATA[欢迎关注创伙伴！！！]]></Content>\n" +
							"</xml>";
					  		 // 响应消息
					  		//logger.info("###回复事件"+replyMessage);
					        PrintWriter out = response.getWriter();  
					        out.print(replyMessage);  
					        out.close(); 
				}else if("unsubscribe".equals(event)){
					
				}
			} catch (Exception e) {
				logger.error("###回复消息出现问题"+e);
			}
		}
}
