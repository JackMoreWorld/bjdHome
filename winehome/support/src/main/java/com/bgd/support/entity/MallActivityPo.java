package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

@Data
public class MallActivityPo extends BasePo {

    private String name;
    private String title;
    private String img;
    private Integer type;

}
