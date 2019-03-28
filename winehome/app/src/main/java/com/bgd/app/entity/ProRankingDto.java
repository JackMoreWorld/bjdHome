package com.bgd.app.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

@Data
public class ProRankingDto extends BasePo {

    private Long productId;
    private String productName;
    private String logo;
    private Long categoryId;
    private String categoryName;
    private String capacity;
    private Long actId;
}
