package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

@Data
public class MallAdPo extends BasePo {

    private String name;
    private String logo;
    private String backImg;
    private int sort;//广告位排序

}
