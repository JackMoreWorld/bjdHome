package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.conf.ManagerConstant;
import com.bgd.admin.dao.CouponsManagerDao;
import com.bgd.admin.entity.CouponsManagerDto;
import com.bgd.admin.entity.param.CouponsFindParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 优惠券信息管理 Service
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@Service
public class CouponsManagerService {

    @Autowired
    CouponsManagerDao couponsManagerDao;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 增加优惠券信息
     *
     * @param couponsManagerDto
     * @throws BusinessException
     */
    public void addCoupons(CouponsManagerDto couponsManagerDto) throws BusinessException {
        log.info("新增优惠券 type = " + couponsManagerDto.getType() + "价格Price = " + couponsManagerDto.getPrice() +
                "会员等级levelName = " + couponsManagerDto.getLevelName() + "酒庄名称chateauName = " + couponsManagerDto.getChateauName());
        try {
            couponsManagerDto.setCreateTime(new Date());
            couponsManagerDao.addCoupons(couponsManagerDto);
            redisUtil.set(ManagerConstant.REDIS.COUPONS + couponsManagerDto.getId(), JSONObject.toJSONString(couponsManagerDto));
        } catch (Exception e) {
            log.error("新增优惠券异常", e);
            throw new BusinessException("新增优惠券异常");
        }
    }

    /**
     * 删除优惠券信息
     *
     * @param couponsId 优惠券ID
     * @throws BusinessException
     */
    public void deleteCoupons(Long couponsId) throws BusinessException {
        log.info("删除优惠券 CouponsId = " + couponsId);
        try {
            couponsManagerDao.deleteCoupons(couponsId);
            redisUtil.remove(ManagerConstant.REDIS.COUPONS + couponsId);
        } catch (Exception e) {
            log.error("删除优惠券异常 CouponsId = " + couponsId, e);
            throw new BusinessException("删除优惠券异常");
        }
    }

    /**
     * 修改优惠券信息
     *
     * @param couponsManagerDto
     * @throws BusinessException
     */
    public void updateCoupons(CouponsManagerDto couponsManagerDto) throws BusinessException {
        log.info("修改优惠券 type = " + couponsManagerDto.getType() + "价格Price = " + couponsManagerDto.getPrice() +
                "会员等级levelName = " + couponsManagerDto.getLevelName() + "酒庄名称chateauName = " + couponsManagerDto.getChateauName());
        try {
            couponsManagerDto.setUpdateTime(new Date());
            couponsManagerDao.updateCoupons(couponsManagerDto);
            redisUtil.set(ManagerConstant.REDIS.COUPONS + couponsManagerDto.getId(), JSONObject.toJSONString(couponsManagerDto));
        } catch (Exception e) {
            log.error("修改优惠券异常", e);
            throw new BusinessException("修改优惠券异常");
        }
    }

    /**
     * 分页查询优惠券信息
     *
     * @param couponsFindParam
     * @return
     * @throws BusinessException
     */
    public PageDto<CouponsManagerDto> findCoupons(CouponsFindParam couponsFindParam) throws BusinessException {
        log.info("查询优惠券 type = " + couponsFindParam.getType() + "价格Price = " + couponsFindParam.getPrice() +
                "会员等级levelName = " + couponsFindParam.getLevelName() + "酒庄名称chateauName = " + couponsFindParam.getChateauName());
        try {
            PageDto<CouponsManagerDto> pageDto = new PageDto<>();
            PageBean<CouponsManagerDto, CouponsFindParam> pageBean = new PageBean<CouponsManagerDto, CouponsFindParam>(couponsFindParam) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return couponsManagerDao.countCoupons(couponsFindParam);
                }

                @Override
                protected List<CouponsManagerDto> generateBeanList() throws BasicException {
                    return couponsManagerDao.findCoupons(couponsFindParam);
                }
            }.execute();
            long total = pageBean.getTotalCount();
            List<CouponsManagerDto> list = pageBean.getPageData();
            pageDto.setList(list);
            pageDto.setTotal(total);
            return pageDto;
        } catch (Exception e) {
            log.error("查询优惠券信息异常", e);
            throw new BusinessException("查询优惠券信息异常");
        }
    }


}
