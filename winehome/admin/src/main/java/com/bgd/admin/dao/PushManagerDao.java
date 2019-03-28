package com.bgd.admin.dao;

import com.bgd.admin.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页推荐信息管理 Dao
 * @author Sunxk
 * @since 2019-3-18
 */
@Mapper
public interface PushManagerDao {

    void addPush(PushManagerDto pushManagerDto);

    void deletePush(@Param("type") Integer type);

    PushManagerDto findPushManager(@Param("type") Integer type);

    //热销国家查询
    List<CountryManagerDto> findPushCountry();
    //推荐酒庄
    List<ChateauManagerDto> findPushChateau();
    //推荐商品
    List<ProductManagerDto> findPushProduct();
    //精选活动
    List<ActivityManagerDto> findActivity();

}
