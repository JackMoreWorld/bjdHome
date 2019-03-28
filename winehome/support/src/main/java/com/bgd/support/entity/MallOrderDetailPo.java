package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/5
 * @描述
 */
@Data
public class MallOrderDetailPo extends BasePo {

    private Long orderId;
    private Long productId;
    private Long count;
    private BigDecimal totalPrice;
    private String capacity;
    private Integer status;
    private Date payTime;
    private Date deliveryTime;
    private Date refundTime;
    private Long vipId;
    private Integer activityId;

}
