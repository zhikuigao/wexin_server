package com.jws.common.constant;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public  class  ConstantZh {
	@SuppressWarnings("rawtypes")
	public final static Map map = new HashMap();  
	// 1.需吧code反馈给用户， 其他为程序问题
	static {  
		  map.put(1, "成功"); 
		  map.put(2, "入参json对象为空"); 
		  map.put(3, "ticket失效，请重新登录");
		  map.put(4, "jpush发送失败");
		  map.put(5, "已经存在");
		  map.put(101, "入参转成json对象出错");  
		  map.put(102, "缺少请求参数"); 
		  map.put(103, "busicode有误");
		  map.put(104, "header内缺少请求参数");
		  map.put(105, "程序异常");
		  map.put(106, "请求参数有误");
		  map.put(107, "未查到用户手机号");
		  map.put(109, "未查到用户信息");		
		  map.put(1001, "验证码发送失败"); 
		  map.put(1002, "验证码已过期"); 
		  map.put(1003, "验证失败"); 
		  map.put(1004, "生成图片失败"); 
		  map.put(1005, "登录失败，账号或密码有误");
		  map.put(1006, "用户未注册");
		  map.put(1007, "缺少session参数");
		  map.put(1008, "ticket校验错误");
		  map.put(1009, "ticket丢失");
		  map.put(1010, "问题信息不实");
		  map.put(1011, "未查到提现信息");
		  map.put(1012, "此手机号已经注册");
		  map.put(1013, "回答次数超过3次");
		
	}

}
