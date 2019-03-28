package com.bgd.app.dao;

import com.bgd.support.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MallDao {

    MallCouponsPo findMallCouponsById(@Param("couponsId") Long couponsId);

    int saveMallInviteRecord(MallInviteRecordPo po);

    MallInviteRecordPo findInviteList(MallInviteRecordPo po);

    List<MallBannerPo> findBanners();


    List<ProductCategoryPo> findCategory();

}
