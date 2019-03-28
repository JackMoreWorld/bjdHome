package com.bgd.admin.controller;

import com.bgd.admin.entity.VipManagerDto;
import com.bgd.admin.entity.param.VipFindParam;
import com.bgd.admin.service.VipManagerService;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/vip")
@Api(tags = "vip信息管理接口")
public class VipManagerController {

    @Autowired
    VipManagerService vipManagerService;

    /**
     * 分页查询会员信息
     * @param name 会员昵称
     * @param phone 会员联系方式
     * @param levelName 会员等级
     * @param pageNo 页码
     */
    @ValidToken(type = 0)
    @GetMapping(value = "/findVip")
    @ApiOperation(value = "vip信息查询", notes = "根据条件分页查询vip信息")
    public ResultDto<PageDto<VipManagerDto>> findVip(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String phone,
                                                     @RequestParam(required = false) String levelName,
                                                     @RequestParam Integer pageNo) {
        log.info("查询VIP信息");
        try {
            VipFindParam vipFindParam = new VipFindParam();
            vipFindParam.setName(name);
            vipFindParam.setPhone(phone);
            vipFindParam.setLevelName(levelName);
            vipFindParam.setPageNo(pageNo);
            PageDto<VipManagerDto> pageDto = vipManagerService.findVip(vipFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", pageDto);
        } catch (BusinessException e) {
            log.error("查询VIP信息失败", e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
