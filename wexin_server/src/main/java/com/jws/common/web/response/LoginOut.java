package com.jws.common.web.response;

import java.util.List;

public class LoginOut<T> implements BasicEntity<T>{
	/**
	 * 是否成功
	 */
	private int success;
	/**
	 * 输出信息
	 */
	private String message;
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	
	public void setSuccess(int success) {
		this.success = success;
	}
	
	@Override
	public int getSuccess() {
		// TODO Auto-generated method stub
		return success;
	}
	
	public List<T> getRows() {
		return null;
	}
}
