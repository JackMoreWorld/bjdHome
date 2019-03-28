package com.bgd.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.entity.MallChateauDto;
import com.bgd.app.entity.param.ChateauParam;
import com.bgd.app.service.ChateauService;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.PageParam;
import com.bgd.support.base.ResultDto;
import com.bgd.support.entity.MallCountryPo;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(value = "/mall")
@Api(tags = "酒庄模块接口")
public class ChateauController {

    @Autowired
    ChateauService chateauService;


    /**
     * @描述 获取国家列表
     * @创建人 JackMore
     * @创建时间 2019/3/5
     */
    @GetMapping(value = "/listCountry/{pageNo}")
    @ApiOperation(value = "获取国家列表")
    public ResultDto<PageDto<MallCountryPo>> listCountry(@PathVariable Integer pageNo) {
        try {

            PageParam param = new PageParam();
            param.setPageNo(pageNo);
            PageDto<MallCountryPo> mallCountryDto = chateauService.listCountryS(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", mallCountryDto);
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
    @PostMapping(value = "/searchChateau")
    @ApiOperation(value = "大搜索酒庄列表")
    public ResultDto<PageDto<MallChateauDto>> listChateauByCategoryName(@RequestBody ChateauParam param) {
        try {
            log.info("根据酒庄信息 " + JSONObject.toJSONString(param) + "，获取酒庄信息列表");
            PageDto<MallChateauDto> result = chateauService.listChateauBySearch(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", result);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * @描述 根据商品ID获取酒庄ID获取酒庄详情
     * @创建人 JackMore
     * @创建时间 2019/3/26
     */
    @PostMapping("/chateauDetail")
    @ApiOperation(value = "获取商品酒庄详情")
    public ResultDto<JSONObject> chateauDetail(@RequestBody ChateauParam param) {
        try {
            JSONObject chateauDetail = chateauService.getChateauDetail(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", chateauDetail);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }

    }

}
