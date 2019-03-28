package com.bgd.app.entity;

import com.bgd.support.base.PersistentObject;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/13
 * @描述 限时购商品展示
 */
@Data
public class GoodActProDto extends PersistentObject {
    private Long id;
    private Long chateauId;
    private String chateauName;
    private Long actId;
    private Long productId;
    private String productName;
    private String logo;
    private String img;
    private Long categoryId;
    private String categoryName;
    private Long like;
    private String newCapacity;

}
