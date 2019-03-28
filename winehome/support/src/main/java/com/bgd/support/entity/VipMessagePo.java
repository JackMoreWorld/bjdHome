package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/8
 * @描述
 */
@Data
public class VipMessagePo extends BasePo {

    private Long vipId;
    private String title;
    private String content;

}
