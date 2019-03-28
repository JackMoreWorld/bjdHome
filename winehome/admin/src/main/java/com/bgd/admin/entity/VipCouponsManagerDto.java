package com.bgd.admin.entity;

import com.bgd.support.entity.VipCouponsPo;
import lombok.Data;

@Data
public class VipCouponsManagerDto extends VipCouponsPo {

    /**
     * 酒庄名称
     */
    private String chateauName;

}
