package com.bgd.support.base;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/20
 * @描述 动态分页
 */

import com.bgd.support.exception.BusinessException;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @描述 分页
 * @创建人 JackMore
 * @创建时间 2019/3/20
 */
public abstract class PageBean<R extends PersistentObject, P extends PageParam> {

    R result;
    P param;


    private List<R> pageData = Lists.newArrayList();
    private Long totalCount = Long.valueOf(0);

    public PageBean(P param) {
        this.param = param;
    }

    protected abstract Long generateRowCount() throws BusinessException;

    protected abstract List<R> generateBeanList() throws BusinessException;

    void initParam() {
        int pageNo = (param.getPageNo() - 1) * param.getPageSize();
        param.setPageNo(pageNo);
        param.setPageSize(param.getPageSize());
    }

    public PageBean<R, P> execute() throws BusinessException {
        initParam();
        this.setTotalCount();
        if (totalCount > 0) this.setPageData();
        return this;
    }


    public List<R> getPageData() {
        return this.pageData;
    }

    public void setPageData() {
        this.pageData = generateBeanList();
    }


    public Long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount() {
        this.totalCount = generateRowCount();
    }

    public Integer getPageNo() {
        return param.getPageNo() - 1;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public P getParam() {
        return param;
    }

    public void setParam(P param) {
        this.param = param;
    }
}
