package com.bgd.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.entity.VipInformationDto;
import com.bgd.app.entity.param.SecureParam;
import com.bgd.app.service.VipService;
import com.bgd.support.base.ResultDto;
import com.bgd.support.entity.VipInformationPo;
import com.bgd.support.entity.VipReward;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(value = "/vip")
@Api(tags = "会员模块接口")
public class VipController {

    @Autowired
    VipService vipService;

    /**
     * 注册
     * 登录
     * 信息
     * 关注酒庄列表
     * 收藏单品列表（足迹）
     * 优惠券修改
     */

    /**
     * @描述 获取验证码
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    @GetMapping("/phoneCode/{phone}/{type}")
    @ApiOperation(value = "获取验证码")
    public ResultDto<JSONObject> vipPhoneCode(@PathVariable String phone, @PathVariable Integer type) {
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "", vipService.getPhoneCode(phone, type));
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @描述 验证手机验证码
     * @创建人 JackMore
     * @创建时间 2019/3/21
     */
    @GetMapping("/validPhone/{phone}/{code}/{type}")
    @ApiOperation(value = "验证验证码")
    public ResultDto<Boolean> validPhoneCode(@PathVariable String phone, @PathVariable String code, @PathVariable Integer type) {
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "", vipService.validPhoneCode(phone, code, type));
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        }
    }


    @PostMapping(value = "/regVipInfo")
    @ApiOperation(value = "注册VIP")
    public ResultDto<JSONObject> regVipInfo(@RequestBody VipInformationDto vip) {
        try {
            JSONObject result = vipService.regVip(vip);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", result);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * @描述 完善vip信息
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    @PostMapping(value = "/completeVipInfo")
    @ApiOperation(value = "完善vip信息")
    public ResultDto<JSONObject> updateVipInfo(@RequestBody VipInformationPo vip) {
        try {
            JSONObject result = vipService.completeVipInfo(vip);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", result);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 账户安全信息维护
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    @PostMapping(value = "/secureVipInfo")
    @ApiOperation(value = "用户信息安全维护")
    public ResultDto<JSONObject> secureVipInfo(@RequestBody SecureParam param) {
        try {
            JSONObject result = vipService.secureVipInfo(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", result);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 获取VIP基础信息
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    @GetMapping("/information/{id}")
    @ApiOperation(value = "获取VIP基础信息")
    public ResultDto<VipInformationDto> vipMsgDetail(@PathVariable Long id) {
        try {
            log.info("vip获取信息");
            VipInformationDto result = vipService.getVipMsgById(id);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", result);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 关注或取消关注酒庄
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    @GetMapping("/focus/{vipId}/{chateauId}/{status}")
    @ApiOperation(value = "关注或取消关注酒庄")
    public ResultDto<String> focusOrCancel(@PathVariable Long vipId, @PathVariable Long chateauId, @PathVariable Integer status) {
        try {
            log.info("关注酒庄操作");
            vipService.focusOrNot(vipId, chateauId, status);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 收藏或取消收藏产品
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    @GetMapping("/collect/{vipId}/{productId}/{status}")
    @ApiOperation(value = "收藏或取消收藏产品")
    public ResultDto<String> collectOrCancel(@PathVariable Long vipId, @PathVariable Long productId, @PathVariable Integer status) {
        try {
            log.info("收藏单品操作");
            vipService.collectOrCancel(vipId, productId, status);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 会员领取优惠券
     * @创建人 JackMore
     * @创建时间 2019/3/7
     */
    @GetMapping("/gather/{vipId}/{couponsId}")
    @ApiOperation(value = "会员领取优惠券")
    public ResultDto<String> gatherCoupons(@PathVariable Long vipId, @PathVariable Long couponsId) {
        try {
            log.info("会员领取优惠券");
            vipService.gatherCoupons(vipId, couponsId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    @GetMapping("/sign/{vipId}")
    @ApiOperation(value = "会员签到")
    public ResultDto<String> signIn(@PathVariable Long vipId) {
        try {
            log.info("会员签到领积分");
            vipService.signIn(vipId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "签到成功", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    @GetMapping("/reward/{vipId}")
    @ApiOperation(value = "会员红包")
    public ResultDto<VipReward> reward(@PathVariable Long vipId) {
        try {
            log.info("会员领取红包");
            VipReward vipReward = vipService.findVipReward(vipId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询红包", vipReward);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
