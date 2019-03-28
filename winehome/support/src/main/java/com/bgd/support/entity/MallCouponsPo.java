package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

import java.math.BigDecimal;

/**
* @创建人 Sunxk
* @创建时间 2019/3/7
* @描述   mall_coupons
*/
@Data
public class MallCouponsPo extends BasePo {

    private Integer type; //类型
    private Long chateauId;//酒庄ID
    private Long levelId;//会员等级ID
    private BigDecimal price;//价格

}
