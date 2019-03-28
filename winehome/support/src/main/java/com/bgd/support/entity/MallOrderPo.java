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
public class MallOrderPo extends BasePo {

    private Long vipId;
    private String orderNo;
    private Integer payType;
    private Integer deliveryType;
    private BigDecimal price;
    private String linkMan;
    private String linkPhone;
    private String linkAddress;
    private String logisticsNo;
    private String remark;
    private BigDecimal freight;
    private Long couponsId;
    private Long discountPrice;
    private Integer status;

}
