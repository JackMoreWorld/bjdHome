package com.bgd.support.base;

import lombok.Data;

import java.util.Date;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/4
 * @描述 Po基础类
 */
@Data
public class BasePo  extends PersistentObject{
    private Long id;//主键
    private Integer status; //状态
    private Integer dataStatus; //数据状态
    private Date createTime; //创建日期
    private Date updateTime; //更新日期

}
