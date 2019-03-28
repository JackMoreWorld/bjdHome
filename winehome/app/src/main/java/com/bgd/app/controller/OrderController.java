package com.bgd.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.entity.MallOrderDto;
import com.bgd.app.service.OrderService;
import com.bgd.app.util.SessionUserUtils;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.ResultDto;
import com.bgd.support.entity.VipInformationPo;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单模块 controller
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-12
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(tags = "订单模块接口")
public class OrderController {


    @Autowired
    OrderService orderService;

    @Autowired
    SessionUserUtils userUtils;


    /**
     * 获取确认订单数据（购物车、立即购买）
     *
     * @param cartIds 购物车id集合 (逗号分割)
     * @param proInfo 商品信息  （例如：{"id":1,"count":1,"actId":1}）
     * @param token   用户登录标识
     * @return 确认订单数据
     */
    @PostMapping(value = "/confirmOrder")
    @ValidToken(type = 1)
    @ApiOperation(value = "获取确认订单数据", notes = "获取确认订单数据")
    public ResultDto<JSONObject> confirmOrder(@RequestParam(required = false) String cartIds,
                                              @RequestParam(required = false) String proInfo, @RequestHeader String token) {
        log.info("获取确认订单数据");
        try {
            //获取redis缓存里的用户信息
            VipInformationPo informationPo = userUtils.getUserByRedis(token);
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功",
                    orderService.confirmOrder(cartIds, proInfo, informationPo.getId()));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 提交订单
     *
     * @param orderDto
     * @return 状态
     */
    @PostMapping(value = "/addOrder")
    @ValidToken(type = 1)
    @ApiOperation(value = "提交订单", notes = "提交订单")
    public ResultDto<JSONObject> addOrder(@RequestBody MallOrderDto orderDto, @RequestHeader String token) {
        try {
            //获取redis缓存里的用户信息
            VipInformationPo informationPo = userUtils.getUserByRedis(token);
            orderDto.setVipId(informationPo.getId());
            //提交订单
            return new ResultDto<>(ResultDto.CODE_SUCC, "下单成功", orderService.addOrder(orderDto));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }
}
