package com.bgd.app.entity;

import com.bgd.support.entity.VipCartPo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车 dto
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-11
 */
@Data
@JsonIgnoreProperties({"actEndTime"})
public class VipCartDto extends VipCartPo {

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品图标
     */
    private String logo;

    /**
     * 产品价格
     */
    private BigDecimal price;

    /**
     * 产品规格
     */
    private Integer content;

    /**
     * 是否参与过活动 0 否 1是
     */
    private Integer discount;

    /**
     * 酒庄名称
     */
    private String chateauName;

    /**
     * 酒庄logo
     */
    private String chateauLogo;

    /**
     * 领券标识 (0 否 1是)
     */
    private Integer couponFlag;

    /**
     * 活动结束时间
     */
    private String actEndTime;

    /**
     * 剩余时间
     */
    private long remainingTime;


}
