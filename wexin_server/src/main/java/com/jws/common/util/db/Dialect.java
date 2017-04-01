/**
 * Program  : Dialect.java
 */

package com.jws.common.util.db;

import com.jws.common.bean.Pagination;

/**
 * 数据库方言类
 */
public abstract class Dialect {

	public static final String MYSQL = "mysql";
	public static final String ORACLE = "oracle";
	public static final String SQLSERVER = "sqlserver";
	public static final String DB2 = "db2";
	public static final String H2 = "h2";

	/**
	 * 获取分页sql,具体实现都是从hibernate中拷贝而来,copy from hibernate
	 * @param sql
	 * @param pagination
	 * @return
	 * @return String
	 */
	public abstract String getLimitString(String sql, Pagination pagination);

	/**
	 * 获取具体方言
	 * @param dataBaseName
	 * @return
	 * @return Dialect
	 */
	public static final Dialect getInstance(String dataBaseName) {
		switch (dataBaseName) {
			case MYSQL :
				return new MySqlDialect();
			case ORACLE :
				return new OracleDialect();
			case SQLSERVER :
				return new SQLServerDialect();
			case DB2 :
				return new DB2Dialect();
			case H2 :
				return new H2Dialect();
			default :
				return new MySqlDialect();
		}
	}
}
