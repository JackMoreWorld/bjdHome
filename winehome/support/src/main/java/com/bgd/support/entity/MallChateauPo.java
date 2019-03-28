package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/5
 * @描述
 */
@Data
public class MallChateauPo extends BasePo {

    private Long countryId;
    private String name;
    private String logo;
    private Long categoryId;
    private String video;
    private String remark;
    private String backImg;

}
