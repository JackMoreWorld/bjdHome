package com.bgd.admin.entity;

import com.bgd.support.entity.VipInformationPo;
import lombok.Data;

@Data
public class VipManagerDto extends VipInformationPo {

    private Long points;
    private String name;
    private Long levelId;
    private String levelName;

}
