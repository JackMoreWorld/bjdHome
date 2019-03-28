package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/9
 * @描述
 */
@Data
public class VipMallInfoPo extends BasePo {

    private  Long vipId;
    private String vipNo;
    private Integer points;
    private BigDecimal balance;
    private Long levelId;



}
