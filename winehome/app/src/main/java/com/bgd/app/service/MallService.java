
package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.conf.AppConstant;
import com.bgd.app.dao.MallDao;
import com.bgd.app.dao.VipDao;
import com.bgd.app.entity.VipInformationDto;
import com.bgd.support.entity.*;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MallService {

    @Autowired
    MallDao mallDao;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    VipDao vipDao;

    /**
     * @描述 邀请处理
     * @创建人 JackMore
     * @创建时间 2019/3/9
     */
    @Transactional
    public void dealInviteRecord(MallInviteRecordPo po) throws BusinessException {
        try {
            //邀请人有优惠券
            VipInformationPo vip = new VipInformationPo();
            vip.setInviteCode(po.getInviteCode());
            VipInformationDto vipByVip = vipDao.findVipByVip(vip);
            if (vipByVip == null) {
                return; //没有这样的邀请人
            }
            MallInviteRecordPo list = mallDao.findInviteList(po);
            if (list != null) {
                return; //已经处理过
            }
            po.setCreateTime(new Date());
            mallDao.saveMallInviteRecord(po);
            VipCouponsPo coupons = new VipCouponsPo();
            coupons.setVipId(vip.getId());
            coupons.setType(AppConstant.COUPONS_TYPE.通用);
            coupons.setStatus(AppConstant.USED_TYPE.已领);
            coupons.setCreateTime(new Date());
            coupons.setPrice(new BigDecimal(50)); //TODO 邀请券
            vipDao.saveVipCoupons(coupons);
            //被邀请人领红包
            VipInformationPo vip1 = new VipInformationPo();
            vip1.setInviteCode(po.getCode());
            VipInformationDto newVip = vipDao.findVipByVip(vip);
            VipReward reward = new VipReward();
            reward.setVipId(newVip.getId());
            reward.setType(1);
            reward.setStatus(AppConstant.USED_TYPE.未领);
            reward.setPrice(new BigDecimal(50));
            reward.setCreateTime(new Date());
            vipDao.saveVipReward(reward);
        } catch (Exception e) {
            log.error("处理邀请记录异常", e);
            throw new BusinessException();
        }

    }


    /**
     * @描述 获取商城banner
     * @创建人 JackMore
     * @创建时间 2019/3/11
     */
    public List<MallBannerPo> getMallBanners() throws BusinessException {
        try {
            String redisBanner = redisUtil.get(AppConstant.REDIS.BANNER);
            if (redisBanner != null) {
                return JSONObject.parseArray(redisBanner, MallBannerPo.class);
            }
            List<MallBannerPo> banners = mallDao.findBanners();
            redisUtil.set(AppConstant.REDIS.BANNER, JSONObject.toJSONString(banners));
            return banners;
        } catch (Exception e) {
            log.error("获取首页轮播图异常：", e);
            throw new BusinessException("获取首页轮播图异常");
        }
    }


    /**
     * @描述 获取分类页 商品大分类
     * @创建人 JackMore
     * @创建时间 2019/3/12
     */
    public List<ProductCategoryPo> getProCategoryList() {
        try {
            String redisCategory = redisUtil.get(AppConstant.REDIS.CATEGORY);
            if (redisCategory != null) {
                return JSONObject.parseArray(redisCategory, ProductCategoryPo.class);
            }
            List<ProductCategoryPo> category = mallDao.findCategory();
            redisUtil.set(AppConstant.REDIS.CATEGORY, JSONObject.toJSONString(category));
            return category;
        } catch (Exception e) {
            log.error("获取产品分类异常：", e);
            throw new BusinessException("获取产品分类异常");
        }


    }


}