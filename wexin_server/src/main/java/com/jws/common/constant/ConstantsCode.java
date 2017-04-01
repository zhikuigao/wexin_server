package com.jws.common.constant;




/**
 * 系统常量
 * @author wujh	
 *
 */
public class ConstantsCode {
	private ConstantsCode() {
	}

	
	/**
	 * 接口返回结果
	 */
	public static final int RESULT_CODE_SUCCESS = 1;//成功
	public static final int RESULT_CODE_FAIL = 0;   //失败
	public static final int RESULT_CODE_FAIL1 = 3;   //登录ticket失效
	public static final int RESULT_CODE_FAIL4 = 4;   //未定义
	public static final int RESULT_CODE_FAIL5 = 5;   //已存在
	//公共
	public static final int CODE_TURN_JSON=101;
	public static final int CODE_LACK_PARAMETER=102;
	public static final int CODE_ERROR_BUSICODE=103;
	public static final int CODE_LACK_PARAMETER_HEADER=104;
	public static final int CODE_EXCEPTION=105;
	public static final int CODE_PARAMETER_ERROR=106;
	public static final int CODE_UPDATE_ERROR=107;
	public static final int CODE_PASE_STREAM=108;
	public static final int CODE_PASE_VERSION=109;
	public static final int CODE_PASE_TOPIC = 110;
	public static final int CODE_PASE_EXTIS = 111;
	
	//用户
	public static final int CODE_SESSION_INVALID=3;
	public static final int LOGIN_CODE_FAIL=2;
	public static final int CODE_ACCOUNT_EXIST=1001;
	public static final int CODE_USER_NAME_LENGTH=1002;
	public static final int CODE_ACCOUNT_FORMAT=1003;
	public static final int CODE_REGISTER_FAIL=1004;
	public static final int CODE_LOGIN_FAIL=1005;
	public static final int CODE_NO_REGISTER=1006;
	public static final int CODE_SEND_EMAIL=1007;
	public static final int CODE_SETTING_PWD=1008;
	public static final int CODE_OLDPWD_ERROR=1009;
	public static final int CODE_VERIFY_ERROR=1010;
	public static final int CODE_VERIFY_INVALID=1011;
	public static final int CODE_USER_INFO=1012;
	public static final int CODE_UPLOAD_FAIL=1013;
	

}
