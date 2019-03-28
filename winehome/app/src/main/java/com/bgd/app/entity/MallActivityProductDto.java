package com.bgd.app.entity;

import com.bgd.support.entity.MallActivityProductPo;
import com.bgd.support.entity.ProductImgPo;
import lombok.Data;

import java.util.List;

@Data
public class MallActivityProductDto extends MallActivityProductPo {

    private Integer type;
    private String actDayStr;
    private String timing;
    private String capacity; //规格
    private String newCapacity; //规格
    private List<ProductImgPo> imgs; //商品图
}
