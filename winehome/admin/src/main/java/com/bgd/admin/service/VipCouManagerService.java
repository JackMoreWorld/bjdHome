package com.bgd.admin.service;


import com.bgd.admin.dao.VipCouponsManagerDao;
import com.bgd.admin.entity.VipCouponsManagerDto;
import com.bgd.support.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  会员优惠券管理 Service
 * @author Sunxk
 * @since 2019-3-15
 */
@Slf4j
@Service
public class VipCouManagerService {

    @Autowired
    VipCouponsManagerDao vipCouponsDao;

    /**
    * @author Sunxk
    * @date 2019/3/15
    * @Param [vipId]
    * @return java.util.List<com.bgd.admin.entity.VipCouponsManagerDto>
    */
    public List<VipCouponsManagerDto> findVipCoupons (Long vipId)throws BusinessException {
        log.info("根据vipId查询拥有的优惠券信息 vipId="+vipId);
        try{
            return vipCouponsDao.findVipCoupons(vipId);
        }catch(Exception e){
            log.error("根据vipId查询拥有的优惠券信息异常",e);
            throw new BusinessException("根据vipId查询拥有的优惠券信息异常");
        }
    }

}
