package com.bgd.admin.controller;

import com.bgd.admin.entity.AdvProManagerDto;
import com.bgd.admin.entity.param.AdvProFindParam;
import com.bgd.admin.service.AdvProManagerService;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 广告产品 Controller
 *
 * @author sunxk
 * @since 2019-3-18
 */
@Slf4j
@RestController
@RequestMapping(value = "/dealAdvPro")
@Api(tags = "广告商品信息管理接口")
public class AdvProManagerController {

    @Autowired
    AdvProManagerService advProManagerService;

    /**
     * 处理广告商品
     *
     * @param advProManagerDto 广告商品Dto
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping("/dealAdvPro")
    @ApiOperation(value = "广告商品信息处理", notes = "根据dealType（0新增，1修改，2删除）的类型对提交的广告商品信息进行处理")
    public ResultDto dealAdvertisingPro(@RequestBody AdvProManagerDto advProManagerDto) {
        log.info("根据dealType（0新增，1修改，2删除）dealType = " + advProManagerDto.getDealType() + "的类型对提交的广告商品信息进行处理");
        try {
            advProManagerService.dealAdvertisingPro(advProManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("处理广告产品失败 广告位ID= " + advProManagerDto.getAdId()
                    + "商品ID=" + advProManagerDto.getProductId(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 分页查询广告商品
     *
     * @param advertisingName 广告名称
     * @param productName     商品名称
     * @param pageNo          页码
     * @return
     */
    @ValidToken(type = 0)
    @GetMapping(value = "/findAdvPro")
    @ApiOperation(value = "分页查询广告产品", notes = "根据条件查询广告产品信息")
    public ResultDto<PageDto<AdvProManagerDto>> findAdvertisingPro(@RequestParam(required = false) String advertisingName,
                                                                   @RequestParam(required = false) String productName,
                                                                   @RequestParam Integer pageNo) {
        log.info("分页查询广告商品");
        try {
            AdvProFindParam advProFindParam = new AdvProFindParam();
            advProFindParam.setAdvertisingName(advertisingName);
            advProFindParam.setProductName(productName);
            advProFindParam.setPageNo(pageNo);
            PageDto<AdvProManagerDto> pageDto = advProManagerService.findAdvertisingPro(advProFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", pageDto);
        } catch (BusinessException e) {
            log.error("查询广告产品失败 advertisingName = " + advertisingName
                    + "productName = " + productName, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
