package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.conf.ManagerConstant;
import com.bgd.admin.dao.AdvManagerProDao;
import com.bgd.admin.dao.ProductManagerDao;
import com.bgd.admin.entity.AdvProManagerDto;
import com.bgd.admin.entity.ProductManagerDto;
import com.bgd.admin.entity.param.AdvProFindParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdStringUtil;
import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

/**
 * 广告商品 Service
 *
 * @author Suxk
 * @since 2019-3-18
 */
@Slf4j
@Service
public class AdvProManagerService {

    @Autowired
    AdvManagerProDao advManagerProDao;

    @Autowired
    ProductManagerDao productManagerDao;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 商品是否参加活动 0否 1是
     */
    private static final int PRO_DISCOUNT_OUT = 0;
    private static final int PRO_DISCOUNT_IN = 1;

    /**
     * 处理广告产品信息
     *
     * @param advProManagerDto 广告商品Dto
     * @throws BusinessException
     */
    public void dealAdvertisingPro(AdvProManagerDto advProManagerDto) throws BusinessException {

        try {
            Integer dealType = advProManagerDto.getDealType();
            ProductManagerDto productManagerDto = new ProductManagerDto();
            productManagerDto.setId(advProManagerDto.getProductId());
            if (ManagerConstant.OPERA_TYPE.CREATE.equals(dealType)) {
                log.info("新增广告产品 广告位ID  = " + advProManagerDto.getAdId() +
                        "产品ID = " + advProManagerDto.getProductId());
                advProManagerDto.setCreateTime(new Date());
                advManagerProDao.addAdvertisingPro(advProManagerDto);
                //维护商品是否参加活动字段
                productManagerDto.setDiscount(PRO_DISCOUNT_IN);
                redisUtil.set(ManagerConstant.REDIS.ADVPRO + advProManagerDto.getId(), JSONObject.toJSONString(advProManagerDto));
            }
            if (ManagerConstant.OPERA_TYPE.DEL.equals(dealType)) {
                if (BgdStringUtil.isStringEmpty(advProManagerDto.getId().toString())) {
                    throw new BusinessException("删除广告商品信息ID不能为空");
                }
                log.info("删除广告产品 广告位ID  = " + advProManagerDto.getAdId() +
                        "产品ID = " + advProManagerDto.getProductId());
                advManagerProDao.deleteAdvertisingPro(advProManagerDto.getId());
                //维护商品是否参加活动字段
                productManagerDto.setDiscount(PRO_DISCOUNT_OUT);
                redisUtil.remove(ManagerConstant.REDIS.ADVPRO + advProManagerDto.getId());
            }
            if (ManagerConstant.OPERA_TYPE.UPDATE.equals(dealType)) {
                log.info("修改广告产品 广告位ID  = " + advProManagerDto.getAdId() +
                        "产品ID = " + advProManagerDto.getProductId());
                advProManagerDto.setUpdateTime(new Date());
                advManagerProDao.updateAdvertisingPro(advProManagerDto);
                redisUtil.set(ManagerConstant.REDIS.ADVPRO + advProManagerDto.getId(), JSONObject.toJSONString(advProManagerDto));
            }
            //修改商品是否参加活动状态
            productManagerDao.updateProduct(productManagerDto);
        } catch (Exception e) {
            log.error("处理广告产品信息异常", e);
            throw new BusinessException("处理广告产品信息异常");

        }

    }

    /**
     * 分页查询广告商品
     *
     * @param advProFindParam 广告商品查询条件
     * @return 广告商品分页数据
     * @throws BusinessException
     */
    public PageDto<AdvProManagerDto> findAdvertisingPro( AdvProFindParam advProFindParam)
            throws BusinessException {
        log.info("查询广告产品 广告位 = " + advProFindParam.getAdvertisingName() +
                "产品 = " + advProFindParam.getProductName());
        try {
            PageDto<AdvProManagerDto> pageDto = new PageDto<>();
            PageBean<AdvProManagerDto, AdvProFindParam> pageBean = new PageBean<AdvProManagerDto, AdvProFindParam>(advProFindParam) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return advManagerProDao.countAdvertising(advProFindParam);
                }

                @Override
                protected List<AdvProManagerDto> generateBeanList() throws BasicException {
                    return advManagerProDao.findAdvertisingPro(advProFindParam);
                }
            }.execute();
            long total = pageBean.getTotalCount();
            List<AdvProManagerDto> advProManagerDtoList = pageBean.getPageData();
            pageDto.setTotal(total);
            pageDto.setList(advProManagerDtoList);
            return pageDto;
        } catch (Exception e) {
            log.error("查询广告产品信息异常 adName = " + advProFindParam.getAdvertisingName() +
                    " proName = " + advProFindParam.getProductName(), e);
            throw new BusinessException("查询广告产品信息异常");
        }
    }

}
