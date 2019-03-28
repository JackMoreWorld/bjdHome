package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

import java.util.Date;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/8
 * @描述
 */
@Data
public class VipRecordPo extends BasePo {

    private Long vipId;
    private Integer type;
    private String content;
    private String recordDay;
    private Date day;

}
