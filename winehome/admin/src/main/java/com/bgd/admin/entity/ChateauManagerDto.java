package com.bgd.admin.entity;

import com.bgd.support.entity.MallChateauPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChateauManagerDto extends MallChateauPo {

    /**
     * 商品大类名称
     */
    private String categoryName;

    /**
     * 商品所属国家名称
     */
    private String countryName;

}
