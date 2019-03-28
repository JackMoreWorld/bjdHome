package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/18
 * @描述  评分
 */
@Data
public class MallOrderStarPo extends BasePo {

    private Long countryId;
    private Long chateauId;
    private Long productId;
    private Long star;


}
