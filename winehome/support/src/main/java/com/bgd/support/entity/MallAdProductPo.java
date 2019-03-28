package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

@Data
public class MallAdProductPo extends BasePo {

    private Long adId;
    private Long productId;
    private String newCapacity; //活动规格

}
