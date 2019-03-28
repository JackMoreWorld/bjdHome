package com.bgd.admin.controller;

import com.bgd.admin.entity.OrderDetManagerDto;
import com.bgd.admin.entity.param.OrderDetFindParam;
import com.bgd.admin.service.OrderDetManagerService;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdStringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单详情信息管理接口
 *
 * @author Sunxk
 * @since 2019-3-22
 */

@Slf4j
@RestController
@RequestMapping("/orderDet")
@Api(tags = "订单详情信息管理接口")
public class OrderDetManagerController {

    @Autowired
    OrderDetManagerService orderDetManagerService;

    /**
     * 审核订单退款申请
     *
     * @param id     订单详情表ID
     * @param result 审核结果 pass/noPass
     */
    @ValidToken(type = 0)
    @PostMapping("/auditRefund")
    @ApiOperation(value = "审核订单退款申请", notes = "审核订单退款申请")
    public ResultDto auditRefund(@RequestParam Long id,
                                 @RequestParam String result) {
        log.info("审核订单退款申请");
        try {
            if (BgdStringUtil.isStringEmpty(id.toString()) || BgdStringUtil.isStringEmpty(result)) {
                throw new BusinessException("审核订单退款申请失败 id或result为空");
            }
            orderDetManagerService.auditRefund(id, result);
            return new ResultDto<>(ResultDto.CODE_SUCC, "审核订单退款申请成功", null);
        } catch (BusinessException e) {
            log.error("审核订单退款申请失败", e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }

    }

    /**
     * 查询待审核退款申请订单
     *
     * @param refundNo    退款单号
     * @param orderNo     订单编号
     * @param vipName     会员昵称
     * @param productName 商品名称
     */
    @ValidToken(type = 0)
    @GetMapping("/findOrderDetRefund")
    @ApiOperation(value = "查询待审核退款申请订单", notes = "查询待审核退款申请订单")
    public ResultDto<PageDto<OrderDetManagerDto>> findOrderDetRefund(@RequestParam(required = false) String refundNo,
                                                                     @RequestParam(required = false) String orderNo,
                                                                     @RequestParam(required = false) String vipName,
                                                                     @RequestParam(required = false) String productName,
                                                                     @RequestParam Integer pageNo) {
        log.info("查询待审核退款申请订单");
        try {
            OrderDetFindParam orderDetFindParam = new OrderDetFindParam();
            orderDetFindParam.setRefundNo(refundNo);
            orderDetFindParam.setOrderNo(orderNo);
            orderDetFindParam.setVipName(vipName);
            orderDetFindParam.setProductName(productName);
            orderDetFindParam.setPageNo(pageNo);
            PageDto<OrderDetManagerDto> pageDto = orderDetManagerService.findOrderDetRefund(orderDetFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询待审核退款申请订单成功", pageDto);
        } catch (BusinessException e) {
            log.error("", e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
