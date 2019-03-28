package com.bgd.app.dao;

import com.bgd.app.entity.*;
import com.bgd.app.entity.param.FirstNewProParam;
import com.bgd.app.entity.param.ProRankingRequestParam;
import com.bgd.support.entity.ProductInformationPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * app首页模块 dao
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-18
 */
@Mapper
public interface HomePageDao {


    /**
     * 查询首页广告位数据
     *
     * @return 首页广告位数据
     */
    List<MallAdDto> queryAdvPositionList();


    /**
     * 查询首页广告位所属商品数据
     *
     * @param advId 广告位id
     * @return 广告位所属商品数据
     */
    List<MallAdProductDto> queryAdvPosProductList(Long advId);

    /**
     * 查询首页热销国家
     */
    List<MallCountryDto> queryHotSellingCountry(@Param("pushDay") String pushDay);

    MallChateauDto queryRecommendChateau(@Param("pushDay") String pushDay);

    List<ProductInformationPo> queryChateauProductList(Long chateauId);

    /**
     * 查询首页为你推荐商品
     *
     * @return 为你推荐商品
     */
    RecommendProDto queryRecommendPro(@Param("pushDay") String pushDay);

    /**
     * 查询首页精选活动
     *
     * @return 首页精选活动
     */
    ChoiceActDto queryChoiceAct(@Param("pushDay") String pushDay);


    ProRankingDto queryProRanking();

    FirstNewProDto queryFirstNewPro(@Param("actday") String actday);
}
