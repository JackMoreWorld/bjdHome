package com.bgd.app.controller;

import com.bgd.app.entity.VipAddrDto;
import com.bgd.app.service.AddrService;
import com.bgd.support.base.ResultDto;
import com.bgd.support.entity.VipAddrPo;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/7
 * @描述 用户收货地址
 */
@Slf4j
@RestController
@RequestMapping(value = "/vip")
@Api(tags = "收货地址模块接口")
public class AddrController {

    @Autowired
    AddrService addrService;

    /**
     * @描述 获取用户收货地址列表
     * @创建人 JackMore
     * @创建时间 2019/3/7
     */
    @GetMapping("/listAddr/{vipId}")
    @ApiOperation(value = "获取用户收货地址列表")
    public ResultDto<List<VipAddrPo>> listVipAddr(@PathVariable Long vipId) {
        try {
            List<VipAddrPo> vipAddrList = addrService.getVipAddrList(vipId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", vipAddrList);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 处理用户收货地址
     * @创建人 JackMore
     * @创建时间 2019/3/7
     */
    @PostMapping("/dealAddr")
    @ApiOperation(value = "新增或编辑用户收货地址")
    public ResultDto<List<VipAddrPo>> dealVipAddr(@RequestBody VipAddrDto addr) {

        try {
            addrService.dealVipAddr(addr);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


}
