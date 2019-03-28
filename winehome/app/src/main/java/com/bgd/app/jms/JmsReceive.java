package com.bgd.app.jms;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bgd.app.dao.CartDao;
import com.bgd.app.service.ActService;
import com.bgd.app.service.MallService;
import com.bgd.app.service.OrderService;
import com.bgd.app.service.VipService;
import com.bgd.support.entity.MallInviteRecordPo;
import com.bgd.support.entity.VipFootMarkPo;
import com.bgd.support.utils.BgdDateUtil;
import com.google.common.collect.Lists;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JmsReceive {
    @Autowired
    MallService mallService;

    @Autowired
    VipService vipService;
    @Autowired
    ActService actService;

    @Autowired
    OrderService orderService;
    @Autowired
    CartDao cartDao;

    /**
     * @描述 注册邀请记录
     * @创建人 JackMore
     * @创建时间 2019/3/9
     */
    @RabbitListener(queues = RabbitConfigure.INVITE_QUEUE)
    public void inviteRecordMqDeal(String content, Channel channel, Message message) {
        String text = new String(message.getBody(), Charset.forName("utf8"));
        JSONObject mqObj = JSONObject.parseObject(text);
        MallInviteRecordPo po = JSONObject.toJavaObject(mqObj, MallInviteRecordPo.class);
        try {
            mallService.dealInviteRecord(po);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("处理邀请记录异常", e);
        }
    }

    /**
     * @描述 用户足迹保存
     * @创建人 JackMore
     * @创建时间 2019/3/12
     */
    @RabbitListener(queues = RabbitConfigure.FOOT_QUEUE)
    public void footMqDeal(String content, Channel channel, Message message) {
        String text = new String(message.getBody(), Charset.forName("utf8"));
        JSONObject mqObj = JSONObject.parseObject(text);
        VipFootMarkPo po = JSONObject.toJavaObject(mqObj, VipFootMarkPo.class);
        try {
            vipService.markFoot(po);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("处理用户足迹异常", e);
        }
    }


    /**
     * 下单超过24h未付款自动取消
     *
     * @author wangguoqing
     * @since 2019-03-13
     */
    @RabbitListener(queues = RabbitConfigure.CANCEL_ORDER_QUEUE)
    public void cancelOrder(String content, Channel channel, Message message) {
        String text = new String(message.getBody(), Charset.forName("utf-8"));
        JSONObject object = JSONObject.parseObject(text);
        try {
            log.info("消费者当前时间:" + BgdDateUtil.formatSdf(new Date(), "yyyy-MM-dd HH:mm:ss"));
            orderService.cancelOrder(object.getString("orderNo"), object.getString("vipId"));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("处理下单超过24h未付款自动取消异常", e);
        }
    }

    /**
     * @return void
     * @author Sunxk
     * @date 2019/3/14
     * @Param []
     */
    @RabbitListener(queues = RabbitConfigure.LEVEL_QUEUE)
    public void addLevel(String content, Channel channel, Message message) {
        String text = new String(message.getBody(), Charset.forName("utf-8"));
        JSONObject jsonObject = JSONObject.parseObject(text);
        try {
            Long id = jsonObject.getLong("vipId");
            Long status = vipService.callUpLevelByPointsP(id, jsonObject.getLong("points"));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if (status == 1L) {
                log.error("uplevel_by_points_py异常 id= " + id);
            }
        } catch (Exception e) {
            log.error("会员等级提升异常", e);
        }
    }

    /**
     * 微信支付回调订单业务处理
     *
     * @author wangguoqing
     * @since 2019-03-15
     */
    @RabbitListener(queues = RabbitConfigure.PAY_NOTIFY_QUEUE)
    public void wxPayOrderNotify(String content, Channel channel, Message message) {
        String text = new String(message.getBody(), Charset.forName("utf-8"));
        JSONObject jsonObject = JSONObject.parseObject(text);
        try {
            //商户订单号
            String orderNo = jsonObject.getString("attach");
            //微信支付订单号
            String transactionId = jsonObject.getString("transactionId");
            //支付成功后订单相关业务处理
            orderService.orderBusinessHandler(orderNo, transactionId);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("支付回调订单业务处理异常", e);
        }
    }

    /**
     * @描述 商品评价星级修改
     * @创建人 JackMore
     * @创建时间 2019/3/18
     */
    @RabbitListener(queues = RabbitConfigure.ORDER_STAR_QUEUE)
    public void addProductStar(String content, Channel channel, Message message) {
        String text = new String(message.getBody(), Charset.forName("utf-8"));
        JSONObject jsonObject = JSONObject.parseObject(text);
        try {
            //订单详情id
            Long detailId = jsonObject.getLong("detailId");
            //评分
            Long star = jsonObject.getLong("star");
            orderService.dealProductStar(detailId, star);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("支付回调订单业务处理异常", e);
        }
    }

    /**
     * 删除购物车活动商品库存处理
     *
     * @author wgq
     * @since 2019-03-21
     */
    @SuppressWarnings("unchecked")
    @RabbitListener(queues = RabbitConfigure.CART_QUEUE)
    public void cartStockHandler(String content, Channel channel, Message message) {
        String text = new String(message.getBody(), Charset.forName("utf-8"));
        try {
            List<Map> map = JSONArray.parseArray(text, Map.class);
            List<Map<String, String>> newMap = Lists.newArrayList();
            for (Map<String, String> mp : map) {
                Object actId = mp.get("actId");
                log.info("活动id：" + actId.toString());
                newMap.add(mp);
            }
            if (newMap.size() > 0) {
                //更新库存
                cartDao.updActProStock(newMap);
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("删除购物车活动商品库存处理异常", e);
        }
    }

}