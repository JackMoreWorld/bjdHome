package com.bgd.app.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/20
 * @描述
 */
@Data
public class ChateauParam extends PageParam {
    private Integer sortTpe; //排序类型 0综合 1销量  2评价
    private Long countryId;
    private Long productId;
    private Long chateauId;
    private String categoryName;

}
