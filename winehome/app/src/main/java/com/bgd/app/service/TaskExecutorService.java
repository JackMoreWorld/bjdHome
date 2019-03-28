package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.entity.MallOrderDto;
import com.bgd.app.jms.JmsSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TaskExecutorService {

    /**
     * 过期时间
     */
    private static final long DELAY_TIME = 60000 * 60 * 24;


    @Autowired
    JmsSend jmsSend;

    /**
     * 异步操作(下单超过24h未付款自动取消)
     */
    @Async("taskExecutor")
    public void orderAsyncHandler(MallOrderDto orderDto) {
        JSONObject object = new JSONObject();
        object.put("vipId", orderDto.getVipId());
        object.put("orderNo", orderDto.getOrderNo());
        jmsSend.cancelOrder(JSONObject.toJSONString(object), DELAY_TIME);
    }
}
