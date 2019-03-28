package com.bgd.app.entity;

import com.bgd.support.entity.VipFootMarkPo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/15
 * @描述
 */
@Data
public class VipFootMarkDto extends VipFootMarkPo {
    private String productName;
    private String capacity;
    private String logo;


}
