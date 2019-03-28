package com.bgd.app.controller;

import com.bgd.app.entity.MallOrderDetailDto;
import com.bgd.app.entity.VipInformationDto;
import com.bgd.app.entity.param.VipOrderParam;
import com.bgd.app.service.OrderService;
import com.bgd.app.util.SessionUserUtils;
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

/**
 * 会员订单模块 controller
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-13
 */
@Slf4j
@RestController
@RequestMapping("/vip/order")
@Api(tags = "会员订单模块接口")
public class VipOrderController {


    @Autowired
    OrderService orderService;

    @Autowired
    SessionUserUtils userUtils;


    /**
     * 查询会员订单列表分页数据
     *
     * @param status 订单状态
     * @param pageNo 页码
     * @param token  用户登录标识
     * @return 订单列表分页数据
     */
    @GetMapping(value = "/queryOrderList")
    @ValidToken(type = 1)
    @ApiOperation(value = "查询会员订单列表分页数据", notes = "查询会员订单列表分页数据")
    public ResultDto<PageDto<String>> queryOrderListByPage(@RequestParam(required = false) Long status,
                                                           @RequestParam Long pageNo, @RequestHeader String token) {
        log.info("查询会员订单列表分页数据start");
        try {
            //获取redis缓存里的用户信息
            VipInformationDto informationDto = userUtils.getUserByRedis(token);
            VipOrderParam vipOrderParam = new VipOrderParam();
            vipOrderParam.setVipId(informationDto.getId());
            vipOrderParam.setStatus(status == null ? null : status.hashCode());
            vipOrderParam.setPageNo(pageNo.hashCode());
            log.info("查询会员订单列表分页数据end");
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功",
                    orderService.queryOrderListByPage(vipOrderParam));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 查询订单详情
     *
     * @param detailId 订单详情id
     * @param token    用户登录标识
     * @return 订单详情
     */
    @GetMapping(value = "/getOrderDetail/{detailId}")
    @ValidToken(type = 1)
    @ApiOperation(value = "根据订单详情id查询订单详情", notes = "根据订单详情id查询订单详情")
    public ResultDto<MallOrderDetailDto> getOrderDetail(@PathVariable String detailId, @RequestHeader String token) {
        log.info("根据订单号查询订单详情");
        try {
            //获取redis缓存里的用户信息
            VipInformationDto informationDto = userUtils.getUserByRedis(token);
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功",
                    orderService.getOrderDetail(detailId, String.valueOf(informationDto.getId())));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 取消订单
     *
     * @param token   用户登录标识
     * @param orderNo 订单号
     */
    @PostMapping(value = "/cancelOrder/{orderNo}")
    @ValidToken(type = 1)
    @ApiOperation(value = "取消订单", notes = "取消订单")
    public ResultDto<String> cancelOrder(@PathVariable String orderNo, @RequestHeader String token) {
        try {
            //获取redis缓存里的用户信息
            VipInformationDto informationDto = userUtils.getUserByRedis(token);
            //删除订单信息
            orderService.cancelOrder(orderNo, String.valueOf(informationDto.getId()));
            return new ResultDto<>(ResultDto.CODE_SUCC, "取消成功", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "取消订单业务异常", null);
        }
    }

    /**
     * 订单确认收货
     *
     * @param token    用户登录标识
     * @param detailId 订单详情id
     */
    @PostMapping(value = "/confirmReceipt/{detailId}")
    @ValidToken(type = 1)
    @ApiOperation(value = "订单确认收货", notes = "订单确认收货")
    public ResultDto<String> confirmReceipt(@PathVariable String detailId, @RequestHeader String token) {
        log.info("订单确认收货start");
        try {
            //获取redis缓存里的用户信息
            VipInformationDto informationDto = userUtils.getUserByRedis(token);
            //订单确认收货
            orderService.confirmReceipt(detailId, String.valueOf(informationDto.getId()));
            return new ResultDto<>(ResultDto.CODE_SUCC, "处理成功", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 申请退款
     *
     * @param token    用户登录标识
     * @param detailId 订单详情id
     */
    @PostMapping("/applyRefund/{detailId}")
    @ApiOperation(value = "申请退款接口", notes = "订单申请退款")
    @ValidToken(type = 1)
    public ResultDto<String> applyRefund(@PathVariable String detailId, @RequestHeader String token) {
        try {
            if (BgdStringUtil.isStringEmpty(detailId)) {
                throw new ParameterException("订单详情id不能为空");
            }
            //获取redis缓存里的用户信息
            VipInformationDto informationDto = userUtils.getUserByRedis(token);
            //申请退款
            orderService.applyRefund(detailId, informationDto.getId());
            return new ResultDto<>(ResultDto.CODE_SUCC, "申请成功", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 订单商品评价
     *
     * @param token    用户登录标识
     * @param remark   评论内容
     * @param detailId 订单详情id
     * @param star     评分等级 0差评 5中评 10好评
     */
    @PostMapping("/orderEvaluate")
    @ApiOperation(value = "订单商品评价接口", notes = "订单商品评价")
    @ValidToken(type = 1)
    public ResultDto<String> orderEvaluate(@RequestParam String detailId, @RequestParam String remark,
                                           @RequestParam Long star, @RequestHeader String token) {
        try {
            if (BgdStringUtil.isStringEmpty(detailId)) {
                throw new ParameterException("订单详情id不能为空");
            }
            //获取redis缓存里的用户信息
            VipInformationDto informationDto = userUtils.getUserByRedis(token);
            //订单商品评价
            orderService.orderEvaluate(detailId, remark, star, informationDto.getId());
            return new ResultDto<>(ResultDto.CODE_SUCC, "评价成功", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 删除订单
     *
     * @param token    用户登录标识
     * @param detailId 订单详情id
     */
    @PostMapping(value = "/delOrder/{detailId}")
    @ValidToken(type = 1)
    @ApiOperation(value = "取消订单", notes = "取消订单")
    public ResultDto<String> delOrder(@PathVariable String detailId, @RequestHeader String token) {
        try {
            //获取redis缓存里的用户信息
            VipInformationDto informationDto = userUtils.getUserByRedis(token);
            //删除订单信息
            orderService.delOrder(detailId, String.valueOf(informationDto.getId()));
            return new ResultDto<>(ResultDto.CODE_SUCC, "删除成功", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
