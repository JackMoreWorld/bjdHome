package com.bgd.app.controller;

import com.bgd.app.service.MallService;
import com.bgd.support.base.ResultDto;
import com.bgd.support.entity.MallBannerPo;
import com.bgd.support.entity.ProductCategoryPo;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/7
 * @描述 商城
 */
@Slf4j
@RestController
@RequestMapping(value = "/mall")
@Api(tags = "商城模块接口")
public class MallController {

    @Autowired
    MallService mallService;


    /**
     * @描述 获取首页banner
     * @创建人 JackMore
     * @创建时间 2019/3/11
     */
    @GetMapping("/listMallBanner")
    @ApiOperation(value = "首页轮播图列表")
    public ResultDto<List<MallBannerPo>> listVipAddr() {
        try {
            List<MallBannerPo> banners = mallService.getMallBanners();
            return new ResultDto<>(ResultDto.CODE_SUCC, "", banners);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 分类页 获取酒分类
     * @创建人 JackMore
     * @创建时间 2019/3/12
     */
    @GetMapping("/listCategory")
    @ApiOperation(value = "分类页_获取酒分类")
    public ResultDto<List<ProductCategoryPo>> listProCategory() {
        try {
            List<ProductCategoryPo> category = mallService.getProCategoryList();
            return new ResultDto<>(ResultDto.CODE_SUCC, "", category);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
