package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/8
 * @描述
 */
@Data
public class ProductImgPo extends BasePo {
    private Long id;
    private Long productId;
    private Integer type;
    private String img;
    /**
     * 轮播图排序用
     */
    private Integer sort;// add by Sunxk 2019-3-25


}
