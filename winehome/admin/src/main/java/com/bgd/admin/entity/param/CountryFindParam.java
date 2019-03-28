package com.bgd.admin.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * 国家信息查询条件
 *
 * @author Sunxk
 * @since 2019-3-20
 */
@Data
public class CountryFindParam extends PageParam {

    /**
     * 国家名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Integer dataStatus;

}
