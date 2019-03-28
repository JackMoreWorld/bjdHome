package com.bgd.app.entity;

import com.bgd.support.entity.MallChateauPo;
import com.bgd.support.entity.ProductInformationPo;
import lombok.Data;

import java.util.List;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/5
 * @描述
 */
@Data
public class MallChateauDto extends MallChateauPo {

    private Long chateauId;
    private String chateauLogo;
    private String categoryName;
    private String countryLogo;
    private String countryName;
    private Integer focusNum;
    List<ProductInformationPo> productInformationPoList;
}
