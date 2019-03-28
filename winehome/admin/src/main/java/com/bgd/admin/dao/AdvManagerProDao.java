package com.bgd.admin.dao;

import com.bgd.admin.entity.AdvProManagerDto;
import com.bgd.admin.entity.param.AdvFindParam;
import com.bgd.admin.entity.param.AdvProFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 广告产品Dao
 * @author Sunxk
 * @since 2019-3-18
 */
@Mapper
public interface AdvManagerProDao {

    void addAdvertisingPro(AdvProManagerDto advProManagerDto);
    void deleteAdvertisingPro(@Param("advertisingProId") Long advertisingProId);
    void updateAdvertisingPro(AdvProManagerDto advProManagerDto);

    Long countAdvertising(AdvProFindParam advProFindParam);
    List<AdvProManagerDto> findAdvertisingPro(AdvProFindParam advProFindParam);
    
}
