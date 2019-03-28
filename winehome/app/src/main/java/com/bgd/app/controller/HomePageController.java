package com.bgd.app.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bgd.app.entity.*;
import com.bgd.app.service.ActService;
import com.bgd.app.service.HomePageService;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * app首页模块 controller
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-18
 */
@RestController
@RequestMapping("/homepage")
@Slf4j
@Api(tags = "app首页模块接口")
public class HomePageController {


    @Autowired
    HomePageService homePageService;
    @Autowired
    ActService actService;

    /**
     * 查询首页广告位数据
     *
     * @return 首页广告位数据
     */
    @GetMapping(value = "/queryAdvPositionList")
    @ApiOperation(value = "查询首页广告位数据接口", notes = "查询首页广告位数据")
    public ResultDto<List<MallAdDto>> queryAdvPositionList() {
        log.info("查询首页广告位数据接口");
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功", homePageService.queryAdvPositionList());
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 查询首页广告位所属商品列表
     *
     * @param advId 广告位id
     * @return 广告位商品列表
     */
    @GetMapping(value = "/queryAdvPosProductList/{advId}")
    @ApiOperation(value = "查询首页广告位所属的商品列表接口", notes = "查询首页广告位所属的商品列表")
    public ResultDto<List<MallAdProductDto>> queryAdvPosProductList(@ApiParam(value = "广告位id") @PathVariable Long advId) {
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功", homePageService.queryAdvPosProductList(advId));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 查询首页热销国家
     *
     * @return 首页热销国家
     */
    @GetMapping("/hotSellingCountry")
    @ApiOperation(value = "查询首页热销国家接口", notes = "查询首页热销国家数据")
    public ResultDto<List<MallCountryDto>> queryHotSellingCountry() {
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功", homePageService.queryHotSellingCountry());
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 查询首页推荐酒庄及商品信息
     *
     * @return 推荐酒庄及商品信息
     */
    @GetMapping("/recommendChateau")
    @ApiOperation(value = "查询首页推荐酒庄及商品信息接口", notes = "查询首页推荐酒庄及商品信息")
    public ResultDto<MallChateauDto> queryRecommendChateau() {
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功", homePageService.queryRecommendChateau());
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 查询首页为你推荐商品
     *
     * @return 为你推荐商品
     */
    @GetMapping("/recommendPro")
    @ApiOperation(value = "查询首页为你推荐商品接口", notes = "查询首页为你推荐商品信息")
    public ResultDto<RecommendProDto> queryRecommendPro() {
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功", homePageService.queryRecommendPro());
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 查询首页精选活动
     *
     * @return 首页精选活动
     */
    @GetMapping("/choiceAct")
    @ApiOperation(value = "查询首页精选活动接口", notes = "查询首页精选活动数据")
    public ResultDto<ChoiceActDto> queryChoiceAct() {
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功", homePageService.queryChoiceAct());
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 查询首页商品排行榜数据
     *
     * @return 商品排行榜数据
     * @author wgq
     * @since 2019-03-18
     */
    @GetMapping(value = "/queryProRanking")
    @ApiOperation(value = "查询首页商品排行榜")
    public ResultDto<ProRankingDto> queryProRanking() {
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功", homePageService.queryProRanking());
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 查询首页新品首发数据
     *
     * @return 新品首发数据
     * @author wgq
     * @since 2019-03-18
     */
    @GetMapping(value = "/queryFirstNewPro")
    @ApiOperation(value = "查询首页新品首发")
    public ResultDto<FirstNewProDto> queryFirstNewPro() {
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功", homePageService.queryFirstNewPro());
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }
}
