package com.bgd.admin.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * 轮播图查询参数
 *
 * @author Sunxk
 * @since 2019-3-20
 */
@Data
public class BannerFindParam extends PageParam {

    /**
     * 广告名称
     */
    private String advertisingName;

}
