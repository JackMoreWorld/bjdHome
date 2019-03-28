package com.bgd.admin.dao;

import com.bgd.admin.entity.OrderDetManagerDto;
import com.bgd.admin.entity.param.OrderDetFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单详情信息管理 Dao
 *
 * @author Sunxk
 * @since 2019-3-22
 */
@Mapper
public interface OrderDetManagerDao {

    /**
     * 查询该订单当前状态
     */
    Map<String,Integer> findOneOrderStatus(@Param("id") Long id);

    /**
     * 修改订单详情状态
     */
    void updateOrderDetail(@Param("status") Long status);

    /**
     * 修改订单退款申请状态
     */
    void updateOrderRefund(@Param("status") Long status);

    /**
     * 分页查询退款申请订单
     */
    Long countOrderDet(OrderDetFindParam orderDetFindParam);

    List<OrderDetManagerDto> findOrderDet(OrderDetFindParam orderDetFindParam);

}
