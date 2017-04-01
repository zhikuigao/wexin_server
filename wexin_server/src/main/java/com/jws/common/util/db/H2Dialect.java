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
public class H2Dialect extends Dialect {

	/**
	 * copy from hibernate
	 * @param sql
	 * @param hasOffset
	 * @return
	 */
	@Override
	public String getLimitString(String sql, Pagination pagination) {
		// return new StringBuffer(sql.length() +
		// 20).append(sql).append(hasOffset ? " limit ? offset ?" : " limit ?")
		// .toString();
		return new StringBuffer(sql.length() + 20).append(sql).append(" limit ").append(pagination.getStart())
				.append(" offset ").append(pagination.getPageSize()).toString();
	}

}
