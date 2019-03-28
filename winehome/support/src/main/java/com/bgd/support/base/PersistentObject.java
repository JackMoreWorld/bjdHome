package com.bgd.support.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @ClassName:  PersistentObject   
 * @Description: 持久化基类
 * @author: JackMore
 * @date:   2019年2月25日 下午2:16:47
 */
@Data
public class PersistentObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer startNo;
    private Integer pageNo;
    private Integer pageSize;

}