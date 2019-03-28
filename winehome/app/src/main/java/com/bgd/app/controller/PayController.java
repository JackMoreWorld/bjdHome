package com.bgd.app.controller;


import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.bgd.app.entity.VipInformationDto;
import com.bgd.app.service.OrderService;
import com.bgd.app.service.PayService;
import com.bgd.app.util.SessionUserUtils;
import com.bgd.app.util.pay.alipay.AlipayConfig;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 微信、支付宝支付 controller
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-14
 */
@RestController
@RequestMapping("/pay")
@Slf4j
@Api(tags = "微信、支付宝支付")
public class PayController {


    @Autowired
    private PayService payService;
    @Autowired
    SessionUserUtils userUtils;
    @Autowired
    OrderService orderService;
    @Autowired
    AlipayConfig alipayConfig;


    /**
     * 支付宝支付请求
     */
    @GetMapping("/alipay/{orderNo}/{totalPrice}")
    @ValidToken(type = 1)
    @ApiOperation(value = "支付宝支付请求")
    public ResultDto<String> pay(@PathVariable String orderNo, @PathVariable BigDecimal totalPrice) {
        try {
            double price = totalPrice.doubleValue();
            System.out.println("price:" + price);
            String form; // 生成支付表单
            // 封装请求客户端 // 实例化客户端
            AlipayClient client = new DefaultAlipayClient(alipayConfig.getUrl(), alipayConfig.getApp_id(),
                    alipayConfig.getRsa_private_key(), alipayConfig.getFormat(), alipayConfig.getCharset(),
                    alipayConfig.getAlipay_public_key(), alipayConfig.getSigntype());

            // 支付请求
            // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
            // SDK已经封装掉了公共参数，这里只需要传入业务参数。
            // 以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradePayModel model = new AlipayTradePayModel();
            // 描述信息 添加附加数据
            model.setProductCode("QUICK_MSECURITY_PAY"); // 设置销售产品码
            model.setOutTradeNo(orderNo); // 设置订单号
            model.setSubject("--酒之家app订单支付--"); // 订单名称
            model.setTotalAmount("" + price + ""); // 支付总金额
            model.setBody("body"); // 设置商品描述
            model.setTimeoutExpress("30m"); // 超时关闭该订单时间
            // model.setSellerId("416032133@qq.com"); // 商家id
            alipayRequest.setBizModel(model);
            // 异步回调地址
            alipayRequest.setNotifyUrl(alipayConfig.getNotify_url());
            AlipayTradeAppPayResponse responses = client.sdkExecute(alipayRequest);
            form = responses.getBody();// 生成支付表单
            System.out.println(form); // 就是orderString 可以直接给客户端请求，无需再做处理
            return new ResultDto<>(ResultDto.CODE_SUCC, "", form);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * 微信支付-统一下单接口
     *
     * @param orderNo  订单号
     * @param payPrice 订单支付金额
     */
    @GetMapping(value = "/weChat/pay/{orderNo}/{payPrice}")
    @ValidToken(type = 1)
    @ApiOperation(value = "微信支付-统一下单接口", notes = "微信支付-统一下单接口")
    public ResultDto<Map<String, String>> doUnifiedOrder(@PathVariable String orderNo, @PathVariable String payPrice,
                                                         @RequestHeader String token) {
        log.info("微信支付-统一下单接口");
        try {
            // 获取redis缓存里的用户信息
            VipInformationDto vipInformationDto = userUtils.getUserByRedis(token);
            return new ResultDto<>(ResultDto.CODE_SUCC, "微信支付成功",
                    payService.doUnifiedOrder(orderNo, payPrice, vipInformationDto.getId()));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 微信支付- 查询订单接口
     * <p>
     * 查询用户实际支付结果
     *
     * @param orderNo 订单号
     */
    @GetMapping(value = "/weChat/queryOrder/{orderNo}")
    @ValidToken(type = 1)
    @ApiOperation(value = "微信支付- 查询订单接口", notes = "微信支付- 查询订单接口")
    public ResultDto<String> queryOrder(@PathVariable String orderNo) {
        log.info("微信支付- 查询订单");
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "微信支付成功",
                    payService.orderQuery(orderNo));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 微信支付- 关闭订单接口
     *
     * @param orderNo 订单号
     */
    @GetMapping(value = "/weChat/closeOrder/{orderNo}")
    @ValidToken(type = 1)
    @ApiOperation(value = "微信支付- 关闭订单接口", notes = "微信支付- 关闭订单接口")
    public ResultDto<String> closeOrder(@PathVariable String orderNo) {
        log.info("微信支付- 关闭订单");
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "微信订单关闭成功", payService.closeOrder(orderNo));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 余额支付
     *
     * @param token    用户登录标识
     * @param orderNo  订单号
     * @param payPrice 支付金额
     */
    @PostMapping("/balancePay")
    @ApiOperation(value = "余额支付", notes = "余额支付")
    @ValidToken(type = 1)
    public ResultDto<String> balancePay(@RequestParam String orderNo, @RequestParam String payPrice,
                                        @RequestHeader String token) {
        try {
            // 获取redis缓存里的用户信息
            VipInformationDto vipInformationDto = userUtils.getUserByRedis(token);
            orderService.balancePay(vipInformationDto.getId(), orderNo, payPrice);
            return new ResultDto<>(ResultDto.CODE_SUCC, "支付成功", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
