package com.bgd.app.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

import java.util.List;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/22
 * @描述
 */
@Data
public class CommunityParam extends PageParam {
    private Long id;//帖子id
    private Long pid;//帖子id
    private Long vipId;
    private List<Long> focus;
    private Integer type; // 0发帖 1跟帖 2分享
    private String typeStr; // 0发帖 1跟帖 2分享

}
