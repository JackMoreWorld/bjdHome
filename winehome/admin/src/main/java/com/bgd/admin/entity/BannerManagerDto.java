package com.bgd.admin.entity;

import com.bgd.support.entity.MallBannerPo;
import lombok.Data;

@Data
public class BannerManagerDto extends MallBannerPo {

    /**
     * 处理类型
     */
    private Integer dealType;

    /**
     * 广告名称
     */
    private String advertisingName;

}
