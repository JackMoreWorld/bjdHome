package com.bgd.admin.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * 活动查询参数
 *
 * @author Sunxk
 * @since 2019-3-20
 */
@Data
public class ActivityFindParam extends PageParam {
    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动类型
     */
    private Integer type;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动状态
     */
    private Integer status;
}
