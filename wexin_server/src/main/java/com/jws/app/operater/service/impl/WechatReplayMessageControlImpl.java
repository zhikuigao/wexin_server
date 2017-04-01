package com.jws.app.operater.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jws.app.operater.service.WechatReplayMessageControl;
@Service("wechatReplayMessageControlImpl")
public class WechatReplayMessageControlImpl implements WechatReplayMessageControl {



	@Override
	public String parseInEvent(Map<String, String> record, String fromUserName,
			String msgType, String toUserName, String createTime) {
		// TODO Auto-generated method stub
	//	String toUserName, String fromUserName, Integer createTime, String msgType
		InTextMsg msg = new InTextMsg(toUserName,fromUserName,null,msgType);
		msg.setContent("您好，欢迎关注创伙伴！");
		return null;
	}

	@Override
	public String parseInTextMsg(Map<String, String> record,String fromUserName, String msgType, String toUserName,String createTime) {
		InTextMsg msg = new InTextMsg(toUserName,fromUserName,null,msgType);
		msg.setContent("您好，欢迎关注创伙伴！");
		return null;
	}

}
