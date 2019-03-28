package com.bgd.admin.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * 酒庄信息查询参数
 *
 * @author Sunxk
 * @since 2019-3-20
 */
@Data
public class ChateauFindParam extends PageParam {

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 主营大类
     */
    private String categoryName;

    /**
     * 酒庄名称
     */
    private String name;

    /**
     * 状态  0停用 1启用
     */
    private Integer status;

}
