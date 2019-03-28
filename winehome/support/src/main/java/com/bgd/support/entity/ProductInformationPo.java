package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @创建人 Sunxk
 * @创建时间 2019/3/7
 * @描述 product_information
 */
@Data
public class ProductInformationPo extends BasePo {

    private String name;//名称
    private String logo;//商品的主标
    private Long countryId;//国家ID
    private Long chateauId;//酒庄ID
    private Long categoryId;//品类ID
    private String capacity;//容量
    private Integer alcohol;//酒精含量
    private String flavor;//口味
    private Integer specifications;//规格
    private Integer discount;//是否参与折扣
    private String remark;//说明
    private BigDecimal price;//最低价
}
