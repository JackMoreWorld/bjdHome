package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

@Data
public class MallOrderRefundPo extends BasePo {

    private Long vipId;
    private Long detailId;
    private Integer status;
    private String remark;
    private String refundNo;
    private Integer orderStatus;
}
