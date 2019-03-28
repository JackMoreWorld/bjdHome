package com.bgd.admin.entity;

import com.bgd.support.entity.MallAdProductPo;
import lombok.Data;

@Data
public class AdvProManagerDto extends MallAdProductPo {

    /**
     * 处理类型
     */
    private Integer dealType;

    /**
     * 广告名称
     */
    private String advertisingName;

    /**
     * 产品名称
     */
    private String productName;

}
