package com.bgd.admin.controller.productManager;

import com.bgd.admin.entity.AdvManagerDto;
import com.bgd.admin.entity.param.AdvFindParam;
import com.bgd.admin.service.AdvManagerService;
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
 * 广告信息管理 Controller
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@RestController
@RequestMapping(value = "/dealAdvertising")
@Api(tags = "广告信息管理接口")
public class AdvertisingManagerController {

    @Autowired
    AdvManagerService advManagerService;

    /**
     * @param advManagerDto
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping("/dealAdvertising")
    @ApiOperation(value = "处理广告信息", notes = "根据dealType（0新增，1修改，2删除）的类型对提交的广告信息进行处理")
    public ResultDto dealAdvertising(@RequestBody AdvManagerDto advManagerDto) {
        log.info("根据dealType（0新增，1修改，2删除）dealType = " + advManagerDto.getDealType() + "的类型对提交的广告信息进行处理");
        try {
            advManagerService.dealAdvertising(advManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("处理广告失败 dealType = " + advManagerDto.getDealType(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 分页查询广告信息
     *
     * @param name   广告名称
     * @param pageNo 页码
     * @return
     */
    @ValidToken(type = 0)
    @GetMapping(value = "/findAdvertising")
    @ApiOperation(value = "分页查询广告信息", notes = "根据查询条件分页查询广告信息")
    public ResultDto<PageDto<AdvManagerDto>> findAdvertising(@RequestParam(required = false) String name,
                                                             @RequestParam Integer pageNo) {
        log.info("分页查询广告信息");
        try {
            AdvFindParam advFindParam = new AdvFindParam();
            advFindParam.setName(name);
            advFindParam.setPageNo(pageNo);
            PageDto<AdvManagerDto> pageDto = advManagerService.findAdvertising(advFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", pageDto);
        } catch (BusinessException e) {
            log.error("查询广告失败 name = " + name, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
