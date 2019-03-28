package com.bgd.app.entity;

import com.bgd.support.entity.ProductInformationPo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductDto extends ProductInformationPo {


    private Long actId;// 活动id
    private Long productId;// 商品id
    private String capacity; //规格

    private BigDecimal price;//价格

    private Integer count;
}
