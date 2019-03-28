package com.bgd.admin.dao;

import com.bgd.admin.entity.VipCouponsManagerDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *会员拥有优惠券信息 Dao
 * @author Sunxk
 * @since 2019-3-18
 */
@Mapper
public interface VipCouponsManagerDao {

    List<VipCouponsManagerDto> findVipCoupons(@Param("vipId") Long vipId);

}
