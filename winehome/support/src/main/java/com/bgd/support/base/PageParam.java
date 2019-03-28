package com.bgd.support.base;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/20
 * @描述
 */
public class PageParam {
    private Integer pageNo;
    private Integer pageSize;

    public Integer getPageNo() {
        return pageNo == null ? 1 : pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
