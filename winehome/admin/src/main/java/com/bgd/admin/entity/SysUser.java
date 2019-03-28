package com.bgd.admin.entity;


import java.util.Date;

import lombok.Data;

@Data
public class SysUser{

    private Long id;
    private String name;
    private String password;
    private String remark;
    private Date createTime;



}
