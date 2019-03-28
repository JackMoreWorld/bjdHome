package com.bgd.admin.controller;

import com.bgd.admin.entity.VipCouponsManagerDto;
import com.bgd.admin.service.VipCouManagerService;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.exception.ParameterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * vip优惠券管理接口 Controller
 * @author  Sunxk
 * @since  2019-3-18
 */
@Slf4j
@RestController
@RequestMapping("/vipCoupons")
@Api(tags = "vip优惠券管理接口")
public class VipCouManagerController {

    @Autowired
    VipCouManagerService vipCouManagerService;

    /**
     * 根据vipId查询vip优惠券信息
     * @param vipId 会员ID
     * @return 该会员所拥有的优惠券信息
     */
    @ValidToken(type = 0)
    @GetMapping("findVipCoupons")
    @ApiOperation(value = "根据vipId查询vip优惠券信息",notes = "根据vipId查询vip优惠券信息")
    public ResultDto<List<VipCouponsManagerDto>> findVipCoupons(@RequestParam Long vipId){
        log.info("根据vipId查询vip优惠券信息");
        try{
            List<VipCouponsManagerDto> vipCouponsManagerDtoList = vipCouManagerService.findVipCoupons(vipId);
            return new ResultDto<>(ResultDto.CODE_SUCC,"查询成功",vipCouponsManagerDtoList);
        }catch(BusinessException e){
            log.error("根据vipId查询vip优惠券信息失败",e);
            return new ResultDto<>(ResultDto.CODE_SUCC,e.getMessage(),null);
        }catch(Exception e){
            return new ResultDto<>(ResultDto.CODE_SUCC,"业务异常",null);
        }
    }

}
