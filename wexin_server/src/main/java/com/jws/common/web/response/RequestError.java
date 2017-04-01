package com.jws.common.web.response;

/**
 * 请求error类
 * @author wujh
 *
 */
public class RequestError {
	// 错误代码
    public String code;
    // 错误信息
    public String msg;
    
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
    
    
}
