package com.jws.common.web.response;


public interface BasicEntity<T>{
	

	/**
	 * 获取提示信息
	 * @return
	 */
	public String getMessage();

	/**
	 * 是否成功
	 * @return
	 */
	public int getSuccess();
}
