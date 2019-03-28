package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

@Data
public class MallBannerPo extends BasePo {
    private Long adId;
    private String img;
    private Integer sort;
}
