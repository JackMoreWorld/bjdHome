package com.bgd.app.dao;

import com.bgd.support.entity.MallActivityPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface MallActivityDao {


    MallActivityPo findProductByMap(Map<String, String> param);

    int updateActivityCount(Map<String, String> param);






}
