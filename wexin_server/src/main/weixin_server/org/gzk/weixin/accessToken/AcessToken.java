package org.gzk.weixin.accessToken;

public class AcessToken {
	
	private String access_token;	// 正确获取到 access_token 时有值
	private String expires_in;		// 正确获取到 access_token 时有值
	private String errcode;		// 出错时有值
	private String errmsg;			// 出错时有值
	private Long expiredTime;		// 正确获取到 access_token 时有值，存放过期时间
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public Long getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(Long expiredTime) {
		this.expiredTime = expiredTime;
	}
	
	
}			
