package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.dao.ActDao;
import com.bgd.app.dao.HomePageDao;
import com.bgd.app.entity.*;
import com.bgd.app.entity.param.FirstNewProParam;
import com.bgd.support.entity.ProductCapacityPo;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdDateUtil;
import com.bgd.support.utils.BgdStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * app首页模块 service
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-18
 */
@Service
@Slf4j
public class HomePageService {


    @Autowired
    HomePageDao homePageDao;

    @Autowired
    ActDao actDao;


    /**
     * 查询首页广告位数据
     *
     * @return 首页广告位数据
     */
    public List<MallAdDto> queryAdvPositionList() throws BusinessException {
        try {
            return homePageDao.queryAdvPositionList();
        } catch (Exception e) {
            throw new BusinessException("查询首页广告位数据异常");
        }
    }


    /**
     * 查询首页广告位所属商品数据
     *
     * @param advId 广告位id
     * @return 广告位所属商品数据
     */
    public List<MallAdProductDto> queryAdvPosProductList(Long advId) throws BusinessException {
        try {
            //查询首页广告位所属商品数据
            List<MallAdProductDto> list = homePageDao.queryAdvPosProductList(advId);
            for (MallAdProductDto adProductDto : list) {
                if (BgdStringUtil.isStringNotEmpty(adProductDto.getNewCapacity())) {
                    ProductCapacityPo capacityPo = JSONObject.parseObject(adProductDto.getNewCapacity(), ProductCapacityPo.class);
                    adProductDto.setProductPrice(capacityPo.getPrice());
                }
            }
            return list;
        } catch (Exception e) {
            throw new BusinessException("查询首页广告位所属商品数据异常");
        }
    }

    /**
     * 查询首页热销国家
     *
     * @return 首页热销国家
     */
    public List<MallCountryDto> queryHotSellingCountry() throws BusinessException {
        try {
            return homePageDao.queryHotSellingCountry(
                    BgdDateUtil.formatSdf(new Date(), "yyyy-MM-dd"));
        } catch (Exception e) {
            throw new BusinessException("查询首页热销国家异常");
        }
    }

    /**
     * 查询首页推荐酒庄及商品信息
     *
     * @return 推荐酒庄及商品信息
     */
    public MallChateauDto queryRecommendChateau() throws BusinessException {
        try {
            MallChateauDto dto = homePageDao.queryRecommendChateau(
                    BgdDateUtil.formatSdf(new Date(), "yyyy-MM-dd"));
            if (null != dto) {
                dto.setProductInformationPoList(homePageDao.queryChateauProductList(dto.getChateauId()));
            }
            return dto;
        } catch (Exception e) {
            throw new BusinessException("查询首页推荐酒庄及商品信息异常");
        }
    }

    /**
     * 查询首页为你推荐商品
     *
     * @return 为你推荐商品
     */
    public RecommendProDto queryRecommendPro() throws BusinessException {
        try {
            return homePageDao.queryRecommendPro(BgdDateUtil.formatSdf(new Date(), "yyyy-MM-dd"));
        } catch (Exception e) {
            throw new BusinessException("查询首页为你推荐商品异常");
        }
    }

    /**
     * 查询首页精选活动
     *
     * @return 首页精选活动
     */
    public ChoiceActDto queryChoiceAct() throws BusinessException {
        try {
            ChoiceActDto choiceActDto = homePageDao.queryChoiceAct(BgdDateUtil.formatSdf(new Date(), "yyyy-MM-dd"));
            if (null != choiceActDto) {
                FirstNewProParam param = new FirstNewProParam();
                param.setActId(Long.valueOf(choiceActDto.getActId()));
                param.setPageNo(0);
                param.setPageSize(4);
                List<FirstNewProDto> list = actDao.queryFirstNewProList(param);
                choiceActDto.setProList(list);
            }
            return choiceActDto;
        } catch (Exception e) {
            throw new BusinessException("查询首页精选活动异常");
        }
    }


    /**
     * 查询首页商品排行榜数据
     *
     * @return 商品排行榜数据
     * @author wgq
     * @since 2019-03-18
     */
    public ProRankingDto queryProRanking() throws BusinessException {
        try {
            return homePageDao.queryProRanking();
        } catch (Exception e) {
            throw new BusinessException("查询首页商品排行榜列表分页数据异常");
        }
    }

    /**
     * 查询首页新品首发数据
     *
     * @return 新品首发数据
     * @author wgq
     * @since 2019-03-18
     */
    public FirstNewProDto queryFirstNewPro() throws BusinessException {
        try {
            return homePageDao.queryFirstNewPro(BgdDateUtil.formatSdf(new Date(), "yyyy-MM-dd"));
        } catch (Exception e) {
            throw new BusinessException("查询首页新品首发分页数据异常");
        }
    }
}
