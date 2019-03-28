package com.bgd.app.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/20
 * @描述
 */
@Data
public class ProParam extends PageParam {
    private Long productId;
    private Long chateauId;
    private Integer minAlcohol;
    private Integer maxAlcohol;
    private Long categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
