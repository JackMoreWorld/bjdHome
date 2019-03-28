package com.bgd.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.entity.MallChateauDto;
import com.bgd.app.entity.VipCenterDto;
import com.bgd.app.entity.param.CouponsParam;
import com.bgd.app.entity.param.VipSomeParam;
import com.bgd.app.service.VipCenterService;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/vip")
@Api(tags = "会员中心模块接口")
public class VipCenterController {

    @Autowired
    VipCenterService vipService;


    /**
     * @描述 获取个人中心页面的展示信息
     * @创建人 JackMore
     * @创建时间 2019/3/9
     */
    @GetMapping("/center/{vipId}")
    @ApiOperation(value = "获取个人中心页面的展示信息")
    public ResultDto<VipCenterDto> listVipAddr(@PathVariable Long vipId) {
        try {
            VipCenterDto vipCenterMsg = vipService.findVipCenterMsg(vipId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", vipCenterMsg);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 关注酒庄列表
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    @GetMapping("/chateauFocusList/{vipId}/{pageNo}")
    @ApiOperation(value = "关注酒庄列表")
    public ResultDto<List<MallChateauDto>> focusChateauList(@PathVariable Long vipId, @PathVariable Integer pageNo) {
        try {
            log.info("获取VIP关注酒庄列表");
            List<MallChateauDto> result = vipService.getVipFocusChateauList(vipId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", result);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * @描述 获取用户优惠券列表
     * @创建人 JackMore
     * @创建时间 2019/3/7
     */
    @GetMapping(value = "/listVipCoupons/{vipId}/{pageNo}")
    @ApiOperation(value = "用户优惠券列表")
    public ResultDto<JSONObject> listVipCoupons(@PathVariable Long vipId, @PathVariable Integer pageNo) {
        try {
            CouponsParam param = new CouponsParam();
            param.setVipId(vipId);
            param.setPageNo(pageNo);
            JSONObject vipCouponsList = vipService.getVipCouponsList(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", vipCouponsList);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    @GetMapping(value = "/listMallCoupons/{pageNo}")
    @ApiOperation(value = "领券中心")
    public ResultDto<JSONObject> listMallCoupons(@PathVariable Integer pageNo) {
        try {
            CouponsParam param = new CouponsParam();
            param.setPageNo(pageNo);
            JSONObject mallCouponsList = vipService.getMallCouponsList(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", mallCouponsList);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 查询用户足迹
     * @创建人 JackMore
     * @创建时间 2019/3/18
     */
    @GetMapping(value = "/listFoot/{vipId}/{pageNo}")
    @ApiOperation(value = "查询用户足迹列表")
    public ResultDto<JSONObject> listVipFoot(@PathVariable Long vipId, @PathVariable Integer pageNo) {
        try {
            VipSomeParam param = new VipSomeParam();
            param.setVipId(vipId);
            param.setPageNo(pageNo);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", vipService.pageVipFoot(param));
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        }
    }
}
