package com.bgd.app.controller;

import com.bgd.app.service.NotifyPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 微信支付回调通知
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-15
 */
@Slf4j
@RestController
@Api(tags = "微信支付回调通知")
public class Notify4WxPayController {


    @Autowired
    NotifyPayService notifyPayService;


    /**
     * 微信支付(统一下单接口)后台通知响应
     *
     * @param request
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/notify/pay/wxPayNotifyRes.htm")
    @ResponseBody
    @ApiOperation(value = "微信支付后台通知响应", notes = "微信支付后台通知响应")
    public String wxPayNotifyRes(HttpServletRequest request) throws ServletException, IOException {
        log.info("====== 开始接收微信支付回调通知 ======");
        String notifyRes = doWxPayRes(request);
        log.info("响应给微信:{}", notifyRes);
        log.info("====== 完成接收微信支付回调通知 ======");
        return notifyRes;
    }

    public String doWxPayRes(HttpServletRequest request) throws ServletException, IOException {
        String logPrefix = "【微信支付回调通知】";
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        log.info("{}通知请求数据:reqStr={}", logPrefix, xmlResult);
        return notifyPayService.doWxPayNotify(xmlResult);
    }

    /**
     * 微信支付(退款)后台通知响应
     *
     * @param request 请求
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/notify/pay/wxRefundNotifyRes.htm")
    @ResponseBody
    @ApiOperation(value = "微信支付(退款接口)后台通知响应", notes = "微信支付(退款接口)后台通知响应")
    public String wxRefundNotifyRes(HttpServletRequest request) throws ServletException, IOException {
        log.info("====== 开始接收微信支付(退款接口)回调通知 ======");
        String notifyRes = doWxRefundRes(request);
        log.info("响应给微信:{}", notifyRes);
        log.info("====== 完成接收微信支付(退款接口)回调通知 ======");
        return notifyRes;
    }

    public String doWxRefundRes(HttpServletRequest request) throws ServletException, IOException {
        String logPrefix = "【微信支付(退款接口)回调通知】";
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        log.info("{}通知请求数据:reqStr={}", logPrefix, xmlResult);
        return notifyPayService.wxRefundNotifyRes(xmlResult);
    }


    /**
     * 微信支付(充值)后台通知响应
     *
     * @param request 请求
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/notify/pay/wxReChargeNotifyRes.htm")
    @ResponseBody
    @ApiOperation(value = "微信支付(充值)后台通知响应", notes = "微信支付(充值)后台通知响应")
    public String wxReChargeNotifyRes(HttpServletRequest request) throws ServletException, IOException {
        log.info("====== 开始接收微信支付(充值)回调通知 ======");
        String notifyRes = doWxReChargeRes(request);
        log.info("响应给微信:{}", notifyRes);
        log.info("====== 完成接收微信支付(充值)回调通知 ======");
        return notifyRes;
    }

    public String doWxReChargeRes(HttpServletRequest request) throws ServletException, IOException {
        String logPrefix = "【微信支付(充值)回调通知】";
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        log.info("{}通知请求数据:reqStr={}", logPrefix, xmlResult);
        return null;
    }
}
