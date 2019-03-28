package com.bgd.app.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/20
 * @描述
 */
@Data
public class ActProParam extends PageParam {
    private Long actId;
    private Long categoryId;
    private String actDay;
    private String timing;

}
