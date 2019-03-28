package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/9
 * @描述
 */
@Data
public class MallInviteRecordPo  extends BasePo {
    private String code; //注册人code
    private String inviteCode; //邀请人code

}
