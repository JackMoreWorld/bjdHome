package com.bgd.admin.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 优惠券查询条件
 *
 * @author Sunxk
 * @since 2019-3-20
 */
@Data
public class CouponsFindParam extends PageParam {

    /**
     * 酒庄名称
     */
    private String chateauName;

    /**
     * 会员等级
     */
    private String levelName;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 类型 0全场通用  1酒庄专属
     */
    private Integer type;

    /**
     * 状态 0停用 1启用
     */
    private Integer dateStatus;

}
