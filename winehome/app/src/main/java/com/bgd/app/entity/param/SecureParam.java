package com.bgd.app.entity.param;

import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/21
 * @描述
 */
@Data
public class SecureParam {
    private Long vipId;
    private Integer type;
    private String phone;
    private String code;
    private String password;
    private String payWord;
}
