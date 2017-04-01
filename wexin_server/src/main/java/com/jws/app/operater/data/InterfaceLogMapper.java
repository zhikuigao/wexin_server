package com.jws.app.operater.data;

import com.jws.app.operater.model.InterfaceLog;
import com.jws.common.data.IMapper;

public interface InterfaceLogMapper  extends IMapper<InterfaceLog>{


    int insert(InterfaceLog record);

    int insertSelective(InterfaceLog record);

    InterfaceLog selectByPrimaryKey(Integer id);

//    int updateByExampleSelective(@Param("record") InterfaceLog record, @Param("example") InterfaceLogExample example);
//
//    int updateByExampleWithBLOBs(@Param("record") InterfaceLog record, @Param("example") InterfaceLogExample example);
//
//    int updateByExample(@Param("record") InterfaceLog record, @Param("example") InterfaceLogExample example);
//
//    int updateByPrimaryKeySelective(InterfaceLog record);

    int updateByPrimaryKeyWithBLOBs(InterfaceLog record);

//    int updateByPrimaryKey(InterfaceLog record);
}