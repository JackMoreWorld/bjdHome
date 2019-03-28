package com.bgd.admin.dao;

import com.bgd.admin.entity.ActivityManagerDto;
import com.bgd.admin.entity.param.ActivityFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动信息管理 Dao
 * @author sunxk
 * @since 2019-3-18
 */
@Mapper
public interface ActivityManagerDao {

    void addActivity(ActivityManagerDto activityManagerDto);
    void deleteActivity(@Param("activityId") Long activityId);
    void updateActivity(ActivityManagerDto activityManagerDto);

    Long countActivity(ActivityFindParam param);
    List<ActivityManagerDto> findActivity(ActivityFindParam param);
    
}
