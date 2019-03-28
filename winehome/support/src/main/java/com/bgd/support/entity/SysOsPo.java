package com.bgd.support.entity;

import lombok.Data;

@Data
public class SysOsPo {
    private Long id;//主键
    private Integer type;
    private String url;
    private Integer indx;
    private String version;
    private String remark;
}
