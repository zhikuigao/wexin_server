package com.jws.app.operater.service;

import java.util.Map;

public interface WechatReplayMessageControl {
			public String parseInEvent( Map<String, String> record,String FromUserName,String msgType,String toUserName,String createTime);
			public String parseInTextMsg( Map<String, String> record,String FromUserName,String msgType,String toUserName,String createTime);
}
