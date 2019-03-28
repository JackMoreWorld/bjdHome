package com.bgd.admin.entity;

import com.bgd.support.entity.ProductImgPo;
import com.bgd.support.entity.ProductInformationPo;
import lombok.Data;

import java.util.List;

@Data
public class ProductManagerDto extends ProductInformationPo {

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 酒庄名称
     */
    private String chateauName;

    /**
     * 商品大类
     */
    private String categoryName;

    /**
     * 商品图片信息
     */
    private List<ProImgManagerDto> ImgS;
}
