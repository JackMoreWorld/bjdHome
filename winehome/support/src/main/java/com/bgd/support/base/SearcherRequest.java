/**
 * FileName: SearcherRequest
 * Author:   JackMore
 * Date:     2018/10/26 10:07
 * Description: 查询请求参数
 */
package com.bgd.support.base;

import lombok.Data;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈查询请求参数〉
 *
 * @author JackMore
 * @create 2018/10/26
 * @since 1.0.0
 */
@Data
public class SearcherRequest extends PersistentObject {

    public Date startDate;
    public Date endDate;
    private Integer startNo;
    private Integer pageNo;
    private Integer pageSize;
    private Integer status;
    public String startDateStr;
    public String endDateStr;
    public String userName;
    public String password;
    public Long operateId;
    public String operateType;
    public String token;


}