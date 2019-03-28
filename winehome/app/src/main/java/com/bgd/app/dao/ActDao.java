package com.bgd.app.dao;

import com.bgd.app.entity.*;
import com.bgd.app.entity.param.ActProParam;
import com.bgd.app.entity.param.FirstNewProParam;
import com.bgd.app.entity.param.ProRankingRequestParam;
import com.bgd.support.entity.MallActivityPo;
import com.bgd.support.entity.MallBannerPo;
import com.bgd.support.entity.ProductCategoryPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ActDao {

    List<MallBannerPo> findBannerList();

    List<MallActivityPo> findMallActivity();

    List<MallActivityProductDto> findHomeActPro(Map<String, Long> map);

    List<MallActivityProductDto> findRankActPro(Map<String, Long> map);

    List<MallActivityProductDto> findHomeLimitActPro(Map<String, Long> map);

    List<TimingDto> findLimitTiming(Map<String, Object> map);

    Long countLimitActPro(ActProParam param);

    List<LimitActProDto> pageLimitActPro(ActProParam param);

    List<ProductCategoryPo> findCategoryList(Map<String, Object> map);

    Long countGoodActPro(ActProParam param);

    List<GoodActProDto> pageGoodActPro(ActProParam param);

    void likeOrNot(LikeDto dto);

    List<ProductCategoryPo> queryCategoryList();

    List<ProRankingDto> queryProRankingList(ProRankingRequestParam requestParam);

    Long queryProRankingCount(ProRankingRequestParam requestParam);

    MallActivityPo findMallActivityById(Long actId);

    List<FirstNewProDto> queryFirstNewProList(FirstNewProParam param);

    Long queryFirstNewProCount(FirstNewProParam param);
}
