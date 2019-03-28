package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/27
 * @描述
 */
@Data
public class VipReward extends BasePo {
    private Long vipId;
    private BigDecimal price;
    private Integer type;
}
