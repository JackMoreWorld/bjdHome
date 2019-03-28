package com.bgd.admin.dao;

import com.bgd.admin.entity.CouponsManagerDto;
import com.bgd.admin.entity.param.CouponsFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券信息管理 Dao
 * @author Sunxk
 * @since 2019-3-18
 */
@Mapper
public interface CouponsManagerDao {

    void addCoupons(CouponsManagerDto couponsManagerDto);
    void deleteCoupons(@Param("couponsId") Long couponsId);
    void updateCoupons(CouponsManagerDto couponsManagerDto);

    Long countCoupons(CouponsFindParam couponsFindParam);
    List<CouponsManagerDto> findCoupons(CouponsFindParam couponsFindParam);

}
