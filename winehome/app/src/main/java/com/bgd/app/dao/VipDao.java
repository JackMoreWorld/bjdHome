package com.bgd.app.dao;

import com.bgd.app.entity.VipInformationDto;
import com.bgd.support.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface VipDao {


    VipInformationDto findVipByVip(VipInformationPo po);

    int saveVip(VipInformationPo po);

    int saveVipMallInfo(VipMallInfoPo info);

    int flushVip(VipInformationPo po);

    int flushVipMallInfo(VipMallInfoPo info);

    void saveOrUpdateFocus(Long vipId, Long chateauId, Integer status);

    void saveFoot(VipFootMarkPo po);

    void saveOrUpdateCollect(Long vipId, Long chateauId, Integer status);

    int saveVipCoupons(VipCouponsPo coupons);

    VipMallInfoPo findVipMallInfo(@Param("vipId") Long vipId);

    void updateVipMallInfo(VipMallInfoPo vipMallInfo);

    Map<String, Long> callUpLevelByPointsP(Map<String, Long> map);

    VipRecordPo findVipRecord(VipRecordPo recordPo);

    int saveVipRecord(VipRecordPo recordPo);


    int saveVipReward(VipReward reward);

    VipReward findRewardOne(VipReward reward);


}
