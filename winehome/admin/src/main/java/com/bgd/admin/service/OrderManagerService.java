package com.bgd.admin.service;

import com.bgd.admin.dao.OrderManagerDao;
import com.bgd.admin.entity.OrderManagerDto;
import com.bgd.admin.entity.param.OrderFindParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单管理模块 Service
 *
 * @author Sunxk
 * @since 2019-3-16
 */
@Slf4j
@Service
public class OrderManagerService {

    @Autowired
    OrderManagerDao orderManagerDao;

    /**
     * 分页查询订单信息
     *
     * @param orderFindParam 订单信息查询条件
     * @throws BusinessException
     */
    public PageDto<OrderManagerDto> findOrder(OrderFindParam orderFindParam) throws BusinessException {
        log.info("查询订单列表");
        try {
            PageDto<OrderManagerDto> pageDto = new PageDto<>();
            PageBean<OrderManagerDto, OrderFindParam> pageBean = new PageBean<OrderManagerDto, OrderFindParam>(orderFindParam) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return orderManagerDao.countOrder(orderFindParam);
                }

                @Override
                protected List<OrderManagerDto> generateBeanList() throws BasicException {
                    return orderManagerDao.findOrder(orderFindParam);
                }
            }.execute();
            long total = pageBean.getTotalCount();
            List<OrderManagerDto> beanList = pageBean.getPageData();
            pageDto.setList(beanList);
            pageDto.setTotal(total);
            return pageDto;
        } catch (Exception e) {
            log.error("查询订单列表异常", e);
            throw new BusinessException("查询订单列表异常");
        }
    }

    /**
     * @return com.bgd.admin.entity.OrderManagerDto
     * @author Sunxk
     * @date 2019/3/16
     * @Param [orderId]
     */
    public OrderManagerDto findOneOrder(Long orderId) throws BusinessException {
        log.info("查询订单详情");
        try {
            return orderManagerDao.findOneOrder(orderId);
        } catch (Exception e) {
            log.error("查询订单详情异常", e);
            throw new BusinessException("查询订单详情异常");
        }
    }


}
