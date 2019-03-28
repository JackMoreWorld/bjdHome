package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/8
 * @描述
 */
@Data
public class VipCartPo extends BasePo {
    private Long vipId;
    private Long activityId;
    private Long productId;
    private String capacity;
    private Integer count;
}
