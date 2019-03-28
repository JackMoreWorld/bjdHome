package com.bgd.app.entity;

import com.bgd.support.entity.ProductImgPo;
import com.bgd.support.entity.ProductInformationPo;
import lombok.Data;

import java.util.List;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/5
 * @描述 商品
 */
@Data
public class ProductDto extends ProductInformationPo {


    private Long actId;// 活动id
    private Long productId;// 商品id
    private String newCapacity;//容量
    private String countryLogo; //国家图标
    private String countryName; //国家名称
    private String chateauLogo; //酒庄图标
    private String chateauName; //酒庄名称
    private List<ProductImgPo> imgs; //商品图


}
