package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

import java.util.Date;

@Data
public class MallActivityProductPo extends BasePo {

    private String name;
    private Long activityId;
    private Long productId;
    private String newCapacity; //活动规格
    private String logo; //商品logo
    private String img; //商品详情大图
    private Long total;//商品总量
    private Long remain;//商品余量
    private Long like; //点赞数
    private Date startTime;
    private Date endTime;
    private String startTimeStr;
    private String endTimeStr;
    private Long categoryId; //酒品类id
    private Date actDay;//活动日
}
