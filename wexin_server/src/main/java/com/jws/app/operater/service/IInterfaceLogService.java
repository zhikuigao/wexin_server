package com.jws.app.operater.service;

import com.jws.app.operater.model.InterfaceLog;
import com.jws.common.service.IService;

/**
 * 日志接口
 * 
 * @author lx
 * 
 */
public interface IInterfaceLogService extends IService<InterfaceLog> {	
	/**
	 * 新增日志
	 * @param log
	 * @return
	 */
	public  InterfaceLog  insert(InterfaceLog log);
	/**
	 * 更新返回结果
	 * @param log
	 * @return
	 */
	public int  updateByPrimaryKey(InterfaceLog log);
}
