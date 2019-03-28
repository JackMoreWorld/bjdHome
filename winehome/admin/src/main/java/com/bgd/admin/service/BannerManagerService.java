package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.conf.ManagerConstant;
import com.bgd.admin.dao.BannerMangerDao;
import com.bgd.admin.entity.BannerManagerDto;
import com.bgd.admin.entity.param.BannerFindParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdStringUtil;
import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 轮播图信息管理 Service
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@Service
public class BannerManagerService {

    @Autowired
    BannerMangerDao bannerMangerDao;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 轮播图信息处理
     *
     * @param bannerManagerDto 轮播图Dto
     * @throws BusinessException
     */
    public void dealBannerManager(BannerManagerDto bannerManagerDto) throws BusinessException {

        try {
            Integer dealType = bannerManagerDto.getDealType();
            if (ManagerConstant.OPERA_TYPE.CREATE.equals(dealType)) {
                log.info("新增轮播图 广告位ID adId = " + bannerManagerDto.getAdId());
                bannerManagerDto.setCreateTime(new Date());
                bannerMangerDao.addBanner(bannerManagerDto);
                redisUtil.set(ManagerConstant.REDIS.BANNER + bannerManagerDto.getId(), JSONObject.toJSONString(bannerManagerDto));
            }
            if (ManagerConstant.OPERA_TYPE.DEL.equals(dealType)) {
                if (BgdStringUtil.isStringEmpty(bannerManagerDto.getId().toString())) {
                    throw new BusinessException("删除轮播图信息ID不能为空");
                }
                log.info("删除轮播图 广告位ID adId = " + bannerManagerDto.getAdId());
                bannerMangerDao.deleteBanner(bannerManagerDto.getId());
                redisUtil.remove(ManagerConstant.REDIS.BANNER + bannerManagerDto.getId());
            }
            if (ManagerConstant.OPERA_TYPE.UPDATE.equals(dealType)) {
                log.info("修改轮播图 广告位ID adId = " + bannerManagerDto.getAdId());
                bannerManagerDto.setUpdateTime(new Date());
                bannerMangerDao.updateBanner(bannerManagerDto);
                redisUtil.set(ManagerConstant.REDIS.BANNER + bannerManagerDto.getId(), JSONObject.toJSONString(bannerManagerDto));
            }
        } catch (Exception e) {
            log.error("处理活动商品异常", e);
            throw new BusinessException("处理活动商品异常");

        }

    }

    /**
     * 分页查询轮播图信息
     *
     * @param bannerFindParam 轮播图Dto
     * @return
     * @throws BusinessException
     */
    public PageDto<BannerManagerDto> findBanner(BannerFindParam bannerFindParam)
            throws BusinessException {
        log.info("查询轮播图 advertisingName = " + bannerFindParam.getAdvertisingName());
        try {
            PageDto<BannerManagerDto> pageDto = new PageDto<>();
            PageBean<BannerManagerDto, BannerFindParam> pageBean = new PageBean<BannerManagerDto, BannerFindParam>(bannerFindParam) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return bannerMangerDao.countBanner(bannerFindParam);
                }

                @Override
                protected List<BannerManagerDto> generateBeanList() throws BasicException {
                    return bannerMangerDao.findBanner(bannerFindParam);
                }
            }.execute();
            long total = pageBean.getTotalCount();
            List<BannerManagerDto> bannerManagerDtoList = pageBean.getPageData();
            pageDto.setTotal(total);
            pageDto.setList(bannerManagerDtoList);
            return pageDto;
        } catch (Exception e) {
            log.error("查询轮播图异常", e);
            throw new BusinessException("查询轮播图异常");
        }
    }

}
