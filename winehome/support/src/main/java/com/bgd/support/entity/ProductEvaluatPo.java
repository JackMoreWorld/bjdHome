package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/8
 * @描述
 */
@Data
public class ProductEvaluatPo extends BasePo {
    private Long vipId;
    private Long productId;
    private String title;
    private String content;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private Integer type;


}
