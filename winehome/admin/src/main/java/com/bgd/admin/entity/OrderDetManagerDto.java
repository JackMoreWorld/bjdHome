package com.bgd.admin.entity;

import com.bgd.support.entity.MallOrderDetailPo;
import lombok.Data;

/**
 * 订单详情 Dto
 *
 * @author Sunxk
 * @since 2019-3-22
 */
@Data
public class OrderDetManagerDto extends MallOrderDetailPo {

    /**
     * 下单会员昵称
     */
    private String vipName;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 商品名称
     */

    private String productName;

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 退款单Id
     */
    private Long refundId;
}
