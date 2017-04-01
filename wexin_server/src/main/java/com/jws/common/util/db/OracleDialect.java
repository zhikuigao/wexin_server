/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */

package com.jws.common.util.db;

import com.jws.common.bean.Pagination;

/**
 * copy from hibernate(请勿改变，包括文件最开始的Copyright注释，尊重作者)
 */
public class OracleDialect extends Dialect {

	/**
	 * copy from hibernate,分页
	 * @param sql
	 * @param hasOffset
	 * @return
	 */
	@Override
	public String getLimitString(String sql, Pagination pagination) {
		sql = sql.trim();
		String forUpdateClause = null;
		boolean isForUpdate = false;
		final int forUpdateIndex = sql.toLowerCase().lastIndexOf("for update");
		if (forUpdateIndex > -1) {
			// save 'for update ...' and then remove it
			forUpdateClause = sql.substring(forUpdateIndex);
			sql = sql.substring(0, forUpdateIndex - 1);
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		if (pagination.getPageNumber() > 1) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		} else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (pagination.getPageNumber() > 1) {
			pagingSelect.append(" ) row_ where rownum <= ").append(pagination.getStart() + pagination.getPageSize())
					.append(") where rownum_ > ").append(pagination.getStart());
		} else {
			pagingSelect.append(" ) where rownum <= ").append(pagination.getPageSize());
		}

		if (isForUpdate) {
			pagingSelect.append(" ");
			pagingSelect.append(forUpdateClause);
		}

		return pagingSelect.toString();
	}
}
