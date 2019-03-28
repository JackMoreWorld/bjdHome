package com.bgd.admin.controller;

import com.bgd.admin.entity.BannerManagerDto;
import com.bgd.admin.entity.param.BannerFindParam;
import com.bgd.admin.service.BannerManagerService;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 轮播图信息管理 Controller
 *
 * @author Sunxk
 * @since 2019-3-18
 */

@Slf4j
@RestController
@RequestMapping(value = "/banner")
@Api(tags = "轮播图信息管理接口")
public class BannerManagerController {

    @Autowired
    BannerManagerService bannerManagerService;

    /**
     * 处理轮播图信息
     *
     * @param bannerManagerDto 轮播图Dto
     * @return
     */
    //@ValidToken(type = 0)
    @PostMapping("/dealBanner")
    @ApiOperation(value = "处理轮播图信息", notes = "根据dealType（0新增，1修改，2删除）的类型对提交的轮播图信息进行处理")
    public ResultDto<List<BannerManagerDto>> dealBanner(@RequestBody BannerManagerDto bannerManagerDto) {
        log.info("根据dealType（0新增，1修改，2删除）dealType = " + bannerManagerDto.getDealType() + "的类型对提交的轮播图信息进行处理");
        try {
            bannerManagerService.dealBannerManager(bannerManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("处理广告轮播图失败", e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 分页查询轮播图
     *
     * @param adName 广告名称
     * @return
     */
    //@ValidToken(type = 0)
    @GetMapping(value = "/findBanner")
    @ApiOperation(value = "分页查询轮播图信息", notes = "根据条件查询轮播图信息")
    public ResultDto<PageDto<BannerManagerDto>> findBanner(@RequestParam(required = false) String adName,
                                                           @RequestParam Integer pageNo) {
        log.info("分页查询轮播图信息 adName = " + adName);
        try {
            BannerFindParam bannerFindParam = new BannerFindParam();
            bannerFindParam.setAdvertisingName(adName);
            bannerFindParam.setPageNo(pageNo);
            PageDto<BannerManagerDto> pageDto = bannerManagerService.findBanner(bannerFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", pageDto);
        } catch (BusinessException e) {
            log.error("查询广告轮播图失败 adName = " + adName, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
