package com.bgd.admin.dao;

import com.bgd.admin.entity.AdvManagerDto;
import com.bgd.admin.entity.param.AdvFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  广告信息管理 Dao
 * @author Sunxk
 * @since 2019-3-18
 */
@Mapper
public interface AdvManagerDao {

    void addAdvertising(AdvManagerDto advManagerDto);
    void deleteAdvertising(@Param("advertisingId") Long advertisingId);
    void updateAdvertising(AdvManagerDto advManagerDto);

    Long countAdvertising(AdvFindParam advFindParam);
    List<AdvManagerDto> findAdvertising(AdvFindParam advFindParam);
    
}
