package com.bgd.admin.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * 论坛帖子查询参数
 *
 * @author Sunxk
 * @since 2019-3-20
 */
@Data
public class ArticleFindParam extends PageParam {

    /**
     * 类型 0发帖 1跟帖
     */
    private Integer type;

    /**
     * 会员昵称
     */
    private String vipName;

    /**
     * 标题
     */
    private String title;

}
