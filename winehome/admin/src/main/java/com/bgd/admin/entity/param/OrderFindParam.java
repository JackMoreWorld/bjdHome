package com.bgd.admin.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * 订单信息查询参数
 * @author Sunxk
 * @since 2019-3-20
 */
@Data
public class OrderFindParam extends PageParam {

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 物流单号
     */
    private String logisticsNo;

    /**
     * 下单人昵称
     */
    private String name;

    /**
     * 下单人联系方式
     */
    private String phone;

}
