
package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.conf.AppConstant;
import com.bgd.app.dao.VipCenterDao;
import com.bgd.app.entity.MallChateauDto;
import com.bgd.app.entity.VipCenterDto;
import com.bgd.app.entity.VipFootMarkDto;
import com.bgd.app.entity.param.CouponsParam;
import com.bgd.app.entity.param.VipSomeParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.entity.MallCouponsPo;
import com.bgd.support.entity.VipCouponsPo;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class VipCenterService {
    @Autowired
    VipCenterDao vipCenterDao;


    /**
     * @描述 获取个人中心页面展示信息
     * @创建人 JackMore
     * @创建时间 2019/3/9
     */
    public VipCenterDto findVipCenterMsg(Long vipId) throws BusinessException {
        try {
            VipCenterDto vipCenterMsg = vipCenterDao.findVipCenterMsg(vipId);
            return vipCenterMsg;
        } catch (Exception e) {
            log.error("个人中心页面展示数据异常", e);
            throw new BusinessException();
        }
    }

    /**
     * @描述 根据vipId获取关注酒庄列表
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    public List<MallChateauDto> getVipFocusChateauList(Long vipId) throws BusinessException {
        try {
            List<MallChateauDto> list = vipCenterDao.findVipFocusChateauList(vipId);
            return list;
        } catch (Exception e) {
            log.error("获取vip关注酒庄信息异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 获取VIP优惠券列表
     * @创建人 JackMore
     * @创建时间 2019/3/7
     */
    public JSONObject getVipCouponsList(CouponsParam param) throws BusinessException {
        try {
            JSONObject result = new JSONObject();
            List<VipCouponsPo> used = Lists.newArrayList();
            List<VipCouponsPo> unused = Lists.newArrayList();
            PageBean<VipCouponsPo, CouponsParam> page = new PageBean<VipCouponsPo, CouponsParam>(param) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return vipCenterDao.countVipCoupons(param);
                }

                @Override
                protected List<VipCouponsPo> generateBeanList() throws BasicException {
                    return vipCenterDao.pageVipCoupons(param);
                }
            }.execute();
            List<VipCouponsPo> list = page.getPageData();
            for (VipCouponsPo po : list) {
                if (AppConstant.USED_TYPE.已领.equals(po.getType())) {
                    unused.add(po);
                } else {
                    used.add(po);
                }
            }
            result.put("unused", unused);
            result.put("used", used);
            return result;
        } catch (Exception e) {
            log.error("获取会员优惠券列表信息异常", e);
            throw new BusinessException();
        }
    }

    /**
     * @描述 获取VIP优惠券列表
     * @创建人 JackMore
     * @创建时间 2019/3/7
     */
    public JSONObject getMallCouponsList(CouponsParam param) throws BusinessException {
        try {
            JSONObject result = new JSONObject();
            List<MallCouponsPo> all = Lists.newArrayList();
            List<MallCouponsPo> chateau = Lists.newArrayList();
            PageBean<MallCouponsPo, CouponsParam> page = new PageBean<MallCouponsPo, CouponsParam>(param) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return vipCenterDao.countMallCoupons(param);
                }

                @Override
                protected List<MallCouponsPo> generateBeanList() throws BasicException {
                    return vipCenterDao.pageMallCoupons(param);
                }
            }.execute();
            List<MallCouponsPo> list = page.getPageData();
            for (MallCouponsPo po : list) {
                if (AppConstant.COUPONS_TYPE.通用.equals(po.getType())) {
                    all.add(po);
                } else {
                    chateau.add(po);
                }
            }
            result.put("all", all);
            result.put("chateau", chateau);
            return result;
        } catch (Exception e) {
            log.error("获取会员优惠券列表信息异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 展示五条获取个人足迹列表
     * @创建人 JackMore
     * @创建时间 2019/3/15
     */
    public JSONObject pageVipFoot(VipSomeParam param) throws BusinessException {
        try {
            JSONObject result = new JSONObject();
            PageBean<VipFootMarkDto, VipSomeParam> page = new PageBean<VipFootMarkDto, VipSomeParam>(param) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return vipCenterDao.countVipFoot(param);
                }

                @Override
                protected List<VipFootMarkDto> generateBeanList() throws BasicException {
                    return vipCenterDao.pageVipFoot(param);
                }
            }.execute();
            List<VipFootMarkDto> list = page.getPageData();
            result.put("list", list);
            return result;
        } catch (Exception e) {
            log.error("获取会员足迹信息异常", e);
            throw new BusinessException();
        }
    }

}