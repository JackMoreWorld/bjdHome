package com.bgd.app.dao;

import com.bgd.support.entity.SysOsPo;
import com.bgd.support.entity.SysRegionPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDao {

    List<SysRegionPo> findRegionByCode(@Param("code") String code);

    SysOsPo findSysOs();

}
