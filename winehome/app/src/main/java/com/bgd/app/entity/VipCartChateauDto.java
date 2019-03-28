package com.bgd.app.entity;

import com.bgd.support.base.BasePo;
import com.bgd.support.base.PersistentObject;
import lombok.Data;

import java.util.List;

@Data
public class VipCartChateauDto extends BasePo {

    private Long chateauId;
    private String chateauLogo;
    private String chateauName;

    private Long vipId;

    private Long cartId;

    private List<VipCartDto> cartList;
}
