package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/8
 * @描述
 */
@Data
public class MallCapitalFlow extends BasePo {

    private Long vipId;
    private Long relationId;
    private Integer userType;
    private Integer dir;
    private Integer payType;
    private Integer tradeSource;
    private BigDecimal tradeMoney;
    private String thirdTradeNo;
    private String remark;
}
