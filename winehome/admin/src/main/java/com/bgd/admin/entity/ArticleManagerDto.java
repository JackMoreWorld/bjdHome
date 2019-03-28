package com.bgd.admin.entity;

import com.bgd.support.entity.CommunityArticlePo;
import lombok.Data;

@Data
public class ArticleManagerDto extends CommunityArticlePo {

    /**
     * 会员昵称
     */
    private String vipName;
    private Integer dealType; //操作类型
}
