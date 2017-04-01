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
 * copy from hibernate(请勿改变，包括文件最开始的hibernate Copyright注释，尊重作者)
 */
public class DB2Dialect extends Dialect {

	/**
	 * copy from hibernate
	 * @param sql
	 * @param hasOffset
	 * @return
	 */
	@Override
	public String getLimitString(String sql, Pagination pagination) {
		int startOfSelect = sql.toLowerCase().indexOf("select");

		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100).append(sql.substring(0, startOfSelect)) // add
																													// the
																													// comment
				.append("select * from ( select ") // nest the main query in an
													// outer select
				.append(getRowNumber(sql)); // add the rownnumber bit into the
											// outer query select list

		if (hasDistinct(sql)) {
			pagingSelect.append(" row_.* from ( ") // add another (inner) nested
													// select
					.append(sql.substring(startOfSelect)) // add the main query
					.append(" ) as row_"); // close off the inner nested select
		} else {
			pagingSelect.append(sql.substring(startOfSelect + 6)); // add the
																	// main
																	// query
		}

		pagingSelect.append(" ) as temp_ where rownumber_ ");

		// add the restriction to the outer select
		if (pagination.getPageNumber() > 1) {
			pagingSelect.append("between ").append(pagination.getStart() + 1).append(" and ")
					.append(pagination.getPageSize());
			// pagingSelect.append("between ?+1 and ?");
		} else {
			// pagingSelect.append("<= ?");
			pagingSelect.append("<= ").append(pagination.getPageSize());
		}

		return pagingSelect.toString();
	}

	private static final boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct") >= 0;
	}

	/**
	 * Render the <tt>rownumber() over ( .... ) as rownumber_,</tt> bit, that
	 * goes in the select list
	 */
	private static final String getRowNumber(String sql) {
		StringBuffer rownumber = new StringBuffer(50).append("rownumber() over(");

		int orderByIndex = sql.toLowerCase().indexOf("order by");

		if (orderByIndex > 0 && !hasDistinct(sql)) {
			rownumber.append(sql.substring(orderByIndex));
		}

		rownumber.append(") as rownumber_,");

		return rownumber.toString();
	}
}
