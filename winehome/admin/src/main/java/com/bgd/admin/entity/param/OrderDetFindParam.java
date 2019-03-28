package com.bgd.admin.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * 订单详情查询参数
 *
 * @author Sunxk
 * @since 2019-3-22
 */
@Data
public class OrderDetFindParam extends PageParam {

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


}
