package com.bgd.admin.controller;

import com.bgd.admin.entity.OrderManagerDto;
import com.bgd.admin.entity.param.OrderFindParam;
import com.bgd.admin.service.OrderManagerService;
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
 * 订单查询 controller
 *
 * @author Sunxk
 * @since 2019-3-16
 */
@Slf4j
@RestController
@RequestMapping("/orderManager")
@Api(tags = "订单管理模块接口")
public class OrderManagerController {

    @Autowired
    OrderManagerService orderManagerService;

    /**
     * 查询订单列表
     *
     * @param orderNo     订单编号
     * @param logisticsNo 物流单号
     * @param name        下单人昵称
     * @param phone       下单人联系方式
     * @param pageNo      页码
     * @return
     */
    @ValidToken(type = 0)
    @GetMapping("/findOrder")
    @ApiOperation(value = "查询订单列表", notes = "查询订单列表")
    public ResultDto<PageDto<OrderManagerDto>> findOrder(@RequestParam(required = false) String orderNo,
                                                         @RequestParam(required = false) String logisticsNo,
                                                         @RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String phone,
                                                         @PathVariable Integer pageNo) {
        log.info("根据条件查询订单列表");
        try {
            OrderFindParam orderFindParam = new OrderFindParam();
            orderFindParam.setOrderNo(orderNo);
            orderFindParam.setLogisticsNo(logisticsNo);
            orderFindParam.setName(name);
            orderFindParam.setPhone(phone);
            orderFindParam.setPageNo(pageNo);
            PageDto<OrderManagerDto> pageDto = orderManagerService.findOrder(orderFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "根据条件查询订单列表成功", pageDto);
        } catch (BusinessException e) {
            log.error("查询订单列表异常", e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * @return com.bgd.support.base.ResultDto<com.bgd.admin.entity.OrderManagerDto>
     * @author Sunxk
     * @date 2019/3/16
     * @Param [orderId]
     */
    @ValidToken(type = 0)
    @GetMapping("/findOneOrder/{orderId}")
    @ApiOperation(value = "查询订单详情", notes = "查询订单详情")
    public ResultDto<OrderManagerDto> findOneOrder(@PathVariable Long orderId) {
        log.info("根据订单Id查询订单详情");
        try {
            if (BgdStringUtil.isStringEmpty(orderId.toString())) {
                throw new ParameterException("订单Id不能为空");
            }
            OrderManagerDto orderManagerDto = orderManagerService.findOneOrder(orderId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "根据订单Id查询订单详情成功", orderManagerDto);
        } catch (BusinessException e) {
            log.error("根据订单Id查询订单详情异常", e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
