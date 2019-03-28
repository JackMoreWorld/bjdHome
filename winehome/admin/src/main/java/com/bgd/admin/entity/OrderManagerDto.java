package com.bgd.admin.entity;

import com.bgd.support.entity.MallOrderPo;
import lombok.Data;

/**
 * @author Sunxk
 * @since 2019/3/16
 *  订单Dto
 */
@Data
public class OrderManagerDto extends MallOrderPo {

    private String name;//下单人昵称
    private String phone;//下单人联系方式

}
