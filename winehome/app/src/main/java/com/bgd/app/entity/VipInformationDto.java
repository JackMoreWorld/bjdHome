package com.bgd.app.entity;

import com.bgd.support.entity.VipInformationPo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/6
 * @描述 vip 信息
 */
@Data
public class VipInformationDto extends VipInformationPo {
    private Integer type;
    private String code;
}
