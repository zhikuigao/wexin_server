/**
 * Program  : DataOut.java
 */

package com.jws.common.web.response;

import java.util.List;

import com.jws.common.bean.Pagination;

/**
 * 数据输出类
 */
public class DataOut<T> implements Out<T> {

	private Pagination pagination;
	private List<T> rows;

	public DataOut(List<T> rows, Pagination pagination) {
		this.rows = rows;
		this.pagination = pagination;
	}

	@Override
	public List<T> getRows() {
		return rows;
	}

	@Override
	public String getMessage() {
		return null;
	}

	@Override
	public int getSuccess() {
		return 1;
	}

	@Override
	public int getTotal() {
		if (this.pagination == null) {
			return 0;
		}
		return this.pagination.getTotalRows();
	}

	// extends
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
