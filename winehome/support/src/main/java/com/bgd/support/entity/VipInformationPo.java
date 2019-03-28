package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/6
 * @描述 vip信息
 */
@Data
public class VipInformationPo extends BasePo {
    private String name;
    private String logo;
    private Integer sex;
    private String password;
    private String phone;
    private String openId;
    private String inviteCode;
    private String regCode;
    private String payPassWord;
    private String alias;//别名
}
