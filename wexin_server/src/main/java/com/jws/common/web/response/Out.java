/**
 * Program  : Out.java
 */

package com.jws.common.web.response;

import java.util.List;

/**
 * 输出接口
 */
public interface Out<T> {
	/**
	 * 获取数据集
	 * @return
	 */
	public List<T> getRows();

	/**
	 * 获取提示信息
	 * @return
	 */
	public String getMessage();

	/**
	 * 是否成功
	 * @return
	 */
	public int getSuccess();

	/**
	 * 获取记录总数
	 * @return
	 */
	public int getTotal();
}
