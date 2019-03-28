package com.bgd.app.entity;

import com.bgd.support.entity.ProductInformationPo;
import lombok.Data;

import java.util.Date;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/13
 * @描述 限时购商品展示
 */
@Data
public class LimitActProDto extends ProductInformationPo {
    private Long id;
    private Long actId;
    private Long productId;
    private String img;
    private String capacity; //元规格
    private String newCapacity;//活动规格
    private Long like;
    private String productName;
    private Integer total;
    private String actDay;
    private Date timing;


}
