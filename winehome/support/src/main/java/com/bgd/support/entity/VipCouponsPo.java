package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/7
 * @描述  VIP 券
 */
@Data
public class VipCouponsPo extends BasePo {

    private Long vipId;
    private Long mallCouponsId;
    private Integer type;
    private Long chateauId;
    private Long levelId;
    private BigDecimal price;

}
