package com.bgd.app.entity.param;

import com.bgd.support.base.PageParam;
import lombok.Data;

import java.util.List;

@Data
public class VipOrderParam extends PageParam {

    private Long vipId;

    private Integer status;

    private List<String> orderIds;
}
