package com.bgd.admin.dao;

import com.bgd.admin.entity.OrderManagerDto;
import com.bgd.admin.entity.param.OrderFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单模块Dao
 * @author Sunxk
 * @since  2019/3/16
 */
@Mapper
public interface OrderManagerDao {

    Long countOrder(OrderFindParam orderFindParam);
    List<OrderManagerDto> findOrder(OrderFindParam orderFindParam);

    OrderManagerDto findOneOrder(@Param("orderId") Long orderId);

}
