package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/7
 * @描述
 */
@Data
public class VipAddrPo extends BasePo {
    private Long vipId;
    private String name;
    private String phone;
    private String province;
    private String city;
    private String area;
    private String addr;
    private Integer type;
    private String tag;
}
