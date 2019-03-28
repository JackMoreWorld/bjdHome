package com.bgd.app.jms;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitConfigure {
    // 队列名称
    public final static String ORDER_QUEUE = "bgd:order:queue";
    public final static String ORDER_STAR_QUEUE = "bgd:order:star:queue";
    public final static String ACTIVITY_QUEUE = "bgd:activity:queue";
    public final static String INVITE_QUEUE = "bgd:invite:queue";
    public final static String FOOT_QUEUE = "bgd:foot:queue";
    public final static String CANCEL_ORDER_QUEUE = "bgd:cancel_order:queue";


    public final static String PAY_NOTIFY_QUEUE = "bgd:pay_notify:queue";
    public final static String LEVEL_QUEUE = "bgd:add_level:queue";

    public final static String CART_QUEUE = "bgd:cart:queue";


    public final static String DL_QUEUE = "DL_QUEUE";
    /**
     * 死信队列 交换机标识符
     */
    private static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列交换机绑定键标识符
     */
    private static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";


    /**
     * 死信队列跟交换机类型没有关系 不一定为directExchange  不影响该类型交换机的特性.
     *
     * @return the exchange
     */
    @Bean("deadLetterExchange")
    public Exchange deadLetterExchange() {
        return ExchangeBuilder.directExchange("DL_EXCHANGE").durable(true).build();
    }


    /**
     * 声明一个死信队列.
     * x-dead-letter-exchange   对应  死信交换机
     * x-dead-letter-routing-key  对应 死信队列
     *
     * @return the queue
     */
    @Bean("deadLetterQueue")
    public Queue deadLetterQueue() {
        Map<String, Object> args = new HashMap<>(2);
        //x-dead-letter-exchange    声明  死信交换机
        args.put(DEAD_LETTER_QUEUE_KEY, "DL_EXCHANGE");
        //x-dead-letter-routing-key    声明 死信路由键
        args.put(DEAD_LETTER_ROUTING_KEY, "KEY_R");
        return QueueBuilder.durable(DL_QUEUE).withArguments(args).build();
    }


    /**
     * 定义死信队列转发队列.
     *
     * @return the queue
     */
    @Bean("redirectQueue")
    public Queue redirectQueue() {
        return QueueBuilder.durable(CANCEL_ORDER_QUEUE).build();
    }

    /**
     * 死信路由通过 DL_KEY 绑定键绑定到死信队列上.
     *
     * @return the binding
     */
    @Bean
    public Binding deadLetterBinding() {
        return new Binding(DL_QUEUE, Binding.DestinationType.QUEUE, "DL_EXCHANGE", "DL_KEY", null);

    }

    /**
     * 死信路由通过 KEY_R 绑定键绑定到死信队列上.
     *
     * @return the binding
     */
    @Bean
    public Binding redirectBinding() {
        return new Binding(CANCEL_ORDER_QUEUE, Binding.DestinationType.QUEUE, "DL_EXCHANGE", "KEY_R", null);
    }


    /**
     * 定义队列
     *
     * @return
     */
    @Bean
    Queue order_queue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    Queue invite_queue() {
        return new Queue(INVITE_QUEUE, true);
    }

    @Bean
    Queue activity_queue() {
        return new Queue(ACTIVITY_QUEUE, true);
    }

    @Bean
    Queue foot_queue() {
        return new Queue(FOOT_QUEUE, true);
    }


    @Bean
    Queue level_queue() {
        return new Queue(LEVEL_QUEUE, true);
    }

    @Bean
    Queue pay_notify_queue() {
        return new Queue(PAY_NOTIFY_QUEUE, true);
    }

    @Bean
    Queue order_star_queue() {
        return new Queue(ORDER_STAR_QUEUE, true);
    }

    @Bean
    Queue cart_queue() {
        return new Queue(CART_QUEUE, true);
    }


}
