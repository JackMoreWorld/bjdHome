package com.bgd.app.dao;

import com.bgd.app.entity.MallChateauDto;
import com.bgd.app.entity.VipCenterDto;
import com.bgd.app.entity.VipFootMarkDto;
import com.bgd.app.entity.param.CouponsParam;
import com.bgd.app.entity.param.VipSomeParam;
import com.bgd.support.entity.MallCouponsPo;
import com.bgd.support.entity.VipCouponsPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VipCenterDao {

    VipCenterDto findVipCenterMsg(@Param("vipId") Long vipId);

    List<MallChateauDto> findVipFocusChateauList(@Param("vipId") Long vipId);

    List<VipCouponsPo> findVipCouponsList(VipCouponsPo po);

    Long countVipCoupons(CouponsParam param);

    List<VipCouponsPo> pageVipCoupons(CouponsParam param);


    Long countMallCoupons(CouponsParam param);

    List<MallCouponsPo> pageMallCoupons(CouponsParam param);


    Long countVipFoot(VipSomeParam param);

    List<VipFootMarkDto> pageVipFoot(VipSomeParam param);

}
