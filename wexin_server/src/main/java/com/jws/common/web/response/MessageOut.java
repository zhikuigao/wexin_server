/**
 * Program  : Message.java
 */

package com.jws.common.web.response;

import java.util.List;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jws.common.util.JsonUtil;

/**
 * 文本消息输出类
 */
public class MessageOut<T> implements Out<T> {

	/**
	 * 是否成功
	 */
	private int success;
	/**
	 * 输出信息
	 */
	private String message;

	public MessageOut() {
	}

	public MessageOut(int success, String message) {
		this.success = success;
		this.message = message;
	}

	@Override
	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}
	@Override
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	// 登录成功/失败 提示信息
	public static final MessageOut<Object> LOGIN_OK_MESSAGE = new MessageOut<Object>(1, "login success");//登录成功
	public static final MessageOut<Object> LOGIN_FAIL_MESSAGE = new MessageOut<Object>(0, "login failed, account or password error");//登录失败,账号 或 密码 有误
	// 登录成功/失败 提示信息
	 public static final String LOGIN_OK = JsonUtil.toJSONString(LOGIN_OK_MESSAGE);//"登录成功";
	 public static final String LOGIN_FAIL = JsonUtil.toJSONString(LOGIN_FAIL_MESSAGE);//"登录失败,账号 或 密码 有误";

	// 退出成功/失败 提示信息
	public static final MessageOut<Object> LOGOUT_OK_MESSAGE = new MessageOut<Object>(1, "login out success");//退出成功
	public static final MessageOut<Object> LOGOUT_FAIL_MESSAGE = new MessageOut<Object>(0, "login out failed, please refresh and try again");//退出失败,请刷新再试
	// 退出成功/失败 提示信息
	// public static final String LOGOUT_OK = "退出成功";
	// public static final String LOGOUT_FAIL = "退出失败,请刷新再试";

	// 名称已存在 提示信息
	// public static final String NAME_EXIST = "此名称已存在,请确认";
	public static final MessageOut<Object> NAME_EXIST_MESSAGE = new MessageOut<Object>(0, "this name has been exist, please confirm");//此名称已存在,请确认
	public static final MessageOut<Object> NAME_OR_CODE_EXIST_MESSAGE = new MessageOut<Object>(0, "account  or name already exists, please confirm");//账号或名称已存在,请确认
	// 记录已经删除 提示信息
	// public static final String RECORD_UN_EXIST = "此记录已删除,请刷新再试";
	public static final MessageOut<Object> RECORD_UN_EXIST_MESSAGE = new MessageOut<Object>(0, "this record has been deleted, please refresh and try again ");//此记录已删除,请刷新再试
	// 无权修改他人信息
	public static final MessageOut<Object> NO_PERMIT_UPDATE_MESSAGE = new MessageOut<Object>(0, "has no right to edit other's infomation");//无权修改他人信息

	// 添加成功/失败 提示信息
	public static final MessageOut<Object> ADD_OK_MESSAGE = new MessageOut<Object>(1, "add success");//新增成功
	public static final MessageOut<Object> ADD_FAIL_MESSAGE = new MessageOut<Object>(0, "add failed, please refresh and try again");//新增失败,请刷新再试
	// public static final String ADD_OK = "新增成功";
	// public static final String ADD_FAIL = "新增失败,请刷新再试";

	// 修改成功/失败 提示信息
	public static final MessageOut<Object> UPDATE_OK_MESSAGE = new MessageOut<Object>(1, "update success");//修改成功
	public static final MessageOut<Object> UPDATE_FAIL_MESSAGE = new MessageOut<Object>(0, "update failed, please refresh and try again");//
	// 修改成功/失败 提示信息
	// public static final String UPDATE_OK = "修改成功";
	// public static final String UPDATE_FAIL = "修改失败,请刷新再试";

	// 删除成功/失败
	public static final MessageOut<Object> DELETE_OK_MESSAGE = new MessageOut<Object>(1, "delete success");//删除成功
	public static final MessageOut<Object> DELETE_FAIL_MESSAGE = new MessageOut<Object>(0, "delete failed,  please refresh and try again");//删除失败,请刷新再试
	// 删除成功/失败
	// public static final String DELETE_OK = "删除成功";
	// public static final String DELETE_FAIL = "删除失败,请刷新再试";

	@Override
	public List<T> getRows() {
		return null;
	}

	@Override
	public int getTotal() {
		return 0;
	}

	//@Override
	public String getSessionID() {
		// TODO Auto-generated method stub
		String sessionID=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getId();
		//System.out.println(sessionID);
		return sessionID;
	}

}
