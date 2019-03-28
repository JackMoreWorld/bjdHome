package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/8
 * @描述  城市编码
 */
@Data
public class SysRegionPo extends BasePo {
    private String code;
    private String name;
    private String parentCode;

}
