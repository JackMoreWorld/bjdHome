package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.conf.AppConstant;
import com.bgd.app.dao.OrderDao;
import com.bgd.app.entity.VipOrderDto;
import com.bgd.app.jms.JmsSend;
import com.bgd.app.util.pay.wechatpay.WxPayUtil;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 支付回调
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-11
 */
@Slf4j
@Component
public class NotifyPayService {


    @Autowired
    JmsSend jmsSend;
    @Autowired
    OrderDao orderDao;


    /**
     * 支付宝支付回调处理逻辑
     *
     * @param paramMap 处理参数
     * @return
     */
    public String doAliPayNotify(Map<String, String> paramMap) {
        return null;
    }

    /**
     * 微信支付回调处理逻辑
     *
     * @param paramResult 处理参数
     * @return
     */
    public String doWxPayNotify(String paramResult) {
        WxPayUtil config = null;
        try {
            config = new WxPayUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap;
        try {
            notifyMap = WXPayUtil.xmlToMap(paramResult);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理

                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
                        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
                        //业务数据持久化
                        log.info("微信手机支付回调成功订单号:{}", out_trade_no);
                        //商户订单号
                        String attach = notifyMap.get("attach");
                        //微信支付订单号
                        String transaction_id = notifyMap.get("transaction_id");
                        //查询订单信息
                        VipOrderDto vipOrderDto = orderDao.getOrderInfo(out_trade_no, attach);
                        if (null != vipOrderDto && AppConstant.PAY_STATUS.DFK.equals(vipOrderDto.getStatus())) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("attach", attach);
                            jsonObject.put("transactionId", transaction_id);
                            //微信支付回调业务处理(消息队列)
                            jmsSend.wxNotifyPay(JSONObject.toJSONString(jsonObject));
                        }
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        //log.info("微信手机支付回调失败订单号:{}", out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                //失败的数据要不要存储？
                log.error("微信手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            log.error("微信手机支付回调通知失败", e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }


    /**
     * 微信支付退款回调处理逻辑
     *
     * @param paramResult 处理参数
     * @return
     */
    public String wxRefundNotifyRes(String paramResult) {
        WxPayUtil config = null;
        try {
            config = new WxPayUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap;
        try {
            notifyMap = WXPayUtil.xmlToMap(paramResult);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理

                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
                        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
                        //业务数据持久化
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        //log.info("微信手机支付回调失败订单号:{}", out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                //失败的数据要不要存储？
                log.error("微信手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            log.error("微信手机支付回调通知失败", e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }
}
