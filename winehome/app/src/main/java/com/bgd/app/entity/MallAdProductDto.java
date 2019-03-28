package com.bgd.app.entity;

import com.bgd.support.entity.MallAdProductPo;
import com.bgd.support.entity.ProductImgPo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 广告位商品 dto
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-18
 */
@Data
public class MallAdProductDto extends MallAdProductPo {

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片
     */
    private String productLogo;

    /**
     * 商品价格
     */
    private BigDecimal productPrice;

    private List<ProductImgPo> imgs; //商品图
}
