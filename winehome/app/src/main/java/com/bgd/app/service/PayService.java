package com.bgd.app.service;

import com.bgd.app.dao.OrderDao;
import com.bgd.app.jms.JmsSend;
import com.bgd.app.util.pay.wechatpay.WxPayUtil;
import com.bgd.support.exception.BusinessException;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信、支付宝支付 service
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-14
 */
@Service
@Slf4j
public class PayService {


    @Autowired
    WxPayUtil wxPayUtil;
    @Autowired
    OrderDao orderDao;
    @Autowired
    JmsSend jmsSend;

    /**
     * 微信支付- 统一下单
     *
     * @param orderNo  订单号
     * @param payPrice 订单支付金额
     * @throws BusinessException 业务异常
     */
    public Map<String, String> doUnifiedOrder(String orderNo, String payPrice, long vipId) throws BusinessException {
        try {
            WXPay wxpay = new WXPay(wxPayUtil);
            Map<String, String> data = new HashMap<>();
            //应用ID
            data.put("appid", wxPayUtil.getAppID());
            //微信支付分配的商户号
            data.put("mch_id", wxPayUtil.getMchID());
            //随机字符串，不长于32位。
            data.put("nonce_str", WXPayUtil.generateNonceStr());
            //商品描述
            String body = "订单支付";
            data.put("body", body);
            //商户订单号
            data.put("out_trade_no", orderNo);
            //订单支付总金额
            data.put("total_fee", payPrice);
            //订单优惠标记
            data.put("goods_tag", "WXG");
            //自己的服务器IP地址
            data.put("spbill_create_ip", wxPayUtil.getSpbillCreateIp());
            //异步通知地址（请注意必须是外网）
            data.put("notify_url", wxPayUtil.getNotifyUrl());
            //交易类型
            data.put("trade_type", "APP");
            //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
            data.put("attach", String.valueOf(vipId));
            data.put("sign", WXPayUtil.generateSignature(data, wxPayUtil.getKey(),
                    WXPayConstants.SignType.MD5));
            //使用官方API请求预付订单
            Map<String, String> response = wxpay.unifiedOrder(data);
            //下单成功
            if ("SUCCESS".equals(response.get("return_code"))) {
                //主要返回以下5个参数
                Map<String, String> param = new HashMap<>();
                param.put("appid", wxPayUtil.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("package", "Sign=WXPay");
                param.put("noncestr", WXPayUtil.generateNonceStr());
                param.put("timestamp", System.currentTimeMillis() / 1000 + "");
                param.put("sign", WXPayUtil.generateSignature(param, wxPayUtil.getKey(),
                        WXPayConstants.SignType.MD5));
                return param;
            }
            throw new BusinessException("微信支付失败");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("微信支付失败");
        }
    }


    /**
     * 微信支付-查询订单
     * <p>
     * 该接口提供所有微信支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
     *
     * @param orderNo 订单号
     */
    public String orderQuery(String orderNo) throws BusinessException {
        try {
            WxPayUtil config = new WxPayUtil();
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<>();
            //应用ID
            data.put("appid", config.getAppID());
            //微信支付分配的商户号
            data.put("mch_id", config.getMchID());
            //随机字符串，不长于32位。
            data.put("nonce_str", WXPayUtil.generateNonceStr());
            //商户订单号
            data.put("out_trade_no", orderNo);
            //签名
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                    WXPayConstants.SignType.MD5));
            //使用官方API请求查询订单
            Map<String, String> response = wxpay.orderQuery(data);
            //查询成功
            if ("SUCCESS".equals(response.get("return_code")) && "SUCCESS".equals(response.get("result_code"))) {
                //商户订单号
                /*String attach = response.get("attach");
                String out_trade_no = response.get("out_trade_no");//商户订单号
                //查询订单信息
                VipOrderDto vipOrderDto = orderDao.getOrderInfo(out_trade_no, attach);
                if (null != vipOrderDto && AppConstant.ORDER_STATUS.YFK.equals(vipOrderDto.getStatus())) {
                    //微信支付回调业务处理(消息队列)
                    jmsSend.wxNotifyPay(attach);
                }*/
                return "SUCCESS";
            }
            return "FAIL";
        } catch (Exception e) {
            throw new BusinessException("查询订单接口异常");
        }
    }

    /**
     * 微信支付- 关闭订单接口
     *
     * @param orderNo 订单号
     */
    public String closeOrder(String orderNo) throws BusinessException {
        try {
            WxPayUtil config = new WxPayUtil();
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<>();
            //应用ID
            data.put("appid", config.getAppID());
            //微信支付分配的商户号
            data.put("mch_id", config.getMchID());
            //随机字符串，不长于32位。
            data.put("nonce_str", WXPayUtil.generateNonceStr());
            //商户订单号
            data.put("out_trade_no", orderNo);
            //签名
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                    WXPayConstants.SignType.MD5));
            //微信支付- 关闭订单
            Map<String, String> response = wxpay.closeOrder(data);
            if ("SUCCESS".equals(response.get("return_code")) && "SUCCESS".equals(response.get("result_code"))) {
                return "SUCCESS";
            }
            return "FAIL";
        } catch (Exception e) {
            throw new BusinessException("微信支付- 关闭订单接口异常");
        }
    }

    /**
     * 微信支付- 退款发起接口
     *
     * @param orderNo     订单号
     * @param refundNo    退款单号
     * @param orderPrice  订单总金额
     * @param refundPrice 退款金额
     * @param refundDesc  退款原因
     */
    public String refundOrder(String orderNo, String refundNo, long orderPrice, long refundPrice,
                              String refundDesc) throws BusinessException {
        try {
            WxPayUtil config = new WxPayUtil();
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<>();
            //应用ID
            data.put("appid", config.getAppID());
            //微信支付分配的商户号
            data.put("mch_id", config.getMchID());
            //随机字符串，不长于32位。
            data.put("nonce_str", WXPayUtil.generateNonceStr());
            //商户订单号
            data.put("out_trade_no", orderNo);
            //商户退款单号
            data.put("out_refund_no", refundNo);
            //订单金额
            data.put("total_fee", String.valueOf(orderPrice));
            //退款金额
            data.put("refund_fee", String.valueOf(refundPrice));
            //退款原因
            data.put("refundDesc", refundDesc);
            //退款回调url
            data.put("notify_url", config.getRefundNotifyUrl());
            //签名
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                    WXPayConstants.SignType.MD5));
            //微信支付- 退款发起接口
            Map<String, String> response = wxpay.refund(data);
            //退款发起申请成功
            if ("SUCCESS".equals(response.get("return_code")) && "SUCCESS".equals(response.get("result_code"))) {
                return "SUCCESS";
            }
            return "FAIL";
        } catch (Exception e) {
            throw new BusinessException("微信支付- 退款接口异常");
        }
    }
}
