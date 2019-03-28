package com.bgd.admin.dao;

import com.bgd.admin.entity.ActivityProManagerDto;
import com.bgd.admin.entity.param.ActivityProFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动产品信息管理 Dao
 * @author Sunxk
 * @since 2019-3-18
 */
@Mapper
public interface ActProManagerDao {

    void addActivityPro(ActivityProManagerDto activityProManagerDto);
    void deleteActivityPro(@Param("activityProId") Long activityProId);
    void updateActivityPro(ActivityProManagerDto activityProManagerDto);

    Long countActivityPro(ActivityProFindParam activityProFindParam);
    List<ActivityProManagerDto> findActivityPro(ActivityProFindParam activityProFindParam);

}
