package com.bgd.app.entity;

import com.bgd.support.base.PersistentObject;
import lombok.Data;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/5
 * @描述
 */
@Data
public class SearchDto extends PersistentObject {

    private Long id;
    private String keyWords;


}
