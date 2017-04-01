package com.jws.common.data;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.jws.common.exception.DataException;

/**
 * 通用接口
 */
public interface IMapper<T> {
	/**
	 * 新增对象
	 * @param object
	 * @throws DataException
	 * @throws Exception
	 */
	public void save(@Param("object") T object) throws Exception;
	
	public void addBatch(@Param("list") List<T> list) throws Exception;

	/**
	 * 修改对象
	 * @param object
	 * @throws DataException
	 * @throws Exception
	 */
	public void update(@Param("object") T object) throws Exception;

	/**
	 * 删除对象
	 * @param object
	 * @throws DataException
	 * @throws Exception
	 */
	public void delete(@Param("object") T object) throws Exception;

	/**
	 * 删除所有相关记录
	 * @throws DataException
	 * @throws Exception
	 */
	public void deleteAll() throws Exception;

	/**
	 * 根据主键,删除对象
	 * @param key
	 * @throws DataException
	 * @throws Exception
	 */
	public void deleteByKey(@Param("key") Serializable key) throws Exception;

	/**
	 * 根据主键,获得对象
	 * @param key
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
	public T getByKey(@Param("key") Serializable key) throws Exception;

	/**
	 * 根据实例,获得对象集
	 * @param object
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
	public List<T> findByExample(@Param("object") T object) throws Exception;

	/**
	 * 根据实例,分页获得对象集,不支持like查找,只能精确查找
	 * @param object
	 * @param page
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
//	public List<T> findOnePageByExample(@Param("object") T object, @Param("page") Pagination page)
//			throws Exception;

	/**
	 * 根据实例,获得对象
	 * @param object
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
	public T findOneByExample(@Param("object") T object) throws Exception;

	/**
	 * 加载所有对象
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
	public List<T> loadAll() throws Exception;

	/**
	 * 获得当前页的对象(没有设置查询条件)
	 * @param page
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
//	public List<T> loadOnePage(@Param("page") Pagination page) throws Exception;

	/**
	 * 根据实例,分页获得对象集,如果字段是字符串,则使用模糊查找
	 * @param object
	 * @param page
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
//	public List<T> search(@Param("object") T object, @Param("page") Pagination page) throws Exception;

	/**
	 * 根据实例,获得记录条数,不支持like查找,只能精确查找.一般与findOnePageByExample一起使用
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public int getCountByExample(@Param("object") T object) throws Exception;

	/**
	 * 获得总记录条数,一般与loadOnePage一起使用
	 * @return
	 * @throws Exception
	 */
	public int getCount() throws Exception;

	/**
	 * 根据实例,获得记录条数,如果字段是字符串,则使用模糊查找(一般与search一起使用)
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public int getCountForSearch(@Param("object") T object) throws Exception;

}
