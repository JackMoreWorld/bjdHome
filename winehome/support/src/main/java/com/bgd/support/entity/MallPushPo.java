package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.Data;

import java.sql.Date;

/**
 * @author sunxk
 * @since 2019-3-18
 */
@Data
public class MallPushPo extends BasePo {

    /**
     * 类型 1热销国家 2推荐酒庄 3推荐产品 4精选活动
     */
    private Integer type;

    /**
     * 推荐信息ID
     * 例：如果是热销国家则此字段为国家ID
     *    如果是推荐酒庄则此字段为酒庄ID
     */
    private Long pushId;

    /**
     * 活动图片
     */
    private String img;

}
