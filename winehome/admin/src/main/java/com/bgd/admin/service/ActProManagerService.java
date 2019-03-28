package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.conf.ManagerConstant;
import com.bgd.admin.dao.ActProManagerDao;
import com.bgd.admin.dao.ProductManagerDao;
import com.bgd.admin.entity.ActivityProManagerDto;
import com.bgd.admin.entity.ProductManagerDto;
import com.bgd.admin.entity.param.ActivityProFindParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.entity.ProductInformationPo;
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
 * 活动产品信息管理 Service
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@Service
public class ActProManagerService {

    @Autowired
    ActProManagerDao actProManagerDao;

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
     * 处理活动商品信息
     *
     * @param activityProManagerDto 活动商品Dto
     * @throws BusinessException
     */
    public void dealActivityProductManager(ActivityProManagerDto activityProManagerDto) throws BusinessException {

        try {
            Integer dealType = activityProManagerDto.getDealType();
            ProductManagerDto productManagerDto = new ProductManagerDto();
            productManagerDto.setId(activityProManagerDto.getProductId());
            if (ManagerConstant.OPERA_TYPE.CREATE.equals(dealType)) {
                log.info("新增活动商品 活动名称 activityName = " + activityProManagerDto.getActivityName() +
                        "商品名称 productName  = " + activityProManagerDto.getProductName());
                activityProManagerDto.setCreateTime(new Date());
                actProManagerDao.addActivityPro(activityProManagerDto);
                //维护商品是否参加活动字段
                productManagerDto.setDiscount(PRO_DISCOUNT_IN);
                redisUtil.set(ManagerConstant.REDIS.ACTIVITYPRO + activityProManagerDto.getId(), JSONObject.toJSONString(activityProManagerDto));
            }
            if (ManagerConstant.OPERA_TYPE.DEL.equals(dealType)) {
                if (BgdStringUtil.isStringEmpty(activityProManagerDto.getId().toString())) {
                    throw new BusinessException("删除活动商品id不能为空");
                }
                log.info("删除活动商品 活动名称 activityName = " + activityProManagerDto.getActivityName() +
                        "商品名称 productName  = " + activityProManagerDto.getProductName());
                actProManagerDao.deleteActivityPro(activityProManagerDto.getId());
                //维护商品是否参加活动字段
                productManagerDto.setDiscount(PRO_DISCOUNT_OUT);
                redisUtil.remove(ManagerConstant.REDIS.ACTIVITYPRO + activityProManagerDto.getId());
            }
            if (ManagerConstant.OPERA_TYPE.UPDATE.equals(dealType)) {
                log.info("修改活动商品 活动名称 activityName = " + activityProManagerDto.getActivityName() +
                        "商品名称 productName  = " + activityProManagerDto.getProductName());
                activityProManagerDto.setUpdateTime(new Date());
                actProManagerDao.updateActivityPro(activityProManagerDto);
                redisUtil.set(ManagerConstant.REDIS.ACTIVITYPRO + activityProManagerDto.getId(), JSONObject.toJSONString(activityProManagerDto));
            }
            //修改商品是否参加活动状态
            productManagerDao.updateProduct(productManagerDto);
        } catch (Exception e) {
            log.error("处理活动商品异常 dealType = " + activityProManagerDto.getDealType(), e);
            throw new BusinessException("处理活动商品异常");

        }

    }

    /**
     * 分页查询活动商品
     *
     * @param activityProFindParam 活动商品查询条件参数
     * @return
     * @throws BusinessException
     */
    public PageDto<ActivityProManagerDto> findActivityPro(@RequestBody ActivityProFindParam activityProFindParam)
            throws BusinessException {
        log.info("查询活动商品 活动名称 activityName = " + activityProFindParam.getActivityName() +
                "商品名称 productName  = " + activityProFindParam.getProductName());
        try {
            PageDto<ActivityProManagerDto> pageDto = new PageDto<>();
            PageBean<ActivityProManagerDto, ActivityProFindParam> pageBean =
                    new PageBean<ActivityProManagerDto, ActivityProFindParam>(activityProFindParam) {
                        @Override
                        protected Long generateRowCount() throws BusinessException {
                            return actProManagerDao.countActivityPro(activityProFindParam);
                        }

                        @Override
                        protected List<ActivityProManagerDto> generateBeanList() throws BusinessException {
                            return actProManagerDao.findActivityPro(activityProFindParam);
                        }
                    }.execute();
            long total = pageBean.getTotalCount();
            List<ActivityProManagerDto> list = pageBean.getPageData();
            pageDto.setTotal(total);
            pageDto.setList(list);
            return pageDto;
        } catch (Exception e) {
            log.error("查询文章信息异常 activityName = " + activityProFindParam.getActivityName() +
                    "productName  = " + activityProFindParam.getProductName(), e);
            throw new BusinessException("查询文章信息异常");
        }
    }

}
