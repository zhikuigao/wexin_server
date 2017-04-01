package com.jws.common.exception;

/**
 * 数据库访问异常
 */
public class DataException extends Exception {
	private static final long serialVersionUID = 1L;

	public DataException() {
		super("dao.DataAccessException");
	}

	public DataException(String messageKey, Throwable cause) {
		super(messageKey, cause);
	}

	public DataException(String messageKey) {
		super(messageKey);
	}

	public DataException(Throwable cause) {
		super("dao.DataAccessException", cause);
	}

}
