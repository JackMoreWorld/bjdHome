package com.bgd.app.entity;

import com.bgd.support.entity.MallCountryPo;
import lombok.Data;

import java.util.List;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/5
 * @描述
 */
@Data
public class MallCountryDto extends MallCountryPo {

    private List<MallChateauDto> chateaus;

}
