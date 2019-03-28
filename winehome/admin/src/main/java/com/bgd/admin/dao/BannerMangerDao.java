package com.bgd.admin.dao;

import com.bgd.admin.entity.BannerManagerDto;
import com.bgd.admin.entity.param.BannerFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 轮播图信息管理Dao
 * @author Sunxk
 * @since 2019-3-18
 */
@Mapper
public interface BannerMangerDao {

    void addBanner(BannerManagerDto bannerManagerDto);
    void deleteBanner(@Param("bannerId") Long bannerId);
    void updateBanner(BannerManagerDto bannerManagerDto);

    Long countBanner(BannerFindParam bannerFindParam);
    List<BannerManagerDto> findBanner(BannerFindParam bannerFindParam);
    
}
