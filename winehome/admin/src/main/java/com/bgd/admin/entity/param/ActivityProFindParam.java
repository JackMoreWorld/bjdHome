package com.bgd.admin.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * 活动产品查询参数
 *
 * @author Sunxk
 * @since 2019-3-20
 */
@Data
public class ActivityProFindParam extends PageParam {

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 商品名称
     */
    private String productName;

}
