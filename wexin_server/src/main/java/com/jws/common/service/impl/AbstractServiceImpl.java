package com.jws.common.service.impl;

import java.io.Serializable;
import java.util.List;

import com.jws.common.data.IMapper;
import com.jws.common.exception.ServiceException;
import com.jws.common.service.IService;

/**
 * 实现通用接口
 */
public abstract class AbstractServiceImpl<T> implements IService<T> {

	public abstract IMapper<T> getMapper();

	@Override
	public void save(T object) throws ServiceException {
		try {
			this.getMapper().save(object);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void addBatch(List<T> list) throws ServiceException{
		try {
			this.getMapper().addBatch(list);
		} catch (Exception e) {
			// TODO: handle exception
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void update(T object) throws ServiceException {
		try {
			this.getMapper().update(object);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public T getByKey(Serializable key) throws ServiceException {
		try {
			return this.getMapper().getByKey(key);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void delete(T object) throws ServiceException {
		try {
			this.getMapper().delete(object);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteAll() throws ServiceException {
		try {
			this.getMapper().deleteAll();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteByKey(Serializable key) throws ServiceException {
		try {
			this.getMapper().deleteByKey(key);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<T> findByExample(T object) throws ServiceException {
		try {
			return this.getMapper().findByExample(object);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public T findOneByExample(T object) throws ServiceException {
		try {
			return this.getMapper().findOneByExample(object);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<T> loadAll() throws ServiceException {
		try {
			return this.getMapper().loadAll();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

//	@Override
//	public List<T> findOnePageByExample(T object, Pagination page) throws ServiceException {
//		try {
//			List<T> list = this.getMapper().findOnePageByExample(object, page);
//			// 如果记录数不够，则不用查询数据，直接计算出记录条数，前提：页面不能提交错误的pageNumber
//			if (list == null || list.size() < page.getPageSize()) {
//				page.setTotalRows((page.getPageNumber() - 1) * page.getPageSize() + (list == null ? 0 : list.size()));
//			} else {
//				page.setTotalRows(this.getMapper().getCountByExample(object));
//			}
//			return list;
//		} catch (Exception e) {
//			throw new ServiceException(e);
//		}
//	}
//
//	@Override
//	public List<T> loadOnePage(Pagination page) throws ServiceException {
//		try {
//			List<T> list = this.getMapper().loadOnePage(page);
//			// 如果记录数不够，则不用查询数据，直接计算出记录条数，前提：页面不能提交错误的pageNumber
//			if (list == null || list.size() < page.getPageSize()) {
//				page.setTotalRows((page.getPageNumber() - 1) * page.getPageSize() + (list == null ? 0 : list.size()));
//			} else {
//			page.setTotalRows(this.getMapper().getCount());
//			}
//			return list;
//		} catch (Exception e) {
//			throw new ServiceException(e);
//		}
//	}
//	@Override
//	public List<T> search(T object, Pagination page) throws ServiceException {
//		try {
//			List<T> list = this.getMapper().search(object, page);
//			// 如果记录数不够，则不用查询数据，直接计算出记录条数，前提：页面不能提交错误的pageNumber
//			if (list == null || list.size() < page.getPageSize()) {
//				page.setTotalRows((page.getPageNumber() - 1) * page.getPageSize() + (list == null ? 0 : list.size()));
//			} else {
//				page.setTotalRows(this.getMapper().getCountForSearch(object));
//			}
//			return list;
//		} catch (Exception e) {
//			throw new ServiceException(e);
//		}
//	}
//
//
//

}
