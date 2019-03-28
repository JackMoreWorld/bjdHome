package com.bgd.admin.controller;

import com.bgd.admin.entity.ActivityProManagerDto;
import com.bgd.admin.entity.param.ActivityProFindParam;
import com.bgd.admin.service.ActProManagerService;
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
 * 活动商品信息管理 Controller
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@RestController
@RequestMapping(value = "/activityPro")
@Api(tags = "活动商品信息管理接口")
public class ActProManagerController {


    @Autowired
    ActProManagerService actProManagerService;

    /**
     * 处理活动商品信息
     *
     * @param activityProManagerDto 活动商品Dto
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping("/dealActivityPro")
    @ApiOperation(value = "处理活动商品信息", notes = "根据dealType（0新增，1修改，2删除）的类型对提交的活动商品信息进行处理")
    public ResultDto dealArticlePro(@RequestBody ActivityProManagerDto activityProManagerDto) {
        log.info("根据dealType（0新增，1修改，2删除）dealType = " + activityProManagerDto.getDealType() + "的类型对提交的活动商品信息进行处理");
        try {
            actProManagerService.dealActivityProductManager(activityProManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("处理活动商品失败 activityName = " + activityProManagerDto.getActivityName()
                    + " productName= " + activityProManagerDto.getProductName(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 分页查询活动商品信息
     *
     * @param activityName 活动名称
     * @param productName  商品名称
     * @param pageNo       页码
     * @return
     */
    @ValidToken(type = 0)
    @GetMapping(value = "/findActivityPro")
    @ApiOperation(value = "分页查询活动商品信息", notes = "根据条件查询活动商品信息")
    public ResultDto<PageDto<ActivityProManagerDto>> findArticlePro(@RequestParam(required = false) String activityName,
                                                                    @RequestParam(required = false) String productName,
                                                                    @RequestParam Integer pageNo) {
        log.info("分页查询活动商品信息");
        try {
            ActivityProFindParam activityProFindParam = new ActivityProFindParam();
            activityProFindParam.setActivityName(activityName);
            activityProFindParam.setProductName(productName);
            activityProFindParam.setPageNo(pageNo);
            PageDto<ActivityProManagerDto> pageDto = actProManagerService.findActivityPro(activityProFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", pageDto);
        } catch (BusinessException e) {
            log.error("查询活动商品失败 activityName = " + activityName
                    + " productName= " + productName, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
