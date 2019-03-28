package com.bgd.app.entity;

import com.bgd.support.entity.ProductInformationPo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/7
 * @描述
 */
@Data
public class VipCenterDto {

    private Long vipId;
    private String logo;
    private String name;
    private String vipNo;
    private Integer collectCount;
    private Integer focusCount;
    private Integer footCount;
    private Long levelId;
    private Integer points;
    private BigDecimal balance;
    List<ProductInformationPo> foots;

    /**
     * 待付款订单数
     */
    private Integer dfkCount;
    /**
     * 待发货订单数
     */
    private Integer dfhCount;
    /**
     * 待收货订单数
     */
    private Integer dshCount;
    /**
     * 待评价订单数
     */
    private Integer dpjCount;
    /**
     * 待退款订单数
     */
    private Integer dtkCount;
}
