package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/4
 * @描述 会员等级
 */
@Data
public class VipLevelPo extends BasePo {
    private String name;
    private long min;
    private long max;
    private String remark;
}
