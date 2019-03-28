package com.bgd.app.jms;

import com.bgd.support.utils.BgdDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JmsSend {


    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息
     *
     * @param msgContent
     */
    public void send(String msgContent) {
        amqpTemplate.convertAndSend(RabbitConfigure.ORDER_QUEUE, msgContent);
    }


    /**
     * @描述 邀请好友
     * @创建人 JackMore
     * @创建时间 2019/3/9
     */
    public void invite(String msgContent) {
        amqpTemplate.convertAndSend(RabbitConfigure.INVITE_QUEUE, msgContent);
    }

    /**
     * @描述 活动下单
     * @创建人 JackMore
     * @创建时间 2019/3/9
     */
    public void activity(String msgContent) {
        amqpTemplate.convertAndSend(RabbitConfigure.ACTIVITY_QUEUE, msgContent);
    }


    /**
     * @描述 足迹
     * @创建人 JackMore
     * @创建时间 2019/3/12
     */
    public void foot(String msgContent) {
        amqpTemplate.convertAndSend(RabbitConfigure.FOOT_QUEUE, msgContent);
    }

    /**
     * 下单超过24h未付款自动取消
     *
     * @author wangguoqing
     * @since 2019-03-13
     */
    public void cancelOrder(String msqContent, final long delayTimes) {
        log.info("参数" + msqContent);
        log.info("提供者当前时间:" + BgdDateUtil.formatSdf(new Date(), "yyyy-MM-dd HH:mm:ss"));
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            //设置编码
            messageProperties.setContentEncoding("utf-8");
            //设置过期时间毫秒
            messageProperties.setExpiration(String.valueOf(delayTimes));
            return message;
        };
        amqpTemplate.convertAndSend("DL_EXCHANGE", "DL_KEY", msqContent, messagePostProcessor);
    }



    /**
     * 微信支付回调业务处理
     *
     * @author wangguoqing
     * @since 2019-03-14
     */
    public void wxNotifyPay(String msgContent) {
        amqpTemplate.convertAndSend(RabbitConfigure.PAY_NOTIFY_QUEUE, msgContent);
    }

    /**
     * 增加积分(订单支付)
     *
     * @author wangguoqing
     * @since 2019-03-15
     */
    public void addIntegralByOrder(String msgContent) {
        amqpTemplate.convertAndSend(RabbitConfigure.LEVEL_QUEUE, msgContent);
    }

    /**
     * 评价积分处理
     * {"detailId":"120","star":"10"}
     *
     * @param msgContent
     */
    public void addStarByOrder(String msgContent) {
        amqpTemplate.convertAndSend(RabbitConfigure.ORDER_STAR_QUEUE, msgContent);
    }

    /**
     * 删除购物车活动商品库存处理
     */
    public void cartStockHandler(String msgContent) {
        amqpTemplate.convertAndSend(RabbitConfigure.CART_QUEUE, msgContent);
    }


}