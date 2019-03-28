package com.bgd.app.entity;

import com.bgd.support.entity.MallOrderPo;
import lombok.Data;

@Data
public class MallOrderDto extends MallOrderPo {

    /**
     * 收货地址id
     */
    private Integer addrId;

    /**
     * 购物车id集合
     */
    private String cartIds;

    /**
     * 产品集合
     */
    private String productJsonStr;
}
