package com.bgd.admin.controller.productManager;

import com.bgd.admin.entity.CouponsManagerDto;
import com.bgd.admin.entity.param.CouponsFindParam;
import com.bgd.admin.service.CouponsManagerService;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.exception.ParameterException;
import com.bgd.support.utils.BgdStringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 优惠券信息管理 Controller
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@RestController
@RequestMapping(value = "/couponsManager")
@Api(tags = "优惠券信息管理接口")
public class CouponsManagerController {

    @Autowired
    CouponsManagerService couponsManagerService;

    /**
     * 新增优惠券信息
     *
     * @param couponsManagerDto 优惠券Dto
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping(value = "/addCoupons")
    @ApiOperation(value = "新增优惠券信息", notes = "新增优惠券信息")
    public ResultDto addCoupons(@RequestBody CouponsManagerDto couponsManagerDto) {
        log.info("新增优惠券信息");
        try {
            couponsManagerService.addCoupons(couponsManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("新增优惠券信息券失败 type = " + couponsManagerDto.getType() + "价格Price = " + couponsManagerDto.getPrice() +
                    "会员等级levelName = " + couponsManagerDto.getLevelName() + "酒庄名称chateauName = "
                    + couponsManagerDto.getChateauName(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 删除优惠券信息
     *
     * @param couponsId 优惠券ID
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping(value = "/deleteCoupons")
    @ApiOperation(value = "删除优惠券信息", notes = "删除优惠券信息")
    public ResultDto deleteCoupons(@RequestBody Long couponsId) {
        log.info("删除优惠券信息");
        try {
            if (BgdStringUtil.isStringEmpty(couponsId.toString())) {
                throw new ParameterException("删除优惠券信息Id不能为空");
            }
            couponsManagerService.deleteCoupons(couponsId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("删除优惠券信息失败 couponsId = " + couponsId, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * 修改优惠券信息
     *
     * @param couponsManagerDto 优惠券Dto
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping(value = "/updateCoupons")
    @ApiOperation(value = "修改优惠券信息", notes = "修改优惠券信息")
    public ResultDto updateCoupons(@RequestBody CouponsManagerDto couponsManagerDto) {
        log.info("修改优惠券信息");
        try {
            couponsManagerService.updateCoupons(couponsManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("修改优惠券 type = " + couponsManagerDto.getType() + "价格Price = " + couponsManagerDto.getPrice() +
                    "会员等级levelName = " + couponsManagerDto.getLevelName() + "酒庄名称chateauName = "
                    + couponsManagerDto.getChateauName() + "失败", e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 分页查询优惠券信息
     *
     * @param chateauName 酒庄名称
     * @param levelName   会员等级
     * @param price       价格
     * @param type        类型 0全场通用  1酒庄专属
     * @param dataStatus  状态 0停用 1启用
     * @param pageNo      页码
     * @return
     */
    @ValidToken(type = 0)
    @GetMapping(value = "/findCoupons")
    @ApiOperation(value = "查询优惠券信息", notes = "查询优惠券信息")
    public ResultDto<PageDto<CouponsManagerDto>> findAllCoupons(@RequestParam(required = false) String chateauName,
                                                                @RequestParam(required = false) String levelName,
                                                                @RequestParam(required = false) BigDecimal price,
                                                                @RequestParam Integer type,
                                                                @RequestParam Integer dataStatus,
                                                                @RequestParam Integer pageNo) {
        log.info("查询优惠券信息");
        try {
            CouponsFindParam couponsFindParam = new CouponsFindParam();
            couponsFindParam.setType(type);
            couponsFindParam.setChateauName(chateauName);
            couponsFindParam.setLevelName(levelName);
            couponsFindParam.setDateStatus(dataStatus);
            couponsFindParam.setPrice(price);
            couponsFindParam.setPageNo(pageNo);
            PageDto<CouponsManagerDto> pageDto = couponsManagerService.findCoupons(couponsFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", pageDto);
        } catch (BusinessException e) {
            log.error("查询优惠券失败 type = " + type + "价格Price = " + price +
                    "会员等级levelName = " + levelName + "酒庄名称chateauName = " + chateauName, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
