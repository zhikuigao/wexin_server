/**
 * PaginationIntercepter.java
 */
package com.jws.common.util.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.log4j.Logger;

import com.jws.common.bean.Pagination;

/**
 * 分页拦截器
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class})})
public class PaginationIntercepter implements Interceptor {

	private Dialect dialect = Dialect.getInstance(Dialect.MYSQL);
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
		Pagination pagination = getPaginationParamter(invocation);
		if (pagination == null) {
			return invocation.proceed();
		}
		BoundSql boundSql = handler.getBoundSql();
		// 获取分页sql，从hibernate拷贝而来(mysql/oracle/db2/h2/sqlserver)
		String sql = dialect.getLimitString(boundSql.getSql(), pagination);
		try {
			Field sqlField = boundSql.getClass().getDeclaredField("sql");
			if (sqlField != null) {
				sqlField.setAccessible(true);
				sqlField.set(boundSql, sql);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return invocation.proceed();
	}

	/**
	 * 包装目标类(Plugin.wrap(target, this),即,代理)
	 * @param target
	 * @return Object
	 */
	@Override
	public Object plugin(Object target) {
		// 能够走到这里的target为以下几种(可以推断Signature注解该写什么)：
		// (1):org.apache.ibatis.executor.Executor(调用Configuration.newExecutor时,以下同)
		// (2):org.apache.ibatis.executor.parameter.ParameterHandler(newParameterHandler)
		// (3):org.apache.ibatis.executor.resultset.ResultSetHandler(newResultSetHandler)
		// (4):org.apache.ibatis.executor.statement.StatementHandler(newStatementHandler)
		// 当目标类是StatementHandler类型时，才可能包装目标类，否者直接返回目标本身,减少目标被代理的次数
		if (target instanceof StatementHandler) {
			StatementHandler tmp = (StatementHandler) target;
			// 如果是select 语句,则，包装
			// update/insert/delete……不包装
			// 也可以判断sqlCommandType:target-->delegate-->mappedStatement-->sqlCommandType,delegate是私有属性
			if (tmp.getBoundSql() != null && this.startWithSelect(tmp.getBoundSql().getSql())) {
				return Plugin.wrap(target, this);
			}
		}
		return target;
	}

	/**
	 * 获取Mapper方法的Pagination参数
	 * @param invocation
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Pagination getPaginationParamter(Invocation invocation) {
		if (invocation == null || invocation.getTarget() == null) {
			return null;
		}
		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
		if (handler == null || handler.getParameterHandler() == null) {
			return null;
		}
		Object parameters = handler.getParameterHandler().getParameterObject();
		if (parameters == null) {
			return null;
		}
		// 如果mapper方法只有pagination参数，则直接返回
		if (parameters instanceof Pagination) {
			return (Pagination) parameters;
		}
		// 如果是多个参数，则判断有没有pagination参数
		if (parameters instanceof Map) {
			Map tmp = (Map) parameters;
			for (Iterator iterator = tmp.values().iterator(); iterator.hasNext();) {
				Object next = iterator.next();
				if (next instanceof Pagination) {
					return (Pagination) next;
				}
			}
		}
		return null;
	}

	/**
	 * 判断是否是select语句
	 * @param sql
	 * @return
	 */
	private boolean startWithSelect(String sql) {
		if (sql == null) {
			return false;
		}
		String tmp = sql.trim();
		if (tmp.length() < 6) {
			return false;
		}
		if ("select".equalsIgnoreCase(tmp.substring(0, 6))) {
			return true;
		}
		return false;
	}

	@Override
	public void setProperties(Properties properties) {
		if (properties.getProperty("dataBase") != null) {
			dialect = Dialect.getInstance(properties.getProperty("dataBase").toLowerCase());
		}
	}
}
