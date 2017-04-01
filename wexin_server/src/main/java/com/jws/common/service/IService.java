package com.jws.common.service;

import java.io.Serializable;
import java.util.List;

import com.jws.common.bean.Pagination;
import com.jws.common.exception.ServiceException;

/**
 * service 接口
 */
public interface IService<T> {

	/**
	 * 保存对象
	 * @param object
	 */
	public void save(T object) throws ServiceException;
	
	/**
	 * 保存对象串
	 * @param list
	 * @throws ServiceException
	 */
	public void addBatch(List<T> list) throws ServiceException;

	/**
	 * 更新对象
	 * @param object
	 */
	public void update(T object) throws ServiceException;

	/**
	 * 根据主键,获得对象
	 * @param key
	 * @return
	 */
	public T getByKey(Serializable key) throws ServiceException;

	/**
	 * 删除对象
	 * @param object
	 */
	public void delete(T object) throws ServiceException;

	/**
	 * 删除对象所有相关记录
	 * 
	 */
	public void deleteAll() throws ServiceException;

	/**
	 * 根据主键,删除对象
	 * @param key
	 */
	public void deleteByKey(Serializable key) throws ServiceException;

	/**
	 * 根据实例,获得对象集
	 * @param object
	 */
	public List<T> findByExample(T object) throws ServiceException;

	/**
	 * 根据实例,分页获得对象集,不支持like查找,只能精确查找
	 * @param object
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	//public List<T> findOnePageByExample(T object, Pagination page) throws ServiceException;

	/**
	 * 根据实例,获得对象
	 * @param object
	 * @return
	 * @throws ServiceException
	 */
	public T findOneByExample(T object) throws ServiceException;

	/**
	 * 加载所有对象
	 * @return
	 * @throws ServiceException
	 */
	public List<T> loadAll() throws ServiceException;

	/**
	 * 获得当前页的对象(没有设置查询条件)
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<T> loadOnePage(Pagination page) throws ServiceException;

	/**
	 * 根据实例,分页获得对象集,如果字段是字符串,则使用模糊查找
	 * @param object
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<T> search(T object, Pagination page) throws ServiceException;

}
