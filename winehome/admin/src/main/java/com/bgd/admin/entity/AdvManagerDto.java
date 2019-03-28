package com.bgd.admin.entity;

import com.bgd.support.entity.MallAdPo;
import lombok.Data;

@Data
public class AdvManagerDto extends MallAdPo {

    /**
     * 处理类型
     */
    private Integer dealType;

}
