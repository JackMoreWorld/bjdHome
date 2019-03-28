package com.bgd.app.entity;

import com.bgd.support.entity.MallOrderDetailPo;
import lombok.Data;

@Data
public class MallOrderDetailDto extends MallOrderDetailPo {

    private String productName;
    private String productLogo;

    private String addrName;
    private String phone;
    private String addr;
}
