package com.bgd.admin.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * 商品信息查询参数
 *
 * @author Sunxk
 * @since 2019-3-20
 */
@Data
public class ProductFindParam extends PageParam {

    /**
     * 商品大类名称
     */
    private String categoryName;

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 酒庄名称
     */
    private String chateauName;

    /**
     * 商品名称
     */
    private String name;

}
