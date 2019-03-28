package com.bgd.admin.entity;

import com.bgd.support.entity.MallActivityProductPo;
import lombok.Data;

@Data
public class ActivityProManagerDto extends MallActivityProductPo {

    /**
     * 处理类型
     */
    private Integer dealType;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 产品名称
     */
    private String productName;
}
