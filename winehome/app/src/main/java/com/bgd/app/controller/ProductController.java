package com.bgd.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.entity.ProductDto;
import com.bgd.app.entity.ProductEvaluatDto;
import com.bgd.app.entity.param.ProParam;
import com.bgd.app.service.ProService;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.ResultDto;
import com.bgd.support.entity.ProductEvaluatPo;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/7
 * @描述 商品
 */
@Slf4j
@RestController
@RequestMapping(value = "/pro")
@Api(tags = "商品模块接口")
public class ProductController {


    @Autowired
    ProService proService;


    /**
     * @描述 商品的详情
     * @创建人 JackMore
     * @创建时间 2019/3/12
     */
    @PostMapping("/detail")
    @ApiOperation(value = "获取商品的详情")
    public ResultDto<JSONObject> productDetail(@RequestBody ProParam dto, @RequestHeader(required = false) String token) {
        try {
            JSONObject json = proService.getProDetail(dto, token);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", json);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 大搜索酒庄列表
     * @创建人 JackMore
     * @创建时间 2019/3/5
     */
    @GetMapping(value = "/evaluate/{productId}/{pageNo}")
    @ApiOperation(value = "获取更多商品评价")
    public ResultDto<PageDto<ProductEvaluatDto>> listChateauByCategoryName(@PathVariable Long productId, @PathVariable Integer pageNo) {
        try {
            ProParam param = new ProParam();
            param.setProductId(productId);
            param.setPageNo(pageNo);
            PageDto<ProductEvaluatDto> result = proService.pageProductEva(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", result);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    @PostMapping("/comment")
    @ApiOperation(value = "评价商品")
    public ResultDto<JSONObject> comment(@RequestBody ProductEvaluatPo po) {
        try {
            proService.commentProduct(po);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    @PostMapping("/filterPro")
    @ApiOperation(value = "筛选商品")
    public ResultDto<PageDto<ProductDto>> filterPro(@RequestBody ProParam param) {
        try {
            PageDto<ProductDto> productDtoPageDto = proService.filterPro(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", productDtoPageDto);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


}
