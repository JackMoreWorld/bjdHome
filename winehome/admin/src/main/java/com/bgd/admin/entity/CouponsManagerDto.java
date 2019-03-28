package com.bgd.admin.entity;

import com.bgd.support.entity.MallCouponsPo;
import lombok.Data;

@Data
public class CouponsManagerDto extends MallCouponsPo {

    /**
     * 酒庄名称
     */
    private String chateauName;

    /**
     * 会员等级
     */
    private String levelName;

}
