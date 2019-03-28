package com.bgd.app.entity;

import com.bgd.support.entity.CommunityArticlePo;
import lombok.Data;

import java.util.List;

@Data
public class CommunityArticleDto extends CommunityArticlePo {
    private String vipName;
    private String vipLogo;
    private Integer commentCount;// 评论数
    List<CommunityArticlePo> articlePoList;//跟帖
}
