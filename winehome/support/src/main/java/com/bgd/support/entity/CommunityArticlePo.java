package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

@Data
public class CommunityArticlePo extends BasePo {
    private Long pid;
    private String no;
    private Long vipId;
    private String title;
    private String Content;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private Integer type;//类型 0发帖 1跟帖 2转载
    private Long like;//点赞数

}
