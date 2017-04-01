package com.jws.app.operater.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.IndusReportMapper;
import com.jws.app.operater.data.InterfaceLogMapper;
import com.jws.app.operater.model.InterfaceLog;
import com.jws.app.operater.service.IInterfaceLogService;
import com.jws.common.bean.Pagination;
import com.jws.common.data.IMapper;
import com.jws.common.exception.ServiceException;
import com.jws.common.service.impl.AbstractServiceImpl;
import com.jws.common.util.RandomUtil;

@Service("interfaceLogService")
public class InterfaceLogServiceImpl extends AbstractServiceImpl<InterfaceLog> implements IInterfaceLogService{

	@Resource
	private InterfaceLogMapper interfaceLogMapper;
	@Resource
	private  IndusReportMapper indusReportMapper;
	
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public IMapper<InterfaceLog> getMapper() {
		return this.interfaceLogMapper;
	}

	@Override
	public InterfaceLog insert(InterfaceLog log) {
		log.setId(this.generateid());
		log.setSource("APP");
		try {
			interfaceLogMapper.insert(log);
			return log;
		} catch (Exception e) {
			System.out.println(e);
			logger.error("写入日志表出错"+e.getMessage());
		}		
		return null;
	}

	@Override
	public int updateByPrimaryKey(InterfaceLog log) {
		int result =0;
		try {
			 result = interfaceLogMapper.updateByPrimaryKeyWithBLOBs(log);
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("更新日志表出错"+e.getMessage());
		}		
		return result;
	}

	/**
	 * 获取日志id
	 * @return
	 */
	private  String generateid(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date =indusReportMapper.selectCurrTime();
		String id = df.format(date)+StringUtils.substring(RandomUtil.toFixdLengthString(5), 0, 4);
		return id;
	}

	@Override
	public List<InterfaceLog> loadOnePage(Pagination page)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterfaceLog> search(InterfaceLog object, Pagination page)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
