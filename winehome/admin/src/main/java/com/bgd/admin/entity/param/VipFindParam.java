package com.bgd.admin.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

/**
 * 会员信息查询参数
 * @author Sunxk
 * @since 2019-3-20
 */
@Data
public class VipFindParam extends PageParam {

    /**
     * 会员昵称
     */
    private String name;

    /**
     * 会员手机
     */
    private String phone;

    /**
     * 会员等级
     */
    private String levelName;

}
